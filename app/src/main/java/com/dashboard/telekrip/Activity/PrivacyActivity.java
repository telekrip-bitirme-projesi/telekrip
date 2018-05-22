package com.dashboard.telekrip.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dashboard.telekrip.R;

public class PrivacyActivity extends Activity {

    private ImageButton _ibUserPanel;
    private TextView _tvUserPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy);

        uiInitialization();
    }

    private void uiInitialization() {
        _ibUserPanel = findViewById(R.id.ibUserPanel);
        _tvUserPanel = findViewById(R.id.tvUserPanel);

        _ibUserPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        _tvUserPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.left,R.transition.out_right);
    }
}
