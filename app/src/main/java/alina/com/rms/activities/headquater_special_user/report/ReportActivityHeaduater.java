package alina.com.rms.activities.headquater_special_user.report;

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
import alina.com.rms.activities.headquaterActivities.HeaduaterBaseActivity;
import alina.com.rms.activities.headquaterActivities.HeaduaterTSSWebViewActivity;
import alina.com.rms.activities.headquaterActivities.HeaduaterWebViewActivity;
import alina.com.rms.activities.headquaterActivities.ProcessHeadquaterActivity;
import alina.com.rms.activities.headquaterActivities.ProcessSPActivity;
import alina.com.rms.activities.headquaterActivities.ProcessSSPActivity;
import alina.com.rms.activities.headquaterActivities.ProcessTSSActivity;
import alina.com.rms.activities.headquater_special_user.HeadQuaterSpecialUserBaseActivity;
import alina.com.rms.activities.user_entry_section.EntryUserTSSWebViewActivity;
import alina.com.rms.activities.user_entry_section.EntryUserWebViewActivity;
import alina.com.rms.adaptor.CustomAdapter;
import alina.com.rms.adaptor.HeadquaterServicesListAdapter;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.model.Grouplist;
import alina.com.rms.model.HeadQuaterGroupResponse;
import alina.com.rms.model.PublicModuleResponseModel;
import alina.com.rms.model.Response;
import alina.com.rms.model.Result;
import alina.com.rms.model.Sectionlist;
import alina.com.rms.util.CallType;
import alina.com.rms.util.LoginDB;
import alina.com.rms.util.Util;

public class ReportActivityHeaduater extends HeadQuaterSpecialUserBaseActivity implements AdapterView.OnItemSelectedListener, AsyncCompleteListner {
    List<String> groupNames = new ArrayList<String>();
    List<String> sectionNames = new ArrayList<String>();
    List<String> stationNames = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private HeadquaterServicesListAdapter servicesListAdapter;
    private boolean intentFlag = false;
    private Response response;
    private List<Response> response1;
    private Spinner group_spinner, section_spinner;//item_spinner;/*select_type_spinner,target_type_spinner*/;
    private List<Sectionlist> sectionlist = new ArrayList<Sectionlist>();
    private String selected_group = "", selected_section = "",station_id="";
    private CustomAdapter groupNameAdapter, sectionNameAdapter, stationNameAdapter;
    private List<Grouplist> getGroupList = new ArrayList<Grouplist>();
    private Button btn_ohe, btn_psi, btn_tr_line, btn_other;
    private int screen_pos;  //1 for simple report 2 for weekly report
    private boolean checkWebView = false;

    LinearLayout section_linear;
    private Spinner station_spinner;
    LinearLayout station_linear;
    List<String> stationpairValues = new ArrayList<String>();
    List<String> stationKeyValues  = new ArrayList<String>();

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
        if (checkWebView) {
            if (screen_pos == 1) {
                headingText.setText("Report");
            } else {
                headingText.setText("Weekly Report");
            }
        } else {
            headingText.setText("Set Progress");
        }


        addView();
        // initUI();
        initView();


    }


    @Override
    public void addView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View myView = factory.inflate(R.layout.activity_headquater, null);
        setView(myView);
    }


    private void initView() {
        group_spinner = (Spinner) findViewById(R.id.group_spinner);
        section_spinner = (Spinner) findViewById(R.id.section_spinner);
        //  item_spinner=(Spinner)findViewById(R.id.item_spinner);


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
                if (checkWebView) {
                    //intent=new Intent(MainActivityHeaduater.this,HeaduaterWebViewActivity.class);
                    if (screen_pos == 1) {
                        intent = new Intent(ReportActivityHeaduater.this, HeaduaterWebViewActivity.class);
                    } else {
                        intent = new Intent(ReportActivityHeaduater.this, EntryUserWebViewActivity.class);
                    }
                    intent.putExtra("screen_name", "OHE Report");
                } else {
                    intent = new Intent(ReportActivityHeaduater.this, ProcessHeadquaterActivity.class);
                }
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
        //item_spinner.setOnItemSelectedListener(this);

        groupNameAdapter = new CustomAdapter(this, groupNames);
        sectionNameAdapter = new CustomAdapter(this, sectionNames);
        stationNameAdapter = new CustomAdapter(this, stationpairValues);
       /* selectItemAdapter=new CustomAdapter(this,selectItemName);*/
        group_spinner.setAdapter(groupNameAdapter);
        section_spinner.setAdapter(sectionNameAdapter);
        station_spinner.setAdapter(stationNameAdapter);
        //item_spinner.setAdapter(selectItemAdapter);

        getGroupFromServer();
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
        AsyncController asyncController = new AsyncController(ReportActivityHeaduater.this, this, CallType.GET_HQ_LIST, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getGroupList1(response.getHeadquater(), response.getUserId());
    }


    private void getSectionListFromServer(String selected_group1) {
        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(this, this, CallType.GET_SECTION_LIST, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getHeadquaterSectionList(response.getHeadquater(), selected_group1);

        // GET_USER_LIST
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
        if (parent.getId() == R.id.group_spinner) {
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
                    // getSectionListFromServer(selected_group);
                }
            } else {
                selected_group = getGroupList.get(position).getId();
                getSectionListFromServer(selected_group);
            }

        } /*else if (parent.getId() == R.id.section_spinner) {
            // selected_section=headquaterlist.get(position).getId();
            selected_section = sectionlist.get(position).getId();

        }*/
        else if(parent.getId()==R.id.section_spinner){
            // selected_section=headquaterlist.get(position).getId();
            if (checkWebView) {
                if(position==0)
                {
                    selected_section ="";
                }
                else {
                    selected_section = sectionlist.get(position).getId();
                }
            }
            else {
                selected_section = sectionlist.get(position).getId();
                getSTationListFromServer();
            }


        }
        else if(parent.getId()==R.id.spinner_station)
        {
 /*           if (position > 0) {*/
            station_id=stationKeyValues.get(position);;
               /* submitBtn.setVisibility(View.VISIBLE);
                linear_other.setVisibility(View.VISIBLE);
                hitToServer();*/
            /*}*/

        }

        else if (parent.getId() == R.id.item_spinner) {
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
        AsyncController asyncController = new AsyncController(ReportActivityHeaduater.this, this, CallType.GET_OHE, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getOHERequest();
    }


    public void addHeadQuater() {
        final Dialog dialog = new Dialog(ReportActivityHeaduater.this);
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
                if (checkWebView) {
                    if (screen_pos == 1) {
                        intent = new Intent(ReportActivityHeaduater.this, HeaduaterWebViewActivity.class);
                    } else {
                        intent = new Intent(ReportActivityHeaduater.this, EntryUserWebViewActivity.class);
                    }
                    intent.putExtra("screen_name", "SP");
                } else {
                    intent = new Intent(ReportActivityHeaduater.this, ProcessSPActivity.class);
                }
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
                if (checkWebView) {
/*                    intent=new Intent(MainActivityHeaduater.this,HeaduaterTSSWebViewActivity.class);*/
                    if (screen_pos == 1) {
                        intent = new Intent(ReportActivityHeaduater.this, HeaduaterTSSWebViewActivity.class);
                    } else {
                        intent = new Intent(ReportActivityHeaduater.this, EntryUserTSSWebViewActivity.class);
                    }
                    intent.putExtra("screen_name", "OHE Report");
                } else {
                    intent = new Intent(ReportActivityHeaduater.this, ProcessTSSActivity.class);
                }
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
                    if (screen_pos == 1) {
                        intent = new Intent(ReportActivityHeaduater.this, HeaduaterWebViewActivity.class);
                    } else {
                        intent = new Intent(ReportActivityHeaduater.this, EntryUserWebViewActivity.class);
                    }
                    intent.putExtra("screen_name", "SSP");
                } else {
                    intent = new Intent(ReportActivityHeaduater.this, ProcessSSPActivity.class);
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


    @Override
    public void asyncCompleteListner(String response, CallType callType) {
        setProgressDialoug(false);
        if (response != null) {
            if (callType == CallType.GET_HQ_LIST) {
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
                        Log.e("value", "" + groupNames.size());

                        //getOHETarget();
                    } else {
                        sectionNames.clear();
                        sectionlist.clear();
                        Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                    }
                } else {

                    Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                }


            } /*else if (callType == CallType.GET_SECTION_LIST) {
                Gson gson = new Gson();
                Result response1 = gson.fromJson(response, Result.class);

                if (response1 != null) {
                    if (response1.getSectionlist() != null) {
                        Log.e("String", "value");
                        sectionNames.clear();
                        sectionlist.clear();
                        sectionlist.addAll(response1.getSectionlist());
                        Log.e("value", "" + sectionlist.size());
                        for (int i = 0; i < sectionlist.size(); i++) {
                            sectionNames.add(sectionlist.get(i).getSection());
                            // Log.e("Section Size",""+sectionNames.get(i));
                        }
                        sectionNameAdapter.notifyDataSetChanged();
                        if (sectionlist.size() > 0) {
                            selected_section = sectionlist.get(0).getId();
                            section_spinner.setSelection(0);
                            section_linear.setVisibility(View.VISIBLE);
                            btn_ohe.setVisibility(View.VISIBLE);
                            btn_psi.setVisibility(View.VISIBLE);
                            btn_tr_line.setVisibility(View.VISIBLE);
                            btn_other.setVisibility(View.VISIBLE);
                        }


                    } else {
                        sectionNames.clear();
                        sectionlist.clear();
                        sectionNameAdapter.notifyDataSetChanged();
                        section_linear.setVisibility(View.GONE);
                        btn_ohe.setVisibility(View.GONE);
                        btn_psi.setVisibility(View.GONE);
                        btn_tr_line.setVisibility(View.GONE);
                        btn_other.setVisibility(View.GONE);
                        Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                    }
                } else {
                    sectionNames.clear();
                    sectionlist.clear();
                    sectionNameAdapter.notifyDataSetChanged();
                    section_linear.setVisibility(View.GONE);
                    btn_ohe.setVisibility(View.GONE);
                    btn_psi.setVisibility(View.GONE);
                    btn_tr_line.setVisibility(View.GONE);
                    btn_other.setVisibility(View.GONE);
                    Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                }


            }*/
            else if(callType==CallType.GET_SECTION_LIST)
            {
                Gson gson=new Gson();
                Result response1=gson.fromJson(response,Result.class);

                if(response1!=null)
                {
                    if(response1.getSectionlist()!=null)
                    {
                        Log.e("String","value");
                        sectionNames.clear();
                        sectionlist.clear();
                        sectionlist.addAll(response1.getSectionlist());
                        Log.e("value",""+sectionlist.size());
                        if (checkWebView) {
                            sectionNames.add("all");
                        }
                        for (int i=0;i<sectionlist.size();i++)
                        {
                            sectionNames.add(sectionlist.get(i).getSection());
                        }
                        sectionNameAdapter.notifyDataSetChanged();
                        if(sectionlist.size()>0)
                        {
                            selected_section=sectionlist.get(0).getId();
                            section_spinner.setSelection(0);
                            section_linear.setVisibility(View.VISIBLE);
/*                            btn_ohe.setVisibility(View.VISIBLE);
                            btn_psi.setVisibility(View.VISIBLE);
                            station_linear.setVisibility(View.VISIBLE);
                            btn_tr_line.setVisibility(View.VISIBLE);
                            btn_other.setVisibility(View.VISIBLE);*/
                        }



                    }
                    else {
                        sectionNames.clear();
                        sectionlist.clear();
                        sectionNameAdapter.notifyDataSetChanged();
                        station_linear.setVisibility(View.GONE);
                        section_linear.setVisibility(View.GONE);
                        btn_ohe.setVisibility(View.GONE);
                        btn_psi.setVisibility(View.GONE);
                        btn_tr_line.setVisibility(View.GONE);
                        btn_other.setVisibility(View.GONE);
                        Toast.makeText(this,"no data found",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    sectionNames.clear();
                    sectionlist.clear();
                    sectionNameAdapter.notifyDataSetChanged();
                    section_linear.setVisibility(View.GONE);
                    station_linear.setVisibility(View.GONE);
                    btn_ohe.setVisibility(View.GONE);
                    btn_psi.setVisibility(View.GONE);
                    btn_tr_line.setVisibility(View.GONE);
                    btn_other.setVisibility(View.GONE);
                    Toast.makeText(this,"no data found",Toast.LENGTH_LONG).show();
                }


            }
            else if(callType == CallType.GET_STATION_LIST)
            {
                if(response1!=null) {
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
                                Toast.makeText(this,"no data found",Toast.LENGTH_LONG).show();

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
                                if(stationKeyValues.size()>0)
                                {
                                    station_id=stationKeyValues.get(0);
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
                }
                else {
                    stationKeyValues.clear();
                    stationpairValues.clear();
                    stationNameAdapter.notifyDataSetChanged();
                    station_linear.setVisibility(View.GONE);
                    btn_ohe.setVisibility(View.GONE);
                    btn_psi.setVisibility(View.GONE);
                    btn_tr_line.setVisibility(View.GONE);
                    btn_other.setVisibility(View.GONE);
                    Toast.makeText(this,"no data found",Toast.LENGTH_LONG).show();
                }

            }
            else if (callType == CallType.GET_OHE) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {

                            Util.showLongToast(ReportActivityHeaduater.this, "No data available");

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
