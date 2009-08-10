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

import com.mexuar.corraleta.audio.AudioInterface;
import com.mexuar.corraleta.audio.SampleListener;
import com.mexuar.corraleta.protocol.AudioSender;
import com.mexuar.corraleta.protocol.VoiceFrame;

public class AndroidAudioInterface implements AudioInterface {
	public void changedProps() {
		// TODO Auto-generated method stub
		
	}

	public void cleanUp() {
		// TODO Auto-generated method stub
		
	}

	public String codecPrefString() {
		// TODO Auto-generated method stub
		return null;
	}

	public AudioInterface getByFormat(Integer format) {
		// TODO Auto-generated method stub
		return null;
	}

	public int getFormatBit() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getSampSz() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void playAudioStream(InputStream in) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public long readDirect(byte[] buff) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public long readWithTime(byte[] buff) throws IOException {
		// TODO Auto-generated method stub
		return 0;
	}

	public void sampleRecord(SampleListener list) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void setAudioSender(AudioSender as) {
		// TODO Auto-generated method stub
	}

	public void startPlay() {
		// TODO Auto-generated method stub
		
	}

	public long startRec() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void startRinging() {
		// TODO Auto-generated method stub
		
	}

	public void stopPlay() {
		// TODO Auto-generated method stub
		
	}

	public void stopRec() {
		// TODO Auto-generated method stub
	}

	public void stopRinging() {
		// TODO Auto-generated method stub
		
	}

	public Integer supportedCodecs() {
		return new Integer(VoiceFrame.LIN16_BIT);
	}

	public void write(byte[] buff, long timestamp) throws IOException {
		// TODO Auto-generated method stub
		
	}

	public void writeDirect(byte[] buff) throws IOException {
		// TODO Auto-generated method stub
		
	}
}

