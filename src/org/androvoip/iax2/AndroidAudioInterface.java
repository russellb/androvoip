/*
 * AndroVoIP -- VoIP for Android.
 *
 * Copyright (C), 2006, Mexuar Technologies Ltd.
 *   -- Adapted from Audio8k.java
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

import android.util.Log;

import com.mexuar.corraleta.audio.AudioInterface;
import com.mexuar.corraleta.audio.SampleListener;
import com.mexuar.corraleta.protocol.AudioSender;
import com.mexuar.corraleta.protocol.VoiceFrame;

public class AndroidAudioInterface implements AudioInterface {
	private AudioSender as;
	private Thread rec_thread;

	/**
	 * Audio properties have changed.
	 * <p>
	 * No active code uses this.
	 * 
	 * @see com.mexuar.corraleta.audio.AudioInterface#changedProps()
	 */
	public void changedProps() {
		Log.e("AndroidAudioInterface", "changedProps() got called.");
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
		final char[] prefs = { VoiceFrame.LIN16_NO };
		String ret = "";
		for (int i = 0; i < prefs.length; i++) {
			ret += (char) (prefs[i] + 66);
		}
		return ret;
	}

	/**
	 * Return an audio interface for a specified format.
	 * <p>
	 * Right now, we only support LIN16, and the handling for LIN16 will be
	 * built in to this class.
	 * <p>
	 * Called by com.mexuar.corraleta.protocol.Call.
	 * 
	 * @see com.mexuar.corraleta.audio.AudioInterface#getByFormat(java.lang.Integer)
	 */
	public AudioInterface getByFormat(Integer format) {
		switch (format.intValue()) {
		case VoiceFrame.LIN16_BIT:
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
		return VoiceFrame.LIN16_BIT;
	}

	/**
	 * Called by com.mexuar.corraleta.protocol.Call.
	 * 
	 * @see com.mexuar.corraleta.audio.AudioInterface#getSampSz()
	 */
	public int getSampSz() {
		return 320;
	}

	/**
	 * No active code uses this.
	 * 
	 * @see com.mexuar.corraleta.audio.AudioInterface#playAudioStream(java.io.InputStream)
	 */
	public void playAudioStream(InputStream in) throws IOException {
		Log.e("AndroidAudioInterface", "playAudioStream() got called");
	}

	/**
	 * No active code uses this.
	 * 
	 * @see com.mexuar.corraleta.audio.AudioInterface#readDirect(byte[])
	 */
	public long readDirect(byte[] buff) throws IOException {
		Log.e("AndroidAudioInterface", "readDirect() got called");
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
		Log.e("AndroidAudioInterface", "sampleRecord() got called");
	}

	/**
	 * Used by com.mexuar.corraleta.protocol.Call.
	 * 
	 * @see com.mexuar.corraleta.audio.AudioInterface#setAudioSender(com.mexuar.corraleta.protocol.AudioSender)
	 */
	public void setAudioSender(AudioSender as) {
		this.as = as;

		final Runnable trec = new Runnable() {
			public void run() {
				recTick();
			}
		};

		this.rec_thread = new Thread(trec, "rec_thread");
		this.rec_thread.setPriority(Thread.MAX_PRIORITY - 1);
		this.rec_thread.start();
	}

	/**
	 * No active code uses this.
	 * 
	 * @see com.mexuar.corraleta.audio.AudioInterface#startPlay()
	 */
	public void startPlay() {
		Log.e("AndroidAudioInterface", "startPlay() got called");
	}

	/**
	 * Used by com.mexuar.corraleta.protocol.Call.
	 * 
	 * @see com.mexuar.corraleta.audio.AudioInterface#startRec()
	 */
	public long startRec() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * Used by com.mexuar.corraleta.protocol.Call.
	 * 
	 * @see com.mexuar.corraleta.audio.AudioInterface#startRinging()
	 */
	public void startRinging() {
		// TODO Auto-generated method stub
	}

	/**
	 * Used by com.mexuar.corraleta.protocol.Call.
	 * 
	 * @see com.mexuar.corraleta.audio.AudioInterface#stopPlay()
	 */
	public void stopPlay() {
		// TODO Auto-generated method stub
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
		// TODO Auto-generated method stub
	}

	/**
	 * Return an Integer bit mask of the codecs we support.
	 * <p>
	 * Used by ProtocolControlFrameNew.
	 * 
	 * @see com.mexuar.corraleta.audio.AudioInterface#supportedCodecs()
	 */
	public Integer supportedCodecs() {
		return new Integer(VoiceFrame.LIN16_BIT);
	}

	/**
	 * Handle received audio.
	 * <p>
	 * Used by com.mexuar.corraleta.protocol.Call.
	 * 
	 * TODO Do something useful with the received audio.
	 * 
	 * @see com.mexuar.corraleta.audio.AudioInterface#write(byte[], long)
	 */
	public void write(byte[] buff, long timestamp) throws IOException {
		/* Drop it on the floor! */
	}

	/**
	 * No active code uses this right now.
	 * 
	 * @see com.mexuar.corraleta.audio.AudioInterface#writeDirect(byte[])
	 */
	public void writeDirect(byte[] buff) throws IOException {
		Log.e("AndroidAudioInterface", "writeDirect() got called");
	}
}
