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

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Settings extends Activity {
	public static final String PREFS_FILE = "AndroVoIP_settings";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);

		((Button) findViewById(R.id.settings_save))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						getSharedPreferences(PREFS_FILE, MODE_PRIVATE).edit()
								.putString("host",
										get_string_by_id(R.id.host_text))
								.putString("username",
										get_string_by_id(R.id.username_text))
								.putString("password",
										get_string_by_id(R.id.password_text))
								.commit();

						startService(new Intent().setClassName("org.androvoip",
								"org.androvoip.iax2.IAX2Service"));
						finish();
					}
				});
		((Button) findViewById(R.id.settings_cancel))
				.setOnClickListener(new View.OnClickListener() {
					public void onClick(View v) {
						// Don't need to save anything, so...just exit.
						finish();
					}
				});

		set_field(R.id.host_text, "host");
		set_field(R.id.username_text, "username");
		set_field(R.id.password_text, "password");
	}

	private void set_field(int id, String key) {
		((EditText) findViewById(id)).setText(getSharedPreferences(PREFS_FILE,
				MODE_PRIVATE).getString(key, ""));
	}

	private String get_string_by_id(int id) {
		return ((EditText) findViewById(id)).getText().toString();
	}

}
