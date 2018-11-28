package alina.com.rms.activities.userActivities;

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
import android.widget.RelativeLayout;
import android.widget.Spinner;
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
import alina.com.rms.adaptor.CustomAdapter;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.model.LoginResultPojo;
import alina.com.rms.util.CallType;
import alina.com.rms.util.LoginDB;
import alina.com.rms.util.Util;

public class SSPActivity extends BaseActivity implements AdapterView.OnItemSelectedListener,AsyncCompleteListner{
    List<String> countryNames=new ArrayList<String>();
    List<String> keyValues=new ArrayList<String>();
   //int flags[] = {R.drawable.round1, R.drawable.round3, R.drawable.round4, R.drawable.round1, R.drawable.round3, R.drawable.round4};
    CustomAdapter customAdapter;
    RelativeLayout date_layout;
    private TextView selected_date_text;
    private List<LoginResultPojo> loginResultPojo;
    private LoginResultPojo loginResultPojo1;
    private String login_response;
    private EditText targetEditText,achiveEditText;
    private Button submitBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        headingText.setText("SSP");

        //setContentView(R.layout.activity_ohe);
        login_response= LoginDB.getLoginResponseAsJSON(SSPActivity.this);
       // Log.e("value",login_response);
        Gson gson = new Gson();
        Type type = new TypeToken<List<LoginResultPojo>>() {}.getType();
     /*   String json = gson.toJson(login_response, type);
        System.out.println(json);*/
        loginResultPojo = gson.fromJson(login_response, type);
        loginResultPojo1=loginResultPojo.get(0);
        //loginResultPojo =
        addView();
        initUI();
    }

    @Override
    public void addView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View myView = factory.inflate(R.layout.activity_ohe, null);
        setView(myView);
    }

    private void initUI() {

        Spinner spin = (Spinner) findViewById(R.id.spinner);
        spin.setOnItemSelectedListener(this);
        selected_date_text=(TextView)findViewById(R.id.selected_date_text);
        date_layout=(RelativeLayout)findViewById(R.id.selected_date_relative);
        targetEditText=(EditText)findViewById(R.id.targetEditText);
        achiveEditText=(EditText)findViewById(R.id.achiveEditText);
        submitBtn=(Button)findViewById(R.id.submitBtn);

        date_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog(selected_date_text,SSPActivity.this);

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Util.chechTextValue(selected_date_text))
                {
                    Util.showShortToast(SSPActivity.this,"Select date");
                    return;
                }
                else if(!Util.chechEditTextValue(targetEditText))
                {
                    Util.showShortToast(SSPActivity.this,"No targeted value is avilable");
                    return;
                }
                else if(!Util.chechEditTextValue(achiveEditText))
                {
                    Util.showShortToast(SSPActivity.this,"Enter value in achive");
                    return;
                }
                else if(Integer.parseInt(achiveEditText.getText().toString())>Integer.parseInt(targetEditText.getText().toString()))
                {
                    Util.showShortToast(SSPActivity.this,"Achive value should be equal or less then Targeted value");
                    return;
                }
                loginResultPojo1.setTarget(achiveEditText.getText().toString());
                submitValue();
            }
        });

        customAdapter=new CustomAdapter(getApplicationContext(),countryNames);
        spin.setAdapter(customAdapter);
        getOHETarget();


    }

    //Performing action onItemSelected and onNothing selected
    @Override
    public void onItemSelected(AdapterView<?> arg0, View arg1, int position,long id) {
        //Toast.makeText(getApplicationContext(), countryNames.get(position), Toast.LENGTH_LONG).show();
        Log.e("Key Values",keyValues.get(position));
        loginResultPojo1.setOhetype(keyValues.get(position));
        selected_date_text.setText("");
        targetEditText.setText("");
        achiveEditText.setText("");

    }

    @Override
    public void onNothingSelected(AdapterView<?> arg0) {
        // TODO Auto-generated method stub
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
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpd.show();
    }


    public void getOHETarget()
    {

        AsyncController asyncController=new AsyncController(SSPActivity.this,this, CallType.GET_SP_TARGET,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getOHERequest();
    }

    public void getTargeValue()
    {

        AsyncController asyncController=new AsyncController(SSPActivity.this,this, CallType.GET_SSP_TARGET_VALUE,loginResultPojo1,true);
        asyncController.setProgressDialoug(true);
        asyncController.getTargetValueRequest();
    }

    public void submitValue()
    {

        AsyncController asyncController=new AsyncController(SSPActivity.this,this, CallType.SAVE_SSP_TARGET_VALUE,loginResultPojo1,true);
        asyncController.setProgressDialoug(true);
        asyncController.saveTargetValueRequest();
    }


    @Override
    public void asyncCompleteListner(String response, CallType callType) {
        if (response != null) {
            if (callType == CallType.GET_SP_TARGET) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            submitBtn.setClickable(false);
                            Util.showLongToast(SSPActivity.this, "No data available");

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
                            if (countryNames.size() > 0) {
                                submitBtn.setClickable(true);
                            }
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (callType == CallType.GET_SSP_TARGET_VALUE) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(SSPActivity.this, "No Target available");
                            selected_date_text.setText("");
                            targetEditText.setText("");
                            achiveEditText.setText("");

                        } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                            JSONArray jsonArray = new JSONArray(jsonObject.getString("response"));
                            JSONObject jsonObject1 = jsonArray.getJSONObject(0);
                            if(jsonObject1.getString("target").equalsIgnoreCase("null"))
                            {
                                targetEditText.setText("0");
                                achiveEditText.setText("");
                            }
                            else
                            {
                                targetEditText.setText(jsonObject1.getString("target"));
                                achiveEditText.setText(jsonObject1.getString("archievement"));
                            }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (callType == CallType.SAVE_SSP_TARGET_VALUE) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showShortToast(SSPActivity.this, "Data not saved successfully");
                        }
                        else {
                            Util.showShortToast(SSPActivity.this, "Data saved successfully");
                            targetEditText.setText("");
                            achiveEditText.setText("");
                            selected_date_text.setText("");

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                Util.showShortToast(SSPActivity.this, "Error in connection");
            }
        }
    }


}
