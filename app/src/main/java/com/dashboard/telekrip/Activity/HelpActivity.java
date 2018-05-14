package com.dashboard.telekrip.Activity;

import android.app.Activity;
import android.os.Bundle;

import com.dashboard.telekrip.R;

public class HelpActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.left,R.transition.out_right);
    }
}
