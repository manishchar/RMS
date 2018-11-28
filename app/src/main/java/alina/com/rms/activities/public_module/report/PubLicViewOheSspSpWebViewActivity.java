package alina.com.rms.activities.public_module.report;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import alina.com.rms.R;
import alina.com.rms.adaptor.CustomAdapter;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.model.Response;
import alina.com.rms.model.Result;
import alina.com.rms.model.Sectionlist;
import alina.com.rms.util.CallType;
import alina.com.rms.util.LoginDB;
import alina.com.rms.util.URL;
import alina.com.rms.util.Util;

public class PubLicViewOheSspSpWebViewActivity extends AppCompatActivity implements AsyncCompleteListner, AdapterView.OnItemSelectedListener {
    List<String> countryNames = new ArrayList<String>();
    List<String> keyValues = new ArrayList<String>();
    List<String> spinnerMonth = new ArrayList<String>();
    private WebView webView;
    private String postUrl = "http://ebunchapps.com/rmsnew/api/oheprogress/01/Foundation-in-Nos";
    CustomAdapter customAdapter, sectionNameAdapter, monthAdapter;
    public String selectedMonth = "01";
    private String selectedKey = "";
    public Toolbar toolbar;
    private Spinner section_spinner;
    public FrameLayout container;
    public DrawerLayout drawer;
    private ImageView navigation_btn;
    public TextView headingText;
    String screenName = "";
    private Dialog openDialog;
    TextView date1, date2;
    private String from_date, to_date = "";
    private Response response;
    private List<Response> response1;
    private String selected_group, selected_section, hadquater_id;
    private ProgressBar progressBar2;
    List<String> sectionNames = new ArrayList<String>();
    private List<Sectionlist> sectionlist = new ArrayList<Sectionlist>();
    TextView txt_section;
    RelativeLayout rel_section;
    TextView download_txt;
    Spinner month_spinner;
    private String month = "1";

    /*    final String[] months = new String[] { "January", "February",
                "March", "April", "May", "June", "July", "August", "September",
                "October", "November", "December" };*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addMonth();
        headingText = (TextView) toolbar.findViewById(R.id.header_title);
        download_txt = findViewById(R.id.download_txt);
        download_txt.setVisibility(View.GONE);
        progressBar2 = (ProgressBar) findViewById(R.id.progressBar2);
        section_spinner = (Spinner) findViewById(R.id.section_spinner);
        txt_section = (TextView) findViewById(R.id.txt_section);
        rel_section = (RelativeLayout) findViewById(R.id.rel_section);
        sectionNameAdapter = new CustomAdapter(this, sectionNames);
        section_spinner.setOnItemSelectedListener(this);
        section_spinner.setAdapter(sectionNameAdapter);

        month_spinner = (Spinner) findViewById(R.id.month_spinner);
        monthAdapter = new CustomAdapter(this, spinnerMonth);
        month_spinner.setAdapter(monthAdapter);
        month_spinner.setOnItemSelectedListener(this);


        try {


            Gson gson = new Gson();
            Type type = new TypeToken<List<Response>>() {
            }.getType();

            response1 = gson.fromJson(LoginDB.getLoginResponseAsJSON(PubLicViewOheSspSpWebViewActivity.this), type);
            response = response1.get(0);
        } catch (Exception ex) {
            Log.e("exception", "" + ex);
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

        // addView();


        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("screen_name")) ;
            {
                screenName = bundle.getString("screen_name");
            }
            if (bundle.containsKey("selected_group")) {
                selected_group = bundle.getString("selected_group");
                if (selected_group.trim().length() < 1) {
                    txt_section.setVisibility(View.GONE);
                    rel_section.setVisibility(View.GONE);
                }
            }

            if (bundle.containsKey("selected_section")) {
                selected_section = bundle.getString("selected_section");
            }
            if (bundle.containsKey("hadquater_id")) {
                hadquater_id = bundle.getString("hadquater_id");
                response.setHeadquater(hadquater_id);
            }
        }
        initUI();
    }


    private void addMonth() {
        spinnerMonth.add("");
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
        webView = (WebView) findViewById(R.id.webView);
        date1 = (TextView) findViewById(R.id.selected_date_text1);
        date2 = (TextView) findViewById(R.id.selected_date_text2);
        webView.setWebViewClient(new MyBrowser());
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {

                if (progress == 100)
                    progressBar2.setVisibility(View.GONE);
            }
        });

        if (screenName.equalsIgnoreCase("SP")) {
            headingText.setText("SP Report");
            txt_section.setText("Select Type");
            getSPOHETarget();
        } else if (screenName.equalsIgnoreCase("SSP")) {
            txt_section.setText("Select Type");
            headingText.setText("SSP Report");
            getSSPOHETarget();
        } else {
            txt_section.setText("Select Section");
            headingText.setText("OHE Report");
            getOHETarget();
        }

        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(date1, PubLicViewOheSspSpWebViewActivity.this, 1);
            }
        });

        date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(date2, PubLicViewOheSspSpWebViewActivity.this, 2);
            }
        });


        download_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (keyValues.size() > 0) {
                    /* if (from_date != null) {*/
                    String postUrl1 = "http://alinasoftwares.in/testapp/webapi/adminProgressExcel" + "?hqId=" + response.getHeadquater() + "&" +
                            "type=" + selectedKey + "&group_id=" + selected_group + "&section_id=" + selected_section +
                            "&month=" + month/*from_date=" + from_date + "&to_date=" + to_date*/;
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(postUrl1));
                    startActivity(browserIntent);
                    /*}
                    else
                    {
                        Toast.makeText(HeaduaterWebViewActivity.this,"Please select date first to download file",Toast.LENGTH_LONG).show();
                    }*/
                } else {
                    Toast.makeText(PubLicViewOheSspSpWebViewActivity.this, "Something is wrong", Toast.LENGTH_LONG).show();
                }
            }

        });
        customAdapter = new CustomAdapter(getApplicationContext(), countryNames);
        spin.setAdapter(customAdapter);
/*        customMonthAdapter=new CustomAdapter(getApplicationContext(),spinnerMonth);
        spinnerMonth1.setAdapter(customMonthAdapter);*/


    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            progressBar2.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            //progressBar2.setVisibility(View.GONE);


        }

    }

    public void setProgressDialoug(boolean flag) {

        if (openDialog == null) {

            openDialog = new Dialog(PubLicViewOheSspSpWebViewActivity.this);
            openDialog.setContentView(R.layout.progress_dialoug1);
            // openDialog.setTitle("Custom Dialog Box");
            openDialog.setCancelable(false);
            // disable scroll on touch


        }
        if (flag) {
            openDialog.show();
        } else {
            openDialog.dismiss();
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
        } else if (arg1.getId() == R.id.section_spinner) {
            // selected_section=headquaterlist.get(position).getId();
            if (position == 0) {
                selected_section = "";
            } else {
                /// selectedKey=keyValues.get(position-0);
                selected_section = sectionlist.get(position - 1).getId();
            }


        } else if (arg1.getId() == R.id.month_spinner) {
            if (position > 0) {
                month = "" + position;
                callWebFun();
            }
        }
        date1.setText("");
        date2.setText("");
        //callWebFun();


    }

    private void callWebFun() {
        String baseUrl = "";

        if (screenName.equalsIgnoreCase("SP")) {
            baseUrl = "spprogress";
        } else if (screenName.equalsIgnoreCase("SSP")) {
            baseUrl = "sspprogress";
        } else {
            baseUrl = "oheprogress";
        }
        if (keyValues.size() > 0) {

            postUrl = URL.url + baseUrl + "?hqId=" + response.getHeadquater() + "&" +
                    "type=" + selectedKey + "&group_id=" + selected_group + "&section_id=" + selected_section +
                    "&month=" + month/*"&from_date=" + from_date + "&to_date=" + to_date*/;
            Log.e("URL : ", postUrl);
            webView.loadUrl(postUrl);
        }
    }

    public void openDatePickerDialog(final TextView editText, Context context, final int position) {
        int mYear;
        int mMonth;
        int mDay;
        final Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, 0);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DATE);

        // Launch Date Picker Dialog
        DatePickerDialog dpd = new DatePickerDialog(context,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        // Display Selected date in textbox
                        String date;
                        String month;
                        if ((monthOfYear + 1) < 10) {
                            month = "0" + (monthOfYear + 1);
                        } else {
                            month = "" + (monthOfYear + 1);
                            ;

                        }

                        if ((dayOfMonth) < 10) {
                            date = "0" + dayOfMonth;
                        } else {
                            date = "" + dayOfMonth;
                        }
                        String date1 = year + "-" + month + "-"
                                + date;
                        editText.setText(date1);
                        if (position == 1) {
                            from_date = date1;
                            //selected_date_text1=date1;
                            //  getTargetValue();
                        } else if (position == 2) {
                            to_date = date1;

                        }
                        callWebFun();
                        // context.addDate_tolist(position,sdate,b);

                    }
                }, mYear, mMonth, mDay);
        dpd.setTitle("Select Date");
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpd.show();
    }


    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
    }

    private void getSectionListFromServer(String selected_group1) {
        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(this, this, CallType.GET_SECTION_LIST, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getHeadquaterSectionList(hadquater_id, selected_group1);

        // GET_USER_LIST
    }

    public void getSPType() {

        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(PubLicViewOheSspSpWebViewActivity.this, this, CallType.GET_SECTION_LIST, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getHeadquaterSP_SSP_TSSListBYGRoupID(selected_group, URL.get_sp);
    }

    public void getSSPType() {

        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(PubLicViewOheSspSpWebViewActivity.this, this, CallType.GET_SECTION_LIST, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getHeadquaterSP_SSP_TSSListBYGRoupID(selected_group, URL.get_ssp);
    }

    public void getOHETarget() {
        setProgressDialoug(true);

        AsyncController asyncController = new AsyncController(PubLicViewOheSspSpWebViewActivity.this, this, CallType.GET_OHE, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getOHERequest();
    }

    public void getSPOHETarget() {
        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(PubLicViewOheSspSpWebViewActivity.this, this, CallType.GET_SP_TARGET, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getOHERequest();
    }

    public void getSSPOHETarget() {
        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(PubLicViewOheSspSpWebViewActivity.this, this, CallType.GET_SP_TARGET, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getOHERequest();
    }

    @Override
    public void asyncCompleteListner(String response, CallType callType) {
        setProgressDialoug(false);
        if (response != null) {

            if (callType == CallType.GET_OHE || callType == CallType.GET_SP_TARGET) {
                try {

                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(PubLicViewOheSspSpWebViewActivity.this, "No data available");

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
                            if (callType == CallType.GET_OHE) {
                                if (selected_group.trim().length() > 0) {
                                    getSectionListFromServer(selected_group);
                                }
                            } else {
                                if (selected_group.trim().length() > 0) {
                                    if (screenName.equalsIgnoreCase("SP")) {
                                        getSPType();
                                    } else if (screenName.equalsIgnoreCase("SSP")) {
                                        getSSPType();
                                    }
                                }
                            }
                        }
                        // month_spinner.setSelection(0);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (callType == CallType.GET_SECTION_LIST) {
                Gson gson = new Gson();
                Result response1 = gson.fromJson(response, Result.class);

                if (response1 != null) {
                    if (response1.getSectionlist() != null) {
                        Log.e("String", "value");
                        sectionNames.clear();
                        sectionlist.clear();
                        sectionlist.addAll(response1.getSectionlist());
                        Log.e("value", "" + sectionlist.size());
                        sectionNames.add("all");
                        for (int i = 0; i < sectionlist.size(); i++) {
                            if (screenName.equalsIgnoreCase("SP")) {
                                sectionNames.add(sectionlist.get(i).getSp_name());
                            } else if (screenName.equalsIgnoreCase("SSP")) {
                                sectionNames.add(sectionlist.get(i).getSsp_name());
                            } else {
                                sectionNames.add(sectionlist.get(i).getSection());
                            }

                        }
                        sectionNameAdapter.notifyDataSetChanged();
                        if (sectionlist.size() > 0) {
                            selected_section = sectionlist.get(0).getId();
                            section_spinner.setSelection(0);
                            txt_section.setVisibility(View.VISIBLE);
                            rel_section.setVisibility(View.VISIBLE);
                        }
                        /*month_spinner.setSelection(0);*/


                    } else {
                        sectionNames.clear();
                        sectionlist.clear();
                        sectionNameAdapter.notifyDataSetChanged();
                        txt_section.setVisibility(View.GONE);
                        rel_section.setVisibility(View.GONE);

                        // Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                    }
                } else {
                    sectionNames.clear();
                    sectionlist.clear();
                    txt_section.setVisibility(View.GONE);
                    rel_section.setVisibility(View.GONE);
                    sectionNameAdapter.notifyDataSetChanged();
                    //   Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
