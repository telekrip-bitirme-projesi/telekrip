package com.dashboard.telekrip.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.dashboard.telekrip.R;
import com.dashboard.telekrip.Tools.Tools;
import com.kosalgeek.android.photoutil.GalleryPhoto;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profil);

        uiInitialization();
    }

    private void uiInitialization() {
        mHandler = new Handler(Looper.getMainLooper());
        _ivAvatar = findViewById(R.id.ivAvatar);
        _ibCamera = findViewById(R.id.ibCamera);
        _etPhoneNumber = findViewById(R.id.etPhoneNumber);
        _etFirsName = findViewById(R.id.etFirstName);
        _etLastName = findViewById(R.id.etLastName);
        _btnUpdate = findViewById(R.id.btnUpdate);

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
                spotsDialog=Tools.createDialog(EditProfilActivity.this,"Kaydediliyor...");
                spotsDialog.show();
                saveProfil();
            }
        });
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

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}
