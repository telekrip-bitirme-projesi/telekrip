package com.dashboard.telekrip.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

import com.dashboard.telekrip.Adapter.AdapterUser;
import com.dashboard.telekrip.R;
import com.dashboard.telekrip.model.User;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity {
    ImageView _ivTalkDetails;
    ListView _lvUser;
    List<User> listUser = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fake data
        User ismail = new User(1,"ismlslck","https://scontent-ams3-1.xx.fbcdn.net/v/t1.0-1/c0.39.160.160/p200x200/18034071_10155233118493844_6653261185578038145_n.jpg?oh=3b9375a155a231bbe7c3c6ae063824fc&oe=5B131FF7","akşam gelicem");
        User ali = new User(1,"Gizem KOÜ","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRZxsUHYzLhw9TBdsJfhYOKDDuKMH7cJMZ2DSrRk1fQBraoeHoe","tmmdır");
        User ahmet = new User(1,"onur sınıf","https://avatars2.githubusercontent.com/u/19731813?s=64&v=4","notlarıda getirmeyi unutma.");
        User mehmet = new User(1,"mehmet","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRogLfOZDaB8STe3Jyk2k9TJQU8V3xEMvQCs7jjbuZn8x2buU4a","ekmekte al.");
        User gizem = new User(1,"gizem","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRrw3076_QBkSdGFPyB9R5i5jnfTypw04EJ-AbaT0V_uf7Ins75wg","ders iptal");
        User gamze = new User(1,"gamze","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRZxsUHYzLhw9TBdsJfhYOKDDuKMH7cJMZ2DSrRk1fQBraoeHoe","neredesin");
            User elif = new User(1,"elif","https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSx6cOMCthC7DEhvaYM7KL-XCefMhE00NgKgvkONdu8aWwLz6Yk","iptal oldu.");
        listUser.add(ismail);
        listUser.add(ali);
        listUser.add(ahmet);
        listUser.add(mehmet);
        listUser.add(gizem);
        listUser.add(gamze);
        listUser.add(elif);
        //fake data

        uiInitialization();
    }

    private void uiInitialization() {
        _ivTalkDetails=findViewById(R.id.ivTalkDetails);
        _lvUser=findViewById(R.id.lvUser);
        _lvUser.setAdapter(new AdapterUser(getApplicationContext(),listUser));


    }
}
