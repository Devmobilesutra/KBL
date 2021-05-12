package com.emsphere.commando4.kirloskarempowerapp;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.emsphere.commando4.kirloskarempowerapp.constantclass.EmpowerApplication;
import com.emsphere.commando4.kirloskarempowerapp.pojo.LicensePOJO;
import com.emsphere.commando4.kirloskarempowerapp.restforreg.ApiClient;
import com.emsphere.commando4.kirloskarempowerapp.restforreg.ApiInterface;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by commando4 on 1/25/2018.
 */

public class LicenseActivity extends AppCompatActivity {

    private EditText licensecode;

    private TextInputLayout inputLayoutlicensecode;
    private Button register;
    private ApiInterface apiService;
    SharedPreferences sp;
    String PREFS_NAME = "EmployeeData";
    public SharedPreferences.Editor editor;
    ProgressDialog pd;
    TextView message;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.licensecode1);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        message=(TextView) findViewById(R.id.message1);
        String email=EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("Mail1"));
        String email1="License code has been sent to your Mail id"+" "+"\""+EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("Mail1"))+"\""+","+" "+"please enter the same here to register.";
        //message.setText(Html.fromHtml("License code has been sent to your Mail id"+" "+email1+","+" "+"please enter the same here to register."));
        String htmlText = email1.replace(email,"<font color='#ffffff'>"+email+"</font>");
        message.setText(Html.fromHtml(htmlText));

        licensecode = (EditText) findViewById(R.id.editText_license_code);
        inputLayoutlicensecode = (TextInputLayout) findViewById(R.id.input_layout_floatreg);
        register = (Button) findViewById(R.id.button_reg);
        licensecode.addTextChangedListener(new MyTextWatcher(licensecode));
        apiService = ApiClient.getClient().create(ApiInterface.class);

        sp = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sp.edit();


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validatelicencecode()) {
                    return;
                }
                // Toast.makeText(LicenseActivity.this,"in reg",Toast.LENGTH_LONG).show();
                if (licensecode.getText().toString().length() <= 50) {

                    // Toast.makeText(LicenseActivity.this,"submitted sucessfully",Toast.LENGTH_LONG).show();
                    //Log.e("empcode",sp.getString("companyCode",""));
                    //Log.e("Companycode",sp.getString("employeeCode",""));
                    //Log.e("Deviceid",getDeviceID());

                    if (Utilities.isNetworkAvailable(LicenseActivity.this)) {

                        SendLicenseRegData(EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeCode1")), getDeviceID(), licensecode.getText().toString(), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("companyCode1")));
                    } else {
                        Toast.makeText(LicenseActivity.this, "No Internet Connection...", Toast.LENGTH_LONG).show();
                    }


                    //startActivity(new Intent(LicenseActivity.this,MainActivity.class));
                } else {
                    //licensecode.setError("License code Should be less than 50 Integers");
                    inputLayoutlicensecode.setError("License code Should be less than 50 digits");
                }

            }
        });

    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.editText_license_code:
                    validatelicencecode();
                    break;


            }
        }
    }

    private boolean validatelicencecode() {
        if (licensecode.getText().toString().trim().isEmpty()) {
            inputLayoutlicensecode.setError("Enter LicenseCode");
            licensecode.requestFocus();
            return false;
        } else {
            inputLayoutlicensecode.setErrorEnabled(false);
        }

        return true;
    }

    public void SendLicenseRegData(String empcode, String deviceid, String licensecode, String companycode) {


        pd = new ProgressDialog(LicenseActivity.this);
        pd.setMessage("loading");
        pd.setCanceledOnTouchOutside(false);
        pd.show();
        Map<String, String> regdata = new HashMap<>();
        regdata.put("employeeCode", empcode);
        regdata.put("deviceId", deviceid);
        regdata.put("licenseNumber", licensecode);
        regdata.put("companyCode", companycode);
        regdata.put("FCMID", "");
        Log.e("name: ", deviceid);
        Log.e("name: ", empcode);
        Log.e("name: ", licensecode);
        Log.e("name: ", companycode);
        // Log.e("fcid: ",fcmid);
        retrofit2.Call<LicensePOJO> call = apiService.SendLicenseRegData(regdata);
        call.enqueue(new Callback<LicensePOJO>() {
            @Override
            public void onResponse(retrofit2.Call<LicensePOJO> call, Response<LicensePOJO> response) {
                pd.dismiss();

                if (response.isSuccessful()) {

                    // Log.d("User ID: ", response.body().getStatus());


                    if (response.body().getStatus().equals("1")) {
                        Log.e("name1: ", response.body().getData().getDesignationName());
                        Log.e("name2: ", response.body().getData().getEmployee().getFirstName());
                        Log.e("name3: ", response.body().getData().getEmployee().getLastName());
                        Log.e("name4: ", response.body().getData().getEmployee().getEmployeeImage());
                        ((EmpowerApplication) getApplication()).EmployeePersonalDetails(response.body().getData().getEmployee().getEmployeeID(), response.body().getData().getEmployee().getFirstName(), response.body().getData().getEmployee().getLastName(), response.body().getData().getEmployee().getEmployeeImage(), response.body().getData().getEmployee().getGender(), response.body().getData().getEmployee().getOfficalEmailID(), response.body().getData().getDesignationName(), response.body().getData().getEmployee().getBirthDate());
                        // editor.putString("EmployeeID", String.valueOf(response.body().getData().getEmployee().getEmployeeID()));
                        //editor.commit();//serviceUrl
                        EmpowerApplication.set_session("employeeId1", String.valueOf(response.body().getData().getEmployee().getEmployeeID()));
                        EmpowerApplication.set_session("serviceUrl1", response.body().getServiceUrl());
                        startActivity(new Intent(LicenseActivity.this, MainActivity.class));
                        finish();
                    } else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), LicenseActivity.this);

                    }
                } else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", LicenseActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", LicenseActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", LicenseActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }
            }

            @Override
            public void onFailure(retrofit2.Call<LicensePOJO> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                pd.dismiss();
                EmpowerApplication.alertdialog(t.getMessage(), LicenseActivity.this);

            }
        });
    }

    public String getDeviceID() {
        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        EmpowerApplication.set_session("deviceId1", android_id);

        return android_id;
    }


}
