// NAME
//      $RCSfile: ProtocolEventListener.java,v $
// DESCRIPTION
//      [given below in javadoc format]
// DELTA
//      $Revision: 1.7 $
// CREATED
//      $Date: 2006/04/03 16:26:26 $
// COPYRIGHT
//      Mexuar Technologies Ltd
// TO DO
//
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

