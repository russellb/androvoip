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

package com.mexuar.corraleta.audio.javasound;

import java.io.*;
import javax.sound.sampled.*;

/**
 * Test program to list available audio mixers
 * @author <a href="mailto:thp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 1.1 $ $Date: 2006/05/10 10:49:41 $
 *
 */
public class ShowAvailableAudio {
    private static final String     version_id =
        "@(#)$Id: ShowAvailableAudio.java,v 1.1 2006/05/10 10:49:41 uid100 Exp $ Copyright Westhawk Ltd";

  public ShowAvailableAudio() {
  }

  public static void main(String[] args) {
    ShowAvailableAudio showAvailableAudio1 = new ShowAvailableAudio();
    showAvailableAudio1.listMix(System.out);
    System.exit(0);
  }

  /**
   * list
   *
   * @param ps PrintStream
   */
  void listMix(PrintStream ps) {
    Mixer.Info[] mixI = AudioSystem.getMixerInfo();
    for (int i = 0; i < mixI.length; i++) {
      Mixer.Info mi = mixI[i];
      ps.println(mi.getClass().getName() + " " + mi.getName() + " " +
                 mi.getVendor());
      Mixer m = AudioSystem.getMixer(mi);
      listLines(ps, m);
    }
  }

  /**
   * listLines
   *
   * @param ps PrintStream
   * @param m Mixer
   */
  void listLines(PrintStream ps, Mixer m) {
    Line.Info[] infos = m.getSourceLineInfo();
// or:
// Line.Info[] infos = AudioSystem.getTargetLineInfo();
    ps.println("\tSource lines");
    for (int i = 0; i < infos.length; i++) {
      if (infos[i] instanceof DataLine.Info) {
        DataLine.Info dataLineInfo = (DataLine.Info) infos[i];
        AudioFormat[] supportedFormats = dataLineInfo.getFormats();
        showFormats(ps, supportedFormats);
      }
    }
    infos = m.getTargetLineInfo();
    ps.println("\tTarget lines");
    for (int i = 0; i < infos.length; i++) {
      if (infos[i] instanceof DataLine.Info) {
        DataLine.Info dataLineInfo = (DataLine.Info) infos[i];
        AudioFormat[] supportedFormats = dataLineInfo.getFormats();
        showFormats(ps, supportedFormats);
      }
    }

  }

  /**
   * showFormats
   *
   * @param ps PrintStream
   * @param fmts AudioFormat[] supportedFormats
   */
  void showFormats(PrintStream ps, AudioFormat[] fmts) {
    for (int i=0;i<fmts.length;i++){
      AudioFormat af = fmts[i];
      ps.println("\t\t"+af.getEncoding()+" "+af.getSampleRate()+" "+af.getSampleSizeInBits());
    }
  }

}
