package com.dashboard.telekrip.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ExpandableListView;

import com.dashboard.telekrip.Adapter.ExpandableListAdapter;
import com.dashboard.telekrip.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelpActivity extends Activity {

    private ExpandableListView listView;
    private ExpandableListAdapter listAdapter;
    private List<String> listDataHeader;
    private HashMap<String,List<String>> listHash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);

        uiInitialization();
    }

    private void uiInitialization() {

        listView = (ExpandableListView)findViewById(R.id.lvExp);
        initData();
        listAdapter = new ExpandableListAdapter(this,listDataHeader,listHash);
        listView.setAdapter(listAdapter);


    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listDataHeader.add("Hesap ve Profil");
        listDataHeader.add("Sohbetler");
        listDataHeader.add("Xamarin");
        listDataHeader.add("UWP");

        List<String> hesapVeProfil = new ArrayList<>();
        hesapVeProfil.add("Hesap ve Profil bilgilerinizi yönetmek için Telekrip -> Hesabım'a gidin.Açılan ekrandan profil bilgilerinizi" +
                " düzenlemek için Profil seçeneğini seçin.Açılan ekrandan profil resminizi,isim ve soy isim bilgilerinizi düzenledikten sonra " +
                " \"Güncelle\" butonuna basarak kişisel bilgilerinizi güncellemiş olursunuz.Tema ve Bildirim ayarlarınıda yine Hesabım Ekranından " +
                " güncelleyebilirsiniz.");

        List<String> sohbetler = new ArrayList<>();
        sohbetler.add("Daha önce yapmış olduğunuz konuşmalar uygulama açılış ekranında listelenmektedir.Bu listelenen konuşmalardan" +
                " silmek istediğiniz bir konuşma var ise bu konuşmanın üzerine bir süre basılı tutmanız gerekmektedir.Ekrana açılan" +
                " \"Konuşmayı Sil\" seçeneğini seçtiğiniz durumda konuşma kalıcı olarak silinecektir ve karşıdaki kişi bu konuşmanın içeriğine" +
                " hiç bir şekilde ulaşamayacaktır.");

        List<String> xamarin = new ArrayList<>();
        xamarin.add("Xamarin Expandable ListView");
        xamarin.add("Xamarin Google Map");
        xamarin.add("Xamarin Chat Application");
        xamarin.add("Xamarin Firebase ");

        List<String> uwp = new ArrayList<>();
        uwp.add("UWP Expandable ListView");
        uwp.add("UWP Google Map");
        uwp.add("UWP Chat Application");
        uwp.add("UWP Firebase ");

        listHash.put(listDataHeader.get(0),hesapVeProfil);
        listHash.put(listDataHeader.get(1),sohbetler);
        listHash.put(listDataHeader.get(2),xamarin);
        listHash.put(listDataHeader.get(3),uwp);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.left,R.transition.out_right);
    }
}
