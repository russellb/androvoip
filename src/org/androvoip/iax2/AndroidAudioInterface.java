/*
 * AndroVoIP -- VoIP for Android.
 *
 * Copyright (C), 2006, Mexuar Technologies Ltd.
 *   -- Partially adapted from Audio8k.java
 *
 * Copyright (C), 2009, Russell Bryant
 *
 * Russell Bryant <russell@russellbryant.net>
 *
 * AndroVoIP is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * AndroVoIP is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with AndroVoIP.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.androvoip.iax2;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.androvoip.audio.ULAW;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.util.Log;

import com.mexuar.corraleta.audio.AudioInterface;
import com.mexuar.corraleta.audio.SampleListener;
import com.mexuar.corraleta.protocol.AudioSender;
import com.mexuar.corraleta.protocol.VoiceFrame;

public class AndroidAudioInterface implements AudioInterface {
	/* Data for the receive path */
	private AudioTrack track = null;
	private Thread play_thread = null;
	private List<short[]> playQ = Collections.synchronizedList(new LinkedList<short[]>());
	private static final int UNUSED_CACHE_MAX = 10;
	private List<short[]> unusedQ = Collections.synchronizedList(new LinkedList<short[]>());

	/* Data for the transmit path */
	private AudioSender as = null;
	private Thread rec_thread = null;

	private Context context = null;
	
	public void setContext(Context c) {
		this.context = c;
	}
	
	/**
	 * Audio properties have changed.
	 * <p>
	 * No active code uses this.
	 *
	 * @see com.mexuar.corraleta.audio.AudioInterface#changedProps()
	 */
	public void changedProps() {
		Log.e("IAX2Audio", "changedProps() got called.");
	}

	/**
	 * Clean up after the parent BinderSE is stopped.
	 * <p>
	 * Called by com.mexuar.corraleta.protocol.netse.BinderSE.
	 *
	 * @see com.mexuar.corraleta.audio.AudioInterface#cleanUp()
	 */
	public void cleanUp() {

	}

	/**
	 * Generate codec preferences string.
	 * <p>
	 * Called by com.mexuar.corraleta.protocol.ProtocolControlFrame.
	 *
	 * @see com.mexuar.corraleta.audio.AudioInterface#codecPrefString()
	 * @see com.mexuar.corraleta.audio.javasound.Audio8k#codecPrefString()
	 */
	public String codecPrefString() {
		final char[] prefs = { VoiceFrame.ULAW_NO };
		String ret = "";
		
		for (int i = 0; i < prefs.length; i++) {
			ret += (char) (prefs[i] + 66);
		}
		
		return ret;
	}

	/**
	 * Return an audio interface for a specified format.
	 * <p>
	 * Right now, we only support ULAW, and the handling for ULAW is
	 * built in to this class.
	 * <p>
	 * Called by com.mexuar.corraleta.protocol.Call.
	 *
	 * @see com.mexuar.corraleta.audio.AudioInterface#getByFormat(java.lang.Integer)
	 */
	public AudioInterface getByFormat(Integer format) {
		switch (format.intValue()) {
		case VoiceFrame.ULAW_BIT:
			return this;
		default:
			return null;
		}
	}

	/**
	 * Return the bit for the format of this AudioInterface.
	 * <p>
	 * Called by com.mexuar.corraleta.protocol.Call.
	 *
	 * @see com.mexuar.corraleta.audio.AudioInterface#getFormatBit()
	 */
	public int getFormatBit() {
		return VoiceFrame.ULAW_BIT;
	}

	/**
	 * Called by com.mexuar.corraleta.protocol.Call.
	 *
	 * @see com.mexuar.corraleta.audio.AudioInterface#getSampSz()
	 */
	public int getSampSz() {
		return 160;
	}

	/**
	 * No active code uses this.
	 *
	 * @see com.mexuar.corraleta.audio.AudioInterface#playAudioStream(java.io.InputStream)
	 */
	public void playAudioStream(InputStream in) throws IOException {
		Log.e("IAX2Audio", "playAudioStream() got called");
	}

	/**
	 * No active code uses this.
	 *
	 * @see com.mexuar.corraleta.audio.AudioInterface#readDirect(byte[])
	 */
	public long readDirect(byte[] buff) throws IOException {
		Log.e("IAX2Audio", "readDirect() got called");
		return 0;
	}

	/**
	 * called every 20 ms
	 */
	private void frameTime() {
		if (this.as != null) {
			try {
				this.as.send();
			} catch (final IOException x) {
				Log.w("AndroidAudioInterface.frameTime()", x.getMessage());
			}
		}
	}

	/**
	 * TODO Handle drift, etc.
	 */
	private void recTick() {
		final long delta = 20;

		while (this.rec_thread != null) {
			frameTime();
			try {
				Thread.sleep(delta);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void writeBuff(short[] buf) {
		if (this.track == null) {
			Log.w("IAX2Audio", "write() without an AudioTrack");
			return;
		}

		// Log.d("IAX2Audio", "write() buf of len " + Integer.toString(buf.length));

		int written = 0;
		while (written < buf.length) {
			int res;

			res = this.track.write(buf, written, buf.length - written);
			switch (res) {
			case AudioTrack.ERROR_INVALID_OPERATION:
				Log.e("IAX2Audio", "Invalid write()");
				return;
			case AudioTrack.ERROR_BAD_VALUE:
				Log.e("IAX2Audio", "Bad arguments to write()");
				return;
			}

			written += res;
		}
	}

	private void playbackTime() {
		while (this.playQ.isEmpty() == false) {
			short[] buf = this.playQ.remove(0);
			writeBuff(buf);
			if (this.unusedQ.size() < AndroidAudioInterface.UNUSED_CACHE_MAX) {
				/* Cache a max of 10 buffers */
				this.unusedQ.add(buf);
			}
		}
	}

	private void playTick() {
		final long delta = 20;

        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);
		
		while (this.play_thread != null) {
			playbackTime();
			try {
				Thread.sleep(delta);
			} catch (final InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * Called by com.mexuar.corraleta.protocol.AudioSender
	 *
	 * @see com.mexuar.corraleta.audio.AudioInterface#readWithTime(byte[])
	 */
	public long readWithTime(byte[] buff) throws IOException {
		return 0;
	}

	/**
	 * No active code uses this.
	 *
	 * @see com.mexuar.corraleta.audio.AudioInterface#sampleRecord(com.mexuar.corraleta.audio.SampleListener)
	 */
	public void sampleRecord(SampleListener list) throws IOException {
		Log.e("IAX2Audio", "sampleRecord() got called");
	}

	/**
	 * Used by com.mexuar.corraleta.protocol.Call.
	 *
	 * @see com.mexuar.corraleta.audio.AudioInterface#setAudioSender(com.mexuar.corraleta.protocol.AudioSender)
	 */
	public void setAudioSender(AudioSender as) {
		this.as = as;

		/* Set up audio recording (transmit) */

		final Runnable trec = new Runnable() {
			public void run() {
				recTick();
			}
		};

		this.rec_thread = new Thread(trec, "rec_thread");
		this.rec_thread.start();

		/* Set up audio playback (receive) */

		if (track != null) {
			Log.w("IAX2Audio", "track not null in setAudioSender()");
		}

		final int minBuffer = AudioTrack.getMinBufferSize(8000,
								AudioFormat.CHANNEL_CONFIGURATION_MONO,
								AudioFormat.ENCODING_PCM_16BIT);

		try {
			this.track = new AudioTrack(AudioManager.STREAM_VOICE_CALL,
								8000, /* 8 kHz sample rate */
								AudioFormat.CHANNEL_CONFIGURATION_MONO,
								AudioFormat.ENCODING_PCM_16BIT,
								(640 > minBuffer) ? 640 : minBuffer,
								AudioTrack.MODE_STREAM);
		} catch (final IllegalArgumentException e) {
			Log.e("IAX2Audio", "Failed to create AudioTrack");
			e.printStackTrace();
			return;
		}

		AudioManager aMan = (AudioManager) this.context.getSystemService(Context.AUDIO_SERVICE);
		aMan.setRouting(AudioManager.MODE_IN_CALL, 
				AudioManager.ROUTE_EARPIECE, 
				AudioManager.ROUTE_ALL);
		aMan.setMode(AudioManager.MODE_IN_CALL);
		
		final Runnable tplay = new Runnable() {
			public void run() {
				playTick();
			}
		};

		this.play_thread = new Thread(tplay, "play_thread");
		this.play_thread.start();

		track.play();
	}

	/**
	 * No active code uses this.
	 *
	 * @see com.mexuar.corraleta.audio.AudioInterface#startPlay()
	 */
	public void startPlay() {
		Log.e("IAX2Audio", "startPlay() got called");
	}

	/**
	 * Used by com.mexuar.corraleta.protocol.Call.
	 *
	 * @see com.mexuar.corraleta.audio.AudioInterface#startRec()
	 */
	public long startRec() {
		Log.d("IAX2Audio", "startRec()");
		return 0;
	}

	/**
	 * Used by com.mexuar.corraleta.protocol.Call.
	 *
	 * @see com.mexuar.corraleta.audio.AudioInterface#startRinging()
	 */
	public void startRinging() {
		Log.d("IAX2Audio", "startRinging()");
	}

	/**
	 * Used by com.mexuar.corraleta.protocol.Call.
	 *
	 * @see com.mexuar.corraleta.audio.AudioInterface#stopPlay()
	 */
	public void stopPlay() {
		Log.d("IAX2Audio", "stopPlay()");

		if (track != null) {
			track.stop();
		}

		try {
			if (this.play_thread != null) {
				final Thread t = this.play_thread;
				/* Setting this to null is the signal to the thread to exit. */
				this.play_thread = null;
				t.join();
			}
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		track = null;
	}

	/**
	 * Stop sending our audio.
	 * <p>
	 * Used by com.mexuar.corraleta.protocol.Call.
	 *
	 * @see com.mexuar.corraleta.audio.AudioInterface#stopRec()
	 */
	public void stopRec() {
		try {
			if (this.rec_thread != null) {
				final Thread t = this.rec_thread;
				/* Setting this to null is the signal to the thread to exit. */
				this.rec_thread = null;
				t.join();
			}
		} catch (final InterruptedException e) {
			e.printStackTrace();
		}

		this.as = null;
	}

	/**
	 * Used by com.mexuar.corraleta.protocol.Call.
	 *
	 * @see com.mexuar.corraleta.audio.AudioInterface#stopRinging()
	 */
	public void stopRinging() {
		Log.d("IAX2Audio", "stopRinging()");
	}

	/**
	 * Return an Integer bit mask of the codecs we support.
	 * <p>
	 * Used by ProtocolControlFrameNew.
	 *
	 * @see com.mexuar.corraleta.audio.AudioInterface#supportedCodecs()
	 */
	public Integer supportedCodecs() {
		return new Integer(VoiceFrame.ULAW_BIT);
	}

	/**
	 * Handle received audio.
	 * <p>
	 * Used by com.mexuar.corraleta.protocol.Call.
	 *
	 * @see com.mexuar.corraleta.audio.AudioInterface#write(byte[], long)
	 */
	public void write(byte[] buf, long timestamp) throws IOException {
		short[] qBuf = null;
		
		/* Try to used a cached buffer if we can */
		
		while (qBuf == null && this.unusedQ.isEmpty() == false) {
			qBuf = this.unusedQ.remove(0);
			if (qBuf.length != buf.length) {
				qBuf = null;
			}
		}
		
		if (qBuf == null) {
			/* Did not find a suitable cached buffer */
			Log.d("IAX2Audio", "unused buffer cache miss");
			qBuf = new short[buf.length];
		}
		
		for (int i = 0; i < buf.length; i++) {
			qBuf[i] = ULAW.ulaw2linear(buf[i]);
		}
		
		this.playQ.add(qBuf);
	}

	/**
	 * No active code uses this right now.
	 *
	 * @see com.mexuar.corraleta.audio.AudioInterface#writeDirect(byte[])
	 */
	public void writeDirect(byte[] buff) throws IOException {
		Log.e("IAX2Audio", "writeDirect() got called");
	}
}
