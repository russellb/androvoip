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

import android.app.TabActivity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TabHost.OnTabChangeListener;

public class AndroVoIP extends TabActivity implements OnTabChangeListener,
		ServiceConnection, OnClickListener {
	static final String DIALER_TAB = "dialer_tab";
	static final String STATUS_TAB = "status_tab";
	private IAX2ServiceAPI serviceConnection = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		final Intent intent = getIntent();
		setContentView(R.layout.main);

		final TabHost tab_host = getTabHost();
		tab_host.addTab(tab_host.newTabSpec(DIALER_TAB).setIndicator("Dialer",
				this.getResources().getDrawable(R.drawable.ic_tab_call))
				.setContent(R.id.dialer));
		tab_host.addTab(tab_host.newTabSpec(STATUS_TAB)
				.setIndicator("Status", this.getResources()
				.getDrawable(R.drawable.ic_tab_info_details))
				.setContent(R.id.status));
		tab_host.setCurrentTab(0);
		tab_host.setOnTabChangedListener(this);
		
		bindService(new Intent().setClassName("org.androvoip",
				"org.androvoip.iax2.IAX2Service"), this, BIND_AUTO_CREATE);

		((Button) findViewById(R.id.status_refresh)).setOnClickListener(this);
		((Button) findViewById(R.id.send_button)).setOnClickListener(this);

		((EditText) findViewById(R.id.dialer_number)).selectAll();

		if (Intent.ACTION_VIEW.equals(intent.getAction())) {
			final Uri data = intent.getData();
			final String scheme = data.getScheme();
			final String path = data.getSchemeSpecificPart();

			Log.d("org.androvoip", "Got a URI: " + scheme + " - " + path);
			
			/* TODO Parse the URI.  Does the IAX2 stack have a method for this? */
		}
	}

	@Override
	public void onDestroy() {
		super.onDestroy();

		if (this.serviceConnection != null) {
			unbindService(this);
			this.serviceConnection = null;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.options_menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		final Intent i = new Intent();

		switch (item.getItemId()) {
		case R.id.settings:
			startActivity(i.setClassName("org.androvoip",
					"org.androvoip.ui.Settings"));
			break;
		case R.id.about:
			startActivity(i.setClassName("org.androvoip",
					"org.androvoip.ui.About"));
			break;
		}

		return false;
	}

	private void setField(int id, String str) {
		((TextView) findViewById(id)).setText(str);
	}

	private void statusTabActive() {
		Log.d("AndroVoIP", "status tab is active.");
		statusRefresh();
	}

	public void onTabChanged(String arg0) {
		if (arg0.equals(STATUS_TAB)) {
			statusTabActive();
		}
	}

	public void onServiceConnected(ComponentName arg0, IBinder arg1) {
		this.serviceConnection = IAX2ServiceAPI.Stub.asInterface(arg1);
	}

	public void onServiceDisconnected(ComponentName arg0) {
		this.serviceConnection = null;
	}

	private void statusRefresh() {
		if (this.serviceConnection == null) {
			Log.e("AndroVoIP",
				"Connection to IAX2Service not present for status refresh.");
			bindService(new Intent().setClassName("org.androvoip",
					"org.androvoip.iax2.IAX2Service"), this, BIND_AUTO_CREATE);
			return;
		}

		try {
			final boolean regStatus = this.serviceConnection
					.getRegistrationStatus();

			setField(R.id.status_text, "Registration: "
					+ (regStatus ? "Registered" : "Not Registered"));

			Log.d("AndroVoIP", "Registration status is: " + regStatus);
		} catch (final RemoteException e) {
			/* Connection Lost. */
			e.printStackTrace();
		}
	}

	private String getStringById(int id) {
		return ((EditText) findViewById(id)).getText().toString();
	}

	private void dialNumber() {
		final String num = getStringById(R.id.dialer_number);
		boolean call_result;
		
		try {
			call_result = this.serviceConnection.dial(num);
		} catch (RemoteException e) {
			call_result = false;
			e.printStackTrace();
		}
		
		Log.i("AndroVoIP", "Request to dial number: " + num + " was " +
				(call_result ? "Successful" : "not Successful"));
	}

	public void onClick(View v) {
		if (v == findViewById(R.id.status_refresh)) {
			statusRefresh();
		} else if (v == findViewById(R.id.send_button)) {
			dialNumber();
		}
	}
}
