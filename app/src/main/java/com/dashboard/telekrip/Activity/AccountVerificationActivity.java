package com.dashboard.telekrip.Activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dashboard.telekrip.R;
import com.dashboard.telekrip.Tools.Tools;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class AccountVerificationActivity extends Activity {
    EditText _etVerificationCode;
    Button _btnVerification;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_verification);

        uiInitialization();

        if(getIntent().hasExtra("isBefore")){
            Toast.makeText(getApplicationContext(),getIntent().getStringExtra("phoneNumber")+" telefon numarasına daha önce gönderilen onay kodunu yazınız.",Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(getApplicationContext(),getIntent().getStringExtra("phoneNumber")+" telefon numarasına gönderilen onay kodunu yazınız.",Toast.LENGTH_LONG).show();
        }

        _btnVerification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_etVerificationCode.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Doğrulama kodu boş geçilemez!",Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog= Tools.createProgressDialog(AccountVerificationActivity.this,"Doğrulanıyor...");
                    progressDialog.show();
                    makeRequestPostAuthCode();
                }
            }
        });

    }

    private void makeRequestPostAuthCode() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://yazlab.xyz:8000/users/authCode",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject rp = new JSONObject(response);
                            if(rp.has("msg")){
                                if(rp.getString("msg").equals("Kod doğrulandı.")){
                                    Toast.makeText(getApplicationContext(),"Telefon doğrulandı.",Toast.LENGTH_SHORT).show();
                                    Tools.setSharedPrefences(AccountVerificationActivity.this,"phoneNumber",getIntent().getStringExtra("phoneNumber"));
                                    Tools.setSharedPrefences(AccountVerificationActivity.this,"isLogin",true);
                                    Intent mainActivity = new Intent(AccountVerificationActivity.this,MainActivity.class);
                                    startActivity(mainActivity);
                                    AccountVerificationActivity.this.finish();
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        progressDialog.dismiss();
                    }

                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if(error.networkResponse.statusCode==400){
                            try {
                                JSONObject rp = new JSONObject(new String(error.networkResponse.data));
                                if(rp.has("msg")){
                                    if(rp.getString("msg").equals("Kod doğrulanamadı.")){
                                        Toast.makeText(getApplicationContext(),rp.getString("msg"),Toast.LENGTH_SHORT).show();
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        progressDialog.dismiss();
                    }
                }

        ) {

            @Override
            protected Map<String, String> getParams()
            {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("authCode", _etVerificationCode.getText().toString());
                params.put("phoneNumber", getIntent().getStringExtra("phoneNumber"));
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);

    }
    private void uiInitialization() {
        _etVerificationCode=findViewById(R.id.etVerificationCode);
        _btnVerification=findViewById(R.id.btnVerification);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}
