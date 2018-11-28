package alina.com.rms.activities.user_entry_section;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
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
import alina.com.rms.activities.headquaterActivities.HeaduaterTSSWebViewActivity;
import alina.com.rms.activities.headquaterActivities.HeaduaterWebViewActivity;
import alina.com.rms.adaptor.CustomAdapter;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.model.Grouplist;
import alina.com.rms.model.HeadQuaterGroupResponse;
import alina.com.rms.model.Response;
import alina.com.rms.model.Result;
import alina.com.rms.model.Sectionlist;
import alina.com.rms.util.CallType;
import alina.com.rms.util.LoginDB;
import alina.com.rms.util.Util;

public class MainActivityEntryUser extends UserEntryBaseActivity implements AdapterView.OnItemSelectedListener,AsyncCompleteListner {

    private Response response;
    private List<Response> response1;
    private Spinner group_spinner,section_spinner;//item_spinner;/*select_type_spinner,target_type_spinner*/;
    private List<Sectionlist> sectionlist=new ArrayList<Sectionlist>();
    List<String> groupNames=new ArrayList<String>();
    List<String> sectionNames=new ArrayList<String>();
    private String selected_group="",selected_section="";
    private CustomAdapter groupNameAdapter,sectionNameAdapter;
    private List<Grouplist>getGroupList=new ArrayList<Grouplist>();
    private Button btn_ohe,btn_psi,btn_tr_line,btn_other;
    private LinearLayout section_linear;
    private boolean checkWebView=false;
    private boolean daily_reprt_flag=false;
    private boolean firstTime=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        Gson gson=new Gson();
        Type type = new TypeToken<List<Response>>() {}.getType();
        response1=gson.fromJson(LoginDB.getLoginResponseAsJSON(this),type);
        response=response1.get(0);



        addView();
       // initUI();
        initView();

        Bundle bundle=getIntent().getExtras();
        if(bundle!=null) {
            if (bundle.containsKey("daily_reprt_flag")) {
                daily_reprt_flag = bundle.getBoolean("daily_reprt_flag");
            }
        }

        if(response.getUser_type()==5)
        {

            checkWebView=true;
            if(daily_reprt_flag)
            {
                headingText.setText("Report");
            }
            else {
                headingText.setText("Weekly Report");
            }

        }
        else
        {
/*            View viewHolder = recyclerView.getLayoutManager().findViewByPosition(0);
            View row1 = recyclerView.getLayoutManager().findViewByPosition(1);
            viewHolder.setVisibility(View.GONE);
            row1.setVisibility(View.GONE);*/
            headingText.setText("Set Progress");
        }

        if (drawer != null && drawer instanceof DrawerLayout) {

            drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
                @Override
                public void onDrawerSlide(View view, float v) {
                    if(response.getUser_type()!=5 && !firstTime) {
                        View viewHolder = recyclerView.getLayoutManager().findViewByPosition(0);
                        View row1 = recyclerView.getLayoutManager().findViewByPosition(1);
                          firstTime=true;
                        if (viewHolder != null) {
                            viewHolder.setVisibility(View.GONE);
                            row1.setVisibility(View.GONE);
                           // row2.setPivotX(0)
                        }

                    }
                }

                @Override
                public void onDrawerOpened(View view) {

                }

                @Override
                public void onDrawerClosed(View view) {
                    // your refresh code can be called from here
                }

                @Override
                public void onDrawerStateChanged(int i) {

                }
            });
        }
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
      //  item_spinner=(Spinner)findViewById(R.id.item_spinner);



        btn_ohe=(Button)findViewById(R.id.btn_ohe);
        btn_psi=(Button)findViewById(R.id.btn_psi);
        btn_tr_line=(Button)findViewById(R.id.btn_tr_line);
        btn_other=(Button)findViewById(R.id.btn_other);
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

        if(response.getUser_type()==5) {
            section_linear.setVisibility(View.GONE);
        }
        btn_ohe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(daily_reprt_flag)
                {
                    intent=new Intent(MainActivityEntryUser.this,HeaduaterWebViewActivity.class);
                    intent.putExtra("screen_name","OHE Report");
                }
                else if(checkWebView)
                {
                   intent=new Intent(MainActivityEntryUser.this,EntryUserWebViewActivity.class);
                   intent.putExtra("screen_name","OHE Report");
                }
                else {
                   intent  = new Intent(MainActivityEntryUser.this, ProcessHeadquaterEntryUserActivity.class);
                }
                intent.putExtra("selected_group",selected_group);
                intent.putExtra("selected_section", selected_section);
                intent.putExtra("hadquater_id",response.getHeadquater());
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
        //item_spinner.setOnItemSelectedListener(this);

        groupNameAdapter=new CustomAdapter(this,groupNames);
        sectionNameAdapter=new CustomAdapter(this,sectionNames);
       /* selectItemAdapter=new CustomAdapter(this,selectItemName);*/
        group_spinner.setAdapter(groupNameAdapter);
        section_spinner.setAdapter(sectionNameAdapter);
        //item_spinner.setAdapter(selectItemAdapter);

        getGroupFromServer();
    }


    private void getGroupFromServer()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(MainActivityEntryUser.this,this,CallType.GET_HQ_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getGroupListForEntryUser(response.getHeadquater(),response.getUserId());
    }


    private void getSectionListFromServer(String selected_group1)
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(this,this, CallType.GET_SECTION_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getHeadquaterSectionList(response.getHeadquater(),selected_group1);

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
                    selected_group="";
                    selected_section="";
                    section_linear.setVisibility(View.GONE);
                    btn_ohe.setVisibility(View.VISIBLE);
                    btn_psi.setVisibility(View.VISIBLE);
                    btn_tr_line.setVisibility(View.VISIBLE);
                    btn_other.setVisibility(View.VISIBLE);
                }
                else {
                    selected_group=getGroupList.get(position-1).getId();
                    if(response.getUser_type()!=5) {
                        getSectionListFromServer(selected_group);
                    }

                }
            }
            else
            {
                selected_group=getGroupList.get(position).getId();
                if(response.getUser_type()!=5) {
                    getSectionListFromServer(selected_group);
                }
            }

        }
        else if(parent.getId()==R.id.section_spinner){
           // selected_section=headquaterlist.get(position).getId();
            selected_section=sectionlist.get(position).getId();

        }
        else if(parent.getId()==R.id.item_spinner){
          //  item_selected=selectTypeName.get(position);
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
        AsyncController asyncController=new AsyncController(MainActivityEntryUser.this,this, CallType.GET_OHE,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getOHERequest();
    }


    public void addHeadQuater()
    {
        final Dialog dialog = new Dialog(MainActivityEntryUser.this);
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
                if(daily_reprt_flag) {
                    intent = new Intent(MainActivityEntryUser.this, HeaduaterWebViewActivity.class);
                }
                else if(checkWebView)
                {
                    intent=new Intent(MainActivityEntryUser.this,EntryUserWebViewActivity.class);

                }
                else {
                    intent = new Intent(MainActivityEntryUser.this, ProcessSPEntryUserActivity.class);
                }
                intent.putExtra("screen_name","SP");
                intent.putExtra("selected_group",selected_group);
                intent.putExtra("selected_section", selected_section);
                intent.putExtra("hadquater_id",response.getHeadquater());
                startActivity(intent);
                dialog.dismiss();
            }
        });

        btn_tss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent;
                if(daily_reprt_flag) {
                    intent = new Intent(MainActivityEntryUser.this, HeaduaterTSSWebViewActivity.class);
                }
                else if(checkWebView)
                {
                    intent=new Intent(MainActivityEntryUser.this,EntryUserTSSWebViewActivity.class);
                    intent.putExtra("screen_name","OHE Report");
                }
                else {
                    intent = new Intent(MainActivityEntryUser.this, ProcessTSSEntryUserActivity.class);
                }
                intent.putExtra("selected_group",selected_group);
                intent.putExtra("selected_section", selected_section);
                intent.putExtra("hadquater_id",response.getHeadquater());
                startActivity(intent);
                dialog.dismiss();
            }
        });

        btn_ssp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(daily_reprt_flag) {
                    intent = new Intent(MainActivityEntryUser.this, HeaduaterWebViewActivity.class);
                }
               else if(checkWebView)
                {
                    intent=new Intent(MainActivityEntryUser.this,EntryUserWebViewActivity.class);

                }
                else {
                    intent=new Intent(MainActivityEntryUser.this,ProcessSSPUserSectionActivity.class);
                }
                intent.putExtra("screen_name","SSP");
                intent.putExtra("selected_group",selected_group);
                intent.putExtra("selected_section", selected_section);
                intent.putExtra("hadquater_id",response.getHeadquater());
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
            if (callType == CallType.GET_HQ_LIST) {
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
                            if(checkWebView)
                            {
                                groupNames.add("all");
                            }
                            for (int i = 0; i < getGroupList.size(); i++) {
                                groupNames.add(getGroupList.get(i).getName());
                            }
                            groupNameAdapter.notifyDataSetChanged();
                            if(groupNames.size()>0)
                            {
                                group_spinner.setSelection(0);


                            }
                        }
                        Log.e("value", "" + groupNames.size());

                        //getOHETarget();
                    } else {
                        Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();

                    }
                }

                else {
                    Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
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
                        sectionNameAdapter.notifyDataSetChanged();
                        if(sectionlist.size()>0)
                        {
                            selected_section=sectionlist.get(0).getId();
                            section_spinner.setSelection(0);
                            section_linear.setVisibility(View.VISIBLE);

                            btn_ohe.setVisibility(View.VISIBLE);
                            btn_psi.setVisibility(View.VISIBLE);
                            btn_tr_line.setVisibility(View.VISIBLE);
                            btn_other.setVisibility(View.VISIBLE);
                        }



                    }
                    else {
                        Toast.makeText(this,response1.getMessage(),Toast.LENGTH_LONG).show();
                        section_linear.setVisibility(View.GONE);
                        btn_ohe.setVisibility(View.GONE);
                        btn_psi.setVisibility(View.GONE);
                        btn_tr_line.setVisibility(View.GONE);
                        btn_other.setVisibility(View.GONE);
                    }
                }
                else {
                    Toast.makeText(this,"no data found",Toast.LENGTH_LONG).show();
                    section_linear.setVisibility(View.GONE);
                    btn_ohe.setVisibility(View.GONE);
                    btn_psi.setVisibility(View.GONE);
                    btn_tr_line.setVisibility(View.GONE);
                    btn_other.setVisibility(View.GONE);
                }


            }
            else if (callType == CallType.GET_OHE) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if (jsonObject.has("response_code")) {
                        if (Integer.parseInt(jsonObject.getString("response_code")) == 0) {

                            Util.showLongToast(MainActivityEntryUser.this, "No data available");

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
                          //  selectItemAdapter.notifyDataSetChanged();
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
