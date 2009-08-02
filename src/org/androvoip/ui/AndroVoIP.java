package org.androvoip.ui;

import org.androvoip.ui.R;

import android.app.Activity;
import android.os.Bundle;

public class AndroVoIP extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }
}