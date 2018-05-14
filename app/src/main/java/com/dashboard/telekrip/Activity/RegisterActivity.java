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

public class RegisterActivity extends Activity {
    EditText _etPhoneNumber;
    Button _btnRegister;
    ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if((boolean)Tools.getSharedPrefences(getApplicationContext(),"isLogin",Boolean.class)){
            Intent mainActivity = new Intent(RegisterActivity.this,MainActivity.class);
            startActivity(mainActivity);
            overridePendingTransition(R.transition.left,R.transition.out_right);
            RegisterActivity.this.finish();
        }
        uiInitialization();

        _btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(_etPhoneNumber.getText().toString().isEmpty()){
                    Toast.makeText(getApplicationContext(),"Lütfen bir telefon numarası giriniz!",Toast.LENGTH_SHORT).show();
                }
                else if(_etPhoneNumber.getText().length()!=10){
                    Toast.makeText(getApplicationContext(),"Telefon numarası 10 haneli olmalıdır!",Toast.LENGTH_SHORT).show();
                }
                else {
                    progressDialog = Tools.createProgressDialog(RegisterActivity.this,"Lütfen bekleyiniz...");
                    progressDialog.show();
                    makeRequestPostRegister();
                }
            }
        });
    }

    private void makeRequestPostRegister() {
        StringRequest postRequest = new StringRequest(Request.Method.POST, "http://yazlab.xyz:8000/users/registerUser",
                new Response.Listener<String>()
                {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject rp = new JSONObject(response);
                            if(rp.has("msg")){
                                Intent accountVerificationActivity = new Intent(getApplicationContext(), com.dashboard.telekrip.Activity.AccountVerificationActivity.class);
                                accountVerificationActivity.putExtra("phoneNumber",_etPhoneNumber.getText().toString());
                                startActivity(accountVerificationActivity);
                                overridePendingTransition(R.transition.left,R.transition.out_right);
                                RegisterActivity.this.finish();
                                progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            progressDialog.dismiss();
                        }
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
                                    if(rp.getString("msg").equals("Kullanıcı zaten kayıtlı.")){
                                        Intent accountVerificationActivity = new Intent(getApplicationContext(), com.dashboard.telekrip.Activity.AccountVerificationActivity.class);
                                        accountVerificationActivity.putExtra("phoneNumber",_etPhoneNumber.getText().toString());
                                        accountVerificationActivity.putExtra("isBefore",true);
                                        startActivity(accountVerificationActivity);
                                        overridePendingTransition(R.transition.left,R.transition.out_right);
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
                params.put("phoneNumber", _etPhoneNumber.getText().toString());
                return params;
            }
        };
        Volley.newRequestQueue(this).add(postRequest);

    }

    private void uiInitialization() {
        _etPhoneNumber=findViewById(R.id.etPhoneNumber);
        _btnRegister=findViewById(R.id.btnRegister);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.transition.left,R.transition.out_right);
    }
}
