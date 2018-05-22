package com.dashboard.telekrip.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dashboard.telekrip.R;
import com.dashboard.telekrip.Tools.Tools;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;

public class UserPanelActivity extends Activity {

    CircleImageView _ivAvatar;
    TextView _tvNameSurname,_tvLastTalk;
    Button _btnEditProfil,_btnTheme,_btnNotification,_btnHelp,_btnPrivacy;
    ImageButton _ibLastTalk;
    SpotsDialog spotsDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpanel);

        uiInitialization();

        getUserInformation();

        _btnEditProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editProfil = new Intent(getApplicationContext(), EditProfilActivity.class);
                startActivity(editProfil);
                overridePendingTransition(R.transition.left,R.transition.out_right);
            }
        });
        _btnTheme.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent themeActivity = new Intent(getApplicationContext(), ThemeActivity.class);
                startActivity(themeActivity);
                overridePendingTransition(R.transition.left,R.transition.out_right);
            }
        });
        _btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent notification = new Intent(getApplicationContext(), NotificationActivity.class);
                startActivity(notification);
                overridePendingTransition(R.transition.left,R.transition.out_right);
            }
        });
        _btnHelp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent helpActivity = new Intent(getApplicationContext(), HelpActivity.class);
                startActivity(helpActivity);
                overridePendingTransition(R.transition.left,R.transition.out_right);
            }
        });
        _btnPrivacy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent privacyActivity = new Intent(getApplicationContext(), PrivacyActivity.class);
                startActivity(privacyActivity);
                overridePendingTransition(R.transition.left,R.transition.out_right);
            }
        });
        _ibLastTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        _tvLastTalk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
    private void getUserInformation() {
        spotsDialog = Tools.createDialog(UserPanelActivity.this, "Yükleniyor...");
        spotsDialog.show();
        StringRequest postRequest = new StringRequest(com.android.volley.Request.Method.GET, "http://yazlab.xyz:8000/chat/kullaniciDetay/" + Tools.getSharedPrefences(UserPanelActivity.this, "phoneNumber", String.class),
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray ja = new JSONArray(response);
                            _tvNameSurname.setText(ja.getJSONObject(0).getString("first_name")+" "+ja.getJSONObject(0).getString("last_name"));
                            Picasso.with(getApplicationContext()).load(ja.getJSONObject(0).getString("avatar")).fit().centerCrop()
                                    .placeholder(R.drawable.default_avatar)
                                    .error(R.drawable.default_avatar)
                                    .into(_ivAvatar);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        spotsDialog.dismiss();
                    }

                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        spotsDialog.dismiss();
                    }
                }

        );
        Volley.newRequestQueue(this).add(postRequest);
    }
    private void uiInitialization() {
        _ivAvatar=findViewById(R.id.ivAvatar);
        _tvNameSurname=findViewById(R.id.tvNameSurname);
        _btnEditProfil=findViewById(R.id.btnUpdateProfil);
        _tvLastTalk=findViewById(R.id.tvLastTalk);
        _ibLastTalk=findViewById(R.id.ibLastTalk);
        _btnTheme=findViewById(R.id.btnTheme);
        _btnNotification=findViewById(R.id.btnNotification);
        _btnHelp=findViewById(R.id.btnHelp);
        _btnPrivacy=findViewById(R.id.btnPrivacy);
        Picasso.with(getApplicationContext()).load((String) Tools.getSharedPrefences(getApplicationContext(),"avatar",String.class)).fit().centerCrop()
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar)
                .into(_ivAvatar);
        _tvNameSurname.setText((String) Tools.getSharedPrefences(getApplicationContext(),"username",String.class));
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.left,R.transition.out_right);
    }
}
