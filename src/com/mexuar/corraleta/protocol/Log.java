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

package com.mexuar.corraleta.protocol;

/**
 * Dumb substitute for a logger
 *
 * @author <a href="mailto:thp@westhawk.co.uk">Tim Panton</a>
 * @version $Revision: 1.10 $ $Date: 2006/03/10 11:10:46 $
 */
public class Log {
    /**
     * Log level ALL
     */
    public static int ALL = 9;
    /**
     * Log level PROL(IX)
     */
    public static int PROL = 6;
    /**
     * Log level VERB(OSE)
     */
    public static int VERB = 5;
    /**
     * Log level DEBUG
     */
    public static int DEBUG = 4;
    /**
     * Log level INFO(RMATION)
     */
    public static int INFO = 3;
    /**
     * Log level WARN(ING)
     */
    public static int WARN = 2;
    /**
     * Log level ERROR (default)
     */
    public static int ERROR = 1;
    /**
     * Log level NONE
     */
    public static int NONE = 0;
    private static int _level = 1;


    /**
     * Constructor for the Log object
     */
    public Log() { }


    /**
     * Sets the log level 
     *
     * @param level The new level value
     */
    public static void setLevel(int level) {
        _level = level;
    }


    /**
     * Returns the log level 
     *
     * @return The level value
     */
    public static int getLevel() {
        return _level;
    }


    /**
     * Logs a warning message.
     * The message will only be logged if the log level is greater or
     * equal then the WARN level.
     *
     * @param string The text to log
     */
    public static void warn(String string) {
        if (_level >= WARN) {
            String message = "WARN: " + System.currentTimeMillis() + " " + Thread.currentThread().getName() + "->" + string;
            System.out.println(message);
        }
    }


    /**
     * Logs a debug message.
     * The message will only be logged if the log level is greater or
     * equal then the DEBUG level.
     *
     * @param string The text to log
     */
    public static void debug(String string) {
        if (_level >= DEBUG) {
            String message = "DEBUG:" + System.currentTimeMillis() + " " + Thread.currentThread().getName() + "->" + string;
            System.out.println(message);
        }
    }


    /**
     * Logs a verbose message.
     * The message will only be logged if the log level is greater or
     * equal then the VERB level.
     *
     * @param string The text to log
     */
    public static void verb(String string) {
        if (_level >= VERB) {
            String message = "VERB: " + System.currentTimeMillis() + " " + Thread.currentThread().getName() + "->" + string;
            System.out.println(message);
        }
    }

    /**
     * Logs a (very ?) verbose message.
     * The message will only be logged if the log level is greater or
     * equal then the PROL level.
     *
     * @param string The text to log
     */
    public static void prol(String string) {
        if (_level >= VERB) {
            String message = "PROL: " + System.currentTimeMillis() + " " + Thread.currentThread().getName() + "->" + string;
            System.out.println(message);
        }
    }


    /**
     * Prints where this message was called from, via a stack trace.
     */
    public static void where() {
        Exception x = new Exception("Called From");
        x.printStackTrace();
    }

}

