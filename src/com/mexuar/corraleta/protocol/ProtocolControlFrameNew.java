/*
 * Copyright (C), 2006, Mexuar Technologies Ltd.
 * 
 * This file is part of AndroVoIP.
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

package com.mexuar.corraleta.protocol;

import com.mexuar.corraleta.audio.AudioInterface;

/**
 * Special cases for outbound 'New' messages.
 *
 * @author <a href="mailto:thp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 1.19 $ $Date: 2006/11/14 16:46:37 $
 */
class ProtocolControlFrameNew extends ProtocolControlFrame {
    /**
     * The outbound constructor. This sets the destination call number
     * to zero.
     *
     * @param p0 The Call object
     */
    public ProtocolControlFrameNew(Call p0) {
        super(p0);
        _dCall = 0;
        _subclass = ProtocolControlFrame.NEW;
        this.setTimestampVal(0);
        _call.resetClock();
    }


    /**
     * The inbound constructor.
     *
     * @param p0 The Call object
     * @param p1 The incoming message bytes
     */
    public ProtocolControlFrameNew(Call p0, byte[] p1) {
        super(p0, p1);
    }


    /**
     * Returns if this is a NEW message. True by default.
     *
     * @return true if NEW, false otherwise
     */
    public boolean isANew() {
        return true;
    }


    /**
     * Sends this NEW message.
     *
     * @param cno The source call number
     * @param username The username (sent in IE)
     * @param calledNo The number we're calling (sent in IE)
     * @param callingNo Number/extension we call from (sent in IE)
     * @param callingName Name of the person calling (sent in IE)
     */
    /*
       IE that can be sent:
       - 8.6.1.   CALLED NUMBER
       - 8.6.2.   CALLING NUMBER
       - 8.6.3.   CALLING ANI
       - 8.6.4.   CALLING NAME
       - 8.6.5.   CALLED CONTEXT
       - 8.6.6.   USERNAME
       - 8.6.8.   CAPABILITY
       - 8.6.9.   FORMAT
       - 8.6.10.  LANGUAGE
       - 8.6.11.  VERSION (MUST, and should be first)
       - 8.6.12.  ADSICPE
       - 8.6.13.  DNID
       - 8.6.25.  AUTOANSWER
       - 8.6.31.  DATETIME
       - 8.6.38.  CALLINGPRES
       - 8.6.39.  CALLINGTON
       - 8.6.43.  ENCRYPTION
       - 8.6.45.  CODEC PREFS
      */
    public void sendNew(Character cno, String username, String calledNo,
                        String callingNo, String callingName) {

        Log.debug("ProtocolControlFrameNew.sendNew: calledNo=" + calledNo
                  + ", callingNo=" + callingNo
                  + ", callingName=" + callingName
                  + ", username=" + username);
        _sCall = cno.charValue();
        _iseq = _call.getIseq();
        _oseq = _call.getOseqInc();

        InfoElement ie = new InfoElement();
        ie.calledNo = calledNo;
        ie.callingNo = callingNo;
        ie.callingName = callingName;
        ie.username = username;
        AudioInterface a = _call.getAudioFace();
        int format = a.supportedCodecs().intValue();
        ie.format = new Integer(format);
        ie.version = new Integer(2);
        ie.codec_prefs = a.codecPrefString().getBytes();
 //       ie.putIaxVar("foobar","724024");
        Log.debug("Sending initial NEW");
        sendMe(ie);
    }


    /**
     * Commit this frame. This method is called when the NEW frame we sent
     * has been acked. This will call Call.gotAckToNew()
     *
     * @param ack The ack frame
     * @see Call#gotAckToNew(FullFrame)
     */
    void commit(FullFrame ack) {
        if (this._call != null) {
            _call.gotAckToNew(ack);
        }
        Log.debug("Commit on new called");
    }

}

