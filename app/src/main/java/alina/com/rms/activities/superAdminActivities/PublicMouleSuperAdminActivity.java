package alina.com.rms.activities.superAdminActivities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import alina.com.rms.R;
import alina.com.rms.adaptor.CustomAdapter;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.model.CrsList;
import alina.com.rms.model.LoginResultPojo;
import alina.com.rms.model.PublicModuleResponseModel;
import alina.com.rms.model.Result;
import alina.com.rms.model.RkmList;
import alina.com.rms.util.CallType;
import alina.com.rms.util.LoginDB;
import alina.com.rms.util.Util;


public class PublicMouleSuperAdminActivity extends SuperAdminBaseActivity implements AsyncCompleteListner,AdapterView.OnItemSelectedListener {
    List<String> orgnaizationpairValues = new ArrayList<String>();
    List<String> orgnaizationKeyValues = new ArrayList<String>();

    List<String> projpairValues = new ArrayList<String>();
    List<String> projKeyValues = new ArrayList<String>();

    List<String> grouppairValues = new ArrayList<String>();
    List<String> groupKeyValues = new ArrayList<String>();

    List<String> sectionpairValues = new ArrayList<String>();
    List<String> sectionKeyValues = new ArrayList<String>();
    List<String> statepairValues = new ArrayList<String>();
    List<String> stateKeyValues = new ArrayList<String>();

    List<String> zoneepairValues = new ArrayList<String>();

    List<String> divisionpairValues = new ArrayList<String>();

    List<RkmList>rkmLists=new ArrayList<RkmList>();

    private CustomAdapter monthNameAdapter,orgNameAdapter,projNameAdapter
            ,groupNameAdapter,sectionNameAdapter,stateNameAdapter,zoneNameAdapter,
            divisionNameAdapter;
    private Spinner spinner_month,spinner_org,spinner_project,spinner_group,spinner_section
            ,spinner_state,spinner_zone,spinner_division;
    private String [] month_array={"April","May","June","July","August"
            ,"September","October","November","December","January","February","March"};
    private String month_name="April";
    private String group_name="",group_id="",head_name="",head_id="",org_name="",section_name="",section_id="",state_name=""
            ,zone_name="",rkm="",division="",org_id="";
    private boolean fisrtTimeFlag;
    private EditText edit_rkm,targetEditText,divisionEditText,zoneEditText,stateEditText,crsEditText;
    private Button submitBtn;
    private boolean firsTimeFlagForDialog;
    private TextView dateTextView;
    private LinearLayout linear_txt_field,date_linear,org_linear,prj_linear,group_linear,section_linear,division_linear,state_linear,zone_linear;

    private List<LoginResultPojo> loginResultPojo;
    private LoginResultPojo loginResultPojo1;
    private String login_response;
    private String crs_id="";
    private CrsList crsList;
    private RkmList rkmList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        login_response = LoginDB.getLoginResponseAsJSON(PublicMouleSuperAdminActivity.this);
        Gson gson = new Gson();
        Type type = new TypeToken<List<LoginResultPojo>>() {
        }.getType();
        loginResultPojo = gson.fromJson(login_response, type);
        loginResultPojo1 = loginResultPojo.get(0);
        Bundle bundle = getIntent().getExtras();

        addView();

        if (bundle != null) {
            headingText.setText("Edit CRS");
            crs_id = bundle.getString("crs_id");
            // rkmList=(RkmList) bundle.getSerializable("RKM_Data");
            getCRSList();
        } else {
            headingText.setText("ADD CRS");
            prj_linear.setVisibility(View.GONE);

            getOrgnaizationList();
        }



    }

    @Override
    public void addView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View myView = factory.inflate(R.layout.activity_public_moule, null);
        setView(myView);
        date_linear=(LinearLayout)findViewById(R.id.date_linear);
        org_linear=(LinearLayout)findViewById(R.id.org_linear);
        prj_linear=(LinearLayout)findViewById(R.id.prj_linear);
        group_linear=(LinearLayout)findViewById(R.id.group_linear);
        section_linear=(LinearLayout)findViewById(R.id.section_linear);
        division_linear=(LinearLayout)findViewById(R.id.division_linear);
        state_linear=(LinearLayout)findViewById(R.id.state_linear);
        zone_linear=(LinearLayout)findViewById(R.id.zone_linear);
        linear_txt_field=(LinearLayout)findViewById(R.id.linear_txt_field);
        linear_txt_field.setVisibility(View.GONE);


        dateTextView=(TextView)findViewById(R.id.txt_date);
        edit_rkm=(EditText)findViewById(R.id.rkmEditText);
        targetEditText=(EditText)findViewById(R.id.targetEditText);
        divisionEditText=(EditText)findViewById(R.id.divisionEditText);
        zoneEditText=(EditText)findViewById(R.id.zoneEditText);
        stateEditText=(EditText)findViewById(R.id.stateEditText);
        crsEditText=(EditText)findViewById(R.id.crsEditText);
        spinner_month=(Spinner)findViewById(R.id.spinner_month);
        spinner_org=(Spinner)findViewById(R.id.spinner_org);
        spinner_project=(Spinner)findViewById(R.id.spinner_project);
        spinner_group=(Spinner)findViewById(R.id.spinner_group);
        spinner_section=(Spinner)findViewById(R.id.spinner_section);
        spinner_state=(Spinner)findViewById(R.id.spinner_state);
        spinner_zone=(Spinner)findViewById(R.id.spinner_zone);
        spinner_division=(Spinner)findViewById(R.id.spinner_division);
/*        prj_linear=(LinearLayout)findViewById(R.id.prj_linear);
        group_linear=(LinearLayout)findViewById(R.id.group_linear);*/
        submitBtn=(Button)findViewById(R.id.submitBtn);
        spinner_month.setOnItemSelectedListener(this);
        spinner_org.setOnItemSelectedListener(this);
        spinner_project.setOnItemSelectedListener(this);
        spinner_group.setOnItemSelectedListener(this);
        spinner_section.setOnItemSelectedListener(this);
        spinner_state.setOnItemSelectedListener(this);
        spinner_zone.setOnItemSelectedListener(this);
        spinner_division.setOnItemSelectedListener(this);
        monthNameAdapter=new CustomAdapter(this, Arrays.asList(month_array));
        spinner_month.setAdapter(monthNameAdapter);

        orgNameAdapter=new CustomAdapter(this, orgnaizationpairValues);
        spinner_org.setAdapter(orgNameAdapter);

        projNameAdapter=new CustomAdapter(this, projpairValues);
        spinner_project.setAdapter(projNameAdapter);

        groupNameAdapter=new CustomAdapter(this, grouppairValues);
        spinner_group.setAdapter(groupNameAdapter);

        sectionNameAdapter=new CustomAdapter(this, sectionpairValues);
        spinner_section.setAdapter(sectionNameAdapter);

        stateNameAdapter=new CustomAdapter(this, statepairValues);
        spinner_state.setAdapter(stateNameAdapter);

        zoneNameAdapter=new CustomAdapter(this, zoneepairValues);
        spinner_zone.setAdapter(zoneNameAdapter);

        divisionNameAdapter=new CustomAdapter(this, divisionpairValues);
        spinner_division.setAdapter(divisionNameAdapter);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if(fisrtTimeFlag && crsList!=null) {
                    updateRkmValue();
                }
                else
                {*/
                    saveRkmValue();
                /*}*/
            }
        });

        if(crsList!=null && !crsList.getMonth().equalsIgnoreCase("0000-00-00"))
        {
            dateTextView.setText(crsList.getMonth());
            org_linear.setVisibility(View.VISIBLE);
        }
        date_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(dateTextView, PublicMouleSuperAdminActivity.this);
            }
        });
    }

    public void openDatePickerDialog(final TextView editText, PublicMouleSuperAdminActivity context) {
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
                        org_linear.setVisibility(View.VISIBLE);
                        // context.addDate_tolist(position,sdate,b);

                    }
                }, mYear, mMonth, mDay);
        dpd.setTitle("Select Date");
       // dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpd.show();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.e("Position", "" + position);
        //super.onItemSelected(parent,view,position,id);
        if (parent.getId() == R.id.spinner_month) {
            month_name = month_array[position];
            linear_txt_field.setVisibility(View.GONE);
        }
        else if(parent.getId() == R.id.spinner_org)
        {
            org_name=orgnaizationpairValues.get(position);
            org_id=orgnaizationKeyValues.get(position);
            if(position==1)
            {
                getProjectList();
                prj_linear.setVisibility(View.VISIBLE);


            }
            else if(position>1)
            {
                prj_linear.setVisibility(View.GONE);
                group_linear.setVisibility(View.GONE);
                section_linear.setVisibility(View.VISIBLE);
                head_id="";
                head_name="";
                group_name="";
                group_id="";
                getSectionListFromServer();
            }
            else {
                prj_linear.setVisibility(View.GONE);
                group_linear.setVisibility(View.GONE);
                section_linear.setVisibility(View.GONE);
            }

            division_linear.setVisibility(View.GONE);
            state_linear.setVisibility(View.GONE);
            zone_linear.setVisibility(View.GONE);
            edit_rkm.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);
            linear_txt_field.setVisibility(View.GONE);
        }
        else if (parent.getId() == R.id.spinner_project) {
            if (position > 0) {
                group_linear.setVisibility(View.VISIBLE);
                head_id = projKeyValues.get(position);
                head_name = projpairValues.get(position);
                getGroupListFromServer();
            } else {
                group_linear.setVisibility(View.GONE);

            }
            section_linear.setVisibility(View.GONE);
            division_linear.setVisibility(View.GONE);
            state_linear.setVisibility(View.GONE);
            zone_linear.setVisibility(View.GONE);
            edit_rkm.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);
            linear_txt_field.setVisibility(View.GONE);
        } else if (parent.getId() == R.id.spinner_group) {
            if (position > 0) {
                group_name = grouppairValues.get(position);
                group_id = groupKeyValues.get(position);
                //group_id = groupKeyValues.get(position);
                getSectionListFromServer();
                section_linear.setVisibility(View.VISIBLE);
                //spinner_section.setSelection(1);
            } else {
                section_linear.setVisibility(View.GONE);
            }

            division_linear.setVisibility(View.GONE);
            state_linear.setVisibility(View.GONE);
            zone_linear.setVisibility(View.GONE);
            edit_rkm.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);
            linear_txt_field.setVisibility(View.GONE);
        } else if (parent.getId() == R.id.spinner_section) {
            if (position > 0) {
                //division_linear.setVisibility(View.VISIBLE);

                section_name = sectionpairValues.get(position);
                section_id = sectionKeyValues.get(position);
                getRKMFromServer();
                linear_txt_field.setVisibility(View.VISIBLE);
                submitBtn.setVisibility(View.VISIBLE);
                //getDivisionListFromServer();

            } else {
                division_linear.setVisibility(View.GONE);
                linear_txt_field.setVisibility(View.GONE);
            }
/*            state_linear.setVisibility(View.GONE);
            zone_linear.setVisibility(View.GONE);
            edit_rkm.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);*/
        } else if (parent.getId() == R.id.spinner_division) {

            if (position > 0) {

                state_linear.setVisibility(View.VISIBLE);
                zone_linear.setVisibility(View.GONE);
                division = divisionpairValues.get(position);
                getSTATEListFromServer();

            } else {
                state_linear.setVisibility(View.GONE);

            }
            zone_linear.setVisibility(View.GONE);
            edit_rkm.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);
        } else if (parent.getId() == R.id.spinner_state) {
            if (position > 0) {

                zone_linear.setVisibility(View.VISIBLE);
                state_name = statepairValues.get(position);
                getZoneListFromServer();
            } else {
                zone_linear.setVisibility(View.GONE);

            }
            edit_rkm.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);
        } else if (parent.getId() == R.id.spinner_zone) {
            if (position > 0) {
                zone_name = zoneepairValues.get(position);
                edit_rkm.setVisibility(View.VISIBLE);
                submitBtn.setVisibility(View.VISIBLE);
            } else {
                edit_rkm.setVisibility(View.GONE);
                submitBtn.setVisibility(View.GONE);
            }


        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getCRSList()
    {
        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(PublicMouleSuperAdminActivity.this, this, CallType.GET_CRS_LIST, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getCRSList(crs_id);
    }
    public void getOrgnaizationList() {
        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(PublicMouleSuperAdminActivity.this, this, CallType.GET_ORGNAIZATION_LIST, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getOrgnaization();
    }

    public void getProjectList() {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(PublicMouleSuperAdminActivity.this,this,CallType.GET_PROJECT_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getProjectList();
    }

    private void getGroupListFromServer()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(PublicMouleSuperAdminActivity.this,this,CallType.GET_GROUP_USER,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getGroupListForPublicUser(head_id);
    }

    private void getSectionListFromServer()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(PublicMouleSuperAdminActivity.this,this,CallType.GET_SECTION_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getSectionListForPublicUser(group_id,org_id);
    }

    private void getDivisionListFromServer()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(PublicMouleSuperAdminActivity.this,this,CallType.GET_DVISION_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getDivisionList(section_name);
    }

    private void getRKMFromServer()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(PublicMouleSuperAdminActivity.this,this, CallType.GET_RKM,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getRKM(section_id);
    }

    private void getSTATEListFromServer()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(PublicMouleSuperAdminActivity.this,this,CallType.GET_STATE_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getStateList(section_name,division);
    }

    private void getZoneListFromServer()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(PublicMouleSuperAdminActivity.this,this,CallType.GET_ZONE_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getZoneList(section_name,division);
    }

    public void saveRkmValue()
    {
        String date=dateTextView.getText().toString().trim();
        if(date.equalsIgnoreCase("Select Date"))
        {
            Util.showLongToast(this,"Please select date");
            return;
        }
        else if(!Util.chechTextValue(crsEditText))
        {
            Util.showLongToast(this,"Please filled valid value in RKM");
            return;
        }
        rkm=crsEditText.getText().toString();

        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(PublicMouleSuperAdminActivity.this,this,CallType.SET_RKM,"",true);
        asyncController.setProgressDialoug(true);
        if(crsList!=null)
        {
            asyncController.saveRKM(rkmLists.get(0).getRkmId(),rkm,date,crs_id);
        }
        else
        {
            asyncController.saveRKM(rkmLists.get(0).getRkmId(),rkm,date,"");
        }

    }

    public void updateRkmValue()
    {
        String date=dateTextView.getText().toString().trim();
        if(date.equalsIgnoreCase("Select Date"))
        {
            Util.showLongToast(this,"Please select date");
            return;
        }
        else if(!Util.chechTextValue(edit_rkm))
        {
            Util.showLongToast(this,"Please filled valid value in RKM");
            return;
        }

        rkm=edit_rkm.getText().toString();

        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(PublicMouleSuperAdminActivity.this,this,CallType.UPDATE_RKM,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.updateRKMForPublicUser(head_id,head_name,group_name,section_name,zone_name,state_name,org_name,rkm,dateTextView.getText().toString(),division);

    }

    @Override
    public void asyncCompleteListner(String response, CallType callType) {

        setProgressDialoug(false);

        if (response != null) {
            if (callType == CallType.GET_CRS_LIST) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(getApplicationContext(), "No data available");

                        } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                            Gson gson = new Gson();
                            Result response1 = gson.fromJson(response, Result.class);
                            if(response1.getCrsList().size()>0)
                            {
                                crsList = response1.getCrsList().get(0);

                            dateTextView.setText(crsList.getMonth());
                            crsEditText.setText(crsList.getCrs());
                                if(crsList.getOrgName().equalsIgnoreCase("CORE"))
                                {
                                    prj_linear.setVisibility(View.VISIBLE);
                                }
                                else {
                                    section_linear.setVisibility(View.VISIBLE);
                                }
                            }

                            getOrgnaizationList();
                        }
                    }
                } catch (JSONException ex) {

                }

                //getOrgnaizationList();
            } else if (callType == CallType.GET_ORGNAIZATION_LIST) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(getApplicationContext(), "No data available");

                        } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                            orgnaizationKeyValues.clear();
                            orgnaizationpairValues.clear();
                            orgnaizationKeyValues.add("");
                            orgnaizationpairValues.add("");
                            Gson gson = new Gson();
                            Type type = new TypeToken<PublicModuleResponseModel>() {
                            }.getType();
                            PublicModuleResponseModel publicModuleResponseModel = gson.fromJson(response, type);
                            //int i = 0;
                            for (int i = 0; i < publicModuleResponseModel.getList().size(); i++) {
                                orgnaizationpairValues.add(publicModuleResponseModel.getList().get(i).getOrgName());
                                orgnaizationKeyValues.add(publicModuleResponseModel.getList().get(i).getOrgId());

                                Log.e(publicModuleResponseModel.getList().get(i).getOrgName(),publicModuleResponseModel.getList().get(i).getOrgId());
                            }
                            orgNameAdapter.notifyDataSetChanged();
                            if(crsList!=null)
                            {
                                for (int i=0;i<orgnaizationpairValues.size();i++)
                                {
                                    if(crsList.getOrgName().equalsIgnoreCase(orgnaizationpairValues.get(i)))
                                    {
                                        spinner_org.setSelection(i);
                                        break;
                                    }
                                }
                            }
                            else {
                                spinner_org.setSelection(0);
                            }


                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } else if (callType == CallType.GET_PROJECT_LIST) {
                Gson gson = new Gson();
                Result response1 = gson.fromJson(response, Result.class);

                if (response1 != null) {

                    if (response1.getProjectList() != null) {
                        projpairValues.clear();
                        projKeyValues.clear();
                        projpairValues.add("");
                        projKeyValues.add("");
                        Log.e("String", "value");
                        boolean flag_to_check = false;
                        for (int i = 0; i < response1.getProjectList().size(); i++) {
                            if (!fisrtTimeFlag && crsList != null) {
                                if (response1.getProjectList().get(i).getProjName().equalsIgnoreCase(crsList.getProjName())) {
                                    flag_to_check = true;
                                    projpairValues.add(1, response1.getProjectList().get(i).getProjName());
                                    projKeyValues.add(1, response1.getProjectList().get(i).getProjectId());
                                } else {
                                    projpairValues.add(response1.getProjectList().get(i).getProjName());
                                    projKeyValues.add(response1.getProjectList().get(i).getProjectId());
                                }
                            } else {
                                projpairValues.add(response1.getProjectList().get(i).getProjName());
                                projKeyValues.add(response1.getProjectList().get(i).getProjectId());
                            }

                        }


                        projNameAdapter.notifyDataSetChanged();
                        if (flag_to_check) {
                            spinner_project.setSelection(1);
                        }
                        else {
                            spinner_project.setSelection(0);
                        }
                    } else {
                        Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                    }


                }
            } else if (callType == CallType.GET_GROUP_USER) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(getApplicationContext(), "No data available");

                        } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                            grouppairValues.clear();
                            groupKeyValues.clear();
                            grouppairValues.add("");
                            groupKeyValues.add("");
                            Gson gson = new Gson();
                            Type type = new TypeToken<PublicModuleResponseModel>() {
                            }.getType();
                            PublicModuleResponseModel publicModuleResponseModel = gson.fromJson(response, type);
                            int i = 0;
                            boolean flag_to_check = false;
                            while (i < publicModuleResponseModel.getGroupList().size()) {

                                if (!fisrtTimeFlag && crsList != null) {
                                    if (publicModuleResponseModel.getGroupList().get(i).getGroupName().equalsIgnoreCase(crsList.getGroupName())) {
                                        flag_to_check = true;
                                        grouppairValues.add(1, publicModuleResponseModel.getGroupList().get(i).getGroupName());
                                        groupKeyValues.add(1, publicModuleResponseModel.getGroupList().get(i).getGroup_id());
                                    } else {
                                        grouppairValues.add(publicModuleResponseModel.getGroupList().get(i).getGroupName());
                                        groupKeyValues.add(publicModuleResponseModel.getGroupList().get(i).getGroup_id());
                                    }
                                } else {
                                    grouppairValues.add(publicModuleResponseModel.getGroupList().get(i).getGroupName());
                                    groupKeyValues.add(publicModuleResponseModel.getGroupList().get(i).getGroup_id());
                                }
                                i++;
                            }
                            groupNameAdapter.notifyDataSetChanged();
                            if (flag_to_check) {
                                spinner_group.setSelection(1);
                            }
                            else {
                                spinner_group.setSelection(1);
                            }
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        /*remaing Section */
            else if (callType == CallType.GET_SECTION_LIST && response != null) {
                try {
                    Log.e("testing", response);
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(getApplicationContext(), "No data available");

                        } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<PublicModuleResponseModel>() {
                            }.getType();
                            PublicModuleResponseModel publicModuleResponseModel = gson.fromJson(response, type);
                            int i = 0;
                            boolean flag_to_check = false;
                            sectionpairValues.clear();
                            sectionKeyValues.clear();
                            sectionpairValues.add("");
                            sectionKeyValues.add("");
                            while (i < publicModuleResponseModel.getSectionList().size()) {
                                if (!fisrtTimeFlag && crsList != null) {
                                    if (publicModuleResponseModel.getSectionList().get(i).getSectionName().equalsIgnoreCase(crsList.getSectionName())) {
                                        flag_to_check = true;
                                        sectionpairValues.add(1, publicModuleResponseModel.getSectionList().get(i).getSectionName());
                                        sectionKeyValues.add(1, publicModuleResponseModel.getSectionList().get(i).getSection_id());
                                    } else {
                                        sectionpairValues.add(publicModuleResponseModel.getSectionList().get(i).getSectionName());
                                        sectionKeyValues.add(publicModuleResponseModel.getSectionList().get(i).getSection_id());
                                    }
                                } else {
                                    sectionpairValues.add(publicModuleResponseModel.getSectionList().get(i).getSectionName());
                                    sectionKeyValues.add(publicModuleResponseModel.getSectionList().get(i).getSection_id());
                                }

                                i++;
                            }
                            sectionNameAdapter.notifyDataSetChanged();
                            if (flag_to_check)
                            {
                                spinner_section.setSelection(1);
                            }
                            else {
                                spinner_section.setSelection(0);
                            }


                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (callType == callType.GET_DVISION_LIST) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(getApplicationContext(), "No data available");

                        } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {

                            divisionpairValues.clear();
                            divisionpairValues.add("");
                            Gson gson = new Gson();
                            Type type = new TypeToken<PublicModuleResponseModel>() {
                            }.getType();
                            PublicModuleResponseModel publicModuleResponseModel = gson.fromJson(response, type);
                            int i = 0;
                            boolean flag_to_check = false;
                            while (i < publicModuleResponseModel.getDivisionList().size()) {
                                if (!fisrtTimeFlag && crsList != null) {
                                    if (publicModuleResponseModel.getDivisionList().get(i).getDivision().equalsIgnoreCase(crsList.getDivision())) {
                                        flag_to_check = true;
                                        divisionpairValues.add(1, publicModuleResponseModel.getDivisionList().get(i).getDivision());
                                    } else {
                                        divisionpairValues.add(publicModuleResponseModel.getDivisionList().get(i).getDivision());
                                    }
                                } else {
                                    divisionpairValues.add(publicModuleResponseModel.getDivisionList().get(i).getDivision());
                                }

                                i++;
                            }
                            divisionNameAdapter.notifyDataSetChanged();
                            if (flag_to_check) {
                                spinner_division.setSelection(1);
                            }
                            else {
                                spinner_division.setSelection(0);
                            }

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            } else if (callType == CallType.GET_STATE_LIST) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(getApplicationContext(), "No data available");

                        } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                            stateKeyValues.clear();
                            statepairValues.clear();
                            stateKeyValues.add("");
                            statepairValues.add("");
                            Gson gson = new Gson();
                            Type type = new TypeToken<PublicModuleResponseModel>() {
                            }.getType();
                            PublicModuleResponseModel publicModuleResponseModel = gson.fromJson(response, type);
                            int i = 0;
                            boolean flag_to_check = false;
                            while (i < publicModuleResponseModel.getStateList().size()) {
                                if (!fisrtTimeFlag && crsList != null) {
                                    if (crsList.getState().equalsIgnoreCase(publicModuleResponseModel.getStateList().get(i).getStateName())) {
                                        flag_to_check = true;
                                        statepairValues.add(1, publicModuleResponseModel.getStateList().get(i).getStateName());
                                        stateKeyValues.add(1, publicModuleResponseModel.getStateList().get(i).getStateId());
                                    } else {
                                        statepairValues.add(publicModuleResponseModel.getStateList().get(i).getStateName());
                                        stateKeyValues.add(publicModuleResponseModel.getStateList().get(i).getStateId());
                                    }
                                } else {
                                    statepairValues.add(publicModuleResponseModel.getStateList().get(i).getStateName());
                                    stateKeyValues.add(publicModuleResponseModel.getStateList().get(i).getStateId());
                                }

                                i++;
                            }
                            stateNameAdapter.notifyDataSetChanged();

                            if (flag_to_check) {
                                spinner_state.setSelection(1);
                            }

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                firsTimeFlagForDialog = true;
            } else if (callType == CallType.GET_ZONE_LIST) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(getApplicationContext(), "No data available");

                        } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {

                            zoneepairValues.clear();
                            zoneepairValues.add("");
                            Gson gson = new Gson();
                            Type type = new TypeToken<PublicModuleResponseModel>() {
                            }.getType();
                            PublicModuleResponseModel publicModuleResponseModel = gson.fromJson(response, type);
                            int i = 0;
                            boolean flag_to_check = false;
                            while (i < publicModuleResponseModel.getZoneList().size()) {
                                if (!fisrtTimeFlag && crsList != null) {
                                    if (publicModuleResponseModel.getZoneList().get(i).getZone().equalsIgnoreCase(crsList.getZone())) {
                                        flag_to_check = true;
                                        zoneepairValues.add(1, publicModuleResponseModel.getZoneList().get(i).getZone());
                                    } else {
                                        zoneepairValues.add(publicModuleResponseModel.getZoneList().get(i).getZone());
                                    }
                                } else {
                                    zoneepairValues.add(publicModuleResponseModel.getZoneList().get(i).getZone());
                                }

                                i++;
                            }
                            zoneNameAdapter.notifyDataSetChanged();
                            if (flag_to_check) {
                                spinner_zone.setSelection(1);
                            }
                            if (!fisrtTimeFlag && crsList != null) {

                                if (crsList.getRkm() != null) {
                                    edit_rkm.setText(crsList.getRkm());
                                }
                            }

                            fisrtTimeFlag = true;
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (callType == CallType.SET_RKM) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(getApplicationContext(), jsonObject.getString("message"));

                        } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                            Util.showLongToast(getApplicationContext(), jsonObject.getString("message"));
                            edit_rkm.setText("");
                            finish();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (callType == CallType.UPDATE_RKM) {
                Gson gson = new Gson();
                Result response1 = gson.fromJson(response, Result.class);

                if (response1 != null) {
                    if (response1.getResponseCode() != null) {
                        if (response1.getResponseCode() == 1) {
                            //  Log.e("String", "value");
                            Toast.makeText(getApplicationContext(), response1.getMessage(), Toast.LENGTH_LONG).show();
                            finish();
                        }

                    } else {
                        Toast.makeText(getApplicationContext(), "RKM not updated successfully", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "RKM not updated successfully", Toast.LENGTH_LONG).show();
                }
            } else if (callType == CallType.GET_RKM) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(getApplicationContext(), "No data available");

                        } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {

                            if (jsonObject.has("rkmList")) {
                                JSONArray jsonArray = jsonObject.getJSONArray("rkmList");

                                if (jsonArray.length() > 0) {
                                    Gson gson = new Gson();
                                    Type type = new TypeToken<List<RkmList>>() {
                                    }.getType();

                                    rkmLists = (List<RkmList>) gson.fromJson(jsonObject.getString("rkmList"), type);

                                    edit_rkm.setText(rkmLists.get(0).getRkm());
                                    targetEditText.setText(rkmLists.get(0).getTraget());
                                    divisionEditText.setText(rkmLists.get(0).getDivisionName());
                                    zoneEditText.setText(rkmLists.get(0).getZone());
                                    stateEditText.setText(rkmLists.get(0).getState());
                                    // crsEditText.setText("");
                                } else {
                                    //  Util.showLongToast(getApplicationContext(),"No data available");
                                    showDialog1("Please set RKM first before proceeding ahead");

                                    linear_txt_field.setVisibility(View.GONE);
                                }
                            }

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
    }
}

/*

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import alina.com.rms.R;
import alina.com.rms.adaptor.CustomAdapter;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.model.PublicModuleResponseModel;
import alina.com.rms.model.Result;
import alina.com.rms.model.RkmList;
import alina.com.rms.util.CallType;
import alina.com.rms.util.Util;

public class PublicMouleSuperAdminActivity extends SuperAdminBaseActivity implements AsyncCompleteListner,AdapterView.OnItemSelectedListener {

    List<String> orgnaizationpairValues = new ArrayList<String>();
    List<String> orgnaizationKeyValues = new ArrayList<String>();

    List<String> projpairValues = new ArrayList<String>();
    List<String> projKeyValues = new ArrayList<String>();

    List<String> grouppairValues = new ArrayList<String>();
   // List<String> groupKeyValues = new ArrayList<String>();

    List<String> sectionpairValues = new ArrayList<String>();

    List<String> statepairValues = new ArrayList<String>();
    List<String> stateKeyValues = new ArrayList<String>();

    List<String> zoneepairValues = new ArrayList<String>();

    List<String> divisionpairValues = new ArrayList<String>();

    private CustomAdapter monthNameAdapter,orgNameAdapter,projNameAdapter
            ,groupNameAdapter,sectionNameAdapter,stateNameAdapter,zoneNameAdapter,
            divisionNameAdapter;
    private Spinner spinner_month,spinner_org,spinner_project,spinner_group,spinner_section
            ,spinner_state,spinner_zone,spinner_division;
    private String [] month_array={"April","May","June","July","August"
            ,"September","October","November","December","January","February","March"};
    private String month_name="April";
    private String group_name="",group_id="",head_name="",head_id="",org_name="",section_name="",state_name=""
            ,zone_name="",rkm="",division="";
    private boolean fisrtTimeFlag;
    private EditText edit_rkm;
    private Button submitBtn;
    private boolean firsTimeFlagForDialog;
    private RkmList rkmList;
    private TextView dateTextView;
    private LinearLayout date_linear,org_linear,prj_linear,group_linear,section_linear,division_linear,state_linear,zone_linear;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            headingText.setText("Edit Public Module");
            rkmList=(RkmList) bundle.getSerializable("RKM_Data");
        }
        else
        {
            headingText.setText("Public Module");
        }

        addView();
        getOrgnaizationList();
    }

    @Override
    public void addView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View myView = factory.inflate(R.layout.activity_public_moule, null);
        setView(myView);
        date_linear=(LinearLayout)findViewById(R.id.date_linear);
        org_linear=(LinearLayout)findViewById(R.id.org_linear);
        prj_linear=(LinearLayout)findViewById(R.id.prj_linear);
        group_linear=(LinearLayout)findViewById(R.id.group_linear);
        section_linear=(LinearLayout)findViewById(R.id.section_linear);
        division_linear=(LinearLayout)findViewById(R.id.division_linear);
        state_linear=(LinearLayout)findViewById(R.id.state_linear);
        zone_linear=(LinearLayout)findViewById(R.id.zone_linear);
        dateTextView=(TextView)findViewById(R.id.txt_date);
        edit_rkm=(EditText)findViewById(R.id.rkmEditText);
        spinner_month=(Spinner)findViewById(R.id.spinner_month);
        spinner_org=(Spinner)findViewById(R.id.spinner_org);
        spinner_project=(Spinner)findViewById(R.id.spinner_project);
        spinner_group=(Spinner)findViewById(R.id.spinner_group);
        spinner_section=(Spinner)findViewById(R.id.spinner_section);
        spinner_state=(Spinner)findViewById(R.id.spinner_state);
        spinner_zone=(Spinner)findViewById(R.id.spinner_zone);
        spinner_division=(Spinner)findViewById(R.id.spinner_division);
*/
/*        prj_linear=(LinearLayout)findViewById(R.id.prj_linear);
        group_linear=(LinearLayout)findViewById(R.id.group_linear);*//*

        submitBtn=(Button)findViewById(R.id.submitBtn);
        spinner_month.setOnItemSelectedListener(this);
        spinner_org.setOnItemSelectedListener(this);
        spinner_project.setOnItemSelectedListener(this);
        spinner_group.setOnItemSelectedListener(this);
        spinner_section.setOnItemSelectedListener(this);
        spinner_state.setOnItemSelectedListener(this);
        spinner_zone.setOnItemSelectedListener(this);
        spinner_division.setOnItemSelectedListener(this);
        monthNameAdapter=new CustomAdapter(this, Arrays.asList(month_array));
        spinner_month.setAdapter(monthNameAdapter);

        orgNameAdapter=new CustomAdapter(this, orgnaizationpairValues);
        spinner_org.setAdapter(orgNameAdapter);

        projNameAdapter=new CustomAdapter(this, projpairValues);
        spinner_project.setAdapter(projNameAdapter);

        groupNameAdapter=new CustomAdapter(this, grouppairValues);
        spinner_group.setAdapter(groupNameAdapter);

        sectionNameAdapter=new CustomAdapter(this, sectionpairValues);
        spinner_section.setAdapter(sectionNameAdapter);

        stateNameAdapter=new CustomAdapter(this, statepairValues);
        spinner_state.setAdapter(stateNameAdapter);

        zoneNameAdapter=new CustomAdapter(this, zoneepairValues);
        spinner_zone.setAdapter(zoneNameAdapter);

        divisionNameAdapter=new CustomAdapter(this, divisionpairValues);
        spinner_division.setAdapter(divisionNameAdapter);

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fisrtTimeFlag && rkmList!=null) {
                    updateRkmValue();
                }
                else
                {
                    saveRkmValue();
                }
            }
        });
        if(!fisrtTimeFlag && rkmList!=null) {
            for (int i=0;i<month_array.length;i++)
            {
                if(rkmList.getMonth()!=null) {
                    if (rkmList.getMonth().equalsIgnoreCase(month_array[i])) {
                        spinner_month.setSelection(i);
                    }
                }
                else {
                    break;
                }
            }
        }
        if(rkmList!=null && !rkmList.getMonth().equalsIgnoreCase("0000-00-00"))
        {
            dateTextView.setText(rkmList.getMonth());
            org_linear.setVisibility(View.VISIBLE);
        }
        date_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(dateTextView,PublicMouleSuperAdminActivity.this);
            }
        });
    }

    public void openDatePickerDialog(final TextView editText, PublicMouleSuperAdminActivity context) {
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
                        org_linear.setVisibility(View.VISIBLE);
                        // context.addDate_tolist(position,sdate,b);

                    }
                }, mYear, mMonth, mDay);
        dpd.setTitle("Select Date");
        dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpd.show();
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.e("Position", "" + position);
        if (parent.getId() == R.id.spinner_month) {
            month_name=month_array[position];
        }
        else if(parent.getId() == R.id.spinner_org)
        {
            org_name=orgnaizationpairValues.get(position);
            if(position==1)
            {
                getProjectList();
                prj_linear.setVisibility(View.VISIBLE);

            }
            else if(position>1)
            {
                prj_linear.setVisibility(View.GONE);
                group_linear.setVisibility(View.GONE);
                section_linear.setVisibility(View.VISIBLE);
                head_id="";
                head_name="";
                group_name="";
                group_id="";
                getSectionListFromServer();
            }
            else {
                prj_linear.setVisibility(View.GONE);
                group_linear.setVisibility(View.GONE);
                section_linear.setVisibility(View.GONE);
            }

            division_linear.setVisibility(View.GONE);
            state_linear.setVisibility(View.GONE);
            zone_linear.setVisibility(View.GONE);
            edit_rkm.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);
        }
        else if(parent.getId() == R.id.spinner_project)
        {
            if(position>0) {
                group_linear.setVisibility(View.VISIBLE);
                head_id = projKeyValues.get(position);
                head_name = projpairValues.get(position);
                getGroupListFromServer();
            }
            else {
                group_linear.setVisibility(View.GONE);

            }
            section_linear.setVisibility(View.GONE);
            division_linear.setVisibility(View.GONE);
            state_linear.setVisibility(View.GONE);
            zone_linear.setVisibility(View.GONE);
            edit_rkm.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);
        }
        else if(parent.getId() == R.id.spinner_group)
        {
            if(position>0) {
                group_name = grouppairValues.get(position);
                //group_id = groupKeyValues.get(position);
                getSectionListFromServer();
                section_linear.setVisibility(View.VISIBLE);
                spinner_section.setSelection(1);
            }
            else {
                section_linear.setVisibility(View.GONE);
                division_linear.setVisibility(View.GONE);
            }


            state_linear.setVisibility(View.GONE);
            zone_linear.setVisibility(View.GONE);
            edit_rkm.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);
        }
        else if(parent.getId() == R.id.spinner_section)
        {

                division_linear.setVisibility(View.VISIBLE);

                section_name = sectionpairValues.get(position);
                getDivisionListFromServer();
            //division_linear.setVisibility(View.GONE);
            state_linear.setVisibility(View.GONE);
            zone_linear.setVisibility(View.GONE);
            edit_rkm.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);
        }
        else if (parent.getId()==R.id.spinner_division)
        {

            if(position>0) {

                state_linear.setVisibility(View.VISIBLE);
                zone_linear.setVisibility(View.GONE);
                division = divisionpairValues.get(position);
                getSTATEListFromServer();

            }
            else {
                state_linear.setVisibility(View.GONE);

            }
            zone_linear.setVisibility(View.GONE);
            edit_rkm.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);
        }
        else if(parent.getId() == R.id.spinner_state)
        {
            if(position>0)
            {

                zone_linear.setVisibility(View.VISIBLE);
                state_name=statepairValues.get(position);
                getZoneListFromServer();
            }
            else {
                zone_linear.setVisibility(View.GONE);

            }
            edit_rkm.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);
        }
        else if(parent.getId() == R.id.spinner_zone)
        {
            if(position>0)
            {
                zone_name=zoneepairValues.get(position);
                edit_rkm.setVisibility(View.VISIBLE);
                submitBtn.setVisibility(View.VISIBLE);
            }
            else {
                edit_rkm.setVisibility(View.GONE);
                submitBtn.setVisibility(View.GONE);
            }



        }

    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    public void getOrgnaizationList() {
        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(PublicMouleSuperAdminActivity.this, this, CallType.GET_ORGNAIZATION_LIST, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getOrgnaization();
    }

    public void getProjectList() {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(PublicMouleSuperAdminActivity.this,this,CallType.GET_HQ_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getHQList();
    }

    private void getGroupListFromServer()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(PublicMouleSuperAdminActivity.this,this,CallType.GET_GROUP_USER,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getGroupListForPublicUser(head_name);
    }

    private void getSectionListFromServer()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(PublicMouleSuperAdminActivity.this,this,CallType.GET_SECTION_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getSectionListForPublicUser(group_name);
    }

    private void getDivisionListFromServer()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(PublicMouleSuperAdminActivity.this,this,CallType.GET_DVISION_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getDivisionList(section_name);
    }

    private void getSTATEListFromServer()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(PublicMouleSuperAdminActivity.this,this,CallType.GET_STATE_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getStateList(section_name,division);
    }

    private void getZoneListFromServer()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(PublicMouleSuperAdminActivity.this,this,CallType.GET_ZONE_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getZoneList(section_name,division);
    }

    public void saveRkmValue()
    {
        String date=dateTextView.getText().toString().trim();
        if(date.equalsIgnoreCase("Select Date"))
        {
            Util.showLongToast(this,"Please select date");
            return;
        }
        else if(!Util.chechTextValue(edit_rkm))
        {
            Util.showLongToast(this,"Please filled valid value in RKM");
            return;
        }
        rkm=edit_rkm.getText().toString();

        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(PublicMouleSuperAdminActivity.this,this,CallType.SET_RKM,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.setRKMForPublicUser(head_name,group_name,section_name,zone_name,state_name,org_name,rkm,dateTextView.getText().toString(),division);

    }

    public void updateRkmValue()
    {
        String date=dateTextView.getText().toString().trim();
        if(date.equalsIgnoreCase("Select Date"))
        {
            Util.showLongToast(this,"Please select date");
            return;
        }
        else if(!Util.chechTextValue(edit_rkm))
        {
            Util.showLongToast(this,"Please filled valid value in RKM");
            return;
        }

        rkm=edit_rkm.getText().toString();

        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(PublicMouleSuperAdminActivity.this,this,CallType.UPDATE_RKM,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.updateRKMForPublicUser(head_id,head_name,group_name,section_name,zone_name,state_name,org_name,rkm,dateTextView.getText().toString(),division);

    }

    @Override
    public void asyncCompleteListner(String response, CallType callType) {

        setProgressDialoug(false);

        if (callType == CallType.GET_ORGNAIZATION_LIST) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("response_code")) {
                    if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                        Util.showLongToast(PublicMouleSuperAdminActivity.this, "No data available");

                    } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                        orgnaizationKeyValues.clear();
                        orgnaizationpairValues.clear();
                        orgnaizationKeyValues.add("");
                        orgnaizationpairValues.add("");
                        Gson gson = new Gson();
                        Type type = new TypeToken<PublicModuleResponseModel>() {
                        }.getType();
                        PublicModuleResponseModel publicModuleResponseModel = gson.fromJson(response, type);
                        int i = 0;
                        boolean flag_to_check=false;
                        while (i < publicModuleResponseModel.getList().size()) {
                            if(!fisrtTimeFlag && rkmList!=null) {
                                if(publicModuleResponseModel.getList().get(i).getOrgName().equalsIgnoreCase(rkmList.getOrg_name()))
                                {
                                    flag_to_check=true;
                                    orgnaizationpairValues.add(1,publicModuleResponseModel.getList().get(i).getOrgName());
                                    orgnaizationKeyValues.add(1,publicModuleResponseModel.getList().get(i).getOrgId());
                                }
                                else {
                                    orgnaizationpairValues.add(publicModuleResponseModel.getList().get(i).getOrgName());
                                    orgnaizationKeyValues.add(publicModuleResponseModel.getList().get(i).getOrgId());
                                }
                            }
                            else {
                                orgnaizationpairValues.add(publicModuleResponseModel.getList().get(i).getOrgName());
                                orgnaizationKeyValues.add(publicModuleResponseModel.getList().get(i).getOrgId());
                            }

                            i++;
                        }

                        orgNameAdapter.notifyDataSetChanged();
                        if(flag_to_check)
                        {
                            spinner_org.setSelection(1);
                        }
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        } else if (callType == CallType.GET_HQ_LIST) {
            Gson gson = new Gson();
            Result response1 = gson.fromJson(response, Result.class);

            if (response1 != null) {

                if (response1.getHeadquaterlist() != null) {
                    projpairValues.clear();
                    projKeyValues.clear();
                    projpairValues.add("");
                    projKeyValues.add("");
                    Log.e("String", "value");
                    boolean flag_to_check=false;
                    for (int i = 0; i < response1.getHeadquaterlist().size(); i++) {
                        if(!fisrtTimeFlag && rkmList!=null) {
                            if(response1.getHeadquaterlist().get(i).getName().equalsIgnoreCase(rkmList.getProjName()))
                            {
                                flag_to_check=true;
                                projpairValues.add(1,response1.getHeadquaterlist().get(i).getName());
                                projKeyValues.add(1,response1.getHeadquaterlist().get(i).getId());
                            }
                            else
                            {
                                projpairValues.add(response1.getHeadquaterlist().get(i).getName());
                                projKeyValues.add(response1.getHeadquaterlist().get(i).getId());
                            }
                        }
                        else{
                            projpairValues.add(response1.getHeadquaterlist().get(i).getName());
                            projKeyValues.add(response1.getHeadquaterlist().get(i).getId());
                        }

                    }


                    projNameAdapter.notifyDataSetChanged();
                    if(flag_to_check)
                    {
                        spinner_project.setSelection(1);
                    }
                } else {
                    Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                }


            }
        }
        else if (callType == CallType.GET_GROUP_USER) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("response_code")) {
                    if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                        Util.showLongToast(PublicMouleSuperAdminActivity.this, "No data available");

                    } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                        grouppairValues.clear();
                      // groupKeyValues.clear();
                        grouppairValues.add("");
                        //groupKeyValues.add("");
                        Gson gson = new Gson();
                        Type type = new TypeToken<PublicModuleResponseModel>() {
                        }.getType();
                        PublicModuleResponseModel publicModuleResponseModel = gson.fromJson(response, type);
                        int i = 0;
                        boolean flag_to_check=false;
                        while (i < publicModuleResponseModel.getGroupList().size()) {

                            if(!fisrtTimeFlag && rkmList!=null) {
                                if(publicModuleResponseModel.getGroupList().get(i).getGroupName().equalsIgnoreCase(rkmList.getGroupName()))
                                {
                                    flag_to_check=true;
                                    grouppairValues.add(1,publicModuleResponseModel.getGroupList().get(i).getGroupName());
                                  //  groupKeyValues.add(1,publicModuleResponseModel.getGroupList().get(i).getGroup_id());
                                }
                                else {
                                    grouppairValues.add(publicModuleResponseModel.getGroupList().get(i).getGroupName());
                                   // groupKeyValues.add(publicModuleResponseModel.getGroupList().get(i).getGroup_id());
                                }
                            }
                            else {
                                grouppairValues.add(publicModuleResponseModel.getGroupList().get(i).getGroupName());
                               // groupKeyValues.add(publicModuleResponseModel.getGroupList().get(i).getGroup_id());
                            }
                            i++;
                        }
                        groupNameAdapter.notifyDataSetChanged();
                        if(flag_to_check)
                        {
                            spinner_group.setSelection(1);
                        }
                    }


                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        */
/*remaing Section *//*

        else if (callType == CallType.GET_SECTION_LIST && response!=null) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("response_code")) {
                    if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                        Util.showLongToast(PublicMouleSuperAdminActivity.this, "No data available");

                    } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                        Gson gson = new Gson();
                        Type type = new TypeToken<PublicModuleResponseModel>() {
                        }.getType();
                        PublicModuleResponseModel publicModuleResponseModel = gson.fromJson(response, type);
                        int i = 0;
                        boolean flag_to_check=false;
                        sectionpairValues.clear();
                        sectionpairValues.add("");
                        while (i < publicModuleResponseModel.getSectionList().size()) {
                            if(!fisrtTimeFlag && rkmList!=null) {
                                if(publicModuleResponseModel.getSectionList().get(i).getSectionName().equalsIgnoreCase(rkmList.getSection_name()))
                                {
                                    flag_to_check=true;
                                    sectionpairValues.add(1,publicModuleResponseModel.getSectionList().get(i).getSectionName());
                                }
                                else
                                {
                                    sectionpairValues.add(publicModuleResponseModel.getSectionList().get(i).getSectionName());
                                }
                            }
                            else
                            {
                                sectionpairValues.add(publicModuleResponseModel.getSectionList().get(i).getSectionName());
                            }

                            i++;
                        }
                        sectionNameAdapter.notifyDataSetChanged();

                        spinner_section.setSelection(1);



                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(callType == callType.GET_DVISION_LIST)
        {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("response_code")) {
                    if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                        Util.showLongToast(PublicMouleSuperAdminActivity.this, "No data available");

                    } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {

                        divisionpairValues.clear();
                        divisionpairValues.add("");
                        Gson gson = new Gson();
                        Type type = new TypeToken<PublicModuleResponseModel>() {
                        }.getType();
                        PublicModuleResponseModel publicModuleResponseModel = gson.fromJson(response, type);
                        int i = 0;
                        boolean flag_to_check=false;
                        while (i < publicModuleResponseModel.getDivisionList().size()) {
                            if(!fisrtTimeFlag && rkmList!=null) {
                                if(publicModuleResponseModel.getDivisionList().get(i).getDivision().equalsIgnoreCase(rkmList.getDivision()))
                                {
                                    flag_to_check=true;
                                    divisionpairValues.add(1,publicModuleResponseModel.getDivisionList().get(i).getDivision());
                                }
                                else
                                {
                                    divisionpairValues.add(publicModuleResponseModel.getDivisionList().get(i).getDivision());
                                }
                            }
                            else
                            {
                                divisionpairValues.add(publicModuleResponseModel.getDivisionList().get(i).getDivision());
                            }

                            i++;
                        }
                        divisionNameAdapter.notifyDataSetChanged();
                        if(flag_to_check)
                        {
                            spinner_division.setSelection(1);
                        }

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }



        }
        else if (callType == CallType.GET_STATE_LIST) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("response_code")) {
                    if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                        Util.showLongToast(PublicMouleSuperAdminActivity.this, "No data available");

                    } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                        stateKeyValues.clear();
                        statepairValues.clear();
                        stateKeyValues.add("");
                        statepairValues.add("");
                        Gson gson = new Gson();
                        Type type = new TypeToken<PublicModuleResponseModel>() {
                        }.getType();
                        PublicModuleResponseModel publicModuleResponseModel = gson.fromJson(response, type);
                        int i = 0;
                        boolean flag_to_check=false;
                        while (i < publicModuleResponseModel.getStateList().size()) {
                            if(!fisrtTimeFlag && rkmList!=null) {
                                if(rkmList.getState().equalsIgnoreCase(publicModuleResponseModel.getStateList().get(i).getStateName()))
                                {
                                    flag_to_check=true;
                                    statepairValues.add(1,publicModuleResponseModel.getStateList().get(i).getStateName());
                                    stateKeyValues.add(1,publicModuleResponseModel.getStateList().get(i).getStateId());
                                }
                                else {
                                    statepairValues.add(publicModuleResponseModel.getStateList().get(i).getStateName());
                                    stateKeyValues.add(publicModuleResponseModel.getStateList().get(i).getStateId());
                                }
                            }
                            else {
                                statepairValues.add(publicModuleResponseModel.getStateList().get(i).getStateName());
                                stateKeyValues.add(publicModuleResponseModel.getStateList().get(i).getStateId());
                            }
                            i++;
                        }
                        stateNameAdapter.notifyDataSetChanged();
                        if(flag_to_check)
                        {
                            spinner_state.setSelection(1);
                        }

                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            firsTimeFlagForDialog=true;
        }
        else if (callType == CallType.GET_ZONE_LIST) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("response_code")) {
                    if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                        Util.showLongToast(PublicMouleSuperAdminActivity.this, "No data available");

                    } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {

                        zoneepairValues.clear();
                        zoneepairValues.add("");
                        Gson gson = new Gson();
                        Type type = new TypeToken<PublicModuleResponseModel>() {
                        }.getType();
                        PublicModuleResponseModel publicModuleResponseModel = gson.fromJson(response, type);
                        int i = 0;
                        boolean flag_to_check=false;
                        while (i < publicModuleResponseModel.getZoneList().size()) {
                            if(!fisrtTimeFlag && rkmList!=null) {
                                if(publicModuleResponseModel.getZoneList().get(i).getZone().equalsIgnoreCase(rkmList.getZone()))
                                {
                                    flag_to_check=true;
                                    zoneepairValues.add(1,publicModuleResponseModel.getZoneList().get(i).getZone());
                                }
                                else {
                                    zoneepairValues.add(publicModuleResponseModel.getZoneList().get(i).getZone());
                                }
                            }
                            else {
                                zoneepairValues.add(publicModuleResponseModel.getZoneList().get(i).getZone());
                            }

                            i++;
                        }
                        zoneNameAdapter.notifyDataSetChanged();
                        if(flag_to_check)
                        {
                            spinner_zone.setSelection(1);
                        }
                        if(!fisrtTimeFlag && rkmList!=null)
                        {

                            if(rkmList.getRkm()!=null)
                            {
                                edit_rkm.setText(rkmList.getRkm());
                            }
                        }

                        fisrtTimeFlag=true;
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if (callType == CallType.SET_RKM) {
            try {
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("response_code")) {
                    if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                        Util.showLongToast(PublicMouleSuperAdminActivity.this, jsonObject.getString("message"));

                    } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                        Util.showLongToast(PublicMouleSuperAdminActivity.this, jsonObject.getString("message"));
                        edit_rkm.setText("");
                        finish();
                    }

                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        else if(callType==CallType.UPDATE_RKM)
        {
            Gson gson=new Gson();
            Result response1=gson.fromJson(response,Result.class);

            if(response1!=null)
            {
                if(response1.getResponseCode()!=null)
                {
                    if(response1.getResponseCode()==1)
                    {
                        //  Log.e("String", "value");
                        Toast.makeText(PublicMouleSuperAdminActivity.this, response1.getMessage(), Toast.LENGTH_LONG).show();
                        finish();
                    }

                }
                else {
                    Toast.makeText(PublicMouleSuperAdminActivity.this,"RKM not updated successfully",Toast.LENGTH_LONG).show();
                }
            }
            else {
                Toast.makeText(PublicMouleSuperAdminActivity.this,"RKM not updated successfully",Toast.LENGTH_LONG).show();
            }
        }
    }
}
*/
