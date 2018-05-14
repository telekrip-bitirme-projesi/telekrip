package com.dashboard.telekrip.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dashboard.telekrip.R;
import com.dashboard.telekrip.Tools.Tools;
import com.dashboard.telekrip.model.User;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddUserDetailActivity extends Activity {
    CircleImageView _ivAvatar;
    TextView _tvNameSurname;
    Button _btnAddUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user_detail);

        uiInitialization();

        //gelen user bilgileri
        User usr = (User) getIntent().getSerializableExtra("user");
        Picasso.with(getApplicationContext()).load(usr.getAvatar()).fit().centerCrop()
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar)
                .into(_ivAvatar);
        _tvNameSurname.setText(usr.getName());
        //gelen user bilgileri

        _btnAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = Tools.alertDialog(AddUserDetailActivity.this, "Kullanıcı Ekle", "Kullanıcıya istek gönderilsin mi ?");
                builder.setPositiveButton("EVET", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Toast.makeText(getApplicationContext(), "İstek başarıyla gönderildi.", Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton("VAZGEÇ", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                    }
                });
                builder.show();

            }
        });
    }

    private void uiInitialization() {
        _ivAvatar = findViewById(R.id.ivAvatar);
        _btnAddUser = findViewById(R.id.btnUserAdd);
        _tvNameSurname = findViewById(R.id.tvNameSurname);
        Picasso.with(getApplicationContext()).load((String) Tools.getSharedPrefences(getApplicationContext(), "avatar", String.class)).fit().centerCrop()
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar)
                .into(_ivAvatar);
        _tvNameSurname.setText((String) Tools.getSharedPrefences(getApplicationContext(), "username", String.class));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.left,R.transition.out_right);
    }
}
