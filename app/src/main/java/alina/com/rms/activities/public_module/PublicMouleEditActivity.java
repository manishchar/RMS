package alina.com.rms.activities.public_module;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import java.util.List;

import alina.com.rms.R;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.model.CrsList;
import alina.com.rms.model.LoginResultPojo;
import alina.com.rms.model.Result;
import alina.com.rms.model.RkmList;
import alina.com.rms.util.CallType;
import alina.com.rms.util.LoginDB;
import alina.com.rms.util.Util;

public class PublicMouleEditActivity extends PublicModuleBaseActivity implements AsyncCompleteListner{


    List<RkmList> rkmLists = new ArrayList<RkmList>();
    private boolean first_time;
    private String[] month_array = {"April", "May", "June", "July", "August"
            , "September", "October", "November", "December", "January", "February", "March"};
    private String month_name = "April";
    private String group_name = "", group_id = "", head_name = "", head_id = "", org_name = "", section_name = "", section_id = "", state_name = "", zone_name = "", rkm = "", division = "";
    private boolean fisrtTimeFlag;
    private EditText edit_rkm, targetEditText, divisionEditText, zoneEditText, stateEditText, crsEditText;
    private Button submitBtn;
    private boolean firsTimeFlagForDialog;
    private TextView dateTextView;
    private LinearLayout linear_txt_field, date_linear, org_linear, prj_linear, group_linear, section_linear, division_linear, state_linear, zone_linear;

    private List<LoginResultPojo> loginResultPojo;
    private LoginResultPojo loginResultPojo1;
    private String login_response;
    private String crs_id = "";
    private CrsList crsList;
    //private RkmList rkmList;
    private EditText txt_org,txt_proj,txt_group,txt_section;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        login_response = LoginDB.getLoginResponseAsJSON(PublicMouleEditActivity.this);
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
            getCRSList();
        }


    }

    @Override
    public void addView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View myView = factory.inflate(R.layout.activity_edit_public_moule, null);
        setView(myView);
        date_linear = (LinearLayout) findViewById(R.id.date_linear);
        org_linear = (LinearLayout) findViewById(R.id.org_linear);
       // org_linear.setVisibility(View.VISIBLE);

        group_linear = (LinearLayout) findViewById(R.id.group_linear);
        section_linear = (LinearLayout) findViewById(R.id.section_linear);
        division_linear = (LinearLayout) findViewById(R.id.division_linear);
        state_linear = (LinearLayout) findViewById(R.id.state_linear);
        zone_linear = (LinearLayout) findViewById(R.id.zone_linear);
        linear_txt_field = (LinearLayout) findViewById(R.id.linear_txt_field);
     //   linear_txt_field.setVisibility(View.GONE);

        dateTextView = (TextView) findViewById(R.id.txt_date);
        edit_rkm = (EditText) findViewById(R.id.rkmEditText);
        targetEditText = (EditText) findViewById(R.id.targetEditText);
        divisionEditText = (EditText) findViewById(R.id.divisionEditText);
        zoneEditText = (EditText) findViewById(R.id.zoneEditText);
        stateEditText = (EditText) findViewById(R.id.stateEditText);
        crsEditText = (EditText) findViewById(R.id.crsEditText);
        prj_linear = (LinearLayout) findViewById(R.id.prj_linear);

        txt_org = (EditText) findViewById(R.id.txt_org);
        txt_proj = (EditText) findViewById(R.id.txt_proj);
        txt_group = (EditText) findViewById(R.id.txt_group);
        txt_section = (EditText) findViewById(R.id.txt_section);



/*        prj_linear=(LinearLayout)findViewById(R.id.prj_linear);
        group_linear=(LinearLayout)findViewById(R.id.group_linear);*/
        submitBtn = (Button) findViewById(R.id.submitBtn);



        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  if(fisrtTimeFlag && crsList!=null) {
                    updateRkmValue();
                }
                else
                {
                    saveRkmValue();
                }*/
                saveRkmValue();
            }
        });

        if (crsList != null && !crsList.getMonth().equalsIgnoreCase("0000-00-00")) {
            dateTextView.setText(crsList.getMonth());
           // org_linear.setVisibility(View.GONE);
        }
        date_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDatePickerDialog(dateTextView, PublicMouleEditActivity.this);
            }
        });
    }

    public void openDatePickerDialog(final TextView editText, PublicMouleEditActivity context) {
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
                        org_linear.setVisibility(View.VISIBLE);

                        //org_linear.setVisibility(View.GONE);
                        // context.addDate_tolist(position,sdate,b);

                    }
                }, mYear, mMonth, mDay);
        dpd.setTitle("Select Date");
      //  dpd.getDatePicker().setMaxDate(System.currentTimeMillis());
        dpd.show();
    }

    public void getCRSList() {
        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(PublicMouleEditActivity.this, this, CallType.GET_CRS_LIST, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getCRSList(crs_id);
    }




    public void saveRkmValue() {
        String date = dateTextView.getText().toString().trim();
        if (date.equalsIgnoreCase("Select Date")) {
            Util.showLongToast(this, "Please select date");
            return;
        } else if (!Util.chechTextValue(crsEditText)) {
            Util.showLongToast(this, "Please filled valid value in RKM");
            return;
        }
        rkm = crsEditText.getText().toString();

        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(PublicMouleEditActivity.this, this, CallType.SET_RKM, "", true);
        asyncController.setProgressDialoug(true);
        //asyncController.saveRKM(rkmLists.get(0).getRkmId(),rkm,date);
        if (crsList != null) {
            asyncController.saveRKM(rkmLists.get(0).getRkmId(), rkm, date, crs_id);
        } else {
            asyncController.saveRKM(rkmLists.get(0).getRkmId(), rkm, date, "");
        }

    }

    public void updateRkmValue() {
        String date = dateTextView.getText().toString().trim();
        if (date.equalsIgnoreCase("Select Date")) {
            Util.showLongToast(this, "Please select date");
            return;
        } else if (!Util.chechTextValue(edit_rkm)) {
            Util.showLongToast(this, "Please filled valid value in RKM");
            return;
        }

        rkm = edit_rkm.getText().toString();

        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(PublicMouleEditActivity.this, this, CallType.UPDATE_RKM, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.updateRKMForPublicUser(head_id, head_name, group_name, section_name, zone_name, state_name, org_name, rkm, dateTextView.getText().toString(), division);

    }


    private void getRKMFromServer() {
        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(this, this, CallType.GET_RKM, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getRKM(section_id);
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
                            Util.showLongToast(PublicMouleEditActivity.this, "No data available");

                        } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                            Gson gson = new Gson();
                            Result response1 = gson.fromJson(response, Result.class);
                            if(response1.getCrsList().size()>0) {
                                crsList = response1.getCrsList().get(0);
                                dateTextView.setText(crsList.getMonth());
                                crsEditText.setText(crsList.getCrs());

                                txt_org.setText(crsList.getOrgName());
                                if(crsList.getProjName()!=null)
                                {
                                    txt_proj.setText(crsList.getProjName());
                                }
                                else {
                                    txt_proj.setText("");
                                }

                                if(crsList.getGroupName()!=null)
                                {
                                    txt_group.setText(crsList.getGroupName());
                                }
                                else {
                                    txt_group.setText("");
                                }
                                txt_section.setText(crsList.getSectionName());


                                if(crsList.getOrgName().equalsIgnoreCase("CORE"))
                                {
                                    prj_linear.setVisibility(View.VISIBLE);
                                    group_linear.setVisibility(View.VISIBLE);
                                }
                                else {
                                    section_linear.setVisibility(View.VISIBLE);
                                    prj_linear.setVisibility(View.GONE);
                                    group_linear.setVisibility(View.GONE);
                                }
                                section_id=crsList.getSectionId();
                                getRKMFromServer();
                               // org_linear.setVisibility(View.VISIBLE);
                            }

                        }
                    }
                } catch (JSONException ex) {

                }

                //getOrgnaizationList();
            }  else if (callType == CallType.SET_RKM) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(PublicMouleEditActivity.this, jsonObject.getString("message"));

                        } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                            Util.showLongToast(PublicMouleEditActivity.this, jsonObject.getString("message"));
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
                            Toast.makeText(PublicMouleEditActivity.this, response1.getMessage(), Toast.LENGTH_LONG).show();
                            finish();
                        }

                    } else {
                        Toast.makeText(PublicMouleEditActivity.this, "RKM not updated successfully", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(PublicMouleEditActivity.this, "RKM not updated successfully", Toast.LENGTH_LONG).show();
                }
            } else if (callType == CallType.GET_RKM) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(PublicMouleEditActivity.this, "No data available");

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
