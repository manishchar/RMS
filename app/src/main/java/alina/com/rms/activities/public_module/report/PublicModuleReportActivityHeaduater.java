package alina.com.rms.activities.public_module.report;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import alina.com.rms.R;
import alina.com.rms.activities.headquaterActivities.ProcessSSPActivity;
import alina.com.rms.activities.public_module.PublicModuleBaseActivity;
import alina.com.rms.adaptor.CustomAdapter;
import alina.com.rms.adaptor.HeadquaterServicesListAdapter;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.model.Grouplist;
import alina.com.rms.model.HeadQuaterGroupResponse;
import alina.com.rms.model.PublicModuleResponseModel;
import alina.com.rms.model.Response;
import alina.com.rms.model.Result;
import alina.com.rms.util.CallType;
import alina.com.rms.util.LoginDB;
import alina.com.rms.util.Util;

public class PublicModuleReportActivityHeaduater extends PublicModuleBaseActivity implements AdapterView.OnItemSelectedListener, AsyncCompleteListner {
    List<String> groupNames = new ArrayList<String>();
    List<String> sectionpairValues = new ArrayList<String>();
    List<String> sectionKeyValues = new ArrayList<String>();
    List<String> stationNames = new ArrayList<>();

    List<String> projpairValues = new ArrayList<String>();
    List<String> projKeyValues = new ArrayList<String>();

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private HeadquaterServicesListAdapter servicesListAdapter;
    private boolean intentFlag = false;
    private Response response;
    private List<Response> response1;
    private Spinner group_spinner, section_spinner;//item_spinner;/*select_type_spinner,target_type_spinner*/;
/*    private List<Sectionlist> sectionlist = new ArrayList<Sectionlist>();*/
    private String selected_group = "", selected_section = "", station_id = "";
    private CustomAdapter groupNameAdapter, sectionNameAdapter, stationNameAdapter,projNameAdapter;
    private List<Grouplist> getGroupList = new ArrayList<Grouplist>();
    private Button btn_ohe, btn_psi, btn_tr_line, btn_other;
    private int screen_pos;  //1 for simple report 2 for weekly report
    private boolean checkWebView = false;
    List<String> orgnaizationpairValues = new ArrayList<String>();
    List<String> orgnaizationKeyValues = new ArrayList<String>();
    LinearLayout section_linear;
    private Spinner station_spinner;
    LinearLayout station_linear, proj_linear, group_linear;
    List<String> stationpairValues = new ArrayList<String>();
    List<String> stationKeyValues = new ArrayList<String>();
    private String org_id="", org_name;
    String  head_id = "",
            head_name = "",
            group_name = "",
            group_id = "";
    private Spinner spinner_project;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey("web_View")) {
                checkWebView = bundle.getBoolean("web_View");
            }
            if (bundle.containsKey("screen_num")) {
                screen_pos = bundle.getInt("screen_num");
            }
        }


        Gson gson = new Gson();
        Type type = new TypeToken<List<Response>>() {
        }.getType();
        response1 = gson.fromJson(LoginDB.getLoginResponseAsJSON(this), type);
        response = response1.get(0);
        //Bundle bundle=getIntent().getExtras();
        headingText.setText("Report");


        addView();
        // initUI();
        initView();


    }


    @Override
    public void addView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View myView = factory.inflate(R.layout.activity_public_user_report, null);
        setView(myView);
    }


    private void initView() {
        group_spinner = (Spinner) findViewById(R.id.group_spinner);
        section_spinner = (Spinner) findViewById(R.id.section_spinner);
        spinner_project = (Spinner) findViewById(R.id.spinner_project);
        //  item_spinner=(Spinner)findViewById(R.id.item_spinner);

        proj_linear = (LinearLayout) findViewById(R.id.proj_linear);
        group_linear = (LinearLayout) findViewById(R.id.group_linear);
        btn_ohe = (Button) findViewById(R.id.btn_ohe);
        btn_psi = (Button) findViewById(R.id.btn_psi);
        btn_tr_line = (Button) findViewById(R.id.btn_tr_line);
        btn_other = (Button) findViewById(R.id.btn_other);
        section_linear = (LinearLayout) findViewById(R.id.section_linear);
        station_linear = (LinearLayout) findViewById(R.id.station_linear);
        station_spinner = (Spinner) findViewById(R.id.station_spinner);
        section_linear.setVisibility(View.GONE);
        station_linear.setVisibility(View.GONE);
        btn_tr_line.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnderConstuctionDialoug(true);
            }
        });
        btn_other.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUnderConstuctionDialoug(true);
            }
        });
        btn_ohe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(PublicModuleReportActivityHeaduater.this,
                        PubLicViewOheSspSpWebViewActivity.class);
                intent.putExtra("screen_name", "OHE Report");
                intent.putExtra("selected_group", selected_group);
                intent.putExtra("selected_section", selected_section);
                intent.putExtra("hadquater_id", response.getHeadquater());
                startActivity(intent);
            }
        });

        btn_psi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHeadQuater();
            }
        });

/*        btn_submit.setVisibility(View.GONE);*/
        group_spinner.setOnItemSelectedListener(this);
        section_spinner.setOnItemSelectedListener(this);
        station_spinner.setOnItemSelectedListener(this);
        spinner_project.setOnItemSelectedListener(this);
        //item_spinner.setOnItemSelectedListener(this);

        groupNameAdapter = new CustomAdapter(this, groupNames);
        sectionNameAdapter = new CustomAdapter(this, sectionpairValues);
        stationNameAdapter = new CustomAdapter(this, stationpairValues);
        projNameAdapter = new CustomAdapter(this, projpairValues);
        spinner_project.setAdapter(projNameAdapter);
       /* selectItemAdapter=new CustomAdapter(this,selectItemName);*/
        group_spinner.setAdapter(groupNameAdapter);
        section_spinner.setAdapter(sectionNameAdapter);
        station_spinner.setAdapter(stationNameAdapter);
        //item_spinner.setAdapter(selectItemAdapter);

        if (response.getName().equalsIgnoreCase("CORE")) {
            proj_linear.setVisibility(View.VISIBLE);
            getProjectList();

        } else {
            proj_linear.setVisibility(View.GONE);
            getOrgnaizationList();
        }

    }


    public void getOrgnaizationList() {
        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(this, this,
                CallType.GET_ORGNAIZATION_LIST, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getOrgnaization();
    }

    private void initUI() {
        mRecyclerView = (RecyclerView) findViewById(R.id.lvHomeScreenItems);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(2);
        mStaggeredLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
/*        servicesListAdapter = new HeadquaterServicesListAdapter(MainActivityHeaduater.this, this);
        mRecyclerView.setAdapter(servicesListAdapter);*/
        //
    }

    private void getGroupFromServer() {
        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(PublicModuleReportActivityHeaduater.this, this, CallType.GET_GROUP_USER, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getGroupList1(head_id, response.getUserId());
    }


    private void getSectionListFromServer(String org_id1) {
        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(this, this,
                CallType.GET_SECTION_LIST, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getSectionListForPublicUser("",org_id1);
    }

    private void getSectionListFromServerForGroup(String group_id1) {
        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(this, this,
                CallType.GET_SECTION_LIST, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getSectionListForPublicUser(group_id1,"");
    }


    private void getSTationListFromServer() {
        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(this,
                this, CallType.GET_STATION_LIST, "", true);
        asyncController.setProgressDialoug(false);
        asyncController.getNewStationList(selected_section);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        Log.e("Position", "" + position);
        if(parent.getId()==R.id.spinner_project)
        {
           if(position>0)
           {
               head_id = projKeyValues.get(position);
               group_linear.setVisibility(View.GONE);
               section_linear.setVisibility(View.GONE);
               getGroupFromServer();
           }
        }
        else if (parent.getId() == R.id.group_spinner) {
            if (checkWebView) {
                if (position == 0) {
                    selected_group = "";
                    selected_section = "";
                    section_linear.setVisibility(View.GONE);
                    btn_ohe.setVisibility(View.VISIBLE);
                    btn_psi.setVisibility(View.VISIBLE);
                    btn_tr_line.setVisibility(View.VISIBLE);
                    btn_other.setVisibility(View.VISIBLE);
                } else {
                    selected_group = getGroupList.get(position - 1).getId();
                    section_linear.setVisibility(View.GONE);
                   // getSectionListFromServerForGroup(selected_group);
                }
            } else {
                selected_group = getGroupList.get(position).getId();
            }

        } /*else if (parent.getId() == R.id.section_spinner) {
            // selected_section=headquaterlist.get(position).getId();
            selected_section = sectionlist.get(position).getId();

        }*/ else if (parent.getId() == R.id.section_spinner) {
            // selected_section=headquaterlist.get(position).getId();
            if (checkWebView) {

                selected_section = sectionKeyValues.get(position);

            }


        } else if (parent.getId() == R.id.spinner_station) {
 /*           if (position > 0) {*/
            station_id = stationKeyValues.get(position);
            ;
               /* submitBtn.setVisibility(View.VISIBLE);
                linear_other.setVisibility(View.VISIBLE);
                hitToServer();*/
            /*}*/

        } else if (parent.getId() == R.id.item_spinner) {
            //  item_selected=selectTypeName.get(position);
        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setTarget() {

    }

    private void achiveTarget() {

    }

    public void getOHETarget() {
        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(PublicModuleReportActivityHeaduater.this, this, CallType.GET_OHE, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getOHERequest();
    }


    public void addHeadQuater() {
        final Dialog dialog = new Dialog(PublicModuleReportActivityHeaduater.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //dialog.setCancelable(false);
        dialog.setContentView(R.layout.alert_psi_dialog);


        Button btn_tss = (Button) dialog.findViewById(R.id.btn_tss);
        Button btn_sp = (Button) dialog.findViewById(R.id.btn_sp);
        Button btn_ssp = (Button) dialog.findViewById(R.id.btn_ssp);
        btn_sp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                intent = new Intent(PublicModuleReportActivityHeaduater.this, PubLicViewOheSspSpWebViewActivity.class);
                intent.putExtra("screen_name", "SP");
                intent.putExtra("selected_group", selected_group);
                intent.putExtra("selected_section", selected_section);
                intent.putExtra("hadquater_id", response.getHeadquater());
                startActivity(intent);
                dialog.dismiss();
            }
        });

        btn_tss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
/*                    intent=new Intent(MainActivityHeaduater.this,HeaduaterTSSWebViewActivity.class);*/
                    intent = new Intent(PublicModuleReportActivityHeaduater.this, PublicViewTSSWebViewActivity.class);
                    intent.putExtra("screen_name", "OHE Report");
                intent.putExtra("selected_group", selected_group);
                intent.putExtra("selected_section", selected_section);
                intent.putExtra("hadquater_id", response.getHeadquater());
                startActivity(intent);
                dialog.dismiss();
            }
        });

        btn_ssp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (checkWebView) {
                    intent = new Intent(PublicModuleReportActivityHeaduater.this, PubLicViewOheSspSpWebViewActivity.class);
                    intent.putExtra("screen_name", "SSP");
                } else {
                    intent = new Intent(PublicModuleReportActivityHeaduater.this, ProcessSSPActivity.class);
                }
                intent.putExtra("selected_group", selected_group);
                intent.putExtra("selected_section", selected_section);
                intent.putExtra("hadquater_id", response.getHeadquater());
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    public void getProjectList() {
        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(this, this, CallType.GET_PROJECT_LIST, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getProjectList();
    }


    @Override
    public void asyncCompleteListner(String response, CallType callType) {
        setProgressDialoug(false);
        if (response != null) {

            if (callType == CallType.GET_ORGNAIZATION_LIST) {
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
                            boolean flag_to_check = false;
                            for (int i = 0; i < publicModuleResponseModel.getList().size(); i++) {
                                if (this.response.getName().equalsIgnoreCase(publicModuleResponseModel.getList().get(i).getOrgName())) {
                                    orgnaizationpairValues.add(publicModuleResponseModel.getList().get(i).getOrgName());
                                    orgnaizationKeyValues.add(publicModuleResponseModel.getList().get(i).getOrgId());
                                    org_id = publicModuleResponseModel.getList().get(i).getOrgId();
                                    org_name = publicModuleResponseModel.getList().get(i).getOrgName();
                                    Log.e("org_id",org_id);
                                    break;

                                }
                                //i++;
                            }
                            head_id = "";
                            head_name = "";
                            group_name = "";
                            group_id = "";
                            org_name = this.response.getName();
                            getSectionListFromServer(org_id);


                           /* if (flag_to_check) {
                                spinner_org.setSelection(1);
                            }*/
                        }


                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if (callType == CallType.GET_PROJECT_LIST) {
                Gson gson = new Gson();
                Result response1 = gson.fromJson(response, Result.class);

                if (response1 != null) {

                    if (response1.getProjectList() != null) {
                        projpairValues.clear();
                        projKeyValues.clear();
                        projpairValues.add("");
                        projKeyValues.add("");
                        for (int i = 0; i < response1.getProjectList().size(); i++) {

                            projpairValues.add(response1.getProjectList().get(i).getProjName());
                            projKeyValues.add(response1.getProjectList().get(i).getProjectId());


                        }
                        proj_linear.setVisibility(View.VISIBLE);
                        projNameAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                    }


                }
            }

            else if (callType == CallType.GET_GROUP_USER) {
                Gson gson = new Gson();

                HeadQuaterGroupResponse response1 = gson.fromJson(response, HeadQuaterGroupResponse.class);
                /*btn_submit.setVisibility(View.GONE);*/
                if (response1 != null) {
                    if (response1.getGrouplist() != null) {
                        Log.e("String", "value");
                        selected_group = "";
                        groupNames.clear();
                        getGroupList.clear();
                        getGroupList.addAll(response1.getGrouplist());
                        if (response1.getGrouplist().size() > 0) {
                            if (checkWebView) {
                                groupNames.add("all");
                            }
                            for (int i = 0; i < getGroupList.size(); i++) {
                                groupNames.add(getGroupList.get(i).getName());
                            }
                            groupNameAdapter.notifyDataSetChanged();
                        }
                        if (getGroupList.size() > 0) {
                            group_spinner.setSelection(0);
                        }
                        group_linear.setVisibility(View.VISIBLE);
                        Log.e("value", "" + groupNames.size());

                        //getOHETarget();
                    } else {
                        sectionpairValues.clear();
                        sectionKeyValues.clear();
                        Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                    }
                } else {

                    Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                }


            }     else if (callType == CallType.GET_SECTION_LIST && response != null) {
                try {
                    Log.e("testing", response);
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                            Util.showLongToast(this, "No data available");

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

                                    sectionpairValues.add(publicModuleResponseModel.getSectionList().get(i).getSectionName());
                                    sectionKeyValues.add(publicModuleResponseModel.getSectionList().get(i).getSection_id());


                                i++;
                            }
                            sectionNameAdapter.notifyDataSetChanged();
                            section_linear.setVisibility(View.VISIBLE);


                        }

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (callType == CallType.GET_STATION_LIST) {
                if (response1 != null) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.has("response_code")) {
                            if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {
                                Util.showLongToast(this, jsonObject.getString("message"));
                                stationKeyValues.clear();
                                stationpairValues.clear();
                                stationNameAdapter.notifyDataSetChanged();
                                station_linear.setVisibility(View.GONE);
                                btn_ohe.setVisibility(View.GONE);
                                btn_psi.setVisibility(View.GONE);
                                btn_tr_line.setVisibility(View.GONE);
                                btn_other.setVisibility(View.GONE);
                                Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();

                            } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {

                                Gson gson = new Gson();
                                Type type = new TypeToken<PublicModuleResponseModel>() {
                                }.getType();
                                PublicModuleResponseModel publicModuleResponseModel = gson.fromJson(response, type);
                                int i = 0;
                                stationpairValues.clear();
                                stationKeyValues.clear();
/*                            stationpairValues.add("");
                            stationKeyValues.add("");*/
                                while (i < publicModuleResponseModel.getStationlist().size()) {

                                    stationpairValues.add(publicModuleResponseModel.getStationlist().get(i).getName());
                                    stationKeyValues.add(publicModuleResponseModel.getStationlist().get(i).getId());


                                    i++;
                                }
                                stationNameAdapter.notifyDataSetChanged();
                                if (stationKeyValues.size() > 0) {
                                    station_id = stationKeyValues.get(0);
                                    station_spinner.setSelection(0);
                                    station_linear.setVisibility(View.VISIBLE);
                                    btn_ohe.setVisibility(View.VISIBLE);
                                    btn_psi.setVisibility(View.VISIBLE);
                                    station_linear.setVisibility(View.VISIBLE);
                                    btn_tr_line.setVisibility(View.VISIBLE);
                                    btn_other.setVisibility(View.VISIBLE);
                                }

                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    stationKeyValues.clear();
                    stationpairValues.clear();
                    stationNameAdapter.notifyDataSetChanged();
                    station_linear.setVisibility(View.GONE);
                    btn_ohe.setVisibility(View.GONE);
                    btn_psi.setVisibility(View.GONE);
                    btn_tr_line.setVisibility(View.GONE);
                    btn_other.setVisibility(View.GONE);
                    Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                }

            } else if (callType == CallType.GET_OHE) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {

                            Util.showLongToast(PublicModuleReportActivityHeaduater.this, "No data available");

                        } else if (Integer.parseInt(jsonObject.getString("response_code")) == 1) {
                           /* countryNames.clear();
                            keyValues.clear();*/
                            JSONObject jsonObject1 = new JSONObject(jsonObject.getString("response"));

                            Iterator<String> keys = jsonObject1.keys();
                            //  int i=0;
                            while (keys.hasNext()) {
                                try {
                                    String value = keys.next();
                                    // Log.e("key : ",value);//,"pair : "+jsonObject1.getString(""+keys.next()));
     /*                               keyValues.add(value);
                                    countryNames.add(String.valueOf(jsonObject1.get(value)));*/
                                    // Log.e("pair",String.valueOf(jsonObject1.get(value)));
                                    //i++;
                                } catch (Exception ex) {
                                    ex.printStackTrace();
                                }


                            }
                            //selectItemAdapter.notifyDataSetChanged();
                            /*if (countryNames.size() > 0) {
                                submitBtn.setClickable(true);
                            }*/
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }
    }

}
