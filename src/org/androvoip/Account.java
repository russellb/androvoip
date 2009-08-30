/*
 * AndroVoIP -- VoIP for Android.
 *
 * Copyright (C), 2009, Jason Parker
 * 
 * Jason Parker <north@ntbox.com>
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

package org.androvoip;

import java.io.Serializable;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class Account implements Serializable {
	private static final long serialVersionUID = 1539547929660026657L;

	public enum Protocol {
		None,
		IAX2,
	};
	
	Protocol mProtocol;
	String mHost;
	String mUserName;
	String mPassword;
	
	public Account(SharedPreferences prefs) {
		refresh(prefs);
	}
	
	public void save(SharedPreferences prefs) {
		Editor prefsEditor = prefs.edit();
		prefsEditor.putString("protocol", mProtocol.toString());
		prefsEditor.putString("host", mHost);
		prefsEditor.putString("username", mUserName);
		prefsEditor.putString("password", mPassword);
		prefsEditor.commit();
	}
	
	public void refresh(SharedPreferences prefs) {
		mProtocol = Protocol.valueOf(prefs.getString("protocol", "None"));
		mHost = prefs.getString("host", "");
		mUserName = prefs.getString("username", "");
		mPassword = prefs.getString("password", "");
	}
	
	public Protocol getProtocol() {
		return this.mProtocol;
	}
	
	public void setProtocol(Protocol protocol) {
		this.mProtocol = protocol;
	}
	
	public void setProtocol(String protocol) {
		this.mProtocol = Protocol.valueOf(protocol);
	}
	
	public String getHost() {
		return this.mHost;
	}
	
	public void setHost(String host) {
		this.mHost = host;
	}
	
	public String getUserName() {
		return this.mUserName;
	}
	
	public void setUserName(String username) {
		this.mUserName = username;
	}
	
	public String getPassword() {
		return this.mPassword;
	}
	
	public void setPassword(String password) {
		this.mPassword = password;
	}
}
