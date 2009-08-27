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

/**
 * TODO Investigate handling of calls that end in receiving a REJECT.
 *      It does not appear that we get any notification from the stack
 *      when this happens.
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
import android.os.RemoteException;
import android.util.Log;

import com.mexuar.corraleta.protocol.Call;
import com.mexuar.corraleta.protocol.CallManager;
import com.mexuar.corraleta.protocol.Friend;
import com.mexuar.corraleta.protocol.ProtocolEventListener;
import com.mexuar.corraleta.protocol.netse.BinderSE;

public class IAX2Service extends Service implements ProtocolEventListener,
		CallManager {
	private String lastHost = "";
	private String lastUsername = "";
	private String lastPassword = "";
	private BinderSE binder = null;
	private Friend friend = null;
	private Call call = null;
	private boolean registered = false;
	private boolean registerSent = false;
	private AndroidAudioInterface audioInterface = null;

	private final IAX2ServiceAPI.Stub apiBinder = new IAX2ServiceAPI.Stub() {
		public boolean getRegistrationStatus() throws RemoteException {
			return IAX2Service.this.registered;
		}

		public void refreshIAX2Registration() throws RemoteException {
			IAX2Service.this.refreshIAX2Binder();
		}

		public boolean dial(String num) throws RemoteException {
			if (IAX2Service.this.call != null) {
				Log.w("IAX2Service", "Can not dial, call in progress");
				return false;
			}

			if (IAX2Service.this.friend == null) {
				Log.w("IAX2Service", "Can not dial, not registered");
				return false;
			}

			IAX2Service.this.call = IAX2Service.this.friend.newCall(
					IAX2Service.this.lastUsername,
					IAX2Service.this.lastPassword, num, "", "");

			return true;
		}
	};

	/**
	 * @see android.app.Service#onBind(android.content.Intent)
	 */
	@Override
	public IBinder onBind(Intent intent) {
		refreshIAX2Binder();
		return this.apiBinder;
	}

	/**
	 * @see android.app.Service#onCreate()
	 */
	@Override
	public void onCreate() {
		Log.d("IAX2Service", "onCreate()");
		this.audioInterface = new AndroidAudioInterface();
	}

	private synchronized void refreshIAX2Binder() {
		final String host = getConfigParam("host");
		final String username = getConfigParam("username");
		final String password = getConfigParam("password");

		Log.d("IAX2Service", "onStart: host - " + host);
		Log.d("IAX2Service", "onStart: last_host - " + this.lastHost);
		Log.d("IAX2Service", "onStart: username - " + username);
		Log.d("IAX2Service", "onStart: last_username - " + this.lastUsername);
		Log.d("IAX2Service", "onStart: password - " + password);
		Log.d("IAX2Service", "onStart: last_password - " + this.lastPassword);

		/* If there is no host set, or the host changed, kill off the binder. */
		if (host.equals("") || !host.equals(this.lastHost)) {
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
				this.binder = new BinderSE(host, this.audioInterface);
				this.lastHost = host;
				this.lastUsername = "";
				this.lastPassword = "";
			}
		} catch (final SocketException e) {
			e.printStackTrace();
		}

		if (username.equals(this.lastUsername)
				&& password.equals(this.lastPassword) && this.registerSent) {
			return;
		}

		try {
			if (this.registered) {
				this.binder.unregister(this);
				this.registered = false;
				this.registerSent = false;
			}
			this.binder.register(username, password, this, true);
			this.lastUsername = username;
			this.lastPassword = password;
			this.registerSent = true;
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Called due to a call to startService().
	 * 
	 * @see android.app.Service#onStart(android.content.Intent, int)
	 */
	@Override
	public void onStart(Intent arg0, int start_id) {
		/* This shouldn't be called. */
		Log.e("IAX2Service", "onStart() called unexpectedly.");
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
				this.registerSent = false;
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
		Log.i("IAX2Service", "Call has been answered: " + c.toString());
	}

	/**
	 * @see com.mexuar.corraleta.protocol.ProtocolEventListener#hungUp(com.mexuar.corraleta.protocol.Call)
	 */
	public void hungUp(Call c) {
		Log.i("IAX2Service", "Call hung up: " + c.toString());
		if (this.call == c) {
			this.call = null;
		}
	}

	/**
	 * @see com.mexuar.corraleta.protocol.ProtocolEventListener#newCall(com.mexuar.corraleta.protocol.Call)
	 */
	public void newCall(Call c) {
		this.call = c;
		Log.i("IAX2Service", "New call: " + this.call.toString());
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
				+ " " + mText, f != null ? f.getStatus() : "", pendingIntent);
		notification.flags |= Notification.FLAG_AUTO_CANCEL;
		notificationManager.notify(0, notification);

		this.registered = s;
		if (s) {
			this.friend = f;
		} else if (this.friend != null) {
			this.friend.stop();
			this.friend = null;
		}
	}

	/**
	 * @see com.mexuar.corraleta.protocol.ProtocolEventListener#ringing(com.mexuar.corraleta.protocol.Call)
	 */
	public void ringing(Call c) {
		Log.i("IAX2Service", "Call is ringing: " + c.toString());
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
	private String getConfigParam(String arg) {
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
