package alina.com.rms.activities.user_entry_section;

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
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
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
import alina.com.rms.adaptor.AOIListAdapter;
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

public class EntryUserTSSWebViewActivity extends AppCompatActivity implements AsyncCompleteListner,AdapterView.OnItemSelectedListener {
    List<String> countryNames=new ArrayList<String>();
    List<String> keyValues=new ArrayList<String>();

    List<String> countryNames1=new ArrayList<String>();
    List<String> keyValues1=new ArrayList<String>();

    List<String> countryNames2=new ArrayList<String>();
    List<String> keyValues2=new ArrayList<String>();
    List<String> spinnerMonth=new ArrayList<String>();
    private WebView webView;
    private String postUrl="http://ebunchapps.com/rmsnew/api/oheprogress/01/Foundation-in-Nos";
 //   CustomAdapter customAdapter,customMonthAdapter;
    public String selectedMonth="01";
    private String selectedKey="";
    public Toolbar toolbar;
    public FrameLayout container;
    public DrawerLayout drawer;
    private ImageView navigation_btn;
    public TextView headingText,select_type_txt_view;
    RadioGroup radioBtnGroup;
    RadioButton r1,r2;
    String screenName="";
    private Dialog openDialog;
    TextView date1,date2;
    private String from_date,to_date="";
    private Response response;
    private List<Response> response1;
    private String selected_group,selected_section;
    private String send_item,hadquater_id;
    Dialog listdialog;
    private List<Boolean>isChecked=new ArrayList<Boolean>();
    AOIListAdapter aoiListAdapter;
    ProgressBar progressBar;

    TextView txt_section;
    RelativeLayout rel_section;
    private Spinner section_spinner;
    List<String> sectionNames=new ArrayList<String>();
    private List<Sectionlist> sectionlist=new ArrayList<Sectionlist>();

    CustomAdapter sectionNameAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_web_view);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        headingText=(TextView)toolbar.findViewById(R.id.header_title);
        headingText.setText("TSS Report");
        progressBar=(ProgressBar)findViewById(R.id.progressBar);
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
        txt_section=(TextView)findViewById(R.id.txt_section);
        txt_section.setText("Select Type");
        rel_section=(RelativeLayout)findViewById(R.id.rel_section);
        try {


            Gson gson=new Gson();
            Type type = new TypeToken<List<Response>>() {}.getType();

            response1=gson.fromJson(LoginDB.getLoginResponseAsJSON(EntryUserTSSWebViewActivity.this),type);
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
               // selected_group = bundle.getString("selected_group");
                selected_group = bundle.getString("selected_group");
                if(selected_group.trim().length()<1)
                {
                    txt_section.setVisibility(View.GONE);
                    rel_section.setVisibility(View.GONE);
                }
            }

            if(bundle.containsKey("selected_section")){
                selected_section = bundle.getString("selected_section");

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


    private void initUI() {
       // spinnerMonth
        select_type_txt_view =(TextView)findViewById(R.id.selected__multiple_item);
        webView=(WebView)findViewById(R.id.webView);
        date1=(TextView)findViewById(R.id.selected_date_text1);
        date2=(TextView)findViewById(R.id.selected_date_text2);

        section_spinner=(Spinner)findViewById(R.id.section_spinner);

        section_spinner.setOnItemSelectedListener(this);
        sectionNameAdapter=new CustomAdapter(this,sectionNames);
        section_spinner.setAdapter(sectionNameAdapter);

        progressBar.setVisibility(View.INVISIBLE);
        webView.setWebViewClient(new MyBrowser());
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress)
            {

                if(progress == 100 )
                    progressBar.setVisibility(View.GONE);
            }
        });
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        radioBtnGroup=(RadioGroup)findViewById(R.id.radioBtnGroup);
       // radioBtnGroup.setVisibility(View.VISIBLE);
        r1=(RadioButton)findViewById(R.id.radioBtnCivil);
        r2=(RadioButton)findViewById(R.id.radioBtnElectrical);

        select_type_txt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showListView();
            }
        });


        radioBtnGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId)
            {
                // This will get the radiobutton that has changed in its check state
                RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                // This puts the value (true/false) into the variable
                boolean isChecked = checkedRadioButton.isChecked();
                // If the radiobutton that has changed in check state is now checked...
                select_type_txt_view.setText("");
                send_item="";
                date1.setText("");
                date2.setText("");
                from_date="";
                to_date="";
                if (isChecked)
                {
                    // Changes the textview's text to "Checked: example radiobutton text"

                }
                Log.e("value","check");
                keyValues2.clear();
                countryNames2.clear();
         /*       EntryUserTSSWebViewActivity.this.isChecked.clear();
                if(checkedId==R.id.radioBtnCivil)
                {
                    keyValues2.addAll(keyValues);
                    countryNames2.addAll(countryNames);
                    EntryUserTSSWebViewActivity.this.isChecked.addAll(isChecked1);


                }
                else {
                    keyValues2.addAll(keyValues1);
                    countryNames2.addAll(countryNames1);
                    EntryUserTSSWebViewActivity.this.isChecked.addAll(isChecked2);
                }*/

            }
        });

        date1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(date1, EntryUserTSSWebViewActivity.this,1);
            }
        });

        date2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(date2, EntryUserTSSWebViewActivity.this,2);
            }
        });

        getOHETarget();



    }

    private void showAreaListDailog() {

        listdialog = new Dialog(EntryUserTSSWebViewActivity.this);
        listdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        listdialog.setContentView(R.layout.dialog_aoi_list);
        listdialog.setCancelable(false);
        // CheckBox  check_btn = (CheckBox) listdialog.findViewById(R.id.check_btn);
        final ListView list_multipal_feeder = (ListView) listdialog.findViewById(R.id.dfeeder_list);
        TextView cancel = (TextView) listdialog.findViewById(R.id.txtbtn_cancel);
        TextView ok = (TextView) listdialog.findViewById(R.id.txtbtn_ok);
        aoiListAdapter = new AOIListAdapter(EntryUserTSSWebViewActivity.this, keyValues2,countryNames2,isChecked);
        list_multipal_feeder.setAdapter(aoiListAdapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listdialog.dismiss();
                send_item="";
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listdialog.dismiss();
                send_item="";
                for(int i=0;i<keyValues2.size();i++)
                {

                    if(isChecked.get(i))
                    {
                        if (i==0)
                        {
                            send_item=keyValues2.get(i);
                            break;
                        }
                        if(send_item.trim().length()==0)
                        {
                            send_item=keyValues2.get(i);
                        }
                        else {
                            send_item+=","+keyValues2.get(i);
                        }
                    }
                }
                select_type_txt_view.setText(send_item);
                if(keyValues2.size()>0 )
                {
                    callWebFun();
                }
            }
        });
       /* check_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                aoiListAdapter = new AOIListAdapter(UserRegistration.this, areaOfIntrestResultLists, isChecked,ids);
                list_multipal_feeder.setAdapter(aoiListAdapter);
                if (isChecked) {
                    for (AreaOfIntrestResultList areaOfIntrestResultList : areaOfIntrestResultLists) {
                        addIds(areaOfIntrestResultList.getCategory_id());

                    }
                } else {
                    for (AreaOfIntrestResultList areaOfIntrestResultList : areaOfIntrestResultLists) {
                        removeIds(areaOfIntrestResultList.getCategory_id());

                    }
                }
            }
        });*/
        listdialog.show();

    }
    private void showListView()
    {
        aoiListAdapter.notifyDataSetChanged();
        listdialog.show();
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            progressBar.setVisibility(View.VISIBLE);
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url)
        {
           // progressBar.setVisibility(View.GONE);
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
       else if(arg1.getId()==R.id.section_spinner)
       {
           if(sectionlist.size()>0)
               selected_section=sectionlist.get(position).getId();
           else
           {
               selected_section="";
           }
       }
        date1.setText("");
        date2.setText("");
        //callWebFun();
/*       if(keyValues.size()>0)
       {
           postUrl="http://ebunchapps.com/rmsnew/api/tssprogress/"+selectedMonth+"/"+selectedKey;
       //    http://ebunchapps.com/rmsnew/api/tssprogress/02/Earth-filling-work
           webView.loadUrl(postUrl);

       }*/


    }

    private void callWebFun()
    {
        String baseUrl="weeklytssprogress";


        if(keyValues.size()>0)
        {
            if(from_date!=null) {

                postUrl = URL.url + baseUrl + "?hqId=" + response.getHeadquater() + "&" +
                        "type=" + send_item + "&group_id=" + selected_group + "&section_id=" + selected_section +
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

    public void getOHETarget()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(EntryUserTSSWebViewActivity.this,this, CallType.GET_TSS_TARGET,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getOHERequest();
    }

    public void getTssType()
    {

        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(EntryUserTSSWebViewActivity.this,this, CallType.GET_TSS,"",true);
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
                                Util.showLongToast(EntryUserTSSWebViewActivity.this, "No data available");

                            } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                                countryNames.clear();
                                keyValues.clear();
                                countryNames1.clear();
                                keyValues1.clear();
                                countryNames2.clear();
                                keyValues2.clear();
                                isChecked.clear();

                                JSONObject jsonObject1 = new JSONObject(jsonObject.getString("civil"));
                                JSONObject jsonObjectElctrical = new JSONObject(jsonObject.getString("electrical"));

                                Iterator<String> keys = jsonObject1.keys();
                                //  int i=0;
                                isChecked.add(false);
                                // isChecked2.add(false);
                                countryNames.add("all");
                                keyValues.add("all");
/*                                countryNames1.add("all");
                                keyValues1.add("all");*/

                                while (keys.hasNext()) {
                                    try {
                                        String value = keys.next();
                                        // Log.e("key : ",value);//,"pair : "+jsonObject1.getString(""+keys.next()));
                                        keyValues.add(value);
                                        countryNames.add(String.valueOf(jsonObject1.get(value)));
                                        isChecked.add(false);
                                        //isChecked1.add(false);
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
                                        isChecked.add(false);
                                        // Log.e("pair",String.valueOf(jsonObject1.get(value)));
                                        //i++;
                                    } catch (Exception ex) {
                                        ex.printStackTrace();
                                    }


                                }

                                if (countryNames.size() > 0) {
                                    // isChecked.clear();
                                    Log.e("civil", "civil1");
                                    keyValues2.addAll(keyValues);
                                    Log.e("civil", "civil1" + keyValues2.size());
                                    countryNames2.addAll(countryNames);
                                    // isChecked.addAll(isChecked1);
                                    r1.setChecked(true);
                                }
                                /*else if(countryNames1.size() > 0)
                                {
                                    r2.setChecked(true);
                                    keyValues2.addAll(keyValues1);
                                    countryNames2.addAll(countryNames1);
                                    isChecked.clear();
                                    isChecked.addAll(isChecked2);
                                }*/
                                int j = 0;
                                for (int i = countryNames.size() - 1; i < (countryNames.size() + countryNames1.size() - 1); i++) {
                                    keyValues2.add(keyValues1.get(j));
                                    countryNames2.add(countryNames1.get(j));
                                    //isChecked.add(false);
                                    j++;
                                }
                                if (countryNames.size() > 0) {
                                    showAreaListDailog();

                                    //customAdapter.notifyDataSetChanged();
                                }
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
    public void setProgressDialoug(boolean flag)
    {

        if(openDialog==null)
        {

            openDialog= new Dialog(EntryUserTSSWebViewActivity.this);
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
}
