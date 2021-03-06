package alina.com.rms.activities.superAdminActivities;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import alina.com.rms.R;
import alina.com.rms.adaptor.CustomAdapter;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.model.GetWiringDatum;
import alina.com.rms.model.LoginResultPojo;
import alina.com.rms.model.PublicModuleResponseModel;
import alina.com.rms.model.Result;
import alina.com.rms.util.CallType;
import alina.com.rms.util.CameraAndGalleryUtil;
import alina.com.rms.util.LocationTrack;
import alina.com.rms.util.LoginDB;
import alina.com.rms.util.Util;

public class SuperAdminWiringActivity extends SuperAdminBaseActivity implements AsyncCompleteListner,AdapterView.OnItemSelectedListener {

    List<String> projpairValues = new ArrayList<String>();
    List<String> projKeyValues = new ArrayList<String>();

    List<String> grouppairValues = new ArrayList<String>();
    List<String> groupKeyValues = new ArrayList<String>();

    List<String> sectionpairValues = new ArrayList<String>();
    List<String> sectionKeyValues = new ArrayList<String>();

    List<String> stationpairValues = new ArrayList<String>();
    List<String> stationKeyValues = new ArrayList<String>();

    private TextView dateTextView;
    private LinearLayout  date_linear,prj_linear, group_linear,section_linear,station_linear,linear_other;

    private List<LoginResultPojo> loginResultPojo;
    private LoginResultPojo loginResultPojo1;
    private String login_response;
    private CustomAdapter  projNameAdapter, groupNameAdapter, sectionNameAdapter,stationNameAdapter;
    private Button submitBtn;
    private Spinner spinner_project, spinner_group, spinner_section,spinner_station;
    public EditText edit_station_km,edit_no_foundation,edit_progress_entry;

    private String group_name = "", group_id = "", head_name = "", head_id = "", section_name = "", section_id = "",
            state_name = "", zone_name = "", rkm = "", division = "",station_id="";


    private boolean gps_enabled,network_enabled;

    private LocationTrack locationTrack;

    private CameraAndGalleryUtil cameraAndGalleryUtil;
    private File output1;


    private LatLng l1;
    double longitude = 0;
    double latitude = 0;
    LocationManager lm;

    private ImageView img_view;
    private List<GetWiringDatum> getWiringData=new ArrayList<GetWiringDatum>();

    List<String> orgnaizationpairValues = new ArrayList<String>();
    List<String> orgnaizationKeyValues = new ArrayList<String>();
    private Spinner spinner_org;
    private CustomAdapter  orgNameAdapter;
    private LinearLayout  org_linear;
    private String org_id="",org_name = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_super_admin_foundation);

        login_response = LoginDB.getLoginResponseAsJSON(this);
        Gson gson = new Gson();
        Type type = new TypeToken<List<LoginResultPojo>>() {
        }.getType();
        loginResultPojo = gson.fromJson(login_response, type);
        loginResultPojo1 = loginResultPojo.get(0);

        headingText.setText("Add Wiring Progress");




        if(locationTrack==null)
        {
            locationTrack = new LocationTrack(this);
            // setLocation();
        }
        addView();
        /*getProjectList();*/
        getOrgnaizationList();
    }
    @Override
    public void addView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View myView = factory.inflate(R.layout.activity_super_admin_wiring, null);
        setView(myView);

        date_linear = (LinearLayout) findViewById(R.id.date_linear);
        prj_linear = (LinearLayout) findViewById(R.id.prj_linear);
        group_linear = (LinearLayout) findViewById(R.id.group_linear);
        section_linear = (LinearLayout) findViewById(R.id.section_linear);
        station_linear = (LinearLayout) findViewById(R.id.station_linear);
        linear_other =(LinearLayout) findViewById(R.id.linear_other);


        img_view=(ImageView)findViewById(R.id.img_view);

        dateTextView = (TextView) findViewById(R.id.txt_date);
        spinner_project = (Spinner) findViewById(R.id.spinner_project);
        spinner_group = (Spinner) findViewById(R.id.spinner_group);
        spinner_section = (Spinner) findViewById(R.id.spinner_section);
        spinner_station =(Spinner) findViewById(R.id.spinner_station);
        edit_station_km=(EditText)findViewById(R.id.edit_station_km);
        edit_no_foundation=(EditText)findViewById(R.id.edit_no_foundation);
        edit_progress_entry=(EditText)findViewById(R.id.edit_progress_entry);

/*        prj_linear=(LinearLayout)findViewById(R.id.prj_linear);
        group_linear=(LinearLayout)findViewById(R.id.group_linear);*/
        station_linear.setVisibility(View.GONE);
        linear_other.setVisibility(View.GONE);

        submitBtn = (Button) findViewById(R.id.submitBtn);
        spinner_project.setOnItemSelectedListener(this);
        spinner_group.setOnItemSelectedListener(this);
        spinner_section.setOnItemSelectedListener(this);
        spinner_station.setOnItemSelectedListener(this);

        projNameAdapter = new CustomAdapter(this, projpairValues);
        spinner_project.setAdapter(projNameAdapter);

        groupNameAdapter = new CustomAdapter(this, grouppairValues);
        spinner_group.setAdapter(groupNameAdapter);

        sectionNameAdapter = new CustomAdapter(this, sectionpairValues);
        spinner_section.setAdapter(sectionNameAdapter);

        stationNameAdapter = new CustomAdapter(this, stationpairValues);
        spinner_station.setAdapter(stationNameAdapter);

        img_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ImageView img=(ImageView)v;
                Bitmap bitmap = ((BitmapDrawable)img.getDrawable()).getBitmap();
                bitmap.setHasAlpha(true);
                selectImage(bitmap);
            }
        });

        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nextStepToFoundation();
            }
        });

        date_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(dateTextView, SuperAdminWiringActivity.this);
            }
        });


        org_linear = (LinearLayout) findViewById(R.id.org_linear);
        spinner_org=(Spinner)findViewById(R.id.spinner_org);
        spinner_org.setOnItemSelectedListener(this);
        prj_linear.setVisibility(View.GONE);
        group_linear.setVisibility(View.GONE);
        section_linear.setVisibility(View.GONE);
        orgNameAdapter = new CustomAdapter(this, orgnaizationpairValues);
        spinner_org.setAdapter(orgNameAdapter);

    }



    public void openDatePickerDialog(final TextView editText, SuperAdminWiringActivity context) {
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

                        editText.setText(year + "-" + month + "-"
                                + date);
                        String sdate = editText.getText().toString();
                        boolean b = true;
                        // prj_linear.setVisibility(View.VISIBLE);
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
        if (parent.getId() == R.id.spinner_project) {
            if (position > 0) {
                group_linear.setVisibility(View.VISIBLE);
                head_id = projKeyValues.get(position);
                head_name = projpairValues.get(position);
                spinner_station.setSelection(0);
                spinner_section.setSelection(0);
                spinner_group.setSelection(0);
                getGroupListFromServer();
            } else {
                group_linear.setVisibility(View.GONE);

            }
            station_linear.setVisibility(View.GONE);
            linear_other.setVisibility(View.GONE);
            section_linear.setVisibility(View.GONE);
            section_linear.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);
        } else if (parent.getId() == R.id.spinner_group) {
            if (position > 0) {
                group_name = grouppairValues.get(position);
                group_id = groupKeyValues.get(position);
                spinner_station.setSelection(0);
                spinner_section.setSelection(0);
                //group_id = groupKeyValues.get(position);
                getSectionListFromServer();
                section_linear.setVisibility(View.VISIBLE);
                //spinner_section.setSelection(1);
            } else {
                section_linear.setVisibility(View.GONE);

            }
            station_linear.setVisibility(View.GONE);
            linear_other.setVisibility(View.GONE);

            submitBtn.setVisibility(View.GONE);
        } else if (parent.getId() == R.id.spinner_section) {
            if (position > 0) {
                //division_linear.setVisibility(View.VISIBLE);
                spinner_station.setSelection(0);
                section_name = sectionpairValues.get(position);
                section_id = sectionKeyValues.get(position);
                submitBtn.setVisibility(View.VISIBLE);
                station_linear.setVisibility(View.VISIBLE);
                linear_other.setVisibility(View.GONE);
                //getDivisionListFromServer();
                getSTationListFromServer();

            }
            else {
                submitBtn.setVisibility(View.GONE);
                station_linear.setVisibility(View.GONE);
                linear_other.setVisibility(View.GONE);

            }}
        else if(parent.getId()==R.id.spinner_station)
        {
            if (position > 0) {
                station_id=stationKeyValues.get(position);
                submitBtn.setVisibility(View.VISIBLE);
                linear_other.setVisibility(View.VISIBLE);
                hitToServer();
            }

        }
        else if(parent.getId() == R.id.spinner_org)
        {
            org_name=orgnaizationpairValues.get(position);
            org_id=orgnaizationKeyValues.get(position);
            spinner_station.setSelection(0);
            spinner_section.setSelection(0);
            spinner_group.setSelection(0);
            spinner_project.setSelection(0);
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
            station_linear.setVisibility(View.GONE);
            linear_other.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);
            submitBtn.setVisibility(View.GONE);
            station_linear.setVisibility(View.GONE);
            linear_other.setVisibility(View.GONE);
        }

    }
    public void getOrgnaizationList() {
        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(this, this, CallType.GET_ORGNAIZATION_LIST, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getOrgnaization();
    }

    private void hitToServer()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(SuperAdminWiringActivity.this,this,
                CallType.GET_FOUNDATION_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getWiringProgrssDataList(station_id);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void getProjectList() {
        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(SuperAdminWiringActivity.this, this, CallType.GET_PROJECT_LIST, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getProjectList();
    }

    private void getGroupListFromServer() {
        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(SuperAdminWiringActivity.this, this, CallType.GET_GROUP_USER, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getGroupListForPublicUser(head_id);
    }

    private void getSectionListFromServer() {
        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(SuperAdminWiringActivity.this, this, CallType.GET_SECTION_LIST, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getSectionListForPublicUser(group_id,org_id);
    }
    private void getSTationListFromServer() {
        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(SuperAdminWiringActivity.this,
                this, CallType.GET_STATION_LIST, "", true);
        asyncController.setProgressDialoug(false);
        asyncController.getStationList(section_id,"");
    }

    private void nextStepToFoundation()
    {

        if(section_id.trim().isEmpty() || section_id.trim().equalsIgnoreCase(""))
        {
            Util.showLongToast(getApplicationContext(),"Please select section");
            spinner_section.requestFocus();
            return;
        }
        if(!Util.chechEditTextValue(edit_progress_entry))
        {
            Util.showLongToast(getApplicationContext(),"Please fill progress");
            return;

        }
        if(!Util.chechTextValue(dateTextView))
        {
            Util.showLongToast(getApplicationContext(),"Please select Date");
            return;
        }

        if(latitude == 0 || longitude ==0)
        {
            Util.showLongToast(getApplicationContext(),"Please Enable GPS and try again");
            setLocation();
            return;
        }
        if(!Util.checkVAlue(edit_no_foundation,edit_progress_entry))
        {
            Util.showLongToast(getApplicationContext(),"Entry wiring can not be greater then given wiring");
            return;
        }

        getWiringData.get(0).setWiring_prog_no(edit_progress_entry.getText().toString());

        submitToserver();
       /* Intent intent=new Intent(PublicModuleWiringActivity.this,PublicModuleNewEntryFoundationList.class);
        intent.putExtra("section_id",section_id);
        intent.putExtra("date",dateTextView.getText().toString());
        startActivity(intent);*/

    }

    private void submitToserver() {

        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(this,this,
                CallType.SAVE_FOUNDATION_ITEM,"",true);
        // asyncController.setProgressDialoug(true);
        //img_view.setBackgroundColor(getResources().getColor(R.color.whiteColor));
        Bitmap bitmap = ((BitmapDrawable)img_view.getDrawable()).getBitmap();
        getWiringData.get(0).setBitmap(bitmap);
        getWiringData.get(0).setLat(""+latitude);
        getWiringData.get(0).setLng(""+longitude);
        asyncController.saveProfileAccount2(getWiringData,dateTextView.getText().toString(),"");

    }



    @Override
    public void asyncCompleteListner(String response, CallType callType) {

        setProgressDialoug(false);

        if (response != null) {
            if (callType == CallType.GET_PROJECT_LIST) {
                Gson gson = new Gson();
                Result response1 = gson.fromJson(response, Result.class);

                if (response1 != null) {

                    if (response1.getProjectList() != null) {
                        projpairValues.clear();
                        projKeyValues.clear();
                        projpairValues.add("");
                        projKeyValues.add("");
                        Log.e("String", "value");
                        for (int i = 0; i < response1.getProjectList().size(); i++) {

                            projpairValues.add(response1.getProjectList().get(i).getProjName());
                            projKeyValues.add(response1.getProjectList().get(i).getProjectId());


                        }


                        projNameAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                    }


                }
            }
            else if (callType == CallType.GET_ORGNAIZATION_LIST) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(this, "No data available");

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
                            spinner_org.setSelection(0);


                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            else if (callType == CallType.GET_GROUP_USER) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(SuperAdminWiringActivity.this, "No data available");

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
                            while (i < publicModuleResponseModel.getGroupList().size()) {
                                grouppairValues.add(publicModuleResponseModel.getGroupList().get(i).getGroupName());
                                groupKeyValues.add(publicModuleResponseModel.getGroupList().get(i).getGroup_id());
                                i++;
                            }

                        }
                        groupNameAdapter.notifyDataSetChanged();

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
                            Util.showLongToast(SuperAdminWiringActivity.this, "No data available");

                        } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                            Gson gson = new Gson();
                            Type type = new TypeToken<PublicModuleResponseModel>() {
                            }.getType();
                            PublicModuleResponseModel publicModuleResponseModel = gson.fromJson(response, type);
                            int i = 0;
                            sectionpairValues.clear();
                            sectionKeyValues.clear();
                            sectionpairValues.add("");
                            sectionKeyValues.add("");
                            while (i < publicModuleResponseModel.getSectionList().size()) {

                                sectionpairValues.add(publicModuleResponseModel.getSectionList().get(i).getSectionName());
                                sectionKeyValues.add(publicModuleResponseModel.getSectionList().get(i).getSection_id());


                                i++;
                            }
                            sectionNameAdapter.notifyDataSetChanged();


                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(callType == CallType.GET_STATION_LIST)
            {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(SuperAdminWiringActivity.this, jsonObject.getString("message"));

                        } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {

                            Gson gson = new Gson();
                            Type type = new TypeToken<PublicModuleResponseModel>() {
                            }.getType();
                            PublicModuleResponseModel publicModuleResponseModel = gson.fromJson(response, type);
                            int i = 0;
                            stationpairValues.clear();
                            stationKeyValues.clear();
                            stationpairValues.add("");
                            stationKeyValues.add("");
                            while (i < publicModuleResponseModel.getStationList().size()) {

                                stationpairValues.add(publicModuleResponseModel.getStationList().get(i).getStationName());
                                stationKeyValues.add(publicModuleResponseModel.getStationList().get(i).getStationId());


                                i++;
                            }
                            stationNameAdapter.notifyDataSetChanged();

                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            if(callType==CallType.GET_FOUNDATION_LIST)
            {

                Gson gson=new Gson();
                Result response1=gson.fromJson(response,Result.class);

                if(response1!=null)
                {
                    if(response1.getGetWiringData()!=null)
                    {
                        Log.e("String","value");
                        getWiringData.clear();
                        getWiringData.addAll(response1.getGetWiringData());
                        Log.e("value",""+getWiringData.size());
                        if(getWiringData.size()<1)
                        {

                            linear_other.setVisibility(View.GONE);
                            submitBtn.setVisibility(View.INVISIBLE);
                        }
                        else {
                            edit_station_km.setText(getWiringData.get(0).getStationKm());
                            edit_no_foundation.setText(getWiringData.get(0).getWiring());

                        }
                    }
                    else {
                        Toast.makeText(this,"no data found",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(this,"no data found",Toast.LENGTH_LONG).show();
                }
            }


            else if (callType == CallType.SET_RKM) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(SuperAdminWiringActivity.this, jsonObject.getString("message"));

                        } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                            Util.showLongToast(SuperAdminWiringActivity.this, jsonObject.getString("message"));

                            finish();
                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            else if(callType==CallType.SAVE_FOUNDATION_ITEM)
            {
                Gson gson=new Gson();
                Result response1=gson.fromJson(response,Result.class);
                if(response1!=null)
                {
                    if(response1.getResponseCode()==1)
                    {
                        Util.showLongToast(getApplicationContext(),response1.getMessage());
                        finish();
                    }
                    else {
                        Util.showLongToast(getApplicationContext(),response1.getMessage());
                    }
                }
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        setLocation();

    }


    private void setLocation()
    {
        // locationTrackSecond.canGetLocation()

        if (locationTrack.canGetLocation()) {
            longitude = locationTrack.getLongitude();
            latitude = locationTrack.getLatitude();
            if (latitude != 0 && longitude != 0) {
                l1=new LatLng(latitude,longitude);
                Log.e("service"+latitude,"provider"+longitude);
            }

            Log.e("service","provider");
        }


    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (locationTrack!=null)
        {
            locationTrack.stopListener();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CameraAndGalleryUtil.REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            //cameraAndGalleryUtil.setPic(profile_image1);
            String photoUri = cameraAndGalleryUtil.getPhotoURI();
            //deleteFile();
            output1 = new File(photoUri);
            Uri uri = Uri.fromFile(output1);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                output1 = new File(cameraAndGalleryUtil.saveGalaryImageOnLitkat(bitmap, "rms.jpg"));
                Bitmap bitmap2 = BitmapFactory.decodeFile(output1.getPath());
                img_view.setImageBitmap(bitmap2);

                cameraAndGalleryUtil.deleteFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void selectImage(final Bitmap bm) {
        final CharSequence[] items = { "Take Photo from camera","View Image","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item==0) {
                    if(cameraAndGalleryUtil ==null)
                    {
                        cameraAndGalleryUtil =new CameraAndGalleryUtil(SuperAdminWiringActivity.this);
                    }
                    cameraAndGalleryUtil.dispatchTakePictureIntent("rms.jpg");

                }
                else if (item==1) {

                    showImageDialog(bm);
                }
                else if (item==2) {
                    dialog.dismiss();

                }
            }
        });
        AlertDialog dialog= builder.create();
        //dialog.getWindow().setLayout(800,600);
        dialog.show();


    }
    public void showImageDialog(Bitmap showDialog)
    {
        final Dialog dialog=new Dialog(this);
        View view=getLayoutInflater().inflate(R.layout.image_dialog,null,false);
        dialog.setContentView(view);
        ImageView dialogImageView=(ImageView) dialog.findViewById(R.id.dialogImageView);
        dialogImageView.setImageBitmap(showDialog);
        ImageView close_btn=(ImageView)dialog.findViewById(R.id.close_btn);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        Window dialogWindow = dialog.getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        dialogWindow.setGravity(Gravity.CENTER);
        dialogWindow.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        int[] width_height=Util.getDisplayHeightAndWidth(this);
        lp.width = width_height[0]-50; // Width
        lp.height = width_height[1]-50; // Heigh
        dialogWindow.setAttributes(lp);
        dialog.show();


    }
}