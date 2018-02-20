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
import android.widget.TextView;

import com.dashboard.telekrip.R;

public class LoginActivity extends Activity {
    EditText _etUsername,_etPassword;
    Button _btnLogin;
    TextView _tvForgetPassword;
    FloatingActionButton _fabRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        uiInitialization();

        _btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(new Intent(getApplicationContext(), MainActivity.class), 2000);
            }
        });

        _fabRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getWindow().setExitTransition(null);
                getWindow().setEnterTransition(null);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, _fabRegister, _fabRegister.getTransitionName());
                    startActivityForResult(new Intent(getApplicationContext(), RegisterActivity.class), 2000, options.toBundle());
                } else {
                    startActivityForResult(new Intent(getApplicationContext(), RegisterActivity.class), 2000);
                }
            }
        });
    }

    private void uiInitialization() {
        _etUsername=findViewById(R.id.etUsername);
        _etPassword=findViewById(R.id.etPassword);
        _btnLogin=findViewById(R.id.btnLogin);
        _tvForgetPassword=findViewById(R.id.tvForgetPassword);
        _fabRegister=findViewById(R.id.fabRegister);
    }
}
