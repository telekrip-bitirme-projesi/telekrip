package com.dashboard.telekrip.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.dashboard.telekrip.R;
import com.dashboard.telekrip.Tools.Tools;

public class SecurityActivity extends Activity {

    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    private int errorEntryPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_security);

        uiInitialization();
    }

    private void uiInitialization() {
         PinLockListener mPinLockListener = new PinLockListener() {
            @Override
            public void onComplete(String pin) {

                if (Tools.getSharedPrefences(SecurityActivity.this, "security", String.class).equals(pin)) {
                    Tools.setSharedPrefences(SecurityActivity.this,"securityLogin",true);
                    onBackPressed();
                }
                else {
                    Toast.makeText(SecurityActivity.this,++errorEntryPassword+". yanlış şifre denemesi!",Toast.LENGTH_SHORT).show();
                }
                mPinLockView.resetPinLockView();
            }

            @Override
            public void onEmpty() {
            }

            @Override
            public void onPinChange(int pinLength, String intermediatePin) {
                System.out.println(pinLength+" : " + intermediatePin);
            }
        };

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
    @Override
    protected void onDestroy() {
        super.onDestroy();
        overridePendingTransition(R.transition.left,R.transition.out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(!(Boolean) Tools.getSharedPrefences(SecurityActivity.this,"securityLogin",Boolean.class)){
            finishAffinity();
        }
        else {
            finish();
            overridePendingTransition(R.transition.left,R.transition.out_right);
        }
    }
}
