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

package com.mexuar.corraleta.audio;

/**
 * class to encapsulate the concept of an audio buffer and it's state
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */
public class ABuffer {
  private byte[] _buff;
  private boolean _written;
  private long _stamp;
    private long _astamp;

    public ABuffer(int sz){
    _buff = new byte[sz];
  }
  public byte [] getBuff(){
    return _buff;
  }
  public boolean isWritten(){
    return _written;
  }
  public void setWritten(){
    _written = true;
  }
  public void setRead(){
    _written = false;
  }
  public long getStamp(){
      return _stamp;
  }
  public void setStamp(long stamp){
      _stamp = stamp;
  }

  public long getAStamp(){
      return _astamp;
  }
  public void setAStamp(long as){
      _astamp = as;
  }
}
