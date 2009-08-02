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

import java.io.IOException;

/**
 * Generic protocol exception for IAX2 Protocol
 * @author <a href="mailto:thp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 1.3 $ $Date: 2006/02/10 15:42:47 $
 *
 */
public class IAX2ProtocolException extends IOException {
  static final long serialVersionUID = 1;
  
  public IAX2ProtocolException() {
  }

  public IAX2ProtocolException(String p0) {
    super(p0);
  }
}
