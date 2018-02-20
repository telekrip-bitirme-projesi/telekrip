package com.dashboard.telekrip.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.dashboard.telekrip.R;
import com.dashboard.telekrip.model.User;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserPanelActivity extends Activity {

    CircleImageView _ivAvatar;
    TextView _tvUserName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        uiInitialization();
    }

    private void uiInitialization() {
        User usr = (User) getIntent().getSerializableExtra("user");
        _ivAvatar=findViewById(R.id.ivAvatar);
        _tvUserName=findViewById(R.id.tvUserName);
        Picasso.with(getApplicationContext()).load(usr.getAvatar()).fit().centerCrop()
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar)
                .into(_ivAvatar);
        _tvUserName.setText(usr.getUserName());
    }
}
