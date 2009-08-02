// NAME
//      $RCSfile: IAX2ProtocolException.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.3 $
// CREATED
//      $Date: 2006/02/10 15:42:47 $
// COPYRIGHT
//      Mexuar Technologies Ltd
// TO DO
//
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
