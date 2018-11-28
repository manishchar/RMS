package alina.com.rms.activities.headquaterActivities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
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
import alina.com.rms.model.LoginResultPojo;
import alina.com.rms.model.Result;
import alina.com.rms.model.Sectionlist;
import alina.com.rms.util.CallType;
import alina.com.rms.util.LoginDB;
import alina.com.rms.util.URL;
import alina.com.rms.util.Util;

public class ProcessSPActivity extends HeaduaterBaseActivity implements AsyncCompleteListner,AdapterView.OnItemSelectedListener{
    List<String> keyValues=new ArrayList<String>();
    List<String> pairVlues=new ArrayList<String>();
   //int flags[] = {R.drawable.round1, R.drawable.round3, R.drawable.round4, R.drawable.round1, R.drawable.round3, R.drawable.round4};

    RelativeLayout date_layout;
    private TextView selected_date_text;
    private List<LoginResultPojo> loginResultPojo;
    private LoginResultPojo loginResultPojo1;
    private String login_response;
    private EditText targetEditText,achiveEditText;
    private Button submitBtn;
    private String selected_group,selected_section,selected_ohe,selected_achive;
    private EditText[] linear_edit_box;
    private EditText[] linear_text_view;
    private EditText[] linear_text_view_target;
    private LinearLayout linear_add;
    private List<String>editTextValues=new ArrayList<>();

    List<String> typeList=new ArrayList<String>();
    private Spinner type_spinner;
    CustomAdapter typeAdapter;
    private String selected_type_id;
    private List<Sectionlist>sectionlists=new ArrayList<Sectionlist>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        headingText.setText("Process SP");
        //setContentView(R.layout.activity_ohe);
        login_response= LoginDB.getLoginResponseAsJSON(ProcessSPActivity.this);
       // Log.e("value",login_response);
        Gson gson = new Gson();
        Type type = new TypeToken<List<LoginResultPojo>>() {}.getType();
     /*   String json = gson.toJson(login_response, type);
        System.out.println(json);*/
        loginResultPojo = gson.fromJson(login_response, type);
        loginResultPojo1=loginResultPojo.get(0);
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
        }
        loginResultPojo1.setSection_id(selected_section);
        loginResultPojo1.setGroup(selected_group);
        //loginResultPojo =
        addView();
        initUI();
    }

    @Override
    public void addView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View myView = factory.inflate(R.layout.activity_ohe1, null);
        setView(myView);
    }

    private void initUI() {

        selected_date_text=(TextView)findViewById(R.id.selected_date_text);
        date_layout=(RelativeLayout)findViewById(R.id.selected_date_relative);
        targetEditText=(EditText)findViewById(R.id.targetEditText);
        achiveEditText=(EditText)findViewById(R.id.achiveEditText);
        submitBtn=(Button)findViewById(R.id.submitBtn);
        linear_add=(LinearLayout)findViewById(R.id.linear_add);
        linear_add.setVisibility(View.GONE);
        submitBtn.setVisibility(View.GONE);

        type_spinner=(Spinner)findViewById(R.id.type_spinner);
        type_spinner.setOnItemSelectedListener(this);

        date_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog(selected_date_text,ProcessSPActivity.this);

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Util.chechTextValue(selected_date_text))
                {
                    Util.showShortToast(ProcessSPActivity.this,"Select date");
                    return;
                }
          /*      else if(!Util.chechEditTextValue(targetEditText))
                {
                    Util.showShortToast(ProcessSPActivity.this,"No targeted value is avilable");
                    return;
                }
                else if(!Util.chechEditTextValue(achiveEditText))
                {
                    Util.showShortToast(ProcessSPActivity.this,"Enter value in achive");
                    return;
                }
                else if(Integer.parseInt(achiveEditText.getText().toString())>Integer.parseInt(targetEditText.getText().toString()))
                {
                    Util.showShortToast(ProcessSPActivity.this,"Achive value should be equal or less then Targeted value");
                    return;
                }*/
              //  loginResultPojo1.setTarget(achiveEditText.getText().toString());
                submitValue();
            }
        });

        typeAdapter=new CustomAdapter(ProcessSPActivity.this,typeList);
        type_spinner.setAdapter(typeAdapter);
        getOHETarget();


    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId()==R.id.type_spinner)
        {
            selected_type_id=sectionlists.get(position).getId();
            loginResultPojo1.setMaster_id(selected_type_id);
            if(selected_date_text.getText().toString()!=null)
            {
                if(selected_date_text.getText().toString().trim().length()>1)
                {
                    loginResultPojo1.setDate(selected_date_text.getText().toString());
                    getTargeValue();
                }

            }

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void openDatePickerDialog(final TextView editText, Context context) {
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

                        editText.setText(year + "-" + month+ "-"
                                + date);
                        String sdate=editText.getText().toString();
                        boolean b=true;
                        if(selected_date_text.getText().toString()!=null)
                        {
                            if(selected_date_text.getText().toString().trim().length()>1)
                            {
                                loginResultPojo1.setDate(selected_date_text.getText().toString());
                                getTargeValue();
                            }

                        }
                        // context.addDate_tolist(position,sdate,b);

                    }
                }, mYear, mMonth, mDay);
        dpd.setTitle("Select Date");
      //  dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpd.show();
    }


    private void setValueInLinearLayout()
    {
        linear_edit_box=new EditText[keyValues.size()];
        linear_text_view=new EditText[keyValues.size()];
        linear_text_view_target=new EditText[keyValues.size()];
        for (int i=0;i<keyValues.size();i++)
        {
            View layoutInflater=getLayoutInflater().inflate(R.layout.process_item_list,null);
            linear_edit_box[i]=(EditText)layoutInflater.findViewById(R.id.edit_data);
            linear_text_view[i]=(EditText) layoutInflater.findViewById(R.id.txt_data);
            linear_text_view_target[i]=(EditText) layoutInflater.findViewById(R.id.txt_target);
            linear_text_view[i].setText(pairVlues.get(i));
            linear_add.addView(layoutInflater);
        }


    }


    public void getSP()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(this,this, CallType.GET_SP,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getHeadquaterSP_SSP_TSSList(selected_section, URL.get_sp);
    }
    public void getOHETarget()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(ProcessSPActivity.this,this, CallType.GET_SP_TARGET,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getOHERequest();
    }

    public void getTargeValue()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(ProcessSPActivity.this,this, CallType.GET_SP_TARGET_VALUE,loginResultPojo1,true);
        asyncController.setProgressDialoug(true);
        asyncController.getProcessTargetValueRequest();
    }

    public void submitValue()
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
        AsyncController asyncController=new AsyncController(ProcessSPActivity.this,this, CallType.SAVE_SP_TARGET_VALUE,loginResultPojo1,true);
        asyncController.setProgressDialoug(true);
       // asyncController.saveTargetValueRequest();
        asyncController.saveTargetProcessValueRequest(loginResultPojo1.getHeadquater(),
                selected_group,selected_section,loginResultPojo1.getDate(),loginResultPojo1.getUserId(),keyValues,editTextValues,selected_type_id);
    }


    @Override
    public void asyncCompleteListner(String response, CallType callType) {
        setProgressDialoug(false);
        if (response != null) {
            if(callType==CallType.GET_SP)
            {

                Gson gson=new Gson();
                Result response1=gson.fromJson(response,Result.class);
                if(response1!=null)
                {
                    if(response1.getSectionlist()!=null)
                    {
                        Log.e("String","value");
                        sectionlists.clear();
                        sectionlists.addAll(response1.getSectionlist());
                        typeList.clear();
                        for (int i=0;i<sectionlists.size();i++)
                        {
                            typeList.add(sectionlists.get(i).getSp_name());
                        }
                        //  Log.e("value",""+headquaterlist.size());
                        typeAdapter.notifyDataSetChanged();
                        //getTSSTarget();
                    }
                    else {
                        Toast.makeText(this,"no data found",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(this,"no data found",Toast.LENGTH_LONG).show();
                }

            }
           else if (callType == CallType.GET_SP_TARGET) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            submitBtn.setClickable(false);
                            Util.showLongToast(ProcessSPActivity.this, "No data available");

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
                            if (pairVlues.size() > 0) {
                                submitBtn.setClickable(true);
                                setValueInLinearLayout();
                                getSP();
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (callType == CallType.GET_SP_TARGET_VALUE) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(ProcessSPActivity.this, "No Target available");
                            selected_date_text.setText("");
                            targetEditText.setText("");
                            achiveEditText.setText("");

                        }
                            else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                            linear_add.setVisibility(View.VISIBLE);
                            submitBtn.setVisibility(View.VISIBLE);
                                JSONArray jsonArray = new JSONArray(jsonObject.getString("response"));
                                for (int i=0;i<keyValues.size();i++)
                                {
                                    JSONObject jsonObject1=jsonArray.getJSONObject(i);
                                    if(jsonObject1.has(keyValues.get(i))) {
                                        JSONArray jsonElements=new JSONArray(jsonObject1.getString(keyValues.get(i)));
                                        {
                                            linear_text_view_target[i].setText(jsonElements.getString(0));
                                            linear_edit_box [i].setText(jsonElements.getString(1));
                                        }

                                    }
                                }
                            }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (callType == CallType.SAVE_SP_TARGET_VALUE) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            selected_date_text.setText("");
                            showDialog("Your Data not saved successfully");
                        }
                        else {
                            showDialog( "Your Data saved successfully");
                            selected_date_text.setText("");
                            linear_add.setVisibility(View.GONE);
                            submitBtn.setVisibility(View.GONE);


                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                selected_date_text.setText("");
                Util.showShortToast(ProcessSPActivity.this, "Error in connection");
            }
        }
    }


}
