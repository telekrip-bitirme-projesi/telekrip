package com.dashboard.telekrip.Activity;

import android.app.ProgressDialog;
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
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

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
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_speech);

        uiInitialization();
        progressDialog= Tools.createProgressDialog(StartSpeechActivity.this,"Kullanıcılar yükleniyor...");
        progressDialog.show();
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
                        if (listUser.get(i).getName().contains(newText) || listUser.get(i).getSurname().contains(newText) || listUser.get(i).getPhoneNumber().contains(newText)) {
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
                    chatActivity.putExtra("user", tempUser.get(i));
                    startActivity(chatActivity);
                    //System.out.println(tempUser.get(i).getUserName());
                } else {
                    Intent chatActivity = new Intent(getApplicationContext(), ChatActivity.class);
                    chatActivity.putExtra("user", listUser.get(i));
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
        StringRequest postRequest = new StringRequest(Request.Method.GET, "http://yazlab.xyz:8000/users/checkUsers/" + Tools.getContactListComma(StartSpeechActivity.this),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Gson gson = new Gson();
                        listUser = new LinkedList<User>(Arrays.asList(gson.fromJson(response, User[].class)));
                        adapterUser = new AdapterStartSpeech(getApplicationContext(), listUser);
                        _lvUser.setAdapter(adapterUser);
                        progressDialog.dismiss();
                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.dismiss();
                    }
                }){
            @Override
            public String getBodyContentType()
            {
                return "application/json; charset=utf-8";
            }
        };
        Volley.newRequestQueue(this).add(postRequest);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
