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

import java.applet.*;

import java.awt.*;
import java.awt.Frame;
import java.awt.event.*;
import javax.swing.*;

import com.mexuar.corraleta.protocol.*;

public class CantRecordDialog
    extends JDialog {
  private static final String version_id =
          "@(#)$Id: CantRecordDialog.java,v 1.3 2006/02/10 15:00:15 uid6102 Exp $ Copyright Mexuar Technologies Ltd";

  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JLabel jLabel1 = new JLabel();
  JButton jButton1 = new JButton();
  JPanel jPanel1 = new JPanel();
  JButton jButton2 = new JButton();
  JLabel jLabel2 = new JLabel();
  JTextArea jTextArea1 = new JTextArea();
  JPanel jPanel2 = new JPanel();
  FlowLayout flowLayout1 = new FlowLayout();
  GridLayout gridLayout1 = new GridLayout();
  JLabel jLabel3 = new JLabel();

  public CantRecordDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch (Exception ex) {
      ex.printStackTrace();
    }
  }

  Applet _applet = null;
  public CantRecordDialog(Applet a, Frame frame, String title, boolean modal) {
    this(frame, title, modal);
    _applet = a;
  }

  public CantRecordDialog() {
    this(null, "", false);
  }

  private void jbInit() throws Exception {
    panel1.setLayout(borderLayout1);
    jLabel1.setFont(new java.awt.Font("Lucida Grande", 1, 15));
    jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel1.setHorizontalTextPosition(SwingConstants.CENTER);
    jLabel1.setText("No permission to access microphone!");
    jButton1.setText("Continue");
    jButton1.addActionListener(new CantRecordDialog_jButton1_actionAdapter(this));
    jButton2.setText("Quit");
    jButton2.addActionListener(new CantRecordDialog_jButton2_actionAdapter(this));
    jLabel2.setText(
        "<html><body><p>This applet does not have permission to capture audio " +
        "from your microphone. </p><p>You must either buy a signed copy of " +
        "the applet from lef.westhawk.co.uk <br> or add the following to your .java.policy " +
        "file:</p></body</html>");
    jTextArea1.setText(
        "grant {\npermission\njavax.sound.sampled.AudioPermission \"record\";\n};");
    jPanel1.setLayout(flowLayout1);
    jPanel2.setLayout(gridLayout1);
    gridLayout1.setColumns(1);
    gridLayout1.setRows(3);
    jLabel3.setText("<html><body><p>Alternatively, you can continue without access to a microphone.</p></body></html>");
    getContentPane().add(panel1);
    panel1.add(jPanel1, BorderLayout.SOUTH);
    jPanel1.add(jButton1, null);
    jPanel1.add(jButton2, null);
    panel1.add(jPanel2, BorderLayout.CENTER);
    jPanel2.add(jLabel2, null);
    jPanel2.add(jTextArea1, null);
    jPanel2.add(jLabel3, null);
    panel1.add(jLabel1, BorderLayout.NORTH);
  }

  void jButton2_actionPerformed(ActionEvent e) {
    Applet c = getApplet();
    if (c instanceof BeanCanApplet) {
      BeanCanApplet bca = (BeanCanApplet) c;
      bca.stop();
    }
    else {
      Log.warn("Huh? Parent is not our applet ?");
    }
    this.setVisible(false);
  }

  void jButton1_actionPerformed(ActionEvent e) {
    Applet c = getApplet();
    if (c instanceof BeanCanApplet) {
      BeanCanApplet bca = (BeanCanApplet) c;
      bca.restart();
    }
    else {
      Log.warn("Huh? Parent is not our applet ?");
    }
    this.setVisible(false);
  }

  /**
   * getApplet
   *
   * @return Applet
   */
  private Applet getApplet() {
    return _applet;
  }
}

class CantRecordDialog_jButton2_actionAdapter
    implements java.awt.event.ActionListener {
  CantRecordDialog adaptee;

  CantRecordDialog_jButton2_actionAdapter(CantRecordDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jButton2_actionPerformed(e);
  }
}

class CantRecordDialog_jButton1_actionAdapter
    implements java.awt.event.ActionListener {
  CantRecordDialog adaptee;

  CantRecordDialog_jButton1_actionAdapter(CantRecordDialog adaptee) {
    this.adaptee = adaptee;
  }

  public void actionPerformed(ActionEvent e) {
    adaptee.jButton1_actionPerformed(e);
  }
}
