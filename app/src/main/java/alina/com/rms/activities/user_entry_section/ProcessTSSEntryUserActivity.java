package alina.com.rms.activities.user_entry_section;

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
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

public class ProcessTSSEntryUserActivity extends UserEntryBaseActivity implements AsyncCompleteListner,AdapterView.OnItemSelectedListener{
    List<String> pairVluesCivil=new ArrayList<String>();
    List<String> keyValuesCivil=new ArrayList<String>();
    List<String> pairVluesElectrical=new ArrayList<String>();
    List<String> keyValuesElectrical=new ArrayList<String>();
    Spinner spin;
   //int flags[] = {R.drawable.round1, R.drawable.round3, R.drawable.round4, R.drawable.round1, R.drawable.round3, R.drawable.round4};

    RelativeLayout date_layout;
    private TextView selected_date_text;
    private List<LoginResultPojo> loginResultPojo;
    private LoginResultPojo loginResultPojo1;
    private String login_response;
    private EditText targetEditText,achiveEditText;
    private Button submitBtn;
    RadioGroup radioBtnGroup;
    RadioButton r1,r2;

    private EditText[] linear_edit_box_civil,linear_edit_box_electrical;
    private EditText[] linear_text_view_civil,linear_text_view_electrical;
    private String selected_group,selected_section;
    private EditText[] linear_text_view_civil_target,linear_text_view_electrical_target;
    private List<String>editTextValuesCivil=new ArrayList<>();
    private List<String>editTextValuesElectrical=new ArrayList<>();
    private LinearLayout linear_add_civil,linear_add_electrical;

    List<String> typeList=new ArrayList<String>();
    private Spinner type_spinner;
    CustomAdapter typeAdapter;
    private String selected_type_id;
    private List<Sectionlist>sectionlists=new ArrayList<Sectionlist>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        headingText.setText("Process TSS");

        //setContentView(R.layout.activity_ohe);
        login_response= LoginDB.getLoginResponseAsJSON(ProcessTSSEntryUserActivity.this);
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
        View myView = factory.inflate(R.layout.activity_ohe_tss, null);
        setView(myView);
    }

    private void initUI() {


        selected_date_text=(TextView)findViewById(R.id.selected_date_text);
        date_layout=(RelativeLayout)findViewById(R.id.selected_date_relative);
        targetEditText=(EditText)findViewById(R.id.targetEditText);
        achiveEditText=(EditText)findViewById(R.id.achiveEditText);
        submitBtn=(Button)findViewById(R.id.submitBtn);
        linear_add_civil=(LinearLayout)findViewById(R.id.linear_add_civil);
        linear_add_electrical=(LinearLayout)findViewById(R.id.linear_add_electrical);
        linear_add_civil.setVisibility(View.GONE);
        linear_add_electrical.setVisibility(View.GONE);
        submitBtn.setVisibility(View.GONE);
        type_spinner=(Spinner)findViewById(R.id.type_spinner);
        type_spinner.setOnItemSelectedListener(this);
        getOHETarget();
        r1=(RadioButton)findViewById(R.id.radioBtnCivil);
        r2=(RadioButton)findViewById(R.id.radioBtnElectrical);

        date_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDatePickerDialog(selected_date_text,ProcessTSSEntryUserActivity.this);

            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!Util.chechTextValue(selected_date_text))
                {
                    Util.showShortToast(ProcessTSSEntryUserActivity.this,"Select date");
                    return;
                }

                //loginResultPojo1.setTarget(achiveEditText.getText().toString());
                submitValue();
            }
        });

        typeAdapter=new CustomAdapter(ProcessTSSEntryUserActivity.this,typeList);
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
       // dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpd.show();
    }


    private void setValueInLinearLayout()
    {
        linear_edit_box_civil=new EditText[keyValuesCivil.size()];
        linear_text_view_civil=new EditText[keyValuesCivil.size()];
        linear_text_view_civil_target=new EditText[keyValuesCivil.size()];

        linear_edit_box_electrical=new EditText[keyValuesElectrical.size()];
        linear_text_view_electrical=new EditText[keyValuesElectrical.size()];
        linear_text_view_electrical_target=new EditText[keyValuesElectrical.size()];
        for (int i=0;i<keyValuesCivil.size();i++)
        {
            View layoutInflater=getLayoutInflater().inflate(R.layout.process_item_list,null);
            linear_edit_box_civil[i]=(EditText)layoutInflater.findViewById(R.id.edit_data);
            linear_text_view_civil[i]=(EditText) layoutInflater.findViewById(R.id.txt_data);
            linear_text_view_civil_target[i]=(EditText)layoutInflater.findViewById(R.id.txt_target);
            linear_text_view_civil[i].setText(pairVluesCivil.get(i));
            linear_add_civil.addView(layoutInflater);
        }

        for (int i=0;i<keyValuesElectrical.size();i++)
        {
            View layoutInflater=getLayoutInflater().inflate(R.layout.process_item_list,null);
            linear_edit_box_electrical[i]=(EditText)layoutInflater.findViewById(R.id.edit_data);
            linear_text_view_electrical[i]=(EditText) layoutInflater.findViewById(R.id.txt_data);
            linear_text_view_electrical_target[i]=(EditText)layoutInflater.findViewById(R.id.txt_target);
            linear_text_view_electrical[i].setText(pairVluesElectrical.get(i));
            linear_add_electrical.addView(layoutInflater);
        }
    }

    public void getTSS()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(this,this, CallType.GET_TSS,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getHeadquaterSP_SSP_TSSList(selected_section, URL.get_tss);
    }
    public void getOHETarget()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(ProcessTSSEntryUserActivity.this,this, CallType.GET_TSS_TARGET,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getOHERequest();
    }

    public void getTargeValue()
    {
        if(selected_type_id.isEmpty())
        {
            Util.showLongToast(ProcessTSSEntryUserActivity.this,"No TSS type found");
            return;
        }
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(ProcessTSSEntryUserActivity.this,this, CallType.GET_TSS_TARGET_VALUE,loginResultPojo1,true);
        asyncController.setProgressDialoug(true);
        asyncController.getProcessTargetValueRequest();
    }

    public void submitValue()
    {
        if(keyValuesCivil.size()==0 || keyValuesElectrical.size()==0)
        {
            Util.showLongToast(this,"No items are available");
            return;
        }

        editTextValuesCivil.clear();
        editTextValuesElectrical.clear();
        for (int i=0;i<linear_edit_box_civil.length;i++)
        {
            String checkString=linear_edit_box_civil[i].getText().toString();
            if(checkString==null)
            {
                editTextValuesCivil.add("0");
            }
            else if(checkString.trim().length()<1)
            {
                editTextValuesCivil.add("0");
            }
            else {
                editTextValuesCivil.add(checkString);
            }
        }

        for (int i=0;i<linear_edit_box_electrical.length;i++)
        {
            String checkString=linear_edit_box_electrical[i].getText().toString();

            if(checkString==null)
            {
                editTextValuesElectrical.add("0");
            }
            else if(checkString.trim().length()<1)
            {
                editTextValuesElectrical.add("0");
            }
            else {
                Log.e("value",checkString);
                editTextValuesElectrical.add(checkString);
            }
        }

        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(ProcessTSSEntryUserActivity.this,this, CallType.SAVE_TSS_TARGET_VALUE,loginResultPojo1,true);
        asyncController.setProgressDialoug(true);
        asyncController.saveTargetValueForProcess(keyValuesCivil,editTextValuesCivil,keyValuesElectrical,editTextValuesElectrical,selected_type_id);
    }


    @Override
    public void asyncCompleteListner(String response, CallType callType) {
        setProgressDialoug(false);
        if (response != null) {
            if(callType==CallType.GET_TSS)
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
                            typeList.add(sectionlists.get(i).getTss_name());
                        }
                        // Log.e("value",""+headquaterlist.size());
                        typeAdapter.notifyDataSetChanged();
                      //  getOHETarget();
                    }
                    else {
                        Toast.makeText(this,"no data found",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(this,"no data found",Toast.LENGTH_LONG).show();
                }

            }
            else if (callType == CallType.GET_TSS_TARGET) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                                if (jsonObject.has("response_code")) {
                                    if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                                        Util.showLongToast(ProcessTSSEntryUserActivity.this, "No data available");

                                    } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                                        keyValuesCivil.clear();
                                        keyValuesElectrical.clear();
                                        pairVluesCivil.clear();
                                        pairVluesElectrical.clear();
                                        JSONObject jsonObject1 = new JSONObject(jsonObject.getString("civil"));
                                        JSONObject jsonObjectElctrical = new JSONObject(jsonObject.getString("electrical"));

                                        Iterator<String> keys = jsonObject1.keys();
                                        //  int i=0;
                                        while (keys.hasNext()) {
                                            try {
                                                String value = keys.next();
                                                // Log.e("key : ",value);//,"pair : "+jsonObject1.getString(""+keys.next()));
                                                keyValuesCivil.add(value);
                                                pairVluesCivil.add(String.valueOf(jsonObject1.get(value)));
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
                                                keyValuesElectrical.add(value);
                                                pairVluesElectrical.add(String.valueOf(jsonObjectElctrical.get(value)));
                                                // Log.e("pair",String.valueOf(jsonObject1.get(value)));
                                                //i++;
                                            } catch (Exception ex) {
                                                ex.printStackTrace();
                                            }


                                        }
                                        Log.e("size of civil",""+keyValuesElectrical.size());

                                        if(keyValuesCivil.size()>0 || keyValuesElectrical.size()>0)
                                        {
                                            setValueInLinearLayout();
                                            getTSS();
                                           // submitBtn.setClickable(false);

                                        }
                                    }

                                }


                    }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                }

             else if (callType == CallType.GET_TSS_TARGET_VALUE) {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(ProcessTSSEntryUserActivity.this, "No Target available");

                        } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                            JSONArray jsonArray = new JSONArray(jsonObject.getString("response"));
                            linear_add_civil.setVisibility(View.VISIBLE);
                            linear_add_electrical.setVisibility(View.VISIBLE);
                            submitBtn.setVisibility(View.VISIBLE);
                            int j=0;
                            for (int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject jsonObject1=jsonArray.getJSONObject(i);

                                //linear_text_view_civil_target,linear_text_view_electrical_target
                                if(jsonObject1.has(keyValuesCivil.get(0))) {
                                    JSONArray jsonElements=new JSONArray(jsonObject1.getString(keyValuesCivil.get(0)));
                                    {
                                        linear_text_view_civil_target[0].setText(jsonElements.getString(0));
                                        linear_edit_box_civil [0].setText(jsonElements.getString(1));
                                    }

                                }
                                else if(jsonObject1.has(keyValuesCivil.get(1))) {
                                    JSONArray jsonElements=new JSONArray(jsonObject1.getString(keyValuesCivil.get(1)));
                                    {
                                        linear_text_view_civil_target[1].setText(jsonElements.getString(0));
                                        linear_edit_box_civil [1].setText(jsonElements.getString(1));
                                    }

                                }
                                else if(jsonObject1.has(keyValuesElectrical.get(j))) {
                                   // Log.e("values","array");
                                    JSONArray jsonElements=new JSONArray(jsonObject1.getString(keyValuesElectrical.get(j)));
                                    {
                                        linear_text_view_electrical_target[j].setText(jsonElements.getString(0));
                                        linear_edit_box_electrical [j].setText(jsonElements.getString(1));
                                    }
                                    j++;
                                }
                                else {
                                    j++;
                                }

                            }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (callType == CallType.SAVE_TSS_TARGET_VALUE) {
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
                            linear_add_civil.setVisibility(View.GONE);
                            linear_add_electrical.setVisibility(View.GONE);
                            submitBtn.setVisibility(View.GONE);

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else {
                selected_date_text.setText("");
                Util.showShortToast(ProcessTSSEntryUserActivity.this, "Error in connection");
            }
        }
    }
    }



