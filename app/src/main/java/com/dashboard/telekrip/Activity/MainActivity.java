package com.dashboard.telekrip.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.dashboard.telekrip.Adapter.AdapterUser;
import com.dashboard.telekrip.R;
import com.dashboard.telekrip.model.User;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
            RelativeLayout.LayoutParams.WRAP_CONTENT,
            RelativeLayout.LayoutParams.WRAP_CONTENT
    );
    MaterialSearchView _svUserList;
    ImageView _ivTalkDetails;
    ListView _lvUser;
    List<User> listUser = new ArrayList<>();
    List<User> tempUser = new ArrayList<>();
    boolean isSearch = false;
    BottomNavigationView _bnView;
    AlertDialog.Builder alertDialogBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        uiInitialization();

        //fake data
        User ismail = new User(1, "ismlslck", "https://cdn0.vox-cdn.com/uploads/chorus_asset/file/7783773/kushner.0.png", "akşam gelicem");
        User ali = new User(1, "Gizem KOÜ", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRZxsUHYzLhw9TBdsJfhYOKDDuKMH7cJMZ2DSrRk1fQBraoeHoe", "tmmdır");
        User ahmet = new User(1, "onur sınıf", "https://avatars2.githubusercontent.com/u/19731813?s=64&v=4", "notlarıda getirmeyi unutma.");
        User mehmet = new User(1, "mehmet", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRogLfOZDaB8STe3Jyk2k9TJQU8V3xEMvQCs7jjbuZn8x2buU4a", "ekmekte al.");
        User gizem = new User(1, "gizem", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRrw3076_QBkSdGFPyB9R5i5jnfTypw04EJ-AbaT0V_uf7Ins75wg", "ders iptal");
        User gamze = new User(1, "gamze", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRZxsUHYzLhw9TBdsJfhYOKDDuKMH7cJMZ2DSrRk1fQBraoeHoe", "neredesin");
        User elif = new User(1, "elif", "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSx6cOMCthC7DEhvaYM7KL-XCefMhE00NgKgvkONdu8aWwLz6Yk", "iptal oldu.");
        User kemal = new User(1, "kemal", "https://cdn0.vox-cdn.com/uploads/chorus_asset/file/7783773/kushner.0.png", "deneme mesajı");
        User samet = new User(1, "samet", "https://cdn0.vox-cdn.com/uploads/chorus_asset/file/7783773/kushner.0.png", "pinterest temeları indiricektin");
        listUser.add(ismail);
        listUser.add(ali);
        listUser.add(ahmet);
        listUser.add(mehmet);
        listUser.add(gizem);
        listUser.add(gamze);
        listUser.add(elif);
        listUser.add(kemal);
        listUser.add(samet);
        //fake data

        _svUserList.setHint("Ara...");
        _lvUser.setAdapter(new AdapterUser(getApplicationContext(), listUser));
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
                        if (listUser.get(i).getUserName().contains(newText)) {
                            tempUser.add(listUser.get(i));
                        }
                    }
                    _lvUser.setAdapter(new AdapterUser(getApplicationContext(), tempUser));
                } else {
                    isSearch = false;
                    _lvUser.setAdapter(new AdapterUser(getApplicationContext(), listUser));
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
                params.setMargins(0, 0, 0, 0);
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

        _bnView.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {

                            case R.id.add_user: {
                                System.out.println("kullanıcı ekle");
                                break;
                            }
                            case R.id.sign_out: {
                                alertDialogBuilder = new AlertDialog.Builder(
                                        MainActivity.this);
                                alertDialogBuilder.setTitle("ÇIKIŞ");
                                alertDialogBuilder
                                        .setMessage("Hesabınızdan çıkış yapmak istediğinize emin misiniz?")
                                        .setCancelable(false)
                                        .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                MainActivity.this.finish();
                                            }
                                        })
                                        .setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                                break;
                            }
                            case R.id.account: {
                                Intent chatActivity = new Intent(getApplicationContext(), UserPanelActivity.class);
                                chatActivity.putExtra("user", listUser.get(0));
                                startActivity(chatActivity);
                                break;
                            }

                        }
                        return true;
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
        getSupportActionBar().setTitle(null);
        _ivTalkDetails = findViewById(R.id.ivTalkDetails);
        _lvUser = findViewById(R.id.lvUser);
        _svUserList = findViewById(R.id.svUserName);
        _bnView = findViewById(R.id.bottom_navigation);
    }
}
