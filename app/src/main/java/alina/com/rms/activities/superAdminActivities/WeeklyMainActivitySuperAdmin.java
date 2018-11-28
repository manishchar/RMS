package alina.com.rms.activities.superAdminActivities;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
import alina.com.rms.activities.headquaterActivities.ProcessHeadquaterActivity;
import alina.com.rms.activities.headquaterActivities.ProcessSPActivity;
import alina.com.rms.activities.headquaterActivities.ProcessSSPActivity;
import alina.com.rms.activities.headquaterActivities.ProcessTSSActivity;
import alina.com.rms.activities.user_entry_section.EntryUserTSSWebViewActivity;
import alina.com.rms.activities.user_entry_section.EntryUserWebViewActivity;
import alina.com.rms.adaptor.CustomAdapter;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.model.Grouplist;
import alina.com.rms.model.HeadQuaterGroupResponse;
import alina.com.rms.model.Headquaterlist;
import alina.com.rms.model.Response;
import alina.com.rms.model.Result;
import alina.com.rms.model.Sectionlist;
import alina.com.rms.util.CallType;
import alina.com.rms.util.LoginDB;
import alina.com.rms.util.Util;

public class WeeklyMainActivitySuperAdmin extends SuperAdminBaseActivity implements AdapterView.OnItemSelectedListener,
        AsyncCompleteListner {

    private boolean intentFlag=false;
    private Response response;
    private List<Response> response1;
    private Spinner group_spinner,section_spinner,headquater_spinner;//item_spinner;/*select_type_spinner,target_type_spinner*/;
    private List<Sectionlist> sectionlist=new ArrayList<Sectionlist>();
    List<String> groupNames=new ArrayList<String>();
    List<String> sectionNames=new ArrayList<String>();
    List<String> headquaterNames=new ArrayList<String>();
    private String selected_group="",selected_section="";
    private CustomAdapter groupNameAdapter,sectionNameAdapter,headquaterNameAdapter;
    private List<Grouplist>getGroupList=new ArrayList<Grouplist>();
    private Button btn_ohe,btn_psi,btn_tr_line,btn_other;
    private TextView headquater_text_view;
    private RelativeLayout rel_headquater;
    private List<Headquaterlist> headquaterlist=new ArrayList<Headquaterlist>();
    private boolean checkWebView=false;
    private String selected_headquater_id;
    private LinearLayout group_linear,section_linear;
    Dialog listdialog;
    /*private List<Boolean>isChecked=new ArrayList<Boolean>();*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);

        checkWebView=true;
        Gson gson=new Gson();
        Type type = new TypeToken<List<Response>>() {}.getType();
        response1=gson.fromJson(LoginDB.getLoginResponseAsJSON(this),type);
        response=response1.get(0);
        //Bundle bundle=getIntent().getExtras();
        if(checkWebView)
        {
            headingText.setText("Weekly Report");
        }
        else {
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





    private void initView()
    {
        group_spinner=(Spinner)findViewById(R.id.group_spinner);
        section_spinner=(Spinner)findViewById(R.id.section_spinner);
        headquater_spinner=(Spinner)findViewById(R.id.headquater_spinner);
        //  item_spinner=(Spinner)findViewById(R.id.item_spinner);
        headquater_text_view=(TextView)findViewById(R.id.headquaterName);
        headquater_text_view.setVisibility(View.VISIBLE);
        rel_headquater=(RelativeLayout)findViewById(R.id.rel_headquater);
        rel_headquater.setVisibility(View.VISIBLE);
        btn_ohe=(Button)findViewById(R.id.btn_ohe);
        btn_psi=(Button)findViewById(R.id.btn_psi);
        btn_tr_line=(Button)findViewById(R.id.btn_tr_line);
        btn_other=(Button)findViewById(R.id.btn_other);
        group_linear=(LinearLayout)findViewById(R.id.group_linear);
        section_linear=(LinearLayout)findViewById(R.id.section_linear);

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
                if(checkWebView)
                {
                    intent=new Intent(WeeklyMainActivitySuperAdmin.this,EntryUserWebViewActivity.class);
                    intent.putExtra("screen_name","OHE Report");
                }
                else {
                    intent  = new Intent(WeeklyMainActivitySuperAdmin.this, ProcessHeadquaterActivity.class);
                }
                intent.putExtra("selected_group",selected_group);
                intent.putExtra("selected_section", selected_section);
                intent.putExtra("hadquater_id",selected_headquater_id);
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
        headquater_spinner.setOnItemSelectedListener(this);
        //item_spinner.setOnItemSelectedListener(this);
        headquaterNameAdapter=new CustomAdapter(this,headquaterNames);
        groupNameAdapter=new CustomAdapter(this,groupNames);
        sectionNameAdapter=new CustomAdapter(this,sectionNames);
       /* selectItemAdapter=new CustomAdapter(this,selectItemName);*/
       headquater_spinner.setAdapter(headquaterNameAdapter);
        group_spinner.setAdapter(groupNameAdapter);
        section_spinner.setAdapter(sectionNameAdapter);
        //item_spinner.setAdapter(selectItemAdapter);

        getHeadQuaterServer();
    }

/*    private void showAreaListDailog() {

        listdialog = new Dialog(MainActivitySuperAdmin.this);
        listdialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        listdialog.setContentView(R.layout.dialog_aoi_list);
        listdialog.setCancelable(false);
        // CheckBox  check_btn = (CheckBox) listdialog.findViewById(R.id.check_btn);
        final ListView list_multipal_feeder = (ListView) listdialog.findViewById(R.id.dfeeder_list);
        TextView cancel = (TextView) listdialog.findViewById(R.id.txtbtn_cancel);
        TextView ok = (TextView) listdialog.findViewById(R.id.txtbtn_ok);
        AOIListAdapter aoiListAdapter = new AOIListAdapter(MainActivitySuperAdmin.this, headquaterNames,headquaterNames,isChecked);
        list_multipal_feeder.setAdapter(aoiListAdapter);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listdialog.dismiss();
                selected_headquater_id="";
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listdialog.dismiss();
                selected_headquater_id="";
                for(int i=0;i<headquaterNames.size();i++)
                {

                    if(isChecked.get(i))
                    {
                        if (i==0)
                        {
                            send_item=keyValues.get(i);
                            break;
                        }
                        if(send_item.trim().length()==0)
                        {
                            send_item=keyValues.get(i);
                        }
                        else {
                            send_item+=","+keyValues.get(i);
                        }
                    }
                }
                select_type_txt_view.setText(send_item);
                if(keyValues.size()>0)
                {
                    callWebFun();
                }
            }
        });
       *//* check_btn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                aoiListAdapter = new AOIListAdapter(UserRegistration.this, areaOfIntrestResultLists, isChecked,ids);
                list_multipal_feeder.setAdapter(aoiListAdapter);
                if (isChecked) {
                    for (AreaOfIntrestResultList areaOfIntrestResultList : areaOfIntrestResultLists) {
                        addIds(areaOfIntrestResultList.getCategory_id());

                    }
                } else {
                    for (AreaOfIntrestResultList areaOfIntrestResultList : areaOfIntrestResultLists) {
                        removeIds(areaOfIntrestResultList.getCategory_id());

                    }
                }
            }
        });*//*
        listdialog.show();

    }*/



    private void getHeadQuaterServer()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(this,this,CallType.GET_HQ_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getHQList();
    }

    /*private void initUI() {
        mRecyclerView = (RecyclerView) findViewById(R.id.lvHomeScreenItems);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(2);
        mStaggeredLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
        servicesListAdapter = new HeadquaterServicesListAdapter(MainActivitySuperAdmin.this, this);
        mRecyclerView.setAdapter(servicesListAdapter);
        //
    }*/

    private void getGroupFromServer()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(WeeklyMainActivitySuperAdmin.this,this, CallType.GET_GROUP_USER,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getGroupListForEntryUser(selected_headquater_id,response.getUserId());
    }


    private void getSectionListFromServer(String selected_group1)
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(this,this, CallType.GET_SECTION_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getHeadquaterSectionList(selected_headquater_id,selected_group1);

        // GET_USER_LIST
    }



    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        if(parent.getId()==R.id.group_spinner)
        {
            if(checkWebView)
            {
                if(position==0)
                {
                    section_linear.setVisibility(View.GONE);
                    btn_ohe.setVisibility(View.VISIBLE);
                    btn_psi.setVisibility(View.VISIBLE);
                    btn_tr_line.setVisibility(View.VISIBLE);
                    btn_other.setVisibility(View.VISIBLE);
                    selected_group="";
                    selected_section="";
                    sectionNames.clear();
                    sectionNameAdapter.notifyDataSetChanged();
                }
                else {
                    selected_group=getGroupList.get(position-1).getId();
                 //   getSectionListFromServer(selected_group);
                }
            }
            else
            {
                selected_group=getGroupList.get(position).getId();
               // getSectionListFromServer(selected_group);
            }

        }
        else if(parent.getId()==R.id.section_spinner){
            // selected_section=headquaterlist.get(position).getId();
            selected_section=sectionlist.get(position).getId();

        }
        else if(parent.getId()==R.id.headquater_spinner){
            //  item_selected=selectTypeName.get(position);

            if(position==0)
            {
                group_linear.setVisibility(View.GONE);
                section_linear.setVisibility(View.GONE);
             selected_headquater_id="all";
                selected_section="";
                selected_group="";
                groupNames.clear();
                sectionNames.clear();
                groupNameAdapter.notifyDataSetChanged();
                sectionNameAdapter.notifyDataSetChanged();
            }
           else
            {
                selected_headquater_id=headquaterlist.get(position-1).getId();
                getGroupFromServer();
            }

        }


    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void setTarget()
    {

    }

    private void achiveTarget()
    {

    }

    public void getOHETarget()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(WeeklyMainActivitySuperAdmin.this,this, CallType.GET_OHE,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getOHERequest();
    }


    public void addHeadQuater()
    {
        final Dialog dialog = new Dialog(WeeklyMainActivitySuperAdmin.this);
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
                if(checkWebView)
                {
                    intent=new Intent(WeeklyMainActivitySuperAdmin.this,EntryUserWebViewActivity.class);
                    intent.putExtra("screen_name","SP");
                }
                else {
                    intent = new Intent(WeeklyMainActivitySuperAdmin.this, ProcessSPActivity.class);
                }
                intent.putExtra("selected_group",selected_group);
                intent.putExtra("selected_section", selected_section);
                intent.putExtra("hadquater_id",selected_headquater_id);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        btn_tss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                if(checkWebView)
                {
                    intent=new Intent(WeeklyMainActivitySuperAdmin.this,EntryUserTSSWebViewActivity.class);
                    intent.putExtra("screen_name","OHE Report");
                }
                else {
                    intent = new Intent(WeeklyMainActivitySuperAdmin.this, ProcessTSSActivity.class);
                }
                intent.putExtra("selected_group",selected_group);
                intent.putExtra("selected_section", selected_section);
                intent.putExtra("hadquater_id",selected_headquater_id);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        btn_ssp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(checkWebView)
                {
                    intent=new Intent(WeeklyMainActivitySuperAdmin.this,EntryUserWebViewActivity.class);
                    intent.putExtra("screen_name","SSP");
                }
                else {
                    intent=new Intent(WeeklyMainActivitySuperAdmin.this,ProcessSSPActivity.class);
                }
                intent.putExtra("selected_group",selected_group);
                intent.putExtra("selected_section", selected_section);
                intent.putExtra("hadquater_id",selected_headquater_id);
                startActivity(intent);
                dialog.dismiss();
            }
        });

        dialog.show();

    }



    @Override
    public void asyncCompleteListner(String response, CallType callType) {
        setProgressDialoug(false);
        if(response!=null) {
            if(callType == CallType.GET_HQ_LIST)
            {
                Gson gson=new Gson();
                Result response1=gson.fromJson(response,Result.class);

                if(response1!=null)
                {
                    if(response1.getHeadquaterlist()!=null)
                    {
                        Log.e("String","value");
                        headquaterlist.clear();
                        headquaterlist.addAll(response1.getHeadquaterlist());
                        Log.e("value",""+headquaterlist.size());
                        //servicesListAdapter.notifyDataSetChanged();
                        if(headquaterlist.size()>0)
                        {
                            headquaterNames.clear();
                            headquaterNames.add("all");
                            for (int i=0;i<headquaterlist.size();i++)
                            {
                                headquaterNames.add(headquaterlist.get(i).getName());
                            }
                            headquaterNameAdapter.notifyDataSetChanged();

                            headquater_spinner.setSelection(0);

                        }
                    }
                    else {
                        Toast.makeText(this,"No Headquater found",Toast.LENGTH_LONG).show();
                        btn_ohe.setVisibility(View.GONE);
                        btn_psi.setVisibility(View.GONE);
                        btn_tr_line.setVisibility(View.GONE);
                        btn_other.setVisibility(View.GONE);
                        group_linear.setVisibility(View.GONE);
                        section_linear.setVisibility(View.GONE);
                    }
                }
                else {
                    Toast.makeText(this,"no data found",Toast.LENGTH_LONG).show();
                    btn_ohe.setVisibility(View.GONE);
                    btn_psi.setVisibility(View.GONE);
                    btn_tr_line.setVisibility(View.GONE);
                    btn_other.setVisibility(View.GONE);
                    group_linear.setVisibility(View.GONE);
                    section_linear.setVisibility(View.GONE);
                }

            }
            else if (callType == CallType.GET_GROUP_USER) {
                Gson gson = new Gson();
                HeadQuaterGroupResponse response1 = gson.fromJson(response, HeadQuaterGroupResponse.class);
                /*btn_submit.setVisibility(View.GONE);*/
                if (response1 != null) {
                    if (response1.getGrouplist() != null) {
                        Log.e("String", "value");
                        selected_group="";
                        groupNames.clear();
                        getGroupList.clear();
                        getGroupList.addAll(response1.getGrouplist());
                        if(response1.getGrouplist().size()>0) {
                            group_linear.setVisibility(View.VISIBLE);
                            if(checkWebView)
                            {
                                groupNames.add("all");
                            }
                            for (int i = 0; i < getGroupList.size(); i++) {
                                groupNames.add(getGroupList.get(i).getName());
                            }
                            groupNameAdapter.notifyDataSetChanged();
                            if(getGroupList.size()>0)
                            {
                                group_spinner.setSelection(0);
                                btn_ohe.setVisibility(View.VISIBLE);
                                btn_psi.setVisibility(View.VISIBLE);
                                btn_tr_line.setVisibility(View.VISIBLE);
                                btn_other.setVisibility(View.VISIBLE);
                                group_linear.setVisibility(View.VISIBLE);
                                section_linear.setVisibility(View.GONE);
                            }
                        }
                        Log.e("value", "" + groupNames.size());

                        //getOHETarget();
                    } else {
                        Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                        btn_ohe.setVisibility(View.GONE);
                        btn_psi.setVisibility(View.GONE);
                        btn_tr_line.setVisibility(View.GONE);
                        btn_other.setVisibility(View.GONE);
                        group_linear.setVisibility(View.GONE);
                        section_linear.setVisibility(View.GONE);
                    }
                }

                else {
                    Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                    btn_ohe.setVisibility(View.GONE);
                    btn_psi.setVisibility(View.GONE);
                    btn_tr_line.setVisibility(View.GONE);
                    btn_other.setVisibility(View.GONE);
                    group_linear.setVisibility(View.GONE);
                    section_linear.setVisibility(View.GONE);
                }


            }
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
                        for (int i=0;i<sectionlist.size();i++)
                        {
                            sectionNames.add(sectionlist.get(i).getSection());
                        }
                        selected_section="";
                        sectionNameAdapter.notifyDataSetChanged();
                        if(sectionNames.size()>0)
                        {
                            section_linear.setVisibility(View.VISIBLE);
                            section_spinner.setSelection(0);
                            btn_ohe.setVisibility(View.VISIBLE);
                            btn_psi.setVisibility(View.VISIBLE);
                            btn_tr_line.setVisibility(View.VISIBLE);
                            btn_other.setVisibility(View.VISIBLE);
                            //  group_linear.setVisibility(View.VISIBLE);

                        }

                    }
                    else {
                        Toast.makeText(this,"no data found",Toast.LENGTH_LONG).show();
                        btn_ohe.setVisibility(View.GONE);
                        btn_psi.setVisibility(View.GONE);
                        btn_tr_line.setVisibility(View.GONE);
                        btn_other.setVisibility(View.GONE);
                        // group_linear.setVisibility(View.GONE);
                        section_linear.setVisibility(View.GONE);
                    }
                }
                else {
                    Toast.makeText(this,"no data found",Toast.LENGTH_LONG).show();
                    btn_ohe.setVisibility(View.GONE);
                    btn_psi.setVisibility(View.GONE);
                    btn_tr_line.setVisibility(View.GONE);
                    btn_other.setVisibility(View.GONE);
                    // group_linear.setVisibility(View.GONE);
                    section_linear.setVisibility(View.GONE);
                }


            }
            else if (callType == CallType.GET_OHE) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {

                            Util.showLongToast(WeeklyMainActivitySuperAdmin.this, "No data available");

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
