package com.dashboard.telekrip.Activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.dashboard.telekrip.R;

public class RegisterActivity extends Activity {
    EditText _etUsername,_etPassword,_etRepeatPassword;
    Button _btnRegister;
    FloatingActionButton _fabLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        uiInitialization();

        _fabLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setExitTransition(null);
                    getWindow().setEnterTransition(null);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this, _fabLogin, _fabLogin.getTransitionName());
                    startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 2000, options.toBundle());
                } else {
                    startActivityForResult(new Intent(getApplicationContext(), LoginActivity.class), 2000);
                }
            }
        });
    }

    private void uiInitialization() {
        _etUsername=findViewById(R.id.etUsername);
        _etPassword=findViewById(R.id.etPassword);
        _etRepeatPassword=findViewById(R.id.etRepeatPassword);
        _btnRegister=findViewById(R.id.btnRegister);
        _fabLogin=findViewById(R.id.fabLogin);
    }
}
