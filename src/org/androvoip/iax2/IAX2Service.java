/*
 * AndroVoIP -- VoIP for Android.
 *
 * Copyright (C), 2009, Russell Bryant
 * 
 * Russell Bryant <russell@russellbryant.net>
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

package org.androvoip.iax2;

import java.net.SocketException;

import org.androvoip.ui.Settings;

import com.mexuar.corraleta.protocol.netse.BinderSE;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class IAX2Service extends Service {
	private String last_host;
	private String last_username;
	private String last_password;
	private BinderSE binder;
	private boolean registered;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		Log.d("IAX2Service", "onCreate(), initializing vars.");
		last_host = "";
		last_username = "";
		last_password = "";
		binder = null;
		registered = false;
	}
	
	@Override
	public void onStart(Intent arg0, int start_id) {
		final String host = get_config_param("host");
		final String username = get_config_param("username");
		final String password = get_config_param("password");
		
		Log.d("IAX2Service", "onStart: host - " + host);
		Log.d("IAX2Service", "onStart: last_host - " + last_host);
		Log.d("IAX2Service", "onStart: username - " + username);
		Log.d("IAX2Service", "onStart: last_username - " + last_username);
		Log.d("IAX2Service", "onStart: password - " + password);
		Log.d("IAX2Service", "onStart: last_password - " + last_password);
		
		/* If there is no host set, or the host changed, kill off the binder. */
		if (host.equals("") || !host.equals(last_host)) {
			if (binder != null) {
				binder.stop();
				binder = null;
			}
			if (host.equals("")) {
				return;
			}
		}
		
		/* Start the binder if we have not done so already. */
		try {
			if (binder == null) {
				binder = new BinderSE(host, null);
				last_host = host;
				last_username = "";
				last_password = "";
			}
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		if (username.equals(last_username) && password.equals(last_password) && registered) {
			return;
		}
		
		try {
			if (registered) {
				binder.unregister(null);		
			}
			binder.register(username, password, null, true);
			last_username = username;
			last_password = password;
			registered = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String get_config_param(String arg) {
		return getSharedPreferences(Settings.PREFS_FILE, MODE_PRIVATE)
				.getString(arg, "");
	}
}
