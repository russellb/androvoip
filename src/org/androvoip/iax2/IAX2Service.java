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
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent arg0, int start_id) {
		final String host = get_config_param("host");
		BinderSE binder = null;
		
		Log.d("IAX2Service", "host - " + host);

		try {
			binder = new BinderSE(host, null);
		} catch (SocketException e) {
			e.printStackTrace();
		}
		
		new Thread(binder).start();
		
		try {
			binder.register(get_config_param("username"),
					get_config_param("password"), null, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private String get_config_param(String arg) {
		return getSharedPreferences(Settings.PREFS_FILE, MODE_PRIVATE)
				.getString(arg, "");
	}
}
