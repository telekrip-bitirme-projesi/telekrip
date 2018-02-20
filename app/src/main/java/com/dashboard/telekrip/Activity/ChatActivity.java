package com.dashboard.telekrip.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.dashboard.telekrip.Adapter.AdapterChat;
import com.dashboard.telekrip.R;
import com.dashboard.telekrip.model.Message;
import com.dashboard.telekrip.model.User;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends Activity {
    RelativeLayout _lUst;
    LinearLayout _lOrta,_lAlt;
    CircleImageView _ivAvatar;
    ImageView _ivAvatarZoom;
    TextView _tvUsername;
    JSONObject message;
    JSONObject chatData;
    private ListView _listView;
    private View _btnSend;
    private EditText _edtTxtMessage;
    private List<Message> chatMessages;
    private AdapterChat adapter;
    Integer user_id = 1;
    Integer birinciKanal, ikinciKanal;
    Integer anonimDeger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        uiInitialization();

        //fake data
        chatMessages = new ArrayList<>();
        Message deneme1 = new Message("aaaa", 12, "12.06.2018");
        Message deneme2 = new Message("bbbb", 1, "12.06.2018");
        Message deneme3 = new Message("cccc", 12, "12.06.2018");
        Message deneme4 = new Message("dddd", 1, "12.06.2018");
        Message deneme5 = new Message("eeee", 12, "12.06.2018");
        chatMessages.add(deneme1);
        chatMessages.add(deneme2);
        chatMessages.add(deneme3);
        chatMessages.add(deneme4);
        chatMessages.add(deneme5);
        //fake data

        _btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message(_edtTxtMessage.getText() + "", 1, "12.11.2018");
                chatMessages.add(msg);
                adapter.notifyDataSetChanged();
                _edtTxtMessage.setText("");
                Message receiver = new Message("random cevap", 12, "12.11.2018");
                chatMessages.add(receiver);
                adapter.notifyDataSetChanged();
            }
        });
        _edtTxtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (_edtTxtMessage.getText().toString().trim().equals("")) {
                    _btnSend.setEnabled(false);
                } else {
                    _btnSend.setEnabled(true);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        //gelen user bilgileri
        User usr = (User) getIntent().getSerializableExtra("user");
        Picasso.with(getApplicationContext()).load(usr.getAvatar()).fit().centerCrop()
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar)
                .into(_ivAvatar);
        Picasso.with(getApplicationContext()).load(usr.getAvatar()).fit().centerCrop()
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar)
                .into(_ivAvatarZoom);
        _tvUsername.setText(usr.getUserName());
        //gelen user bilgileri

        //set ListView adapter first
        adapter = new AdapterChat(getApplicationContext(), chatMessages);
        _listView.setAdapter(adapter);

        _ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _ivAvatarZoom.setVisibility(View.VISIBLE);
                _lUst.setVisibility(View.INVISIBLE);
                _lOrta.setVisibility(View.INVISIBLE);
                _lAlt.setVisibility(View.INVISIBLE);
            }
        });
        _ivAvatarZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _ivAvatarZoom.setVisibility(View.INVISIBLE);
                _lUst.setVisibility(View.VISIBLE);
                _lOrta.setVisibility(View.VISIBLE);
                _lAlt.setVisibility(View.VISIBLE);
            }
        });


    }

    private void uiInitialization() {
        _listView = findViewById(R.id.list_msg);
        _btnSend = findViewById(R.id.btn_chat_send);
        _ivAvatar=findViewById(R.id.ivAvatar);
        _ivAvatarZoom=findViewById(R.id.ivAvatarZoom);
        _tvUsername=findViewById(R.id.tvUserName);
        _edtTxtMessage = findViewById(R.id.msg_type);
        _lUst = findViewById(R.id.rel_ust);
        _lOrta = findViewById(R.id.lin_orta);
        _lAlt = findViewById(R.id.lin_alt);
    }
}
