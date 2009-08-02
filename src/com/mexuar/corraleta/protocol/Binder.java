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

import com.mexuar.corraleta.audio.*;
import com.mexuar.corraleta.util.*;

public abstract class Binder {
 
    abstract public void send(String string, ByteBuffer bs);

    abstract public Friend removeFriend(String _iad);


    abstract public void stop();

    abstract public void unregister(ProtocolEventListener gui) throws Exception;
    
    abstract public AudioInterface getAudioFace();
    
    abstract public void register(String username, String password,
        ProtocolEventListener gui, boolean wantIncoming)
    throws Exception ;


    protected static char[]  hex = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D',
        'E', 'F'};



    /**
     * Turns a byte array into a hexidecimal string, using the specified
     * character as separator.
     *
     * @param dig The byte array
     * @param sep The separator
     * @return The hex string
     */

    public static String enHex(byte[] dig, Character sep) {
        StringBuffer ret = new StringBuffer(32);
        for (int i = 0;i < dig.length ; i++) {
            int v = (0x7f & dig[i]) + ( (dig[i] < 0) ? 128 : 0);
            int h = v >> 4;
            int l = v & 0xf;
            ret.append(hex[h]).append(hex[l]);
            if (sep != null) {
                ret.append(sep);
            }

        }
        return ret.toString();
    }

    /**
     * getGuiEventSender
     *
     * @param _gui ProtocolEventListener
     * @return ProtocolEventListener
     */
    public abstract ProtocolEventListener getGuiEventSender(
        ProtocolEventListener _gui);

}
