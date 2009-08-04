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

package com.mexuar.corraleta.protocol;

/**
 * Represents an IAX DTMF FRAME
 *
 * @author <a href="mailto:thp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 1.6 $ $Date: 2006/02/16 13:21:33 $
 */
class DtmfFrame extends FullFrame {
    /**
     * The outbound constructor.
     *
     * @param ca The Call object
     * @param c The outgoing DTMF character: 0-9, A-D, *, # 
     */
    DtmfFrame(Call ca, char c) {
        super(ca);
        _retry = false;
        _cbit = false;
        _frametype = FullFrame.DTMF;
        _subclass = 0x7f & c;
        byte buf[] = new byte[0];
        sendMe(buf);
        Log.debug("Sent DTMF " + c);
        this.dump();
    }


    /**
     * ack is called to send any required response. This method is empty
     * (for the moment?).
     */
    void ack() {
        // inbound - ignore it for now....
    }

}

