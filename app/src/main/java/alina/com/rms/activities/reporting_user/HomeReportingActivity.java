package alina.com.rms.activities.reporting_user;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import alina.com.rms.R;
import alina.com.rms.activities.SplashScreen2;

public class HomeReportingActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView buttonFoundation, buttonMast, buttonWiring,buttonCrs;
    private RadioButton radioButtonDaily, radioButtonWeekly;
    ImageView logout;
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_reporting);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        buttonFoundation = (TextView) findViewById(R.id.buttonFoundation);
        buttonMast = (TextView) findViewById(R.id.buttonMast);
        buttonWiring = (TextView) findViewById(R.id.buttonWiring);
        buttonCrs = (TextView) findViewById(R.id.buttonCrs);
        radioButtonDaily = (RadioButton) findViewById(R.id.radioButtonDaily);
        radioButtonWeekly = (RadioButton) findViewById(R.id.radioButtonWeekly);
        logout = (ImageView)findViewById(R.id.logout);
        logout.setVisibility(View.VISIBLE);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(HomeReportingActivity.this)
                        .setTitle("Are you sure,you want to logout")
                        .setIcon(R.drawable.logo)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface view, int pos) {
                                SharedPreferences pref = PreferenceManager
                                        .getDefaultSharedPreferences(HomeReportingActivity.this);
                                SharedPreferences.Editor edit = pref.edit();
                                edit.clear();
                                edit.commit();
                                finish();
                                Intent intent = new Intent(HomeReportingActivity.this, SplashScreen2.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .show();
            }
        });
        buttonFoundation.setOnClickListener(this);
        buttonMast.setOnClickListener(this);
        buttonWiring.setOnClickListener(this);
        buttonCrs.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {

        String reportType;
        String type = "";
        String screenName = "";
        if (radioButtonWeekly.isChecked()) {
            reportType = "weekly";
        } else {
            reportType = "daily";
        }

        switch (v.getId()) {
            case R.id.buttonFoundation:
                Intent intent1 = new Intent(this, ReportingProjectActivity.class);
                type = "foundation";
                screenName = "Foundation";
                intent1.putExtra("screenName",screenName);
                intent1.putExtra("type", type);
                intent1.putExtra("reportType", reportType);
                startActivity(intent1);
                break;
            case R.id.buttonMast:
                Intent intent2 = new Intent(this, ReportingProjectActivity.class);
                type = "mast";
                screenName = "Mast Eriction";
                intent2.putExtra("screenName",screenName);
                intent2.putExtra("type", type);
                intent2.putExtra("reportType", reportType);
                startActivity(intent2);
                break;
            case R.id.buttonWiring:
                Intent intent = new Intent(this, ReportingProjectActivity.class);
                type = "wiring";
                screenName = "Wiring";
                intent.putExtra("screenName",screenName);
                intent.putExtra("type", type);
                intent.putExtra("reportType", reportType);
                startActivity(intent);
                break;
            case R.id.buttonCrs:
                Intent intent3 = new Intent(this, ReportWebViewActivity.class);
                startActivity(intent3);
                break;
                default:
                    break;

        }

    }
}
