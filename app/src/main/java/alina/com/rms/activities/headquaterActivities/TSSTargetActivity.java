package alina.com.rms.activities.headquaterActivities;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

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
import alina.com.rms.model.Result;
import alina.com.rms.model.Sectionlist;
import alina.com.rms.model.Tsstargetvalue;
import alina.com.rms.util.CallType;
import alina.com.rms.util.LoginDB;
import alina.com.rms.util.URL;
import alina.com.rms.util.Util;

public class TSSTargetActivity extends HeaduaterBaseActivity implements AdapterView.OnItemSelectedListener,AsyncCompleteListner {
    List<String> pairVluesCivil=new ArrayList<String>();
    List<String> keyValuesCivil=new ArrayList<String>();
    List<String> pairVluesElectrical=new ArrayList<String>();
    List<String> keyValuesElectrical=new ArrayList<String>();
    List<String> typeList=new ArrayList<String>();
    private boolean flagFirstTime;

    //int flags[] = {R.drawable.round1, R.drawable.round3, R.drawable.round4, R.drawable.round1, R.drawable.round3, R.drawable.round4};
    CustomAdapter monthAdapter,yearAdapter,typeAdapter;
    private List<Sectionlist>sectionlists=new ArrayList<Sectionlist>();

    private String selected_type_id;
    private Button submitBtn;
    private Response response;
    private List<Response> response1;
    List<String> monthNames=new ArrayList<String>();
    List<String> yearNames=new ArrayList<String>();
    private Spinner month_spinner,year_spinner,type_spinner;
    int[] months = {0,1,2,3,4,5,6,7,8,9,10,11};
    int current_year_position,current_month_position,current_position;
    private LinearLayout linear_add_civil,linear_add_electrical;
    private EditText[] linear_edit_box_civil,linear_edit_box_electrical;
    private EditText[] linear_text_view_civil,linear_text_view_electrical;
    private String selected_group,selected_section;
    private List<String>editTextValuesCivil=new ArrayList<>();
    private List<String>editTextValuesElectrical=new ArrayList<>();
    private Tsstargetvalue targetvalue;
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
        response1=gson.fromJson(LoginDB.getLoginResponseAsJSON(this),type);
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
        //hitToServer();
        getTSSTarget();


    }

    @Override
    public void addView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View myView = factory.inflate(R.layout.activity_tsstarget, null);
        setView(myView);
    }
    private void initUI() {
        headingText.setText("Set TSS Target");
        month_spinner= (Spinner) findViewById(R.id.spinner_month);
        year_spinner= (Spinner) findViewById(R.id.spinner_year);
        type_spinner=(Spinner)findViewById(R.id.type_spinner);
        //spin_item.setOnItemSelectedListener(this);
        month_spinner.setOnItemSelectedListener(this);
        year_spinner.setOnItemSelectedListener(this);
        type_spinner.setOnItemSelectedListener(this);
        linear_add_civil=(LinearLayout)findViewById(R.id.linear_add_civil);
        linear_add_electrical=(LinearLayout)findViewById(R.id.linear_add_electrical);
        submitBtn=(Button)findViewById(R.id.submitBtn);

        monthAdapter=new CustomAdapter(TSSTargetActivity.this,monthNames);
        month_spinner.setAdapter(monthAdapter);
        yearAdapter=new CustomAdapter(TSSTargetActivity.this,yearNames);
        year_spinner.setAdapter(yearAdapter);
        typeAdapter=new CustomAdapter(TSSTargetActivity.this,typeList);
        type_spinner.setAdapter(typeAdapter);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setHeadQuater();
            }
        });


    }

    @Override
    public void onResume()
    {
        super.onResume();

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent.getId()==R.id.type_spinner)
        {
            selected_type_id=sectionlists.get(position).getId();
                    getTargetValue();
        }

       /* if(parent.getId()==R.id.spinner_month)
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
            if(flagFirstTime)
            {
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

    private void setValueInLinearLayout()
    {
        linear_edit_box_civil=new EditText[keyValuesCivil.size()];
        linear_text_view_civil=new EditText[keyValuesCivil.size()];
        linear_edit_box_electrical=new EditText[keyValuesElectrical.size()];
        linear_text_view_electrical=new EditText[keyValuesElectrical.size()];
        for (int i=0;i<keyValuesCivil.size();i++)
        {
            View layoutInflater=getLayoutInflater().inflate(R.layout.item_list,null);
            linear_edit_box_civil[i]=(EditText)layoutInflater.findViewById(R.id.edit_data);
            linear_text_view_civil[i]=(EditText) layoutInflater.findViewById(R.id.txt_data);
            linear_text_view_civil[i].setText(pairVluesCivil.get(i));
            linear_add_civil.addView(layoutInflater);
        }

        for (int i=0;i<keyValuesElectrical.size();i++)
        {
            View layoutInflater=getLayoutInflater().inflate(R.layout.item_list,null);
            linear_edit_box_electrical[i]=(EditText)layoutInflater.findViewById(R.id.edit_data);
            linear_text_view_electrical[i]=(EditText) layoutInflater.findViewById(R.id.txt_data);
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


    public void getTSSTarget()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(this,this, CallType.GET_TSS_TARGET,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getOHERequest();
    }

    public void getTargetValue()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(this,this, CallType.GET_TARGET_VALUE_HEADQUATER,"",true);
        asyncController.setProgressDialoug(true);
//        if((Integer.parseInt(selected_month)+1)<10)
//        {
//            String monthName1="0"+((Integer.parseInt(selected_month))+1);
//            asyncController.getValueForTSSHeadquater(response.getHeadquater(),selected_group,selected_section,
//                    monthName1,selected_year);
//            // ,selected_date_text_eig1,selected_date_text_crs1,targetEditText1
//        }
//        else {
//            String monthName1=""+((Integer.parseInt(selected_month))+1);
//            asyncController.getValueForTSSHeadquater(response.getHeadquater(),selected_group,selected_section,
//                    monthName1,selected_year);
//        }
        if((current_month_position+1)<10)
        {
            String monthName1="0"+(current_month_position+1);
            asyncController.getValueForTSSHeadquater(response.getHeadquater(),selected_group,selected_section,
                    monthName1,""+current_year_position,selected_type_id);
            // ,selected_date_text_eig1,selected_date_text_crs1,targetEditText1
        }
        else {
            String monthName1=""+(current_month_position+1);
            asyncController.getValueForTSSHeadquater(response.getHeadquater(),selected_group,selected_section,
                    monthName1,""+current_year_position,selected_type_id);
        }




        /*
        AsyncController asyncController=new AsyncController(this,this, CallType.GET_TARGET_VALUE_HEADQUATER,"",true);
        asyncController.setProgressDialoug(true);*/
       // asyncController.getValueForTSSHeadquater(response.getHeadquater(),selecte_target,selected_item,selected_date_text1);
    }

    private void setValueInEditBox()
    {
        Gson gson=new Gson();
        String jsonStr=gson.toJson(targetvalue);
        // Log.e("Json Value",jsonStr);
        try {
            JSONObject jsonObject=new JSONObject(jsonStr);
            for (int i=0;i<keyValuesCivil.size();i++)
            {
                if(jsonObject.getString(keyValuesCivil.get(i))!=null)
                {
                    linear_edit_box_civil[i].setText(jsonObject.getString(keyValuesCivil.get(i)));
                }
            }
            for (int i=0;i<keyValuesElectrical.size();i++)
            {
                if(jsonObject.getString(keyValuesElectrical.get(i))!=null)
                {
                    linear_edit_box_electrical[i].setText(jsonObject.getString(keyValuesElectrical.get(i)));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    private void setBlankValueInEditBox()
    {
        // Log.e("Json Value",jsonStr);
        for (int i=0;i<keyValuesCivil.size();i++)
        {

                linear_edit_box_civil[i].setText("");
        }
        for (int i=0;i<keyValuesElectrical.size();i++)
        {

                linear_edit_box_electrical[i].setText("");

        }



    }


    public void setHeadQuater()
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
                editTextValuesCivil.add("");
            }
            else if(checkString.trim().length()<1)
            {
                editTextValuesCivil.add("");
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
                editTextValuesElectrical.add("");
            }
            else if(checkString.trim().length()<1)
            {
                editTextValuesElectrical.add("");
            }
            else {
                editTextValuesElectrical.add(checkString);
            }
        }

        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(this,this, CallType.SET_TARGET_VALUE,"",true);
        asyncController.setProgressDialoug(true);
        /*if((Integer.parseInt(selected_month)+1)<10)
        {
            String monthName1="0"+((Integer.parseInt(selected_month))+1);
            asyncController.setTSSTargetValueForHeadquater(response.getHeadquater(),selected_group,selected_section,
                    monthName1,selected_year,keyValuesCivil,editTextValuesCivil,keyValuesElectrical,editTextValuesElectrical);
            // ,selected_date_text_eig1,selected_date_text_crs1,targetEditText1
        }
        else {
            String monthName1=""+((Integer.parseInt(selected_month))+1);
            asyncController.setTSSTargetValueForHeadquater(response.getHeadquater(),selected_group,selected_section,
                    monthName1,selected_year,keyValuesCivil,editTextValuesCivil,keyValuesElectrical,editTextValuesElectrical);
        }*/
        if((current_month_position+1)<10)
        {
            String monthName1="0"+((current_month_position)+1);
            asyncController.setTSSTargetValueForHeadquater(response.getHeadquater(),selected_group,selected_section,
                    monthName1,""+current_year_position,keyValuesCivil,editTextValuesCivil,keyValuesElectrical,
                    editTextValuesElectrical,selected_type_id);
            // ,selected_date_text_eig1,selected_date_text_crs1,targetEditText1
        }
        else {
            String monthName1=""+((current_month_position)+1);
            asyncController.setTSSTargetValueForHeadquater(response.getHeadquater(),selected_group,selected_section,
                    monthName1,""+current_year_position,keyValuesCivil,editTextValuesCivil,keyValuesElectrical,
                    editTextValuesElectrical,selected_type_id);
        }

    }

    @Override
    public void asyncCompleteListner(String response, CallType callType) {
        setProgressDialoug(false);
        if(response!=null) {
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
                            Util.showLongToast(TSSTargetActivity.this, "No data available");

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
                                     Log.e("key : ",value);//,"pair : "+jsonObject1.getString(""+keys.next()));
                                    keyValuesCivil.add(value);
                                    pairVluesCivil.add(String.valueOf(jsonObject1.get(value)));
                                     Log.e("pair",String.valueOf(jsonObject1.get(value)));
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
                                     Log.e("key : ",value);//,"pair : "+jsonObject1.getString(""+keys.next()));
                                    keyValuesElectrical.add(value);
                                    pairVluesElectrical.add(String.valueOf(jsonObjectElctrical.get(value)));
                                     Log.e("pair",String.valueOf(jsonObjectElctrical.get(value)));
                                    //i++;
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }


                            }
                            Log.e("size of civil",""+keyValuesCivil.size());

                            if(keyValuesCivil.size()>0 || keyValuesElectrical.size()>0)
                            {
                                setValueInLinearLayout();
                                month_spinner.setSelection(current_month_position);
                                flagFirstTime=true;
                                year_spinner.setSelection(current_position-1);
                                getTSS();
                              //  getTargetValue();
                            }
                        }

                    }

                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(CallType.GET_TARGET_VALUE_HEADQUATER==callType)
            {
                Gson gson = new Gson();
                HeadQuaterGroupResponse response1 = gson.fromJson(response, HeadQuaterGroupResponse.class);
                if(response1!=null)
                {

                    if(response1.getResponseCode()==1)
                    {

                        if(response1.getTarget().get(0).getTsstargetvalue()!=null)
                        {
                            targetvalue = response1.getTarget().get(0).getTsstargetvalue();
                            setValueInEditBox();
                        }
                        else {
                            setBlankValueInEditBox();
                        }

                }


            }
            }
            else if(CallType.SET_TARGET_VALUE==callType) {
                Gson gson = new Gson();
                HeadQuaterGroupResponse response1 = gson.fromJson(response, HeadQuaterGroupResponse.class);
                if (response1.getResponseCode() == 1) {
               // Util.showLongToast(this,"Saved target successfully");
                   // showDialog("Your data saved target successfully");
                    //finish();
                    showDialog1(response1.getMessage());
                } else {
                    showDialog(response1.getMessage());
                }

            }
        }
    }
}
