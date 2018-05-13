package com.dashboard.telekrip.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dashboard.telekrip.R;
import com.dashboard.telekrip.Tools.Tools;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserPanelActivity extends Activity {

    CircleImageView _ivAvatar;
    TextView _tvNameSurname;
    Button _btnEditProfil;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userpanel);

        uiInitialization();

        _btnEditProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editProfil = new Intent(getApplicationContext(), EditProfilActivity.class);
                startActivity(editProfil);
            }
        });
    }

    private void uiInitialization() {
        _ivAvatar=findViewById(R.id.ivAvatar);
        _tvNameSurname=findViewById(R.id.tvNameSurname);
        _btnEditProfil=findViewById(R.id.btnUpdateProfil);
        Picasso.with(getApplicationContext()).load((String) Tools.getSharedPrefences(getApplicationContext(),"avatar",String.class)).fit().centerCrop()
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar)
                .into(_ivAvatar);
        _tvNameSurname.setText((String) Tools.getSharedPrefences(getApplicationContext(),"username",String.class));
    }
}
