package alina.com.rms.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import alina.com.rms.R;
import alina.com.rms.activities.groupActivities.GroupMainActivity;
import alina.com.rms.activities.headquaterActivities.MainActivityHeaduater;
import alina.com.rms.activities.headquater_special_user.HeadQuaterSpecialUserMainActivity;
import alina.com.rms.activities.public_module.PublicUserMainActivity;
import alina.com.rms.activities.reporting_user.HomeReportingActivity;
import alina.com.rms.activities.special_user.SpecialUserMainActivity;
import alina.com.rms.activities.superAdminActivities.MainActivitySuperAdmin;
import alina.com.rms.activities.user_entry_section.MainActivityEntryUser;
import alina.com.rms.model.LoginResultPojo;
import alina.com.rms.util.LoginDB;

public class SplashScreen extends AppCompatActivity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;

    private List<LoginResultPojo> loginResultPojo;
    private LoginResultPojo loginResultPojo1;
    private String login_response;
    private String TAG = SplashScreen.class.getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        //askPermission();


        timerFun();

    }

//    public void setContentView(@LayoutRes int layoutResID) {
//        getDelegate().setContentView(layoutResID);
//    }


    private boolean checkPermission() {
        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (result == PackageManager.PERMISSION_GRANTED) {

            return true;

        } else {

            return false;

        }

    }




    private void timerFun() {
        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // close this activity
                if (LoginDB.getLoginFlag(SplashScreen.this)) {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<LoginResultPojo>>() {
                    }.getType();
     /*   String json = gson.toJson(login_response, type);
        System.out.println(json);*/
                    login_response = LoginDB.getLoginResponseAsJSON(SplashScreen.this);
                    loginResultPojo = gson.fromJson(login_response, type);
                    loginResultPojo1 = loginResultPojo.get(0);
                    if (loginResultPojo1.getUser_type() == 20 && loginResultPojo1.getRole() == 1) {
                        Intent intent = new Intent(SplashScreen.this, HeadQuaterSpecialUserMainActivity.class);
                        startActivity(intent);
                    } else if (loginResultPojo1.getUser_type() == 23 && loginResultPojo1.getRole() == 23) {
                        Intent intent = new Intent(SplashScreen.this, SpecialUserMainActivity.class);
                        startActivity(intent);
                    } else if (loginResultPojo1.getUser_type() == 6 && loginResultPojo1.getRole() == 6) {
                        Intent intent = new Intent(SplashScreen.this, PublicUserMainActivity.class);
                        startActivity(intent);
                    } else if (loginResultPojo1.getUser_type() == 4 && loginResultPojo1.getRole() == 5) {
                        Intent intent = new Intent(SplashScreen.this, MainActivityEntryUser.class);
                        startActivity(intent);
                    } else if (loginResultPojo1.getUser_type() == 5) {

                    } else if (loginResultPojo1.getRole() == 0) {
                        Intent intent = new Intent(SplashScreen.this, /*WeeklyMainActivitySuperAdmin*/MainActivitySuperAdmin.class);
                        startActivity(intent);
                    } else if (loginResultPojo1.getRole() == 1) {
/*                        Intent intent=new Intent(SplashScreen22.this,MainActivity.class);
                        startActivity(intent);*/
                        Intent intent = new Intent(SplashScreen.this, MainActivityHeaduater.class);
                        startActivity(intent);
                    } else if (loginResultPojo1.getRole() == 2) {
                        Intent intent = new Intent(SplashScreen.this, GroupMainActivity.class);
                        startActivity(intent);
                    }
                    else if (loginResultPojo1.getRole() == 4) {
                        Intent intent = new Intent(SplashScreen.this, HomeReportingActivity.class);
                        startActivity(intent);
                    }
                    finish();
                } else {
                    Intent i = new Intent(SplashScreen.this, SplashScreen2.class);
                    startActivity(i);
                    finish();
                }

            }
        }, SPLASH_TIME_OUT);
    }
}
