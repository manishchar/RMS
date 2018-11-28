package alina.com.rms.activities.headquaterActivities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.model.LoginResultPojo;
import alina.com.rms.util.CallType;
import alina.com.rms.util.LoginDB;
import alina.com.rms.util.Util;

public class ProcessHeadquaterActivity extends HeaduaterBaseActivity implements AsyncCompleteListner{
    List<String> pairValues=new ArrayList<String>();
    List<String> keyValues=new ArrayList<String>();
   //int flags[] = {R.drawable.round1, R.drawable.round3, R.drawable.round4, R.drawable.round1, R.drawable.round3, R.drawable.round4};

    RelativeLayout date_layout;
    private TextView selected_date_text;
    private List<LoginResultPojo> loginResultPojo;
    private LoginResultPojo loginResultPojo1;
    private String login_response;
    private Button btn_submit;
    private String selected_date="";
    private String selected_group,selected_section,selected_ohe,selected_achive;
    private EditText targetEditText,achiveEditText;
    private LinearLayout linear_add;
    private EditText[] linear_edit_box;
    private EditText[] linear_text_view;
    private EditText[] linear_text_view_target;
    private List<String>editTextValues=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        login_response= LoginDB.getLoginResponseAsJSON(ProcessHeadquaterActivity.this);
       // Log.e("value",login_response);
        Gson gson = new Gson();
        Type type = new TypeToken<List<LoginResultPojo>>() {}.getType();
     /*   String json = gson.toJson(login_response, type);
        System.out.println(json);*/
        loginResultPojo = gson.fromJson(login_response, type);
        loginResultPojo1=loginResultPojo.get(0);
        //loginResultPojo =

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
        addView();
        initUI();
    }

    @Override
    public void addView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View myView = factory.inflate(R.layout.activity_ohe_headquater, null);
        setView(myView);
    }

    private void initUI() {
        headingText.setText("Process OHE");
        selected_date_text=(TextView)findViewById(R.id.selected_date_text);
        date_layout=(RelativeLayout)findViewById(R.id.selected_date_relative);
        btn_submit=(Button)findViewById(R.id.submitBtn);
        targetEditText=(EditText)findViewById(R.id.targetEditText);
        achiveEditText=(EditText)findViewById(R.id.achiveEditText);
        linear_add=(LinearLayout)findViewById(R.id.linear_add);
        linear_add.setVisibility(View.GONE);
        btn_submit.setVisibility(View.GONE);
        date_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog(selected_date_text,ProcessHeadquaterActivity.this);

            }
        });

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Util.chechTextValue(selected_date_text))
                {
                    Util.showShortToast(ProcessHeadquaterActivity.this,"Select date");
                    return;
                }

                /*else if(Integer.parseInt(achiveEditText.getText().toString())>Integer.parseInt(targetEditText.getText().toString()))
                {
                    Util.showShortToast(ProcessHeadquaterActivity.this,"Achive value should be equal or less then Targeted value");
                    return;
                }*/
                submitValue();
               // submitValue();
            }
        });




        getOHETarget();

    }

    //Performing action onItemSelected and onNothing selected





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
                                selected_date=selected_date_text.getText().toString();
                                //loginResultPojo1.setDate(selected_date_text.getText().toString());
                                getTargeValue();
                            }

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
        AsyncController asyncController=new AsyncController(ProcessHeadquaterActivity.this,this, CallType.GET_OHE,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getOHERequest();
    }
    public void getTargeValue()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(ProcessHeadquaterActivity.this,this, CallType.GET_TARGET_VALUE,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getTargetProcessValueRequest(loginResultPojo1.getHeadquater(),selected_group,selected_section,selected_date);
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
        AsyncController asyncController=new AsyncController(ProcessHeadquaterActivity.this,this, CallType.SUBMITTED_VALUE,loginResultPojo1,true);
        asyncController.setProgressDialoug(true);
        asyncController.saveTargetProcessValueRequest(loginResultPojo1.getHeadquater(),
                selected_group,selected_section,selected_date,loginResultPojo1.getUserId(),keyValues,editTextValues,"");

        /*//final String headquater,final String group,
        final String section_id,final String ohetype,
        final String date,final String user_id*/
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
            linear_text_view[i].setText(pairValues.get(i));
            linear_add.addView(layoutInflater);
        }
    }

 /*   private void setValueInEditBox()
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
    }*/

    @Override
    public void asyncCompleteListner(String response, CallType callType) {
        setProgressDialoug(false);
        if (response != null) {
            if (callType == CallType.GET_OHE) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(ProcessHeadquaterActivity.this, "No data available");

                        } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                            pairValues.clear();
                            keyValues.clear();
                            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("response"));

                            Iterator<String> keys = jsonObject1.keys();
                            //  int i=0;
                            while (keys.hasNext()) {
                                try {
                                    String value = keys.next();
                                    // Log.e("key : ",value);//,"pair : "+jsonObject1.getString(""+keys.next()));
                                    keyValues.add(value);
                                    pairValues.add(String.valueOf(jsonObject1.get(value)));
                                    // Log.e("pair",String.valueOf(jsonObject1.get(value)));
                                    //i++;
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }


                            }
                            if (pairValues.size() > 0) {
                                setValueInLinearLayout();
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (callType == CallType.GET_TARGET_VALUE) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(ProcessHeadquaterActivity.this, "No Target available");
                            selected_date_text.setText("");
                            targetEditText.setText("0");
                            achiveEditText.setText("");


                        } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                            JSONArray jsonArray = new JSONArray(jsonObject.getString("response"));
                            linear_add.setVisibility(View.VISIBLE);
                            btn_submit.setVisibility(View.VISIBLE);

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
            } else if (callType == CallType.SUBMITTED_VALUE) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            // Util.showShortToast(ProcessHeadquaterEntryUserActivity.this, "Data not saved successfully");
                            selected_date_text.setText("");
                            Util.showDialog(ProcessHeadquaterActivity.this,"Your data not saved successfully");
                        }
                        else {
                            selected_date_text.setText("");
                            linear_add.setVisibility(View.GONE);
                            btn_submit.setVisibility(View.GONE);
                            // Util.showShortToast(ProcessHeadquaterEntryUserActivity.this, "Data saved successfully");
                            Util.showDialog(ProcessHeadquaterActivity.this,"Your data saved successfully");
                            //finish();
                           // selected_date_text.setText("");

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {

                Util.showShortToast(ProcessHeadquaterActivity.this, "Error in connection");
            }
        }
    }


}
