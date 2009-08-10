/*
 * AndroVoIP -- VoIP for Android.
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

package org.androvoip.audio;

import java.io.IOException;
import java.io.InputStream;

import android.util.Log;

import com.mexuar.corraleta.audio.AudioInterface;
import com.mexuar.corraleta.audio.SampleListener;
import com.mexuar.corraleta.protocol.AudioSender;
import com.mexuar.corraleta.protocol.VoiceFrame;

public class AndroidAudioInterface implements AudioInterface {
	/**
	 * Audio properties have changed.
	 * <p>
	 * This shouldn't be called, as there are no settings available for changing
	 * audio device configuration right now.
	 * 
	 * @see com.mexuar.corraleta.audio.AudioInterface#changedProps()
	 */
	public void changedProps() {
		Log.e("AndroidAudioInterface", "changedProps() got called.");
	}

	/**
	 * Clean up after the parent BinderSE is stopped.
	 * 
	 * @see com.mexuar.corraleta.audio.AudioInterface#cleanUp()
	 */
	public void cleanUp() {
		/* Nothing, yet. */
	}

	/**
	 * Generate codec preferences string.
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
	 * 
	 * @see com.mexuar.corraleta.audio.AudioInterface#getFormatBit()
	 */
	public int getFormatBit() {
		return VoiceFrame.LIN16_BIT;
	}

	/**
	 * @see com.mexuar.corraleta.audio.AudioInterface#getSampSz()
	 */
	public int getSampSz() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see com.mexuar.corraleta.audio.AudioInterface#playAudioStream(java.io.InputStream)
	 */
	public void playAudioStream(InputStream in) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see com.mexuar.corraleta.audio.AudioInterface#readDirect(byte[])
	 */
	public long readDirect(byte[] buff) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see com.mexuar.corraleta.audio.AudioInterface#readWithTime(byte[])
	 */
	public long readWithTime(byte[] buff) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see com.mexuar.corraleta.audio.AudioInterface#sampleRecord(com.mexuar.corraleta.audio.SampleListener)
	 */
	public void sampleRecord(SampleListener list) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see com.mexuar.corraleta.audio.AudioInterface#setAudioSender(com.mexuar.corraleta.protocol.AudioSender)
	 */
	public void setAudioSender(AudioSender as) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see com.mexuar.corraleta.audio.AudioInterface#startPlay()
	 */
	public void startPlay() {
		// TODO Auto-generated method stub

	}

	/**
	 * @see com.mexuar.corraleta.audio.AudioInterface#startRec()
	 */
	public long startRec() {
		// TODO Auto-generated method stub
		return 0;
	}

	/**
	 * @see com.mexuar.corraleta.audio.AudioInterface#startRinging()
	 */
	public void startRinging() {
		// TODO Auto-generated method stub

	}

	/**
	 * @see com.mexuar.corraleta.audio.AudioInterface#stopPlay()
	 */
	public void stopPlay() {
		// TODO Auto-generated method stub

	}

	/**
	 * @see com.mexuar.corraleta.audio.AudioInterface#stopRec()
	 */
	public void stopRec() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see com.mexuar.corraleta.audio.AudioInterface#stopRinging()
	 */
	public void stopRinging() {
		// TODO Auto-generated method stub

	}

	/**
	 * @see com.mexuar.corraleta.audio.AudioInterface#supportedCodecs()
	 */
	public Integer supportedCodecs() {
		return new Integer(VoiceFrame.LIN16_BIT);
	}

	/**
	 * @see com.mexuar.corraleta.audio.AudioInterface#write(byte[], long)
	 */
	public void write(byte[] buff, long timestamp) throws IOException {
		// TODO Auto-generated method stub

	}

	/**
	 * @see com.mexuar.corraleta.audio.AudioInterface#writeDirect(byte[])
	 */
	public void writeDirect(byte[] buff) throws IOException {
		// TODO Auto-generated method stub

	}
}
