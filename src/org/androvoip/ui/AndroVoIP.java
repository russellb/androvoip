/*
 * Copyright (C), 2009, Russell Bryant
 * 
 * Russell Bryant <russell@russellbryant.net>
 * 
 * This file is part of AndroVoIP.
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

import org.androvoip.ui.R;

import android.app.TabActivity;
import android.widget.TabHost;
import android.os.Bundle;

public class AndroVoIP extends TabActivity {
	private TabHost mTabHost;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        mTabHost = getTabHost();
        
        mTabHost.addTab(mTabHost.newTabSpec("dialer_tab").setIndicator("Dialer").setContent(R.id.textview1));
        mTabHost.addTab(mTabHost.newTabSpec("settings_tab").setIndicator("Settings").setContent(R.id.textview2));
        mTabHost.addTab(mTabHost.newTabSpec("status_tab").setIndicator("Status").setContent(R.id.textview3));
        
        mTabHost.setCurrentTab(0);

    }
}