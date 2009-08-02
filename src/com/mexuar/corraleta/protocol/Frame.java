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

import com.mexuar.corraleta.util.*;
/**
 * Base class for all frames
 *
 * @author <a href="mailto:thp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 1.13 $ $Date: 2006/05/12 14:14:13 $
 */
abstract class Frame {
    final static byte[] EMPTY = new byte[0];

    /** The call object */
    protected Call _call;

    /** The timestamp */
    protected Long _timestamp;

    /** The F bit */
    protected boolean _fullBit;

    /** The source call number */
    protected int _sCall;

    /** The data */
    protected ByteBuffer _data;



    /**
     * Sets the timestamp as int.
     *
     * @param v The timestamp
     * @see #setTimestamp(Long)
     */
    void setTimestampVal(long v) {
        _timestamp = new Long(v);
    }


    /**
     * Sets the timestamp as Integer object.
     *
     * @param val The timestamp
     * @see #setTimestampVal(long)
     */
    void setTimestamp(Long val) {
        _timestamp = val;
    }



    /**
     * Returns the timestamp as int
     *
     * @return the timestamp
     * @see #getTimestamp
     */
    long getTimestampVal() {
        long ret = 0;
        if (_timestamp != null) {
            ret = _timestamp.longValue();
        }
        return ret;
    }


    /**
     * Returns the timestamp as Integer object
     *
     * @return the timestamp
     * @see #getTimestampVal
     */
    Long getTimestamp() {
        return _timestamp;
    }


    /**
     * arrived is called when a packet arrives.
     *
     * @throws IAX2ProtocolException
     */
    abstract void arrived() throws IAX2ProtocolException;


    /**
     * ack is called to send any required response.
     */
    abstract void ack();
}

