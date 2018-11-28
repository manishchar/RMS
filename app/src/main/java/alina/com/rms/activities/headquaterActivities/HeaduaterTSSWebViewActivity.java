package alina.com.rms.activities.headquaterActivities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

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

public class HeaduaterTSSWebViewActivity extends AppCompatActivity implements AsyncCompleteListner,AdapterView.OnItemSelectedListener {
    List<String> countryNames=new ArrayList<String>();
    List<String> keyValues=new ArrayList<String>();

    List<String> countryNames1=new ArrayList<String>();
    List<String> keyValues1=new ArrayList<String>();

    List<String> countryNames2=new ArrayList<String>();
    List<String> keyValues2=new ArrayList<String>();
    List<String> spinnerMonth=new ArrayList<String>();
    private WebView webView;
    private String postUrl="http://ebunchapps.com/rmsnew/api/oheprogress/01/Foundation-in-Nos";
    CustomAdapter customAdapter,sectionNameAdapter,monthAdapter;
    public String selectedMonth="01";
    private String selectedKey="";
    public Toolbar toolbar;
    public FrameLayout container;
    public DrawerLayout drawer;
    private ImageView navigation_btn;
    public TextView headingText;
    RadioGroup radioBtnGroup;
    RadioButton r1,r2;
    String screenName="";
    private Dialog openDialog;
    TextView date1,date2;
    private String from_date,to_date="";
    private Response response;
    private List<Response> response1;
    private String selected_group,selected_section,hadquater_id;
    private ProgressBar progressBar2;

    TextView txt_section;
    RelativeLayout rel_section;
    private Spinner section_spinner;
    List<String> sectionNames=new ArrayList<String>();
    private List<Sectionlist> sectionlist=new ArrayList<Sectionlist>();
    Spinner month_spinner;
    private String month = "1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        headingText=(TextView)toolbar.findViewById(R.id.header_title);
        headingText.setText("TSS Report");
        progressBar2=(ProgressBar)findViewById(R.id.progressBar2);
        navigation_btn=(ImageView) toolbar.findViewById(R.id.navigation_btn);
        navigation_btn.setScaleType(ImageView.ScaleType.FIT_CENTER);
        navigation_btn.setImageResource(R.drawable.back_arrow);
        navigation_btn.getLayoutParams().width = 80;
        navigation_btn.getLayoutParams().height = 50;
        navigation_btn.requestLayout();
        txt_section=(TextView)findViewById(R.id.txt_section);
        txt_section.setText("Select Type");
        rel_section=(RelativeLayout)findViewById(R.id.rel_section);
        month_spinner = (Spinner) findViewById(R.id.month_spinner);
        monthAdapter = new CustomAdapter(this, spinnerMonth);
        month_spinner.setAdapter(monthAdapter);
        month_spinner.setOnItemSelectedListener(this);
        navigation_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        try {


            Gson gson=new Gson();
            Type type = new TypeToken<List<Response>>() {}.getType();

            response1=gson.fromJson(LoginDB.getLoginResponseAsJSON(HeaduaterTSSWebViewActivity.this),type);
            response=response1.get(0);
        }
        catch (Exception ex)
        {
            Log.e("exception",""+ex);
        }

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            if(bundle.containsKey("selected_group"))
            {
                selected_group = bundle.getString("selected_group");

            }

            if(bundle.containsKey("selected_section")){

                selected_group = bundle.getString("selected_group");
                if(selected_group.trim().length()<1)
                {
                    txt_section.setVisibility(View.GONE);
                    rel_section.setVisibility(View.GONE);
                }
            }
            if(bundle.containsKey("hadquater_id")){
                hadquater_id = bundle.getString("hadquater_id");
                response.setHeadquater(hadquater_id);
            }
        }

       // addMonth();
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
        addMonth();
        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);

        section_spinner=(Spinner)findViewById(R.id.section_spinner);

        section_spinner.setOnItemSelectedListener(this);
        sectionNameAdapter=new CustomAdapter(this,sectionNames);
        section_spinner.setAdapter(sectionNameAdapter);

        webView=(WebView)findViewById(R.id.webView);
        date1=(TextView)findViewById(R.id.selected_date_text1);
        date2=(TextView)findViewById(R.id.selected_date_text2);
        webView.setWebViewClient(new MyBrowser());
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {

                if(progress == 100 )
                    progressBar2.setVisibility(View.GONE);
            }
        });
        radioBtnGroup=(RadioGroup)findViewById(R.id.radioBtnGroup);
     //   radioBtnGroup.setVisibility(View.VISIBLE);
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
                selectedKey="";
                date1.setText("");
                date2.setText("");
                from_date="";
                to_date="";
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

        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(date1,HeaduaterTSSWebViewActivity.this,1);
            }
        });

        date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(date2,HeaduaterTSSWebViewActivity.this,2);
            }
        });


        customAdapter=new CustomAdapter(getApplicationContext(),countryNames2);
        spin.setAdapter(customAdapter);
        getOHETarget();


    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            progressBar2.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url)
        {
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
           Log.e("Key Values",keyValues2.get(position));
           selectedKey=keyValues2.get(position);
       }
       else if(arg1.getId()==R.id.section_spinner)
       {
           if(sectionlist.size()>0)
           selected_section=sectionlist.get(position).getId();
           else
           {
               selected_section="";
           }
       }
       else if (arg1.getId() == R.id.month_spinner) {
           if (position > 0) {
               month = "" + position;
               callWebFun();
           }
       }
        date1.setText("");
        date2.setText("");
       // callWebFun();
/*       if(keyValues.size()>0)
       {
           postUrl="http://ebunchapps.com/rmsnew/api/tssprogress/"+selectedMonth+"/"+selectedKey;
       //    http://ebunchapps.com/rmsnew/api/tssprogress/02/Earth-filling-work
           webView.loadUrl(postUrl);

       }*/


    }

    private void callWebFun()
    {
        String baseUrl="tssprogress";


        if(keyValues2.size()>0 && selectedKey.length()>0)
        {
            if(from_date!=null) {

                postUrl = URL.url + baseUrl + "?hqId=" + response.getHeadquater() + "&" +
                        "type=" + selectedKey + "&group_id=" + selected_group + "&section_id=" + selected_section +
                        "&from_date=" + from_date + "&to_date=" + to_date;
                Log.e("URL : ",postUrl);
                webView.loadUrl(postUrl);
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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
                        if((monthOfYear + 1)<10)
                        {
                            month="0"+(monthOfYear + 1);
                        }
                        else {
                            month=""+(monthOfYear + 1);;

                        }

                        if((dayOfMonth)<10)
                        {
                            date="0"+dayOfMonth;
                        }
                        else {
                            date=""+dayOfMonth;
                        }
                        String date1=year + "-" + month+ "-"
                                + date;
                        editText.setText(date1);
                        if(position==1)
                        {
                            from_date=date1;
                            //selected_date_text1=date1;
                            //  getTargetValue();
                        }
                        else if(position==2)
                        {
                            to_date=date1;

                        }
                        callWebFun();
                        // context.addDate_tolist(position,sdate,b);

                    }
                }, mYear, mMonth, mDay);
        dpd.setTitle("Select Date");
        // dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpd.show();
    }
    public void setProgressDialoug(boolean flag)
    {

        if(openDialog==null)
        {

            openDialog= new Dialog(HeaduaterTSSWebViewActivity.this);
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

    public void getOHETarget()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(HeaduaterTSSWebViewActivity.this,this, CallType.GET_TSS_TARGET,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getOHERequest();
    }

    public void getTssType()
    {

        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(HeaduaterTSSWebViewActivity.this,this, CallType.GET_TSS,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getHeadquaterSP_SSP_TSSListBYGRoupID(selected_group,URL.get_tss);
    }

    @Override
    public void asyncCompleteListner(String response, CallType callType) {
        setProgressDialoug(false);
        if (response != null) {

                if (callType == CallType.GET_TSS_TARGET) {try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.has("response_code")) {
                            if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                                Util.showLongToast(HeaduaterTSSWebViewActivity.this, "No data available");

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
                                  //  r1.setChecked(true);
                                }
                                int j=0;
                                for (int i=countryNames.size()-1;i<(countryNames.size()+countryNames1.size()-1);i++)
                                {
                                    keyValues2.add(keyValues1.get(j));
                                    countryNames2.add(countryNames1.get(j));
                                    j++;
                                }
                                /* if(countryNames1.size() > 0)
                                {
                                   // r2.setChecked(true);

                                    keyValues2.addAll(keyValues1);
                                    countryNames2.addAll(countryNames1);
                                }*/

                                customAdapter.notifyDataSetChanged();
                                Log.e("length of group",""+selected_group.length());
                                if(selected_group.trim().length()>0)
                                {
                                    txt_section.setVisibility(View.VISIBLE);
                                    rel_section.setVisibility(View.VISIBLE);
                                    getTssType ();

                                }
                                else {
                                    txt_section.setVisibility(View.GONE);
                                    rel_section.setVisibility(View.GONE);
                                }
                              // getTssType ();
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

            }
                else if (callType == CallType.GET_TSS) {
                    Gson gson = new Gson();
                    Result response1 = gson.fromJson(response, Result.class);

                    if (response1 != null) {
                        if (response1.getSectionlist() != null) {
                            Log.e("String", "value");
                            sectionNames.clear();
                            sectionlist.clear();
                            sectionlist.addAll(response1.getSectionlist());
                            Log.e("value", "" + sectionlist.size());
                            for (int i = 0; i < sectionlist.size(); i++) {
                                sectionNames.add(sectionlist.get(i).getTss_name());
                            }
                            sectionNameAdapter.notifyDataSetChanged();
                            if (sectionlist.size() > 0) {
                                selected_section = sectionlist.get(0).getId();
                                section_spinner.setSelection(0);
                                txt_section.setVisibility(View.VISIBLE);
                                rel_section.setVisibility(View.VISIBLE);
                            }


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
