package com.dashboard.telekrip.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.dashboard.telekrip.R;
import com.dashboard.telekrip.Tools.Tools;

public class SecurityOnOffActivity extends Activity {

    private int errorEntryPassword=0;
    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    private Button _btnResetPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security_on_off);

        uiInitialization();
    }

    private void uiInitialization() {
        _btnResetPassword = findViewById(R.id.btnResetPassword);
         PinLockListener mPinLockListener = new PinLockListener() {
            @Override
            public void onComplete(String pin) {
                if (Tools.getSharedPrefences(SecurityOnOffActivity.this, "security", String.class) == null) {
                    Tools.setSharedPrefences(SecurityOnOffActivity.this, "security", pin);
                    Intent userPanelActivity = new Intent(SecurityOnOffActivity.this, UserPanelActivity.class);
                    startActivity(userPanelActivity);
                    overridePendingTransition(R.transition.left, R.transition.out_right);
                    Toast.makeText(SecurityOnOffActivity.this, "Parola Oluşturuldu.", Toast.LENGTH_SHORT).show();
                    finish();
                } else if (_btnResetPassword.getVisibility() == View.VISIBLE) {
                    Tools.setSharedPrefences(SecurityOnOffActivity.this, "security", pin);
                    Intent userPanelActivity = new Intent(SecurityOnOffActivity.this, UserPanelActivity.class);
                    startActivity(userPanelActivity);
                    overridePendingTransition(R.transition.left, R.transition.out_right);
                    Toast.makeText(SecurityOnOffActivity.this, "Parola Güncellendi.", Toast.LENGTH_SHORT).show();
                    finish();
                }
                if (Tools.getSharedPrefences(SecurityOnOffActivity.this, "security", String.class).equals(pin)) {
                    _btnResetPassword.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(SecurityOnOffActivity.this,++errorEntryPassword+". yanlış şifre denemesi!",Toast.LENGTH_SHORT).show();
                }
                mPinLockView.resetPinLockView();
            }

            @Override
            public void onEmpty() {
            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {
                System.out.println(pinLength + " : " + intermediatePin);
            }
        };

        _btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Tools.removeSharedPrefences(SecurityOnOffActivity.this, "security");
                Toast.makeText(SecurityOnOffActivity.this, "Parola iptal edildi.", Toast.LENGTH_SHORT).show();
            }
        });
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        mPinLockView = findViewById(R.id.pin_lock_view);
        mIndicatorDots = findViewById(R.id.indicator_dots);


        mPinLockView.attachIndicatorDots(mIndicatorDots);
        mPinLockView.setPinLockListener(mPinLockListener);
        //mPinLockView.setCustomKeySet(new int[]{2, 3, 1, 5, 9, 6, 7, 0, 8, 4});
        //mPinLockView.enableLayoutShuffling();

        mPinLockView.setPinLength(4);
        mPinLockView.setTextColor(ContextCompat.getColor(this, R.color.white));

        mIndicatorDots.setIndicatorType(IndicatorDots.IndicatorType.FILL_WITH_ANIMATION);
    }
}
