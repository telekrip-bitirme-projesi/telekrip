package com.dashboard.telekrip.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.SwitchCompat;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dashboard.telekrip.R;
import com.dashboard.telekrip.Tools.Tools;

public class NotificationActivity extends Activity {

    ImageButton _ibUserPanel;
    SwitchCompat _sNotification,_sSound,_sVibration;
    TextView _tvUserPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        uiInitialization();
    }

    private void uiInitialization() {
        _ibUserPanel=findViewById(R.id.ibUserPanel);
        _tvUserPanel=findViewById(R.id.tvUserPanel);
        _sNotification=findViewById(R.id.sNotification);
        _sSound=findViewById(R.id.sSound);
        _sVibration=findViewById(R.id.sVibration);

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
        _sSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Tools.setSharedPrefences(NotificationActivity.this,"sound",true);
                }
                else {
                    Tools.setSharedPrefences(NotificationActivity.this,"sound",false);
                }
            }
        });

        _sVibration.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Tools.setSharedPrefences(NotificationActivity.this,"vibration",true);
                }
                else {
                    Tools.setSharedPrefences(NotificationActivity.this,"vibration",false);
                }
            }
        });

        _sNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b){
                    Tools.setSharedPrefences(NotificationActivity.this,"notification",true);
                    _sSound.setEnabled(true);
                    _sVibration.setEnabled(true);
                }
                else {
                    Tools.setSharedPrefences(NotificationActivity.this,"notification",false);
                    Tools.setSharedPrefences(NotificationActivity.this,"sound",false);
                    Tools.setSharedPrefences(NotificationActivity.this,"vibration",false);
                    _sSound.setChecked(false);
                    _sSound.setEnabled(false);
                    _sVibration.setChecked(false);
                    _sVibration.setEnabled(false);
                }
            }
        });

        if(!(boolean)Tools.getSharedPrefences(NotificationActivity.this,"notification",Boolean.class)){
            _sNotification.setChecked(false);
            _sSound.setChecked(false);
            _sVibration.setChecked(false);
            _sSound.setEnabled(false);
            _sVibration.setEnabled(false);
        }
        else {
            _sNotification.setChecked(true);
            if((boolean)Tools.getSharedPrefences(NotificationActivity.this,"sound",Boolean.class)){
                _sSound.setChecked(true);
            }
            if((boolean)Tools.getSharedPrefences(NotificationActivity.this,"vibration",Boolean.class)){
                _sVibration.setChecked(true);
            }
        }
    }
}
