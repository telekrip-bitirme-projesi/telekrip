package com.dashboard.telekrip.Activity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dashboard.telekrip.R;

public class LoginActivity extends Activity {
    EditText _etUsername,_etPassword;
    Button _btnLogin;
    TextView _tvForgetPassword;
    FloatingActionButton _fabRegister;
    AlertDialog.Builder alertDialogBuilder;

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
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    getWindow().setExitTransition(null);
                    getWindow().setEnterTransition(null);
                }

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ActivityOptions options =
                            ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this, _fabRegister, _fabRegister.getTransitionName());
                    startActivityForResult(new Intent(getApplicationContext(), RegisterActivity.class), 2000, options.toBundle());
                } else {
                    startActivityForResult(new Intent(getApplicationContext(), RegisterActivity.class), 2000);
                }
            }
        });

        _tvForgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final EditText input = new EditText(LoginActivity.this);
                input.setHint("Mail adresiniz.");
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                input.setLayoutParams(lp);
                alertDialogBuilder = new AlertDialog.Builder(
                        LoginActivity.this);
                alertDialogBuilder.setView(input);
                alertDialogBuilder.setTitle("ŞİFRE HATIRLATMA");
                alertDialogBuilder
                        .setMessage("Şifreniz sisteme kayitli mail adresinize gönderilecektir.")
                        .setCancelable(false)
                        .setPositiveButton("Gönder", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
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
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
