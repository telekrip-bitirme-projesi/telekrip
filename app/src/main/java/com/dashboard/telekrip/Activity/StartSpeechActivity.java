package com.dashboard.telekrip.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dashboard.telekrip.Adapter.AdapterStartSpeech;
import com.dashboard.telekrip.R;
import com.dashboard.telekrip.Tools.Tools;
import com.dashboard.telekrip.model.User;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;

public class StartSpeechActivity extends AppCompatActivity {
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
    );
    MaterialSearchView _svUserList;
    ListView _lvUser;
    List<User> listUser = new ArrayList<>();
    List<User> tempUser = new ArrayList<>();
    boolean isSearch = false;
    AdapterStartSpeech adapterUser;
    SpotsDialog spotsDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_speech);

        uiInitialization();
        getSpeechList();

        _svUserList.setHint("Ara...");
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
                        if (listUser.get(i).getName().toLowerCase().contains(newText.toLowerCase()) || listUser.get(i).getSurname().toLowerCase().contains(newText) || listUser.get(i).getPhoneNumber().contains(newText)) {
                            tempUser.add(listUser.get(i));
                        }
                    }
                    adapterUser = new AdapterStartSpeech(getApplicationContext(), tempUser);
                    _lvUser.setAdapter(adapterUser);
                } else {
                    isSearch = false;
                    adapterUser = new AdapterStartSpeech(getApplicationContext(), listUser);
                    _lvUser.setAdapter(adapterUser);
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
                    chatActivity.putExtra("old", tempUser.get(i));
                    startActivity(chatActivity);
                    //System.out.println(tempUser.get(i).getUserName());
                } else {
                    Intent chatActivity = new Intent(getApplicationContext(), ChatActivity.class);
                    chatActivity.putExtra("old", listUser.get(i));
                    startActivity(chatActivity);
                    //System.out.println(listUser.get(i).getUserName());
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        MenuItem item = menu.findItem(R.id.action_search);
        _svUserList.setMenuItem(item);

        return true;
    }

    private void uiInitialization() {
        getSupportActionBar().setTitle("Konuşma başlat");
        _lvUser = findViewById(R.id.lvUser);
        _svUserList = findViewById(R.id.svUserName);
    }

    private void getSpeechList() {
        spotsDialog = Tools.createDialog(StartSpeechActivity.this, "Yükleniyor...");
        spotsDialog.show();
        final String speecList=Tools.getContactListComma(StartSpeechActivity.this).replace("+90","").replace(",0",",");
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://yazlab.xyz:8000/users/checkUsers/",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println(response);
                        Gson gson = new Gson();
                        listUser = new Gson().fromJson(response,
                                new TypeToken<List<User>>() {
                                }.getType());
                        for(int i=0;i<listUser.size();i++){
                            if(listUser.get(i).getPhoneNumber().equals((String)Tools.getSharedPrefences(StartSpeechActivity.this,"phoneNumber",String.class))){
                                listUser.remove(listUser.get(i));
                            }
                        }
                        adapterUser = new AdapterStartSpeech(getApplicationContext(), listUser);
                        _lvUser.setAdapter(adapterUser);
                        spotsDialog.dismiss();
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        spotsDialog.dismiss();
                    }
                })


        {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("data", speecList);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
