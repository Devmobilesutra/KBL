package com.emsphere.commando4.kirloskarempowerapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.emsphere.commando4.kirloskarempowerapp.constantclass.EmpowerApplication;
import com.emsphere.commando4.kirloskarempowerapp.pojo.ChangePwdPOJO;
import com.emsphere.commando4.kirloskarempowerapp.rest.ApiClient;
import com.emsphere.commando4.kirloskarempowerapp.rest.ApiInterface;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Callback;
import retrofit2.Response;

import static com.emsphere.commando4.kirloskarempowerapp.MainActivity.userName;


public class ChangepwdActivity extends AppCompatActivity {

    TextView old_pwd,new_pwd,confirm_pwd;
    Button update,close;
    ProgressDialog pd;
    private ApiInterface apiService;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_changepwd);

        old_pwd = findViewById(R.id.old_pwd);
        new_pwd = findViewById(R.id.new_pwd);
        confirm_pwd = findViewById(R.id.confirm_pwd);
        update =findViewById(R.id.update);
        close = findViewById(R.id.close);
        apiService = ApiClient.getClient().create(ApiInterface.class);


        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                 startActivity(new Intent(ChangepwdActivity.this, MainActivity.class));

            }
        });

        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(old_pwd.getText().toString().isEmpty()){

                    Toast.makeText(ChangepwdActivity.this, "Please Enter Old password", Toast.LENGTH_SHORT).show();

                }else if(new_pwd.getText().toString().isEmpty()){

                    Toast.makeText(ChangepwdActivity.this, "Please Enter new password", Toast.LENGTH_SHORT).show();

                }else if(!new_pwd.getText().toString().equalsIgnoreCase(confirm_pwd.getText().toString())){

                    Toast.makeText(ChangepwdActivity.this, "Not matched your password", Toast.LENGTH_SHORT).show();

                }else{

                     UpdatePassword(old_pwd.getText().toString(), new_pwd.getText().toString(), confirm_pwd.getText().toString(), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("deviceId1")), EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("employeeId1")));
                }

            }
        });

    }

    public void UpdatePassword(String old_pwd, String new_pwd,String confirm_pwd, String deviceid, String employeecode) {
        pd = new ProgressDialog(ChangepwdActivity.this);
        pd.setMessage("Loading....");
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        Map<String, String> changePwd = new HashMap<String, String>();
        changePwd.put("userName", userName);
        changePwd.put("employeeCode", userName);
        changePwd.put("oldPassword", old_pwd);
        changePwd.put("newPassword", new_pwd);
        changePwd.put("confirmPassword", confirm_pwd);
        changePwd.put("deviceId", deviceid);
        changePwd.put("token", EmpowerApplication.aesAlgorithm.Decrypt(EmpowerApplication.get_session("data")));
     //   Toast.makeText(this, ""+logindata, Toast.LENGTH_LONG).show();
        Log.d("", "UpdatePasswordlogindata: "+changePwd);
        retrofit2.Call<ChangePwdPOJO> call = apiService.UpdatePassword(changePwd);

        call.enqueue(new Callback<ChangePwdPOJO>() {
            @Override
            public void onResponse(retrofit2.Call<ChangePwdPOJO> call, Response<ChangePwdPOJO> response) {
                pd.dismiss();

                if (response.isSuccessful()) {
                    //Log.d("User ID1: ", response.body().toString());
                    if (response.body().getStatus().equals("1")) {

                       startActivity(new Intent(ChangepwdActivity.this, MainActivity.class));

                        Toast.makeText(ChangepwdActivity.this, ""+ response.body().getMessage(), Toast.LENGTH_SHORT).show();


                    }





                } else {
                    switch (response.code()) {
                        case 404:
                            //Toast.makeText(ErrorHandlingActivity.this, "not found", Toast.LENGTH_SHORT).show();
                            EmpowerApplication.alertdialog("File or directory not found", ChangepwdActivity.this);
                            break;
                        case 500:
                            EmpowerApplication.alertdialog("server broken", ChangepwdActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "server broken", Toast.LENGTH_SHORT).show();
                            break;
                        default:
                            EmpowerApplication.alertdialog("unknown error", ChangepwdActivity.this);

                            //Toast.makeText(ErrorHandlingActivity.this, "unknown error", Toast.LENGTH_SHORT).show();
                            break;
                    }
                }

            }

            @Override
            public void onFailure(retrofit2.Call<ChangePwdPOJO> call, Throwable t) {
                // Log error here since request failed
                Log.e("TAG", t.toString());
                pd.dismiss();
                try {
                    if (t.getMessage().equals("Field map contained null value for key 'employeeCode'") || t.getMessage().equals("Field map contained null value for key 'deviceId'")) {
                        EmpowerApplication.alertdialog("Empolyee need to do registration", ChangepwdActivity.this);
                    } else {
                        EmpowerApplication.alertdialog(t.getMessage(), ChangepwdActivity.this);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

            }
        });
    }

}
