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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class GroupTSSWebViewActivity extends AppCompatActivity implements AsyncCompleteListner,AdapterView.OnItemSelectedListener {
    List<String> countryNames=new ArrayList<String>();
    List<String> keyValues=new ArrayList<String>();

    List<String> countryNames1=new ArrayList<String>();
    List<String> keyValues1=new ArrayList<String>();

    List<String> countryNames2=new ArrayList<String>();
    List<String> keyValues2=new ArrayList<String>();
    List<String> spinnerMonth=new ArrayList<String>();
    private WebView webView;
    private String postUrl="http://ebunchapps.com/rmsnew/api/oheprogress/01/Foundation-in-Nos";
    CustomAdapter customAdapter,customMonthAdapter;
    public String selectedMonth="01";
    private String selectedKey="";
    public Toolbar toolbar;
    public FrameLayout container;
    public DrawerLayout drawer;
    private ImageView navigation_btn;
    public TextView headingText;
    RadioGroup radioBtnGroup;
    RadioButton r1,r2;
    private Dialog openDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        headingText=(TextView)toolbar.findViewById(R.id.header_title);
        headingText.setText("TSS Report");

        navigation_btn=(ImageView) toolbar.findViewById(R.id.navigation_btn);
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





    private void addMonth()
    {
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
        webView=(WebView)findViewById(R.id.webView);
        webView.setWebViewClient(new MyBrowser());
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        radioBtnGroup=(RadioGroup)findViewById(R.id.radioBtnGroup);
        radioBtnGroup.setVisibility(View.VISIBLE);
        r1=(RadioButton)findViewById(R.id.radioBtnCivil);
        r2=(RadioButton)findViewById(R.id.radioBtnElectrical);
        radioBtnGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                if (isChecked)
                {
                    // Changes the textview's text to "Checked: example radiobutton text"

                }
                Log.e("value","check");
                keyValues2.clear();
                countryNames2.clear();
                if(checkedId==R.id.radioBtnCivil)
                {
                    keyValues2.addAll(keyValues);
                    countryNames2.addAll(countryNames);


                }
                else {
                    keyValues2.addAll(keyValues1);
                    countryNames2.addAll(countryNames1);

                }
                customAdapter.notifyDataSetChanged();

            }
        });
        getOHETarget();
        customAdapter=new CustomAdapter(getApplicationContext(),countryNames2);
        spin.setAdapter(customAdapter);
        customMonthAdapter=new CustomAdapter(getApplicationContext(),spinnerMonth);
        spinnerMonth1.setAdapter(customMonthAdapter);


    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if(openDialog==null) {
                openDialog = new Dialog(getApplicationContext());
                openDialog.setContentView(R.layout.progress_dialoug);
               // openDialog.setTitle("Custom Dialog Box");
                WebView webviewSpinner = (WebView) openDialog.findViewById(R.id.webView);
                webviewSpinner.setWebViewClient(new WebViewClient());
                webviewSpinner.getSettings().setJavaScriptEnabled(true);
                webviewSpinner.setBackgroundColor(Color.TRANSPARENT);
                webviewSpinner.setScrollContainer(false);
                webviewSpinner.setLayerType(WebView.LAYER_TYPE_SOFTWARE, null);
                webviewSpinner.getSettings().setRenderPriority(WebSettings.RenderPriority.HIGH);
                webviewSpinner.getSettings().setCacheMode(WebSettings.LOAD_NO_CACHE);
                webviewSpinner.loadDataWithBaseURL("file:///android_asset/", Util.htmlString, "text/html", "utf-8", "");
                openDialog.setCancelable(false);
                // disable scroll on touch
                webviewSpinner.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        return (event.getAction() == MotionEvent.ACTION_MOVE);
                    }
                });
            }
            openDialog.show();

            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url)
        {
            if(openDialog!=null)
            {
                openDialog.dismiss();
            }
        }
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon)
        {


        }
    }


    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg1, View arg0, int position,long id) {
        //Toast.makeText(getApplicationContext(), countryNames.get(position), Toast.LENGTH_LONG).show();
       if(arg1.getId()==R.id.spinner)
       {
           Log.e("Key Values",keyValues.get(position));
           selectedKey=keyValues.get(position);
       }
       else if(arg1.getId()==R.id.spinnerMonth)
       {
          // Log.e("Key Values",keyValues.get(position));
           if(position<9)
           {
               selectedMonth="0"+(position+1);
           }
           else {
               selectedMonth=""+(position+1);
           }
       }
       if(keyValues.size()>0)
       {
           postUrl="http://ebunchapps.com/rmsnew/api/tssprogress/"+selectedMonth+"/"+selectedKey;
       //    http://ebunchapps.com/rmsnew/api/tssprogress/02/Earth-filling-work
           webView.loadUrl(postUrl);
       }


    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }


    public void getOHETarget()
    {

        AsyncController asyncController=new AsyncController(GroupTSSWebViewActivity.this,this, CallType.GET_TSS_TARGET,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getOHERequest();
    }

    @Override
    public void asyncCompleteListner(String response, CallType callType) {
        if (response != null) {

                if (callType == CallType.GET_TSS_TARGET) {try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.has("response_code")) {
                            if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                                Util.showLongToast(GroupTSSWebViewActivity.this, "No data available");

                            } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                                countryNames.clear();
                                keyValues.clear();
                                countryNames1.clear();
                                keyValues1.clear();
                                countryNames2.clear();
                                keyValues2.clear();
                                JSONObject jsonObject1 = new JSONObject(jsonObject.getString("civil"));
                                JSONObject jsonObjectElctrical = new JSONObject(jsonObject.getString("electrical"));

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

                                Iterator<String> keys1 = jsonObjectElctrical.keys();
                                //  int i=0;
                                while (keys1.hasNext()) {
                                    try {
                                        String value = keys1.next();
                                        // Log.e("key : ",value);//,"pair : "+jsonObject1.getString(""+keys.next()));
                                        keyValues1.add(value);
                                        countryNames1.add(String.valueOf(jsonObjectElctrical.get(value)));
                                        // Log.e("pair",String.valueOf(jsonObject1.get(value)));
                                        //i++;
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }


                                }

                                if (countryNames.size() > 0) {
                                    Log.e("civil","civil1");
                                    keyValues2.addAll(keyValues);
                                    Log.e("civil","civil1"+keyValues2.size());
                                    countryNames2.addAll(countryNames);
                                    r1.setChecked(true);
                                }
                                else if(countryNames1.size() > 0)
                                {
                                    r2.setChecked(true);
                                    keyValues2.addAll(keyValues1);
                                    countryNames2.addAll(countryNames1);
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

}
