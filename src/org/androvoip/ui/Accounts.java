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

package org.androvoip.ui;

import org.androvoip.R;
import org.androvoip.Account;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class Accounts extends ListActivity implements OnItemClickListener {
	private final String PREFS_FILE = "AndroVoIP_settings";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accounts);
		
		ListView listView = getListView();
		listView.setOnItemClickListener(this);
		listView.setEmptyView(findViewById(R.id.empty));
		refresh();
	}
	
	@Override
	public void onResume() { 
		super.onResume();
		refresh();
	}
	
	private void refresh() {
		Account[] accounts = new Account[1];
		accounts[0] = new Account(getSharedPreferences(PREFS_FILE, MODE_PRIVATE));

		getListView().setAdapter(new AccountsListAdapter(accounts));
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Account account = (Account) parent.getItemAtPosition(position);
		Intent intent = new Intent(this, Settings.class);
		intent.putExtra("account", account);
		startActivity(intent);
	}
	
	class AccountsListAdapter extends ArrayAdapter<Account> {
		AccountsListAdapter(Account[] accounts) {
			super(Accounts.this, R.layout.accountsrow, accounts);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			Account account = getItem(position);
			LayoutInflater inflater = getLayoutInflater();
			View view = inflater.inflate(R.layout.accountsrow, null);
			
			if (view == null) {
				return super.getView(position, convertView, parent);
			}
			((TextView) view.findViewById(R.id.protocol)).setText(account.getProtocol().toString());
			((TextView) view.findViewById(R.id.host)).setText(account.getHost());
			((TextView) view.findViewById(R.id.username)).setText(account.getUserName());
			
			return view;
		}
	}
}
