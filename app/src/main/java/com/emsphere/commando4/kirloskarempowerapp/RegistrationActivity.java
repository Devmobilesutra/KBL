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
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.emsphere.commando4.kirloskarempowerapp.constantclass.EmpowerApplication;
import com.emsphere.commando4.kirloskarempowerapp.pojo.RegistrationDetails;
import com.emsphere.commando4.kirloskarempowerapp.restforreg.ApiClient;
import com.emsphere.commando4.kirloskarempowerapp.restforreg.ApiInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by commando4 on 1/24/2018.
 */

public class RegistrationActivity extends AppCompatActivity {

    private EditText Empid, phoneno, companycode;
    private Button submit;
    private TextInputLayout inputLayoutEmpid, inputLayoutphoneno, inputLayoutcompanycode;
    private ApiInterface apiService;
    private Spinner spinner;
    public SharedPreferences sharedPref;
    public SharedPreferences.Editor editor;
    String PREFS_NAME = "EmployeeData";
    ProgressDialog pd;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        //Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
        Empid = (EditText) findViewById(R.id.editText_Empid);
        phoneno = (EditText) findViewById(R.id.editText_phone);
        companycode = (EditText) findViewById(R.id.editText_CompanyID);
        submit = (Button) findViewById(R.id.button_submit);
        spinner = (Spinner) findViewById(R.id.editText_spinner);
        inputLayoutEmpid = (TextInputLayout) findViewById(R.id.input_layout_floatEmpID);
        inputLayoutphoneno = (TextInputLayout) findViewById(R.id.input_layout_floatphone);
        inputLayoutcompanycode = (TextInputLayout) findViewById(R.id.input_layout_floatCopanyID);
        Empid.addTextChangedListener(new MyTextWatcher(Empid));
        phoneno.addTextChangedListener(new MyTextWatcher(phoneno));
        companycode.addTextChangedListener(new MyTextWatcher(companycode));
        sharedPref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        editor = sharedPref.edit();
        ArrayList<String> arrayList = new ArrayList<>();
        arrayList.add("New");
        arrayList.add("Mobile Change/Format");
        arrayList.add("Uninstalled");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, R.layout.spinner_item, arrayList);
        spinner.setAdapter(arrayAdapter);

        apiService =
                ApiClient.getClient().create(ApiInterface.class);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitCheck();
                Toast.makeText(RegistrationActivity.this, "in register", Toast.LENGTH_LONG).show();
            }
        });

    }

    private boolean validateempid() {
        if (Empid.getText().toString().trim().isEmpty()) {
            inputLayoutEmpid.setError(getString(R.string.err_msg_empid));
            Empid.requestFocus();
            return false;
        } else {
            inputLayoutEmpid.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validatephone() {
        if (phoneno.getText().toString().trim().isEmpty()) {
            inputLayoutphoneno.setError(getString(R.string.err_msg_phone));
            phoneno.requestFocus();
            return false;
        } else {
            inputLayoutphoneno.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validatecompanycode() {
        if (companycode.getText().toString().trim().isEmpty()) {
            inputLayoutcompanycode.setError(getString(R.string.err_msg_Companycode));
            companycode.requestFocus();
            return false;
        } else {
            inputLayoutcompanycode.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }

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
                case R.id.editText_Empid:
                    validateempid();
                    break;

                case R.id.editText_phone:
                    validatephone();
                    break;
                case R.id.editText_CompanyID:
                    validatecompanycode();
                    break;
            }
        }
    }

    public void submitCheck() {

        if (!validateempid()) {
            return;
        }

        if (!validatephone()) {
            return;
        }
        if (!validatecompanycode()) {
            return;
        }
        if (Empid.getText().toString().length() <= 25) {
            if (phoneno.getText().toString().length() <= 25) {
                if (companycode.getText().toString().length() <= 10) {
                    Toast.makeText(RegistrationActivity.this, "submitted sucessfully", Toast.LENGTH_LONG).show();
                    //startActivity(new Intent(RegistrationActivity.this,LicenseActivity.class));
                    if (Utilities.isNetworkAvailable(this)) {
                        SendRegistrationData(getEmpId(), getPhoneNumber(), getCompanyId(), getRegistrationType(), getDeviceID());
                    } else {
                        Toast.makeText(RegistrationActivity.this, "No Internet Connection...", Toast.LENGTH_LONG).show();
                    }
                } else {
                    //companycode.setError("Company code should be Less than 10 Integers");
                    inputLayoutcompanycode.setError("Company code should be Less than 10 Alphanumeric");

                }

            } else {
                //phoneno.setError("Phone Number should be Less than 25 Integers");
                inputLayoutphoneno.setError("Mobile no's should be less then or equal to 25 digits");

            }

        } else {
            //Empid.setError("Company code should be Less than 4 Integers");
            inputLayoutEmpid.setError("employee Code should be less then or equal to 5 characters");

        }
    }

    public void SendRegistrationData(final String empid, String Phone, final String Companycode, String regtype, String deviceid) {
        pd = new ProgressDialog(RegistrationActivity.this);
        pd.setMessage("loading");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        Map<String, String> regdata = new HashMap<>();
        regdata.put("employeeCode", empid);
        regdata.put("mobileNumber", Phone);
        regdata.put("companyCode", Companycode);
        regdata.put("registrationType", regtype);
        regdata.put("deviceID", deviceid);
        Log.e("deviceidinreg", deviceid);

        retrofit2.Call<RegistrationDetails> call = apiService.SendRegistrationData(regdata);
        call.enqueue(new Callback<RegistrationDetails>() {
            @Override
            public void onResponse(retrofit2.Call<RegistrationDetails> call, Response<RegistrationDetails> response) {

                pd.dismiss();
                //Log.d("User ID: ", response.body().getMessage());
                if (response.isSuccessful()) {

                    if (response.body().getStatus().equals("1")) {
                        Log.e("responsedata", response.body().getStatus());
                        EmpowerApplication.set_session("employeeCode1", getEmpId());
                        //editor.putString("employeeCode",getEmpId());
                        //Log.e("share22222222data",EmpowerApplication.sharedPref.getString("employeeCode",""));
                        //editor.putString("companyCode",getCompanyId());
                        EmpowerApplication.set_session("companyCode1", getCompanyId());
                        EmpowerApplication.set_session("Mail1", response.body().getData());
                        //editor.commit();

                        Toast.makeText(RegistrationActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                        startActivity(new Intent(RegistrationActivity.this, LicenseActivity.class));

                    } else {
                        EmpowerApplication.alertdialog(response.body().getMessage(), RegistrationActivity.this);

                    }
                } else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", RegistrationActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", RegistrationActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", RegistrationActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }


            }

            @Override
            public void onFailure(retrofit2.Call<RegistrationDetails> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                pd.dismiss();
                EmpowerApplication.alertdialog(t.getMessage(), RegistrationActivity.this);

            }
        });
    }

    public String getEmpId() {
        return Empid.getText().toString().trim();
    }

    public String getPhoneNumber() {
        return phoneno.getText().toString().trim();
    }

    public String getCompanyId() {
        return companycode.getText().toString().trim();
    }

    public String getRegistrationType() {
        return spinner.getSelectedItem().toString();
    }

    public String getDeviceID() {
        String android_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        EmpowerApplication.set_session("deviceId", android_id);

        return android_id;
    }


}
