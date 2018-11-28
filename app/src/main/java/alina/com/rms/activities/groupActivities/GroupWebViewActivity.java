package alina.com.rms.activities.groupActivities;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import alina.com.rms.R;
import alina.com.rms.adaptor.CustomAdapter;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.util.CallType;
import alina.com.rms.util.Util;

public class GroupWebViewActivity extends AppCompatActivity implements AsyncCompleteListner, AdapterView.OnItemSelectedListener {
    List<String> countryNames = new ArrayList<String>();
    List<String> keyValues = new ArrayList<String>();
    List<String> spinnerMonth = new ArrayList<String>();
    private WebView webView;
    private String postUrl = "http://ebunchapps.com/rmsnew/api/oheprogress/01/Foundation-in-Nos";
    CustomAdapter customAdapter, customMonthAdapter;
    public String selectedMonth = "01";
    private String selectedKey = "";
    public Toolbar toolbar;
    public FrameLayout container;
    public DrawerLayout drawer;
    private ImageView navigation_btn;
    public TextView headingText;
    String screenName = "";
    private Dialog openDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        headingText = (TextView) toolbar.findViewById(R.id.header_title);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("screen_name")) ;
            {
                screenName = bundle.getString("screen_name");
            }
        }


        navigation_btn = (ImageView) toolbar.findViewById(R.id.navigation_btn);
        navigation_btn.setScaleType(ImageView.ScaleType.FIT_CENTER);
        navigation_btn.setImageResource(R.drawable.back_arrow);
        navigation_btn.getLayoutParams().width = 80;
        navigation_btn.getLayoutParams().height = 50;
        navigation_btn.requestLayout();
        navigation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        addMonth();
        // addView();

        initUI();
    }


    private void addMonth() {
        spinnerMonth.add("January");
        spinnerMonth.add("February");
        spinnerMonth.add("March");
        spinnerMonth.add("April");
        spinnerMonth.add("May");
        spinnerMonth.add("June");
        spinnerMonth.add("July");
        spinnerMonth.add("August");
        spinnerMonth.add("September");
        spinnerMonth.add("October");
        spinnerMonth.add("November");
        spinnerMonth.add("December");
    }

    private void initUI() {
        // spinnerMonth
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        Spinner spinnerMonth1 = (Spinner) findViewById(R.id.spinnerMonth);
        spinnerMonth1.setOnItemSelectedListener(this);
        webView = (WebView) findViewById(R.id.webView);
        webView.setWebViewClient(new MyBrowser());
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);

        if (screenName.equalsIgnoreCase("SP")) {
            headingText.setText("SP Report");
            getSPOHETarget();
        } else if (screenName.equalsIgnoreCase("SSP")) {
            headingText.setText("SSP Report");
            getSSPOHETarget();
        } else {
            headingText.setText("OHE Report");
            getOHETarget();
        }

        customAdapter = new CustomAdapter(getApplicationContext(), countryNames);
        spin.setAdapter(customAdapter);
        customMonthAdapter = new CustomAdapter(getApplicationContext(), spinnerMonth);
        spinnerMonth1.setAdapter(customMonthAdapter);


    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (openDialog != null) {
                openDialog.dismiss();
            }
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {


        }
    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg1, View arg0, int position, long id) {
        //Toast.makeText(getApplicationContext(), countryNames.get(position), Toast.LENGTH_LONG).show();
        if (arg1.getId() == R.id.spinner) {
            Log.e("Key Values", keyValues.get(position));
            selectedKey = keyValues.get(position);
        } else if (arg1.getId() == R.id.spinnerMonth) {
            // Log.e("Key Values",keyValues.get(position));
            if (position < 9) {
                selectedMonth = "0" + (position + 1);
            } else {
                selectedMonth = "" + (position + 1);
            }
        }
        if (keyValues.size() > 0) {

            postUrl = "http://ebunchapps.com/rmsnew/api/oheprogress/" + selectedMonth + "/" + selectedKey;
            webView.loadUrl(postUrl);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public void getOHETarget() {

        AsyncController asyncController = new AsyncController(GroupWebViewActivity.this, this, CallType.GET_OHE, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getOHERequest();
    }

    public void getSPOHETarget() {

        AsyncController asyncController = new AsyncController(GroupWebViewActivity.this, this, CallType.GET_SP_TARGET, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getOHERequest();
    }

    public void getSSPOHETarget() {

        AsyncController asyncController = new AsyncController(GroupWebViewActivity.this, this, CallType.GET_SP_TARGET, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getOHERequest();
    }

    @Override
    public void asyncCompleteListner(String response, CallType callType) {
        if (response != null) {

            if (callType == CallType.GET_OHE || callType == CallType.GET_SP_TARGET) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(GroupWebViewActivity.this, "No data available");

                        } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                            countryNames.clear();
                            keyValues.clear();
                            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("response"));

                            Iterator<String> keys = jsonObject1.keys();
                            //  int i=0;
                            while (keys.hasNext()) {
                                try {
                                    String value = keys.next();
                                    // Log.e("key : ",value);//,"pair : "+jsonObject1.getString(""+keys.next()));
                                    keyValues.add(value);
                                    countryNames.add(String.valueOf(jsonObject1.get(value)));
                                    // Log.e("pair",String.valueOf(jsonObject1.get(value)));
                                    //i++;
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }


                            }
                            customAdapter.notifyDataSetChanged();
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void setProgressDialoug(boolean flag)
    {

        if(openDialog==null)
        {

            openDialog= new Dialog(this);
            openDialog.setContentView(R.layout.progress_dialoug1);
            //openDialog.setTitle("Custom Dialog Box");
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

}
