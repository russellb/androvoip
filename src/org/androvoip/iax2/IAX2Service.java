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

import org.androvoip.R;
import org.androvoip.ui.AndroVoIP;
import org.androvoip.ui.Settings;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.mexuar.corraleta.protocol.Call;
import com.mexuar.corraleta.protocol.CallManager;
import com.mexuar.corraleta.protocol.Friend;
import com.mexuar.corraleta.protocol.ProtocolEventListener;
import com.mexuar.corraleta.protocol.netse.BinderSE;

public class IAX2Service extends Service implements ProtocolEventListener,
		CallManager {
	private String last_host;
	private String last_username;
	private String last_password;
	private BinderSE binder;
	private boolean registered;
	private AndroidAudioInterface audio_interface;

	/**
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	/**
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		Log.d("IAX2Service", "onCreate(), initializing vars.");
		this.last_host = "";
		this.last_username = "";
		this.last_password = "";
		this.binder = null;
		this.registered = false;
		this.audio_interface = new AndroidAudioInterface();
	}

	/**
	 * Called due to a call to startService().
	 * <p>
	 * This function is a bit hackish right now, but it's a start. Essentially,
	 * every time the AndroVoIP Activities decide that the IAX2 service should
	 * refresh itself and check to see if it needs to change registration
	 * parameters, it calls startService(). Note that this will get called on
	 * every startService() call, but onCreate() is what only gets called once
	 * when a call to startService() results in the service starting up.
	 * <p>
	 * Eventually, we should move to creating an API for interacting with this
	 * service. Instead of startService(), Activities will bind to the service
	 * using bindService(). With this approach, a service can provide an API for
	 * the activity to interact with it after binding.
	 * 
	 * @see android.app.Service#onStart(android.content.Intent, int)
	 */
	@Override
	public void onStart(Intent arg0, int start_id) {
		final String host = get_config_param("host");
		final String username = get_config_param("username");
		final String password = get_config_param("password");

		Log.d("IAX2Service", "onStart: host - " + host);
		Log.d("IAX2Service", "onStart: last_host - " + this.last_host);
		Log.d("IAX2Service", "onStart: username - " + username);
		Log.d("IAX2Service", "onStart: last_username - " + this.last_username);
		Log.d("IAX2Service", "onStart: password - " + password);
		Log.d("IAX2Service", "onStart: last_password - " + this.last_password);

		/* If there is no host set, or the host changed, kill off the binder. */
		if (host.equals("") || !host.equals(this.last_host)) {
			if (this.binder != null) {
				this.binder.stop();
				this.binder = null;
			}
			if (host.equals("")) {
				return;
			}
		}

		/* Start the binder if we have not done so already. */
		try {
			if (this.binder == null) {
				this.binder = new BinderSE(host, this.audio_interface);
				this.last_host = host;
				this.last_username = "";
				this.last_password = "";
			}
		} catch (final SocketException e) {
			e.printStackTrace();
		}

		if (username.equals(this.last_username)
				&& password.equals(this.last_password) && this.registered) {
			return;
		}

		try {
			if (this.registered) {
				this.binder.unregister(this);
				this.registered = false;
			}
			this.binder.register(username, password, this, true);
			this.last_username = username;
			this.last_password = password;
			// this.registered = true;
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see android.app.Service#onDestroy()
	 */
	@Override
	public void onDestroy() {
		if (this.binder == null) {
			return;
		}

		try {
			if (this.registered) {
				this.binder.unregister(this);
				this.registered = false;
			}
			this.binder.stop();
			this.binder = null;
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see com.mexuar.corraleta.protocol.ProtocolEventListener#answered(com.mexuar.corraleta.protocol.Call)
	 */
	public void answered(Call c) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see com.mexuar.corraleta.protocol.ProtocolEventListener#hungUp(com.mexuar.corraleta.protocol.Call)
	 */
	public void hungUp(Call c) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see com.mexuar.corraleta.protocol.ProtocolEventListener#newCall(com.mexuar.corraleta.protocol.Call)
	 */
	public void newCall(Call c) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see com.mexuar.corraleta.protocol.ProtocolEventListener#registered(com.mexuar.corraleta.protocol.Friend,
	 *      boolean)
	 */
	public void registered(Friend f, boolean s) {
		final Intent intent = new Intent(this, AndroVoIP.class);
		final PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
				intent, 0);

		final NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

		final CharSequence mText = s ? "Registered" : "Unregistered";
		final Notification notification = new Notification(R.drawable.icon,
				getString(R.string.app_name) + " " + mText, System
						.currentTimeMillis());

		notification.setLatestEventInfo(this, getString(R.string.app_name)
				+ " " + mText, f.getStatus(), pendingIntent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(0, notification);

		this.registered = s;
	}

	/**
	 * @see com.mexuar.corraleta.protocol.ProtocolEventListener#ringing(com.mexuar.corraleta.protocol.Call)
	 */
	public void ringing(Call c) {
		// TODO Auto-generated method stub
	}

	/**
	 * @see com.mexuar.corraleta.protocol.ProtocolEventListener#setHostReachable(com.mexuar.corraleta.protocol.Friend,
	 *      boolean, int)
	 */
	public void setHostReachable(Friend f, boolean b, int roundtrip) {
		// TODO Auto-generated method stub
	}

	/**
	 * Retrieve a configuration value.
	 * 
	 * @param arg
	 *            configuration parameter to retrieve
	 * 
	 * @return String configuration value
	 */
	private String get_config_param(String arg) {
		return getSharedPreferences(Settings.PREFS_FILE, MODE_PRIVATE)
				.getString(arg, "");
	}

	/**
	 * Return whether or not we will handle this call.
	 * 
	 * @return Returning false immediately rejects the call. Returning true
	 *         means that we will give the user the opportunity to consider
	 *         answering it, and will request that the call be answered if the
	 *         user decides that is what should happen.
	 * 
	 * @see com.mexuar.corraleta.protocol.CallManager#accept(com.mexuar.corraleta.protocol.Call)
	 */
	public boolean accept(Call ca) {
		return false;
	}
}
