package com.dashboard.telekrip.Activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.dashboard.telekrip.Adapter.ExpandableListAdapter;
import com.dashboard.telekrip.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class HelpActivity extends Activity {

    private ImageButton _ibUserPanel;
    private TextView _tvUserPanel;
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

        listView = findViewById(R.id.lvExp);
        initData();
        listAdapter = new ExpandableListAdapter(this,listDataHeader,listHash);
        listView.setAdapter(listAdapter);

        _ibUserPanel = findViewById(R.id.ibUserPanel);
        _tvUserPanel = findViewById(R.id.tvUserPanel);

        _ibUserPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        _tvUserPanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


    }

    private void initData() {
        listDataHeader = new ArrayList<>();
        listHash = new HashMap<>();

        listDataHeader.add("HESAP VE PROFİL");
        listDataHeader.add("SOHBETLER");
        listDataHeader.add("MESAJLARI DEPOLAMADAN GÖNDERME");
        listDataHeader.add("GÜVENLİK VE GİZLİLİK");

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

        List<String> mesajlariDepolamadanGonderme = new ArrayList<>();
        mesajlariDepolamadanGonderme.add("Eğer yapmış olduğunuz yazışmaların Telekrip sunucularında saklanmasını istemiyorsanız," +
                " konuşma ekranının sağ üst tarafında yer alan switch seçeneğini aktif durumuna getirmeniz yeterli olacaktır.Aktif hale" +
                " getirdikten sonra ekranda bu seçenekle ilgili mesaj belirmiş olacaktır.Bu mesajdan sonra gönderdiğiniz hiç bir mesaj " +
                " Telekrip sunucularında saklanmayacaktır,ayrıca bu bilgi karşı taraf ile paylaşılmayacaktır yani sizin bu özelliği" +
                " açtığınız karşı tarafa bildirilmemektedir.");

        List<String> guvenlikVeGizlilik = new ArrayList<>();
        guvenlikVeGizlilik.add("Göndermiş olduğunuz mesajların hepsi sunucuya gitmeden önce şifrelenmektedir,göndermiş olduğunuz mesajlar" +
                " sadece alıcıya ulaştığı zaman mesajı açma işlemi yapılmaktadır,hiç bir şekilde 3.kişiler tarafından mesajlarınızın" +
                " içeriği görüntülenememktedir.");

        listHash.put(listDataHeader.get(0),hesapVeProfil);
        listHash.put(listDataHeader.get(1),sohbetler);
        listHash.put(listDataHeader.get(2),mesajlariDepolamadanGonderme);
        listHash.put(listDataHeader.get(3),guvenlikVeGizlilik);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.left,R.transition.out_right);
    }
}
