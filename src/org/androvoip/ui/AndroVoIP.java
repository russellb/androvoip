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
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.os.Bundle;

public class AndroVoIP extends TabActivity {	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        final TabHost tab_host = getTabHost();
        tab_host.addTab(tab_host.newTabSpec("dialer_tab").setIndicator("Dialer").setContent(R.id.textview1));
        tab_host.addTab(tab_host.newTabSpec("status_tab").setIndicator("Status").setContent(R.id.textview2));
        tab_host.setCurrentTab(0);
    }
    
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.options_menu, menu);
        return true;
    }
    
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
        case R.id.settings:
        	/* neat */
        	break;
        case R.id.about:
        	final Intent i = new Intent();
        	startActivity(i.setClassName("org.androvoip.ui", "org.androvoip.ui.About"));
        	break;
        }
        return false;
    }
}