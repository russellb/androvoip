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

/**
 * Minimal events sent by the protocol engine.
 *
 * @author <a href="mailto:thp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 1.7 $ $Date: 2006/04/03 16:26:26 $
 */
public interface ProtocolEventListener {
    public void newCall(Call c);
    public void registered(Friend f, boolean s);
    public void hungUp(Call c);
    public void ringing(Call c);
    public void answered(Call c);
    public void setHostReachable(Friend f, boolean b, int roundtrip);
}

