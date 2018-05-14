package com.dashboard.telekrip.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dashboard.telekrip.Adapter.AdapterOldUserMessage;
import com.dashboard.telekrip.R;
import com.dashboard.telekrip.Tools.Tools;
import com.dashboard.telekrip.model.OldMessage;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class EditProfilActivity extends Activity {

    ImageView _ivAvatar;
    ImageButton _ibCamera;
    EditText _etPhoneNumber, _etFirsName, _etLastName;
    Button _btnUpdate;
    private static final int IMAGE_PICK = 2;
    private static final MediaType MEDIA_TYPE_PNG = MediaType.parse("image/*");
    Bitmap bitmapAvatar;
    private Handler mHandler;
    private SpotsDialog spotsDialog;
    private ImageButton _ibUserPanel;
    private TextView _tvUserPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        uiInitialization();

        getUserInformation();
    }

    private void getUserInformation() {
        spotsDialog = Tools.createDialog(EditProfilActivity.this, "Yükleniyor...");
        spotsDialog.show();
        StringRequest postRequest = new StringRequest(com.android.volley.Request.Method.GET, "http://yazlab.xyz:8000/chat/kullaniciDetay/" + Tools.getSharedPrefences(EditProfilActivity.this, "phoneNumber", String.class),
                new com.android.volley.Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONArray ja = new JSONArray(response);
                            _etFirsName.setText(ja.getJSONObject(0).getString("first_name"));
                            _etLastName.setText(ja.getJSONObject(0).getString("last_name"));
                            Picasso.with(getApplicationContext()).load(ja.getJSONObject(0).getString("avatar")).fit().centerCrop()
                                    .placeholder(R.drawable.default_avatar)
                                    .error(R.drawable.default_avatar)
                                    .into(_ivAvatar);
                            bitmapAvatar=((BitmapDrawable)_ivAvatar.getDrawable()).getBitmap();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        spotsDialog.dismiss();
                    }

                },
                new com.android.volley.Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        spotsDialog.dismiss();
                    }
                }

        );
        Volley.newRequestQueue(this).add(postRequest);
    }

    private void uiInitialization() {
        mHandler = new Handler(Looper.getMainLooper());
        _ivAvatar = findViewById(R.id.ivAvatar);
        _ibCamera = findViewById(R.id.ibCamera);
        _etPhoneNumber = findViewById(R.id.etPhoneNumber);
        _etFirsName = findViewById(R.id.etFirstName);
        _etLastName = findViewById(R.id.etLastName);
        _btnUpdate = findViewById(R.id.btnUpdate);
        _ibUserPanel = findViewById(R.id.ibLastTalk);
        _tvUserPanel = findViewById(R.id.tvUserPanel);
        bitmapAvatar=((BitmapDrawable)_ivAvatar.getDrawable()).getBitmap();

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

        _etPhoneNumber.setText((String) Tools.getSharedPrefences(EditProfilActivity.this, "phoneNumber", String.class));
        _etPhoneNumber.setEnabled(false);

        _ibCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(Intent.createChooser(intent, "Resim Seç"), IMAGE_PICK);
            }
        });
        _btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate()){
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditProfilActivity.this);
                    builder.setMessage("Değişiklikler kaydedilsin mi?")
                            .setCancelable(false)
                            .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    spotsDialog = Tools.createDialog(EditProfilActivity.this, "Kaydediliyor...");
                                    spotsDialog.show();
                                    saveProfil();
                                }
                            })
                            .setTitle("GÜNCELLEME")
                            .setIcon(R.drawable.info).setNegativeButton("Vazgeç", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
                else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditProfilActivity.this);
                    builder.setMessage("Ad,Soyad alanları boş geçilemez!")
                            .setCancelable(false)
                            .setPositiveButton("Kapat", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            })
                            .setTitle("UYARI")
                            .setIcon(R.drawable.info);
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            }
        });

    }

    private boolean validate() {
        if(_etFirsName.getText().toString().equals("")){
            return false;
        }
        if(_etLastName.getText().toString().equals("")){
            return false;
        }
        return true;
    }


    private void saveProfil() {
        String phoneNumber = _etPhoneNumber.getText().toString();
        String firstName = _etFirsName.getText().toString();
        String lastName = _etLastName.getText().toString();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        OkHttpClient client = new OkHttpClient();
        MultipartBuilder form_datas = new MultipartBuilder();
        form_datas.type(MultipartBuilder.FORM);
        form_datas.addPart(
                Headers.of("Content-Disposition", "form-data; name=\"phoneNumber\""),
                RequestBody.create(null, phoneNumber));
        form_datas.addPart(
                Headers.of("Content-Disposition", "form-data; name=\"first_name\""),
                RequestBody.create(null, firstName));
        form_datas.addPart(
                Headers.of("Content-Disposition", "form-data; name=\"last_name\""),
                RequestBody.create(null, lastName));
        bitmapAvatar.compress(Bitmap.CompressFormat.PNG, 0, stream);
        form_datas.addPart(
                Headers.of("Content-Disposition", "form-data; name=\"avatar\"; filename=\"" + (Tools.randomFileName() + System.currentTimeMillis() + ".png") + "\""),
                RequestBody.create(MEDIA_TYPE_PNG, stream.toByteArray()));

        RequestBody requestBody = form_datas.build();
        final Request request = new Request.Builder()
                .url("http://yazlab.xyz:8000/users/updateUser")
                .post(requestBody)
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(final Request request, final IOException e) {
                spotsDialog.dismiss();
                AlertDialog alertDialog = Tools.generateAlertDialog(EditProfilActivity.this, "HATA", "Bir sorun oluştu.");
                alertDialog.show();
            }

            @Override
            public void onResponse(final Response response) throws IOException {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            try {
                                if (response.body().string().contains("kaydedildi")) {
                                    AlertDialog alertDialog = Tools.generateAlertDialog(EditProfilActivity.this, "BAŞARILI", "Değişiklikler kaydedildi.");
                                    alertDialog.show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            spotsDialog.dismiss();
                        } else {
                            AlertDialog alertDialog = Tools.generateAlertDialog(EditProfilActivity.this, "HATA", "Bir sorun oluştu.");
                            alertDialog.show();
                            spotsDialog.dismiss();
                        }
                    }
                });
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_PICK) {
            if (resultCode == Activity.RESULT_OK) {
                GalleryPhoto galleryPhoto = new GalleryPhoto(EditProfilActivity.this);
                galleryPhoto.setPhotoUri(data.getData());
                String photoPath = galleryPhoto.getPath();
                try {
                    bitmapAvatar = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                    _ivAvatar.setImageBitmap(bitmapAvatar);
                    //Bitmap sunucudanGelenResim = ((BitmapDrawable)_ivAvatar.getDrawable()).getBitmap();


                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.left,R.transition.out_right);
    }
}
