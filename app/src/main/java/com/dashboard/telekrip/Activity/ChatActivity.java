package com.dashboard.telekrip.Activity;

import android.app.Activity;
import android.content.Intent;
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

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dashboard.telekrip.Adapter.AdapterChat;
import com.dashboard.telekrip.Adapter.AdapterStartSpeech;
import com.dashboard.telekrip.R;
import com.dashboard.telekrip.Tools.Tools;
import com.dashboard.telekrip.model.Message;
import com.dashboard.telekrip.model.User;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChatActivity extends Activity {
    RelativeLayout _lUst;
    LinearLayout _lOrta,_lAlt;
    CircleImageView _ivAvatar;
    ImageView _ivAvatarZoom;
    TextView _tvNameSurname;
    private ListView _listView;
    private View _btnSend;
    private EditText _edtTxtMessage;
    private List<Message> chatMessages;
    private AdapterChat adapter;
    User usr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        uiInitialization();

        //gelen user bilgileri
        usr = (User) getIntent().getSerializableExtra("user");
        Picasso.with(getApplicationContext()).load(usr.getAvatar()).fit().centerCrop()
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar)
                .into(_ivAvatar);
        Picasso.with(getApplicationContext()).load(usr.getAvatar()).fit().centerCrop()
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar)
                .into(_ivAvatarZoom);
        _tvNameSurname.setText(usr.getName()+' '+usr.getSurname());
        //gelen user bilgileri


        makeRequestPostcheckMessage();

        //fake data
        chatMessages = new ArrayList<>();
        Message deneme1 = new Message("aaaa", "5389784533", "12.06.2018");
        Message deneme2 = new Message("bbbb", (String) Tools.getSharedPrefences(ChatActivity.this,"phoneNumber",String.class), "12.06.2018");
        Message deneme3 = new Message("cccc", "5443452354", "12.06.2018");
        Message deneme4 = new Message("dddd", (String) Tools.getSharedPrefences(ChatActivity.this,"phoneNumber",String.class), "12.06.2018");
        Message deneme5 = new Message("eeee", "5327865436", "12.06.2018");
        chatMessages.add(deneme1);
        chatMessages.add(deneme2);
        chatMessages.add(deneme3);
        chatMessages.add(deneme4);
        chatMessages.add(deneme5);
        //fake data

        _btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message msg = new Message(_edtTxtMessage.getText() + "", (String) Tools.getSharedPrefences(ChatActivity.this,"phoneNumber",String.class), Tools.getDate());
                chatMessages.add(msg);
                adapter.notifyDataSetChanged();
                _edtTxtMessage.setText("");
                Message receiver = new Message("random cevap", "5342312367", Tools.getDate());
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

    private void makeRequestPostcheckMessage() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://yazlab.xyz:8000/chat/checkMessage/",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject rp = new JSONObject(response);
                            if(rp.has("new")){
                                System.out.println("konuşma listesini yükleme.");
                            }
                            else if(rp.has("exist")){
                                System.out.println("konuşma listesini yükle.");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }

        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("sender", (String)Tools.getSharedPrefences(ChatActivity.this,"phoneNumber",String.class));
                params.put("receiver", usr.getPhoneNumber());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);

    }
    private void uiInitialization() {
        _listView = findViewById(R.id.list_msg);
        _btnSend = findViewById(R.id.btn_chat_send);
        _ivAvatar=findViewById(R.id.ivAvatar);
        _ivAvatarZoom=findViewById(R.id.ivAvatarZoom);
        _tvNameSurname=findViewById(R.id.tvNameSurname);
        _edtTxtMessage = findViewById(R.id.etMessage);
        _lUst = findViewById(R.id.rel_ust);
        _lOrta = findViewById(R.id.lin_orta);
        _lAlt = findViewById(R.id.lin_alt);
    }
}
