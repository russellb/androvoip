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
import javax.swing.*;
import java.awt.event.*;

/**
 *
 *
 * @author <a href="mailto:ray@westhawk.co.uk">Ray Tran</a>
 * @version $Revision: 1.3 $ $Date: 2006/02/10 15:42:47 $
 */
public class AboutDialog extends JDialog {
  private static final String     version_id =
        "@(#)$Id: AboutDialog.java,v 1.3 2006/02/10 15:42:47 uid6102 Exp $ Copyright Mexuar Technologies Ltd";
  JPanel panel1 = new JPanel();
  BorderLayout borderLayout1 = new BorderLayout();
  JButton jButton1 = new JButton();
  JPanel jPanel1 = new JPanel();
  JLabel jLabel1 = new JLabel();
  JLabel jLabel2 = new JLabel();

  public AboutDialog(Frame frame, String title, boolean modal) {
    super(frame, title, modal);
    try {
      jbInit();
      pack();
    }
    catch(Exception ex) {
      ex.printStackTrace();
    }
  }

  public AboutDialog() {
    this(null, "", false);
  }

  private void jbInit() throws Exception {
    panel1.setLayout(borderLayout1);
    jButton1.setText("Ok");
    jButton1.addActionListener(new AboutDialog_jButton1_actionAdapter(this));
    jLabel1.setHorizontalAlignment(SwingConstants.CENTER);
    jLabel1.setText("BeanCan");
    jLabel2.setText("<html><body><h1>BeanCan</h1><p>A pure java soft phone Copyright <a href=\"www.westhawk.co.uk\">Westhawk " +
    "Ltd 2005</a></p><p>"+this.getTitle()+"</p></body></html>");
    getContentPane().add(panel1);
    panel1.add(jPanel1, BorderLayout.SOUTH);
    jPanel1.add(jButton1, null);
    panel1.add(jLabel1, BorderLayout.NORTH);
    panel1.add(jLabel2, BorderLayout.CENTER);
  }

  void jButton1_actionPerformed(ActionEvent e) {
    this.hide();
  }
}

class AboutDialog_jButton1_actionAdapter implements java.awt.event.ActionListener {
  AboutDialog adaptee;

  AboutDialog_jButton1_actionAdapter(AboutDialog adaptee) {
    this.adaptee = adaptee;
  }
  public void actionPerformed(ActionEvent e) {
    adaptee.jButton1_actionPerformed(e);
  }
}
