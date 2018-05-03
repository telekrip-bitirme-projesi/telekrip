package com.dashboard.telekrip.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
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
import com.dashboard.telekrip.Adapter.AdapterUser;
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

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import de.hdodenhof.circleimageview.CircleImageView;
import dmax.dialog.SpotsDialog;
import se.simbio.encryption.Encryption;
import third.part.android.util.Base64;

public class ChatActivity extends Activity {

    RelativeLayout _lUst;
    LinearLayout _lOrta, _lAlt;
    CircleImageView _ivAvatar;
    ImageView _ivAvatarZoom;
    TextView _tvNameSurname;
    android.support.v7.widget.SwitchCompat _switchSaveNoSave;
    private ListView _listView;
    private View _btnSend;
    private EditText _edtTxtMessage;
    private List<Message> chatMessages;
    private AdapterChat adapter = null;
    String chatKey;
    OldMessage oldUser;
    User usr;
    private WebSocketClient mWebSocketClient;
    private boolean isSave = true;
    SpotsDialog spotsDialog;
    Encryption encryption = null;
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

        }


        if (getIntent().hasExtra("user")) {
            _tvNameSurname.setText(oldUser.getSenderName());
        } else {
            _tvNameSurname.setText(usr.getName());

        }

        //gelen user bilgileri


        makeRequestPostcheckMessage();

        _btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    encryption = Encryption.Builder.getDefaultBuilder("MyKey", "MySalt", new byte[16])
                            .setIterationCount(1) // use 1 instead the default of 65536
                            .build();
                } catch (NoSuchAlgorithmException e) {

                }
                try {
                    encryption = new Encryption.Builder()
                            .setKeyLength(128)
                            .setKeyAlgorithm("AES")
                            .setCharsetName("UTF8")
                            .setIterationCount(100)
                            .setKey("mor€Z€cr€tKYss")
                            .setDigestAlgorithm("SHA1")
                            .setSalt("tuzluk")
                            .setBase64Mode(Base64.DEFAULT)
                            .setAlgorithm("AES/CBC/PKCS5Padding")
                            .setSecureRandomAlgorithm("SHA1PRNG")
                            .setSecretKeyType("PBKDF2WithHmacSHA1")
                            .setIv(new byte[] { 29, 88, -79, -101, -108, -38, -126, 90, 52, 101, -35, 114, 12, -48, -66, -30 })
                            .build();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                String phoneNumber = (String) Tools.getSharedPrefences(ChatActivity.this, "phoneNumber", String.class);
                String message = _edtTxtMessage.getText().toString();
                try {
                    message=encryption.encrypt(message);
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidAlgorithmParameterException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                } catch (InvalidKeySpecException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                }
                message=message.replace("\n","");
                mWebSocketClient.send("{\"sender\": " + phoneNumber + ", \"text\": \"" + message + "\",\"isSave\":" + isSave + "}");

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
                    _btnSend.setEnabled(false);
                } else {
                    _btnSend.setEnabled(true);
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
                        try {
                            encryption = Encryption.Builder.getDefaultBuilder("MyKey", "MySalt", new byte[16])
                                    .setIterationCount(1) // use 1 instead the default of 65536
                                    .build();
                        } catch (NoSuchAlgorithmException e) {

                        }
                        try {
                            encryption = new Encryption.Builder()
                                    .setKeyLength(128)
                                    .setKeyAlgorithm("AES")
                                    .setCharsetName("UTF8")
                                    .setIterationCount(100)
                                    .setKey("mor€Z€cr€tKYss")
                                    .setDigestAlgorithm("SHA1")
                                    .setSalt("tuzluk")
                                    .setBase64Mode(Base64.DEFAULT)
                                    .setAlgorithm("AES/CBC/PKCS5Padding")
                                    .setSecureRandomAlgorithm("SHA1PRNG")
                                    .setSecretKeyType("PBKDF2WithHmacSHA1")
                                    .setIv(new byte[] { 29, 88, -79, -101, -108, -38, -126, 90, 52, 101, -35, 114, 12, -48, -66, -30 })
                                    .build();
                        } catch (NoSuchAlgorithmException e) {
                            e.printStackTrace();
                        }
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
        _listView = findViewById(R.id.list_msg);
        _btnSend = findViewById(R.id.btn_chat_send);
        _ivAvatar = findViewById(R.id.ivAvatar);
        _ivAvatarZoom = findViewById(R.id.ivAvatarZoom);
        _tvNameSurname = findViewById(R.id.tvNameSurname);
        _edtTxtMessage = findViewById(R.id.etMessage);
        _lUst = findViewById(R.id.rel_ust);
        _lOrta = findViewById(R.id.lin_orta);
        _lAlt = findViewById(R.id.lin_alt);
        _switchSaveNoSave = findViewById(R.id.switchSave);

    }
}
