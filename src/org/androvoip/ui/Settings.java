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

package org.androvoip.ui;

import org.androvoip.R;
import org.androvoip.iax2.IAX2ServiceAPI;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class Settings extends Activity implements OnClickListener,
		ServiceConnection {
	public static final String PREFS_FILE = "AndroVoIP_settings";
	private IAX2ServiceAPI serviceConnection = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		((Button) findViewById(R.id.settings_save)).setOnClickListener(this);
		((Button) findViewById(R.id.settings_cancel)).setOnClickListener(this);

		setField(R.id.host_text, "host");
		setField(R.id.username_text, "username");
		setField(R.id.password_text, "password");

		bindService(new Intent().setClassName("org.androvoip",
				"org.androvoip.iax2.IAX2Service"), this, BIND_AUTO_CREATE);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (this.serviceConnection != null) {
			unbindService(this);
			this.serviceConnection = null;
		}
	}

	private void setField(int id, String key) {
		((EditText) findViewById(id)).setText(getSharedPreferences(PREFS_FILE,
				MODE_PRIVATE).getString(key, ""));
	}

	private String getStringById(int id) {
		return ((EditText) findViewById(id)).getText().toString();
	}

	private void buttonSave() {
		getSharedPreferences(PREFS_FILE, MODE_PRIVATE).edit().putString("host",
				getStringById(R.id.host_text)).putString("username",
				getStringById(R.id.username_text)).putString("password",
				getStringById(R.id.password_text)).commit();

		if (this.serviceConnection == null) {
			Log.e("AndroVoIP", "Connection to IAX2Service not present when saving config.");
			bindService(new Intent().setClassName("org.androvoip",
					"org.androvoip.iax2.IAX2Service"), this, BIND_AUTO_CREATE);
			return;
		}

		try {
			this.serviceConnection.refreshIAX2Registration();
		} catch (final RemoteException e) {
			/* Lost connection. */
			e.printStackTrace();
		}

		finish();
	}

	private void buttonCancel() {
		// Don't need to save anything, so...just exit.
		finish();
	}

	public void onClick(View v) {
		if (v == findViewById(R.id.settings_save)) {
			buttonSave();
		} else if (v == findViewById(R.id.settings_cancel)) {
			buttonCancel();
		}
	}

	public void onServiceConnected(ComponentName arg0, IBinder arg1) {
		this.serviceConnection = IAX2ServiceAPI.Stub.asInterface(arg1);
	}

	public void onServiceDisconnected(ComponentName arg0) {
		this.serviceConnection = null;
	}
}
