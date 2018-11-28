package alina.com.rms.activities.headquaterActivities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import alina.com.rms.R;
import alina.com.rms.adaptor.CustomAdapter;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.model.HeadQuaterGroupResponse;
import alina.com.rms.model.Response;
import alina.com.rms.model.Targetvalue;
import alina.com.rms.util.CallType;
import alina.com.rms.util.LoginDB;
import alina.com.rms.util.Util;

public class OHETargetActivity extends HeaduaterBaseActivity implements AdapterView.OnItemSelectedListener,AsyncCompleteListner {
    List<String> countryNames=new ArrayList<String>();
    List<String> pairVlues=new ArrayList<String>();
    List<String> keyValues=new ArrayList<String>();
    //int flags[] = {R.drawable.round1, R.drawable.round3, R.drawable.round4, R.drawable.round1, R.drawable.round3, R.drawable.round4};
    CustomAdapter monthAdapter,yearAdapter;
    RelativeLayout date_layout;
    private TextView /*selected_date_text,*/selected_date_text_eig,selected_date_text_crs;

    private Button submitBtn;
    private Response response;
    private List<Response> response1;
    private String selected_month,selected_year,selected_date_text_eig1="",selected_date_text_crs1="";
    private Button btn_show;
    private LinearLayout linear_show_hide;
    List<String> monthNames=new ArrayList<String>();
    List<String> yearNames=new ArrayList<String>();
    private Spinner month_spinner,year_spinner;
    int[] months = {0,1,2,3,4,5,6,7,8,9,10,11};
    int current_year_position,current_month_position,current_position;
    private LinearLayout linear_add;
    private EditText[] linear_edit_box;
    private EditText[] linear_text_view;
    private String selected_group,selected_section;
    private List<String>editTextValues=new ArrayList<>();
    private boolean flagFirstTime=false;
    private Targetvalue targetvalue;
    private String selected_station = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_ohetarget);
        Calendar c = Calendar.getInstance();
        current_year_position = c.get(Calendar.YEAR);
        current_month_position = c.get(Calendar.MONTH);

        current_position=current_year_position-1999;
        for(int i=2000;i<=(current_year_position+20);i++)
        {
            yearNames.add(""+i);
        }

        for (int i = 0; i < months.length; i++) {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat month_date = new SimpleDateFormat("MMMM");
            cal.set(Calendar.MONTH, months[i]);
            String month_name = month_date.format(cal.getTime());
            monthNames.add(month_name);
        }
        Gson gson=new Gson();
        Type type = new TypeToken<List<Response>>() {}.getType();
        response1=gson.fromJson(LoginDB.getLoginResponseAsJSON(OHETargetActivity.this),type);
        response=response1.get(0);
            Bundle bundle=getIntent().getExtras();
            if(bundle!=null)
            {
                if(bundle.containsKey("selected_group"))
                {
                    selected_group = bundle.getString("selected_group");
                }

                  if(bundle.containsKey("selected_section")){
                    selected_section = bundle.getString("selected_section");
                  }
                  if(bundle.containsKey("selected_station"))
                {
                    selected_station = bundle.getString("selected_station");
                }

            }
        addView();
        initUI();
       // hitToServer();
        getOHETarget();
    }

    @Override
    public void addView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View myView = factory.inflate(R.layout.activity_ohetarget, null);
        setView(myView);
    }
    private void initUI() {
        headingText.setText("Set OHE Target");

        month_spinner = (Spinner) findViewById(R.id.spinner_month);
        year_spinner  = (Spinner) findViewById(R.id.spinner_year);
        //spin_item.setOnItemSelectedListener(this);
        month_spinner.setOnItemSelectedListener(this);
        year_spinner.setOnItemSelectedListener(this);

      //  selected_date_text=(TextView)findViewById(R.id.selected_date_text);
        linear_add=(LinearLayout)findViewById(R.id.linear_add);
        selected_date_text_eig=(TextView)findViewById(R.id.selected_date_text1);
        selected_date_text_crs=(TextView)findViewById(R.id.selected_date_text2);
        date_layout=(RelativeLayout)findViewById(R.id.selected_date_relative);
        submitBtn=(Button)findViewById(R.id.submitBtn);
        btn_show=(Button)findViewById(R.id.btn_show);
        linear_show_hide=(LinearLayout)findViewById(R.id.linear_hide_show);
        linear_show_hide.setVisibility(View.GONE);
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(linear_show_hide.getVisibility()==View.VISIBLE)
                {
                    btn_show.setText("Click here to show EIG & CRS Papers");
                    linear_show_hide.setVisibility(View.GONE);
                }
                else {

                    btn_show.setText("Click here to hide EIG & CRS Papers");
                    linear_show_hide.setVisibility(View.VISIBLE);
                }
            }
        });
/*        date_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog(selected_date_text,OHETargetActivity.this,1);

            }
        });*/
        selected_date_text_eig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog(selected_date_text_eig,OHETargetActivity.this,2);

            }
        });
        selected_date_text_crs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog(selected_date_text_crs,OHETargetActivity.this,3);

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setHeadQuater();
            }
        });


        monthAdapter=new CustomAdapter(OHETargetActivity.this,monthNames);
        month_spinner.setAdapter(monthAdapter);
        yearAdapter=new CustomAdapter(OHETargetActivity.this,yearNames);
        year_spinner.setAdapter(yearAdapter);
    }

    @Override
    public void onResume()
    {
        super.onResume();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


/*        if(parent.getId()==R.id.spinner_month)
        {
            selected_month=""+position;
            if(flagFirstTime)
            {
                getTargetValue();
            }
            Log.e("current month",monthNames.get(position));

        }
        else if(parent.getId()==R.id.spinner_year)
        {
            selected_year=yearNames.get(position);
            if(flagFirstTime) {
                getTargetValue();
            }



           // getTargetValue();
            Log.e("current Year",yearNames.get(position));
        }*/
        /*else {
            selecte_target=headquaterlist.get(position).getId();
        }*/

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

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
                            //selected_date_text1=date1;
                          //  getTargetValue();
                        }
                        else if(position==2)
                        {
                            selected_date_text_eig1=date1;
                        }
                       else if(position==3)
                        {
                            selected_date_text_crs1=date1;
                        }
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
        AsyncController asyncController=new AsyncController(this,this, CallType.GET_OHE,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getOHERequest();
    }

    public void getTargetValue()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(this,this, CallType.GET_TARGET_VALUE_HEADQUATER,"",true);
        asyncController.setProgressDialoug(true);

        /*if((Integer.parseInt(selected_month)+1)<10)
        {
            String monthName1="0"+((Integer.parseInt(selected_month))+1);
            asyncController.getValueForHeadquater(response.getHeadquater(),selected_group,selected_section,
                    monthName1,selected_year);
            // ,selected_date_text_eig1,selected_date_text_crs1,targetEditText1
        }
        else {
            String monthName1=""+((Integer.parseInt(selected_month))+1);
            asyncController.getValueForHeadquater(response.getHeadquater(),selected_group,selected_section,
                    monthName1,selected_year);
        }*/
        if((current_month_position+1)<10)
        {
            String monthName1="0"+(current_month_position+1);
            asyncController.getValueForHeadquater(response.getHeadquater(),selected_group,selected_section,
                    monthName1,""+current_year_position);
            // ,selected_date_text_eig1,selected_date_text_crs1,targetEditText1
        }
        else {
            String monthName1=""+(current_month_position+1);
            asyncController.getValueForHeadquater(response.getHeadquater(),selected_group,selected_section,
                    monthName1,""+current_year_position);
        }

/*        final String headquater_id,final String group_id,final String section_id
            ,final String month
            ,final String year*/
    }


    public void setHeadQuater()
    {

        if(keyValues.size()==0)
        {
            Util.showLongToast(this,"No items are available");
            return;
        }

        editTextValues.clear();
        for (int i=0;i<linear_edit_box.length;i++)
        {
            String checkString=linear_edit_box[i].getText().toString();
            if(checkString==null)
            {
                editTextValues.add("");
            }
            else if(checkString.trim().length()<1)
            {
                editTextValues.add("");
            }
            else {
                editTextValues.add(checkString);
            }
        }

        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(this,this, CallType.SET_TARGET_VALUE,"",true);
        asyncController.setProgressDialoug(true);
       // headquater_id,group_id,section_id,month,year,eigdate,crsdate
          /*if((Integer.parseInt(selected_month)+1)<10)
          {
                String monthName1="0"+((Integer.parseInt(selected_month))+1);
              asyncController.setTargetValueForHeadquater(response.getHeadquater(),selected_group,selected_section,
                      monthName1,selected_year,selected_date_text_eig1,selected_date_text_crs1,keyValues,editTextValues);
                     // ,selected_date_text_eig1,selected_date_text_crs1,targetEditText1
          }
          else {
              String monthName1=""+((Integer.parseInt(selected_month))+1);
              asyncController.setTargetValueForHeadquater(response.getHeadquater(),selected_group,selected_section,
                      monthName1,selected_year,selected_date_text_eig1,selected_date_text_crs1,keyValues,editTextValues);
          }*/
        if((current_month_position+1)<10)
        {
            String monthName1="0"+(current_month_position+1);
            asyncController.setTargetValueForHeadquater(response.getHeadquater(),selected_group,selected_section,
                    monthName1,""+current_year_position,selected_date_text_eig1,selected_date_text_crs1,keyValues,editTextValues);
            // ,selected_date_text_eig1,selected_date_text_crs1,targetEditText1
        }
        else {
            String monthName1=""+(current_month_position+1);
            asyncController.setTargetValueForHeadquater(response.getHeadquater(),selected_group,selected_section,
                    monthName1,""+current_year_position,selected_date_text_eig1,selected_date_text_crs1,keyValues,editTextValues);
        }

    }

    private void setValueInLinearLayout()
    {
        linear_edit_box=new EditText[keyValues.size()];
        linear_text_view=new EditText[keyValues.size()];
        for (int i=0;i<keyValues.size();i++)
        {
            View layoutInflater=getLayoutInflater().inflate(R.layout.item_list,null);
            linear_edit_box[i]=(EditText)layoutInflater.findViewById(R.id.edit_data);
            linear_text_view[i]=(EditText) layoutInflater.findViewById(R.id.txt_data);
            linear_text_view[i].setText(pairVlues.get(i));
            linear_add.addView(layoutInflater);
        }
    }

    private void setValueInEditBox()
    {
        Gson gson=new Gson();
        String jsonStr=gson.toJson(targetvalue);
       // Log.e("Json Value",jsonStr);
        try {
            JSONObject jsonObject=new JSONObject(jsonStr);
            for (int i=0;i<keyValues.size();i++)
            {
              if(jsonObject.getString(keyValues.get(i))!=null)
              {
                  linear_edit_box[i].setText(jsonObject.getString(keyValues.get(i)));
              }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setBlankValueInEditBox()
    {
        // Log.e("Json Value",jsonStr);
        for (int i=0;i<keyValues.size();i++)
        {

            linear_edit_box[i].setText("");

        }
    }

    @Override
    public void asyncCompleteListner(String response, CallType callType) {
        setProgressDialoug(false);
        if(response!=null) {
            if (callType == CallType.GET_OHE ) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(OHETargetActivity.this, "No data available");

                        } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                            pairVlues.clear();
                            keyValues.clear();
                            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("response"));

                            Iterator<String> keys = jsonObject1.keys();
                            //  int i=0;
                            while (keys.hasNext()) {
                                try {
                                    String value = keys.next();
                                    // Log.e("key : ",value);//,"pair : "+jsonObject1.getString(""+keys.next()));
                                    keyValues.add(value);
                                    pairVlues.add(String.valueOf(jsonObject1.get(value)));
                                    // Log.e("pair",String.valueOf(jsonObject1.get(value)));
                                    //i++;

                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }


                            }
                            if(keyValues.size()>0)
                            {
                                setValueInLinearLayout();
                                month_spinner.setSelection(current_month_position);
                                flagFirstTime=true;
                                year_spinner.setSelection(current_position-1);
                                getTargetValue();
                            }



                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else if(CallType.GET_TARGET_VALUE_HEADQUATER==callType)
            {
                Gson gson = new Gson();
                HeadQuaterGroupResponse response1 = gson.fromJson(response, HeadQuaterGroupResponse.class);
                if(response1.getResponseCode()==1)
                {

                    if(response1.getTarget().get(0).getTargetvalue()!=null)
                    {
                        targetvalue = response1.getTarget().get(0).getTargetvalue();
                        setValueInEditBox();
                    }
                    else {
                        setBlankValueInEditBox();
                    }

                    if(response1.getTarget().get(0).getEigdate()!=null)
                    {
                        if(response1.getTarget().get(0).getEigdate().trim().length()>0)
                        {
                            if(("0000-00-00").equalsIgnoreCase(response1.getTarget().get(0).getEigdate()))
                            {
                                selected_date_text_eig1="";
                            }
                            else {
                                selected_date_text_eig1=response1.getTarget().get(0).getEigdate();
                            }
                            selected_date_text_eig.setText(selected_date_text_eig1);
                        }
                    }
                    if(response1.getTarget().get(0).getCrsdate()!=null)
                    {
                        if(response1.getTarget().get(0).getCrsdate().trim().length()>0)
                        {
                            if(("0000-00-00").equalsIgnoreCase(response1.getTarget().get(0).getCrsdate()))
                            {
                                selected_date_text_crs1="";
                            }
                            else {
                                selected_date_text_crs1=response1.getTarget().get(0).getCrsdate();
                            }
                            selected_date_text_crs.setText(selected_date_text_crs1);
                        }
                    }
                }


            }
            else if(CallType.SET_TARGET_VALUE==callType) {
                Gson gson = new Gson();
                HeadQuaterGroupResponse response1 = gson.fromJson(response, HeadQuaterGroupResponse.class);
                if (response1.getResponseCode() == 1) {
                    //showDialog("Your data saved target successfully");
                    showDialog1(response1.getMessage());
              // finish();
                }
                else {
                    showDialog(response1.getMessage());
                }
            }
        }
    }
}
