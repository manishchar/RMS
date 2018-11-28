package alina.com.rms.activities;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import alina.com.rms.R;
import alina.com.rms.activities.headquaterActivities.MainActivityHeaduater;
import alina.com.rms.activities.headquater_special_user.HeadQuaterSpecialUserMainActivity;
import alina.com.rms.activities.public_module.PublicUserMainActivity;
import alina.com.rms.activities.reporting_user.HomeReportingActivity;
import alina.com.rms.activities.special_user.SpecialUserMainActivity;
import alina.com.rms.activities.superAdminActivities.MainActivitySuperAdmin;
import alina.com.rms.activities.user_entry_section.MainActivityEntryUser;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.model.LoginDto;
import alina.com.rms.model.Response;
import alina.com.rms.model.Result;
import alina.com.rms.util.CallType;
import alina.com.rms.util.LoginDB;
import alina.com.rms.util.Util;

public class LoginActivity extends AppCompatActivity implements AsyncCompleteListner{
    private String email="";
    private String password="";

    private EditText emailEditTxt;
    private EditText passEditTxt;
    private ImageButton loginImageButton;
    private TextView textView3;
    private Dialog openDialog;
    private String TAG = LoginActivity.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        emailEditTxt=(EditText)findViewById(R.id.emailEditText);
        passEditTxt=(EditText)findViewById(R.id.passwordEditText);
        loginImageButton=(ImageButton)findViewById(R.id.loginImageButton);
        textView3=(TextView)findViewById(R.id.textView3);
        textView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RecoveryPasswodActivity.class);
                startActivity(intent);
            }
        });
        loginImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Util.isValidEmail(emailEditTxt.getText()))
                {
                 email=emailEditTxt.getText().toString();
                }
                else
                {
                Util.showLongToast(LoginActivity.this,"Email is not valid");
                return;
                }

                if(passEditTxt.getText()!=null)
                {   if(passEditTxt.getText().toString().length()>2)
                {
                    password=passEditTxt.getText().toString();
                }
                else {
                    Util.showLongToast(LoginActivity.this,"Password must be greater then 3");
                    return;
                }
                }
                else
                {
                    Util.showLongToast(LoginActivity.this,"Password is not valid");
                    return;
                }
                makeHttpRequest();

            }
        });

    }

    private void makeHttpRequest()
    {
        LoginDto loginDto =new LoginDto();
        loginDto.setEmail(email);
        loginDto.setPassword(password);
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(LoginActivity.this,this,CallType.POST_LOGIN,loginDto,true);
        asyncController.setProgressDialoug(true);
        asyncController.loginRequest();

    }

    @Override
    public void asyncCompleteListner(String response, CallType callType) {
        setProgressDialoug(false);
    if(response!=null)
    {
        Log.e("response",response);
        try {
            Gson gson=new Gson();
            Result result= gson.fromJson(response, Result.class);
            JSONObject jsonObject=new JSONObject(response);
            if(result.getResponseCode()!=null)
            {
                if(jsonObject.has("response_code"))
                {
                    if(Integer.parseInt(jsonObject.getString("response_code"))==0)
                    {
                        Util.showLongToast(LoginActivity.this,jsonObject.getString("message"));

                    }
                    else {
                        List<Response> response1=result.getResponse();
                        Response response2=response1.get(0);

                        LoginDB.setLoginResponseAsJSON(LoginActivity.this,jsonObject.getString("response"));
                        if(response2.getUser_type()==20 && response2.getRole()==1)
                        {
                            Intent intent=new Intent(LoginActivity.this,HeadQuaterSpecialUserMainActivity.class);
                            startActivity(intent);
                        }
                        else if(response2.getUser_type()==23 && response2.getRole()==23)
                        {   //UserType 23 is Board
                            Intent intent=new Intent(LoginActivity.this,SpecialUserMainActivity.class);
                            startActivity(intent);
                        }
                       else if(response2.getUser_type()==6 && response2.getRole()==6)
                        {
                            Intent intent=new Intent(LoginActivity.this,PublicUserMainActivity.class);
                            startActivity(intent);
                        }
                       else if(response2.getUser_type()==4 && response2.getRole()==5)
                        {
                            Intent intent=new Intent(LoginActivity.this,MainActivityEntryUser.class);
                            startActivity(intent);
                        }

                        else if(response2.getRole()==0)
                        {
                            Intent intent=new Intent(LoginActivity.this,/*WeeklyMainActivitySuperAdmin*/MainActivitySuperAdmin.class);
                            startActivity(intent);
                        }
                        else if(response2.getRole()==1)
                        {
                            Intent intent=new Intent(LoginActivity.this,MainActivityHeaduater.class);
                            startActivity(intent);
                        }
                        else if(response2.getRole()==2)
                        {
/*                            LoginDB.setLoginResponseAsJSONForUSER(LoginActivity.this,response);
                           Intent intent=new Intent(LoginActivity.this,GroupMainActivity.class);
                            startActivity(intent);*/
                        }
                        else if (response2.getRole() == 4) {
                            Intent intent = new Intent(LoginActivity.this, HomeReportingActivity.class);
                            startActivity(intent);
                        }
                        finish();
                    }
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    else {
        Util.showShortToast(LoginActivity.this,"Error in connection");
    }
    }

    public void setProgressDialoug(boolean flag)
    {

        if(openDialog==null)
        {

            openDialog= new Dialog(LoginActivity.this);
            openDialog.setContentView(R.layout.progress_dialoug1);
           // openDialog.setTitle("Custom Dialog Box");
            openDialog.setCancelable(false);
            // disable scroll on touch


        }
        if(flag)
        {
            openDialog.show();
        }
        else
        {
            openDialog.dismiss();
        }

    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(LoginActivity.this,SplashScreen2.class));
        finish();
        super.onBackPressed();
    }


    public void askPermission() {
        if (Build.VERSION.SDK_INT >= 23) {
            if ((checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED) &&
                    (checkSelfPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) &&
                    (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
                    && (checkSelfPermission(android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
                    ) {
                Log.v(TAG, "Permission is granted");

            } else {

                Log.v(TAG, "Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1025);
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("size", "" + grantResults.length);
        askPermission();
    }

}
