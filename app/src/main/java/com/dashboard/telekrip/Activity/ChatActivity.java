package com.dashboard.telekrip.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dashboard.telekrip.Adapter.AdapterChat;
import com.dashboard.telekrip.R;
import com.dashboard.telekrip.Tools.Tools;
import com.dashboard.telekrip.model.Message;
import com.dashboard.telekrip.model.OldMessage;
import com.dashboard.telekrip.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.squareup.picasso.Picasso;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import hani.momanii.supernova_emoji_library.Actions.EmojIconActions;
import hani.momanii.supernova_emoji_library.Helper.EmojiconEditText;

public class ChatActivity extends Activity {

    RelativeLayout _lUst;
    LinearLayout _lOrta;
    CircleImageView _ivAvatar;
    ImageView _ivAvatarZoom;
    TextView _tvNameSurname;
    android.support.v7.widget.SwitchCompat _switchSaveNoSave;
    private ListView _listView;
    private List<Message> chatMessages;
    private AdapterChat adapter = null;
    String chatKey;
    OldMessage oldUser;
    User usr;
    private WebSocketClient mWebSocketClient;
    private boolean isSave = true;
    SpotsDialog spotsDialog;

    EmojiconEditText _edtTxtMessage, _edtTxtMessage2;
    ImageView _btnSend;
    ImageView submitButton;
    View rootView;
    EmojIconActions emojIcon;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        uiInitialization();


        spotsDialog = Tools.createDialog(ChatActivity.this, "Yükleniyor...");
        spotsDialog.show();

        //gelen user bilgileri
        if (getIntent().hasExtra("user")) {
            oldUser = (OldMessage) getIntent().getSerializableExtra("user");
            if(oldUser.getAvatar()!=null && !oldUser.getAvatar().equals("")){
                Picasso.with(getApplicationContext()).load(oldUser.getAvatar()).fit().centerCrop()
                        .placeholder(R.drawable.default_avatar)
                        .error(R.drawable.default_avatar)
                        .into(_ivAvatar);
                Picasso.with(getApplicationContext()).load(oldUser.getAvatar()).fit().centerCrop()
                        .placeholder(R.drawable.default_avatar)
                        .error(R.drawable.default_avatar)
                        .into(_ivAvatarZoom);
            }
            else {
                _ivAvatar.setImageResource(R.drawable.default_avatar);
                _ivAvatarZoom.setImageResource(R.drawable.default_avatar);
            }
            if(oldUser.getReceiverName().equals("")){
                if(!Tools.getSharedPrefences(ChatActivity.this,"phoneNumber",String.class).equals(oldUser.getReceiverPhone())){
                    _tvNameSurname.setText(oldUser.getReceiverPhone());
                }
                else if(!Tools.getSharedPrefences(ChatActivity.this,"phoneNumber",String.class).equals(oldUser.getSenderPhone())){
                    _tvNameSurname.setText(oldUser.getSenderPhone());
                }
            }
            else {
                _tvNameSurname.setText(oldUser.getReceiverName());
            }
        } else {
            usr = (User) getIntent().getSerializableExtra("old");
            if(usr.getAvatar()!=null && !usr.getAvatar().equals("")){
                Picasso.with(getApplicationContext()).load(usr.getAvatar()).fit().centerCrop()
                        .placeholder(R.drawable.default_avatar)
                        .error(R.drawable.default_avatar)
                        .into(_ivAvatar);
                Picasso.with(getApplicationContext()).load(usr.getAvatar()).fit().centerCrop()
                        .placeholder(R.drawable.default_avatar)
                        .error(R.drawable.default_avatar)
                        .into(_ivAvatarZoom);
            }
            else {
                _ivAvatar.setImageResource(R.drawable.default_avatar);
                _ivAvatarZoom.setImageResource(R.drawable.default_avatar);
            }
            _tvNameSurname.setText(usr.getName()+" "+usr.getSurname());
        }

        //gelen user bilgileri


        makeRequestPostcheckMessage();

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String phoneNumber = (String) Tools.getSharedPrefences(ChatActivity.this, "phoneNumber", String.class);
                String message = _edtTxtMessage.getText().toString();
                message=Tools.getEncrypt(message);
                message=message.replace("\n","");
                if(!mWebSocketClient.getConnection().isClosed()){
                    mWebSocketClient.send("{\"sender\": " + phoneNumber + ", \"text\": \"" + message + "\",\"isSave\":" + isSave + "}");
                }

                if (adapter == null) {
                    adapter = new AdapterChat(ChatActivity.this, chatMessages);
                    _listView.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
                _edtTxtMessage.setText("");
            }
        });
        _edtTxtMessage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (_edtTxtMessage.getText().toString().trim().equals("")) {
                    submitButton.setEnabled(false);
                } else {
                    submitButton.setEnabled(true);

                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        _ivAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _ivAvatarZoom.setVisibility(View.VISIBLE);
                _lUst.setVisibility(View.INVISIBLE);
                _lOrta.setVisibility(View.INVISIBLE);

            }
        });
        _ivAvatarZoom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _ivAvatarZoom.setVisibility(View.INVISIBLE);
                _lUst.setVisibility(View.VISIBLE);
                _lOrta.setVisibility(View.VISIBLE);

            }
        });

        _switchSaveNoSave.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    isSave = false;
                    Toast.makeText(ChatActivity.this, "Gönderdiğin mesajlar artık kaydedilmeyecektir.", Toast.LENGTH_SHORT).show();
                } else {
                    isSave = true;
                    Toast.makeText(ChatActivity.this, "Gönderdiğin mesajlar kaydedilecektir.", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void makeRequestPostcheckMessage() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://yazlab.xyz:8000/chat/checkMessage/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject rp = new JSONObject(response);
                            if (rp.has("new")) {
                                chatKey = rp.getString("key");
                            } else if (rp.has("exist")) {
                                chatKey = rp.getString("key");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        connectWebSocket();
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }

        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();


                if (getIntent().hasExtra("user")) {
                    params.put("sender", oldUser.getSenderPhone());
                    params.put("receiver", oldUser.getReceiverPhone());
                } else {
                    params.put("sender", (String) Tools.getSharedPrefences(ChatActivity.this, "phoneNumber", String.class));
                    params.put("receiver", usr.getPhoneNumber());
                }

                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);

    }

    private void connectWebSocket() {
        chatMessages = new ArrayList<>();

        URI uri;
        try {
            uri = new URI("http://yazlab.xyz:8000/chat/message/" + chatKey);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                previousTalk();
            }

            @Override
            public void onMessage(final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Gson gson = new Gson();
                        Message message = gson.fromJson(s, Message.class);

                        if (!isSave) {
                            message.setSave(false);
                        } else {
                            message.setSave(true);
                        }


                        chatMessages.add(message);
                        adapter = new AdapterChat(getApplicationContext(), chatMessages);
                        _listView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                        _listView.setSelection(_listView.getCount() - 1);
                    }

                });
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                System.out.println("closed");
            }

            @Override
            public void onError(Exception e) {
                System.out.println("error");
            }
        };
        mWebSocketClient.connect();
    }

    private void previousTalk() {
        String url="http://yazlab.xyz:8000/chat/mesajlar/"+chatKey;
        StringRequest postRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        chatMessages = new Gson().fromJson(response,
                                new TypeToken<List<Message>>() {
                                }.getType());
                        adapter = new AdapterChat(getApplicationContext(), chatMessages);
                        _listView.setAdapter(adapter);
                        _listView.setSelection(_listView.getCount() - 1);

                        spotsDialog.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        spotsDialog.dismiss();

                    }
                }

        );
        Volley.newRequestQueue(this).add(postRequest);
    }

    private void uiInitialization() {

        rootView = findViewById(R.id.root_view);
        _btnSend = findViewById(R.id.emoji_btn);
        submitButton = findViewById(R.id.btnSend);
        _edtTxtMessage =findViewById(R.id.etMessage);
        _edtTxtMessage2 =findViewById(R.id.emojicon_edit_text2);
        emojIcon = new EmojIconActions(this, rootView, _edtTxtMessage, _btnSend);
        emojIcon.ShowEmojIcon();
        emojIcon.setKeyboardListener(new EmojIconActions.KeyboardListener() {
            @Override
            public void onKeyboardOpen() {
                Log.e("Keyboard", "open");
            }

            @Override
            public void onKeyboardClose() {
                Log.e("Keyboard", "close");
            }
        });
        emojIcon.addEmojiconEditTextList(_edtTxtMessage2);
        _listView = findViewById(R.id.list_msg);

        _ivAvatar = findViewById(R.id.ivAvatar);
        _ivAvatarZoom = findViewById(R.id.ivAvatarZoom);
        _tvNameSurname = findViewById(R.id.tvNameSurname);


        _lUst = findViewById(R.id.rel_ust);
        _lOrta = findViewById(R.id.lin_orta);

        _switchSaveNoSave = findViewById(R.id.switchSave);
        switch ((int)Tools.getSharedPrefences(ChatActivity.this,"theme",Integer.class)){
            case 0:{
                _listView.setBackgroundResource(R.mipmap.bg1);
                break;
            }
            case 1:{_listView.setBackgroundResource(R.mipmap.bg2);
                break;}
            case 2:{
                _listView.setBackgroundResource(R.mipmap.bg3);
                break;
            }
            case 3:{
                _listView.setBackgroundResource(R.mipmap.bg4);
                break;
            }
            case 4:{
                _listView.setBackgroundResource(R.mipmap.bg5);
                break;
            }
            case 5:{
                _listView.setBackgroundResource(R.mipmap.bg6);
                break;
            }
            case 6:{
                _listView.setBackgroundResource(R.mipmap.bg7);
                break;
            }
        }

    }
}
