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

package com.mexuar.corraleta.ui;

import java.awt.*;
import java.applet.*;
import com.mexuar.corraleta.audio.javasound.AudioProperties;

/**
 * Swing phone implementation
 * @author <a href="mailto:thp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 1.13 $ $Date: 2006/05/10 10:49:42 $
 *
 */
public class BeanCanApplet
    extends Applet {
    private static final String version_id =
        "@(#)$Id: BeanCanApplet.java,v 1.13 2006/05/10 10:49:42 uid100 Exp $ Copyright Mexuar Technologies Ltd";

  private boolean isStandalone = false;
  String host = "lef.westhawk.co.uk";
  String user = "ijax2";
  String pass = "moofoo";
  Integer debug;
  BeanCanFrameManager bcf;
  private String audioIn;
  private String audioOut;

  //Get a parameter value
  public String getParameter(Object target, String key, String def) {
    String ret;
    if (target == null) {
      ret = getParameter(key, def);
    }
    else {
      ret = target.toString();
    }
    return ret;
  }

  //Get a parameter value
  public String getParameter(String key, String def) {
    return isStandalone ? System.getProperty(key, def) :
        (getParameter(key) != null ? getParameter(key) : def);
  }

  //Construct the applet
  public BeanCanApplet() {

  }

  //Initialize the applet
  public void init() {
    try {
      host = this.getParameter(host, "host", "lef.westhawk.co.uk");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    try {
      user = this.getParameter(user, "user", "ijax2");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    try {
      pass = this.getParameter(pass, "pass", "moofoo");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    try {
      debug = new Integer(this.getParameter(debug, "debug", "0"));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    try {
      audioIn = this.getParameter(audioIn, "AudioInputDevice", null);
      if (null != audioIn) {
        AudioProperties.setInputDeviceName(audioIn);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    try {
      audioOut = this.getParameter(audioOut, "AudioOutputDevice", null);
      if (null != audioOut){
        AudioProperties.setOutputDeviceName(audioOut);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }

    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  //Component initialization
  private void jbInit() throws Exception {
    bcf = new BeanCanFrameManager(true, debug.intValue(), host);
    bcf.validate();

  }

  //Start the applet
  public void start() {
    if (bcf.canRecord()) {
      restart();
    }
    else {
      CantRecordDialog crd = new CantRecordDialog(this, bcf,
                                                  "No permission to access microphone", false);
      crd.show();
    }
  }

  void restart() {
    if (bcf != null) {
      bcf.set_host(host);
      bcf.set_username(user);
      bcf.set_password(pass);
      bcf.set_debug(debug.intValue());
      bcf.start();
      bcf.register();

    }
  }

  //Stop the applet
  public void stop() {
    if (bcf != null) {
      bcf.stop();
    }
  }

  //Destroy the applet
  public void destroy() {
  }

  //Get Applet information
  public String getAppletInfo() {
    return "Westhawk's IJAX 2 softphone www.westhawk.co.uk";
  }

  //Get parameter info
  public String[][] getParameterInfo() {
    String[][] pinfo = {
        {
        "host", "String", ""}
        , {
        "user", "String", ""}
        , {
        "pass", "String", ""}
        , {
        "debug", "int", ""}
        , {
        "audioIn", "String", ""}
        , {
        "audioOut", "String", ""}
    };
    return pinfo;
  }

  //Main method
  public static void main(String[] args) {
    BeanCanApplet applet = new BeanCanApplet();
    applet.isStandalone = true;
    Frame frame;
    frame = new Frame();
    frame.setTitle("Applet Frame");
    frame.add(applet, BorderLayout.CENTER);
    applet.init();
    applet.start();
    frame.setSize(400, 320);
    Dimension d = Toolkit.getDefaultToolkit().getScreenSize();
    frame.setLocation( (d.width - frame.getSize().width) / 2,
                      (d.height - frame.getSize().height) / 2);
    frame.setVisible(true);
  }
}
