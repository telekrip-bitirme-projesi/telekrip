package com.dashboard.telekrip.Activity;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dashboard.telekrip.Adapter.AdapterChat;
import com.dashboard.telekrip.Adapter.AdapterOldUserMessage;
import com.dashboard.telekrip.Adapter.AdapterUser;
import com.dashboard.telekrip.R;
import com.dashboard.telekrip.Service.Service1;
import com.dashboard.telekrip.Tools.Tools;
import com.dashboard.telekrip.model.Message;
import com.dashboard.telekrip.model.OldMessage;
import com.dashboard.telekrip.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class MainActivity extends AppCompatActivity {
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
    );
    MaterialSearchView _svUserList;
    ListView _lvUser;
    List<OldMessage> listUser = new ArrayList<>();
    List<OldMessage> tempUser = new ArrayList<>();
    boolean isSearch = false;
    BottomNavigationView _bnView;
    AdapterOldUserMessage adapterOldUserMessage;
    SpotsDialog spotsDialog;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Tools.getSharedPrefences(MainActivity.this, "security", String.class) != null && (Boolean) Tools.getSharedPrefences(MainActivity.this, "securityLogin", Boolean.class) == false) {
            Intent securityActivity = new Intent(MainActivity.this, SecurityActivity.class);
            startActivity(securityActivity);
            //MainActivity.this.finish();
        }

        uiInitialization();

        if (!isMyServiceRunning(Service1.class)) {
            Intent i = new Intent(getApplicationContext(), Service1.class);
            startService(i);
        }

        getListConversations();

        _svUserList.setHint("Ara...");
        registerForContextMenu(_lvUser);
        _svUserList.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                tempUser.clear();
                if (newText.length() > 0) {
                    isSearch = true;
                    for (int i = 0; i < listUser.size(); i++) {
                        if (listUser.get(i).getSenderPhone().toLowerCase().contains(newText.toLowerCase()) || listUser.get(i).getReceiverName().toLowerCase().contains(newText.toLowerCase()) || listUser.get(i).getReceiverPhone().contains(newText) || listUser.get(i).getSenderPhone().contains(newText)) {
                            tempUser.add(listUser.get(i));
                        }
                    }
                    adapterOldUserMessage = new AdapterOldUserMessage(getApplicationContext(), tempUser);
                    _lvUser.setAdapter(adapterOldUserMessage);
                } else {
                    isSearch = false;
                    adapterOldUserMessage = new AdapterOldUserMessage(getApplicationContext(), listUser);
                    _lvUser.setAdapter(adapterOldUserMessage);
                }
                return false;
            }
        });
        _svUserList.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                params.setMargins(0, 100, 0, 0);
                _lvUser.setLayoutParams(params);
            }

            @Override
            public void onSearchViewClosed() {
                params.setMargins(0, 0, 0, 100);
                _lvUser.setLayoutParams(params);

            }
        });
        _lvUser.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isSearch) {
                    Intent chatActivity = new Intent(getApplicationContext(), ChatActivity.class);
                    chatActivity.putExtra("user", tempUser.get(i));
                    startActivity(chatActivity);
                    overridePendingTransition(R.transition.left, R.transition.out_right);
                    finish();
                    //System.out.println(tempUser.get(i).getUserName());
                } else {
                    Intent chatActivity = new Intent(getApplicationContext(), ChatActivity.class);
                    chatActivity.putExtra("user", listUser.get(i));
                    startActivity(chatActivity);
                    overridePendingTransition(R.transition.left, R.transition.out_right);
                    finish();
                    //System.out.println(listUser.get(i).getUserName());
                }
            }
        });

        _bnView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.add_user: {
                                Intent startSpeechActivity = new Intent(getApplicationContext(), StartSpeechActivity.class);
                                startActivity(startSpeechActivity);
                                overridePendingTransition(R.transition.left, R.transition.out_right);
                                finish();
                                break;
                            }
                            case R.id.account: {
                                Intent userPanelActivity = new Intent(getApplicationContext(), UserPanelActivity.class);
                                startActivity(userPanelActivity);
                                overridePendingTransition(R.transition.left, R.transition.out_right);
                                break;
                            }


                        }
                        return true;
                    }
                });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Konuşmayı sil");
        menu.add("Sessize al");
    }


    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        super.onContextItemSelected(item);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        final Object obj = _lvUser.getItemAtPosition(info.position);
        final int indis = Integer.parseInt(obj.toString());
        if (item.getTitle().equals("Konuşmayı sil")) {
            AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("Konuşma Silinsin mi ??")
                    .setCancelable(false)
                    .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            if (isSearch) {
                                for (int i = 0; i < listUser.size(); i++) {
                                    if (tempUser.get(indis).getSenderName() == listUser.get(i).getSenderName()) {
                                        listUser.remove(i);
                                        break;
                                    }
                                }
                                deleteTalk(tempUser.get(indis).getKey(), indis, "temp");

                            } else {
                                deleteTalk(listUser.get(indis).getKey(), indis, "orjinal");
                            }
                        }
                    })
                    .setTitle("Konuşmayı Sil")
                    .setIcon(R.drawable.info).setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog alert = builder.create();
            alert.show();
        } else if (item.getTitle().equals("Sessize al")) {
            if (isSearch) {
                for (int i = 0; i < listUser.size(); i++) {
                    if (tempUser.get(indis).getSenderName() == listUser.get(i).getSenderName()) {
                        JSONObject jo =  new JSONObject();
                        //jo.put("quietList",listUser.get(i).getSenderPhone());
                        //Tools.setSharedPrefences(MainActivity.this,"quietList",jo);
                        listUser.get(i).setQuiet(true);
                        adapterOldUserMessage = new AdapterOldUserMessage(MainActivity.this, tempUser);
                        _lvUser.setAdapter(adapterOldUserMessage);
                        break;
                    }
                }


            } else {
                listUser.get(indis).setQuiet(true);
                adapterOldUserMessage = new AdapterOldUserMessage(MainActivity.this, listUser);
                _lvUser.setAdapter(adapterOldUserMessage);
            }
        }

        return true;
    }

    private void deleteTalk(final String key, final int sil, final String tip) {

        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://yazlab.xyz:8000/chat/mesajSil/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (tip.equals("temp")) {
                            tempUser.remove(sil);
                            adapterOldUserMessage = new AdapterOldUserMessage(MainActivity.this, tempUser);
                            _lvUser.setAdapter(adapterOldUserMessage);
                        } else {
                            listUser.remove(sil);
                            adapterOldUserMessage = new AdapterOldUserMessage(MainActivity.this, listUser);
                            _lvUser.setAdapter(adapterOldUserMessage);
                        }


                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(MainActivity.this, "Bir sorun oluştu!", Toast.LENGTH_LONG).show();
                    }
                }
        ) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("key", key);
                params.put("sil", "sil");

                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        _svUserList.setMenuItem(item);

        return true;
    }

    private void uiInitialization() {
        getSupportActionBar().setTitle("Son Konuşmalar");
        _lvUser = findViewById(R.id.lvUser);
        _svUserList = findViewById(R.id.svUserName);
        _bnView = findViewById(R.id.bottom_navigation);
    }

    private void getListConversations() {
        spotsDialog = Tools.createDialog(MainActivity.this, "Yükleniyor...");
        spotsDialog.show();
        StringRequest postRequest = new StringRequest(Request.Method.GET, "http://yazlab.xyz:8000/chat/eskiMesajlar/" + Tools.getSharedPrefences(MainActivity.this, "phoneNumber", String.class),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        listUser = new Gson().fromJson(response,
                                new TypeToken<List<OldMessage>>() {
                                }.getType());
                        adapterOldUserMessage = new AdapterOldUserMessage(getApplicationContext(), listUser);
                        _lvUser.setAdapter(adapterOldUserMessage);

                        if (listUser.size() == 0) {
                            Tools.generateAlertDialog(MainActivity.this, "BİLGİLENDİRME", "Henüz biriyle konuşmanız bulunmuyor,\"Konuşma Başlat\" butonuna basarak yeni bir konuşma başlatabilirsiniz.").show();
                        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Tools.setSharedPrefences(MainActivity.this, "securityLogin", false);
        MainActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Tools.setSharedPrefences(MainActivity.this, "securityLogin", false);
        MainActivity.this.finish();
    }
}
