/*
 * AndroVoIP -- VoIP for Android.
 *
 * Copyright (C), 2006, Mexuar Technologies Ltd.
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

package com.mexuar.corraleta.audio.dev;

import com.mexuar.corraleta.audio.AudioInterface;
import java.io.IOException;
import com.mexuar.corraleta.protocol.AudioSender;
import java.io.*;
import com.mexuar.corraleta.audio.SampleListener;

public class RawDev
    implements AudioInterface , Runnable {
    InputStream _inS;
    OutputStream _outS;
    long _readTime = 0;
    boolean _playing;
    private AudioSender _as;
    private Thread _record;
    public RawDev() throws FileNotFoundException {
        _inS = new FileInputStream("/dev/audio");
        _outS = new FileOutputStream("/dev/audio");
    }
    public static void main(String[] args) {
        try {
            RawDev r = new RawDev();
            byte [] buff = new byte[r.getSampSz()];
            long t = 0;
            while(t < 10000){
                t= r.readWithTime(buff);
                r.writeDirect(buff);
            }
        }
        catch (Exception ex) {
        }
    }

    /**
     * Return the minimum sample size for use in creating buffers etc.
     *
     * @return int
     * @todo Implement this com.mexuar.corraleta.audio.AudioInterface method
     */
    public int getSampSz() {
        return 160;
    }

    /**
     * Read from the Microphone, using the buffer provided, but _only_ filling
     * getSampSz() bytes.
     *
     * @param buff byte[]
     * @return long
     * @throws IOException
     * @todo Implement this com.mexuar.corraleta.audio.AudioInterface method
     */
    public long readWithTime(byte[] buff) throws IOException {
        _inS.read(buff);
        return this._readTime;
    }

    /**
     * readDirect
     *
     * @param buff byte[]
     * @throws IOException
     * @return long
     * @todo Implement this com.mexuar.corraleta.audio.AudioInterface method
     */
    public long readDirect(byte[] buff) throws IOException {
        _inS.read(buff);
        return this._readTime;
    }

    /**
     * stop the reccorder - but don't throw it away.
     *
     * @todo Implement this com.mexuar.corraleta.audio.AudioInterface method
     */
    public void stopRec() {
        _record =null;
    }

    /**
     * start the recorder returning the time...
     *
     * @return long
     * @todo Implement this com.mexuar.corraleta.audio.AudioInterface method
     */
    public long startRec() {
        _record = new Thread(this,"record");
        _record.start();
        return 0L;
    }

    /**
     *
     * @todo Implement this com.mexuar.corraleta.audio.AudioInterface method
     */
    public void changedProps() {
    }

    /**
     *
     * @todo Implement this com.mexuar.corraleta.audio.AudioInterface method
     */
    public void startPlay() {
        _playing = true;
    }

    /**
     *
     * @todo Implement this com.mexuar.corraleta.audio.AudioInterface method
     */
    public void stopPlay() {
        _playing = false;
    }

    /**
     *
     * @param buff byte[]
     * @param timestamp long
     * @throws IOException
     * @todo Implement this com.mexuar.corraleta.audio.AudioInterface method
     */
    public void write(byte[] buff, long timestamp) throws IOException {
        _outS.write(buff);
    }

    /**
     * writeDirect
     *
     * @param buff byte[]
     * @throws IOException
     * @todo Implement this com.mexuar.corraleta.audio.AudioInterface method
     */
    public void writeDirect(byte[] buff) throws IOException {
        _outS.write(buff);
    }

    /**
     *
     * @todo Implement this com.mexuar.corraleta.audio.AudioInterface method
     */
    public void startRinging() {
        System.err.print("ring!");
    }

    /**
     *
     * @todo Implement this com.mexuar.corraleta.audio.AudioInterface method
     */
    public void stopRinging() {
    }

    /**
     * getFormatBit
     *
     * @return int
     * @todo Implement this com.mexuar.corraleta.audio.AudioInterface method
     */
    public int getFormatBit() {
        return com.mexuar.corraleta.protocol.VoiceFrame.ULAW_BIT;
    }

    /**
     * setAudioSender
     *
     * @param as AudioSender
     * @todo Implement this com.mexuar.corraleta.audio.AudioInterface method
     */
    public void setAudioSender(AudioSender as) {
        _as = as;
    }

    /**
     * playAudioStream
     *
     * @param in InputStream
     * @throws IOException
     * @todo Implement this com.mexuar.corraleta.audio.AudioInterface method
     */
    public void playAudioStream(InputStream in) throws IOException {
    }

    /**
     * sampleRecord
     *
     * @param list SampleListener
     * @throws IOException
     * @todo Implement this com.mexuar.corraleta.audio.AudioInterface method
     */
    public void sampleRecord(SampleListener list) throws IOException {
    }

    /**
     * supportedCodecs
     *
     * @return Integer
     * @todo Implement this com.mexuar.corraleta.audio.AudioInterface method
     */
    public Integer supportedCodecs() {
        return new Integer(this.getFormatBit());
    }

    /**
     * codecPrefString
     *
     * @return String
     * @todo Implement this com.mexuar.corraleta.audio.AudioInterface method
     */
    public String codecPrefString() {
        return ""+(char) (com.mexuar.corraleta.protocol.VoiceFrame.ULAW_NO + 66);
    }

    /**
     * cleanUp
     *
     * @todo Implement this com.mexuar.corraleta.audio.AudioInterface method
     */
    public void cleanUp() {
        try {
            if (_inS != null) {
                _inS.close();
            }
            if (_outS != null) {
                _outS.close();
            }
        } catch (Exception x){
            x.printStackTrace();
        }
    }

    /**
     * getByFormat
     *
     * @param format Integer
     * @return AudioInterface
     * @todo Implement this com.mexuar.corraleta.audio.AudioInterface method
     */
    public AudioInterface getByFormat(Integer format) {
        return this;
    }

    /**
     * run
     */
    public void run() {
        while (_record != null){
            try {
                _as.send();
            }
            catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
