package alina.com.rms.activities.public_module;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import alina.com.rms.R;
import alina.com.rms.activities.public_module_adapter.PublicUserWiringProgressListAdapter;
import alina.com.rms.activities.superAdminActivities.PublicMouleSuperAdminActivity;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.custom_interface.ServiceClickCallBack;
import alina.com.rms.model.GetWiringDatum;
import alina.com.rms.model.LoginResultPojo;
import alina.com.rms.model.Result;
import alina.com.rms.model.RkmList;
import alina.com.rms.util.CallType;
import alina.com.rms.util.LoginDB;
import alina.com.rms.util.Util;

public class PublicModuleNewEntryProgressList extends PublicModuleBaseActivity implements ServiceClickCallBack,AsyncCompleteListner,SwipeRefreshLayout.OnRefreshListener
{

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private PublicUserWiringProgressListAdapter servicesListAdapter;
    private List<GetWiringDatum> getWiringData=new ArrayList<GetWiringDatum>();
  //  RelativeLayout relativeLayout;
    private AlertDialog alertDialog;
    private String add_head_quater_name;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<LoginResultPojo> loginResultPojo;
    private LoginResultPojo loginResultPojo1;
    private String login_response;
    private String section_id;
    private Button submitBtn;
    private String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_super_admin_hqlist);
        Gson gson = new Gson();
        Type type = new TypeToken<List<LoginResultPojo>>() {}.getType();
     /*   String json = gson.toJson(login_response, type);
        System.out.println(json);*/
        login_response= LoginDB.getLoginResponseAsJSON(PublicModuleNewEntryProgressList.this);
        loginResultPojo = gson.fromJson(login_response, type);
        loginResultPojo1=loginResultPojo.get(0);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            section_id=bundle.getString("section_id");
            date=bundle.getString("date");
        }

        addView();
        initUI();
    }

    @Override
    public void addView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View myView = factory.inflate(R.layout.activity_super_admin_fooundation_list, null);
        setView(myView);
    }



    private void initUI() {
        headingText.setText("Add Progress List");

        submitBtn=(Button)findViewById(R.id.submitBtn);
        mRecyclerView = (RecyclerView) findViewById(R.id.lvHomeScreenItems);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(1);
        mStaggeredLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
        servicesListAdapter = new PublicUserWiringProgressListAdapter(PublicModuleNewEntryProgressList.this, this,getWiringData);
        mRecyclerView.setAdapter(servicesListAdapter);
        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        relativeLayout.setVisibility(View.GONE);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitToserver();
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHeadQuater();
            }
        });
     //   hitToServer();
        //
    }

    private void submitToserver() {

        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(PublicModuleNewEntryProgressList.this,this,
                CallType.SAVE_FOUNDATION_ITEM,"",true);
       // asyncController.setProgressDialoug(true);
        asyncController.saveWiringProgressListItem(getWiringData,date,"");

    }

    private void hitToServer()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(PublicModuleNewEntryProgressList.this,this,
                CallType.GET_FOUNDATION_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getWiringProgrssDataList(section_id);

    }
    @Override
    protected void onResume() {
        super.onResume();
       if(section_id!=null)
        hitToServer();
    }

    @Override
    public void callBack(int postion) {

    }



    public void addHeadQuater()
    {

        Intent intent=new Intent(PublicModuleNewEntryProgressList.this, PublicMouleSuperAdminActivity.class);
        startActivity(intent);
    }


    public void updateHeadQuater(RkmList rkmList)
    {
        Intent intent = new Intent(PublicModuleNewEntryProgressList.this,PublicMouleSuperAdminActivity.class);
      /*  Bundle bundle=new Bundle();
        bundle.putSerializable("RKM_Data",rkmList);
        intent.putExtras(bundle);*/
        intent.putExtra("crs_id",rkmList.getCrs_id());
        startActivity(intent);

    }



    public void deleteModule(final String id)
    {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(PublicModuleNewEntryProgressList.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(PublicModuleNewEntryProgressList.this);
        }
        builder.setTitle("Delete Public Module")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        setProgressDialoug(true);
                        AsyncController asyncController=new AsyncController(PublicModuleNewEntryProgressList.this, PublicModuleNewEntryProgressList.this,CallType.DELETE_MODULE,"",true);
                        asyncController.setProgressDialoug(true);
                        asyncController.deletePublicModuleForSuperAdmin(id);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    @Override
    public void asyncCompleteListner(String response, CallType callType) {
        setProgressDialoug(false);
        if(mSwipeRefreshLayout.isRefreshing())
        {
            // Stopping swipe refresh
            mSwipeRefreshLayout.setRefreshing(false);
        }
        //Log.e("response",response);
        ///return;

        try {


            if (response != null) {
                if (callType == CallType.GET_FOUNDATION_LIST) {
                    Gson gson = new Gson();
                    Result response1 = gson.fromJson(response, Result.class);

                    if (response1 != null) {
                        if (response1.getGetWiringData() != null) {
                            Log.e("String", "value");
                            getWiringData.clear();
                            getWiringData.addAll(response1.getGetWiringData());
                            Log.e("value", "" + getWiringData.size());
                            servicesListAdapter.notifyDataSetChanged();
                            if (getWiringData.size() < 1) {
                                submitBtn.setVisibility(View.INVISIBLE);
                            } else {
                                submitBtn.setVisibility(View.VISIBLE);
                            }
                        } else {
                            Toast.makeText(PublicModuleNewEntryProgressList.this, "no data found", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(PublicModuleNewEntryProgressList.this, "no data found", Toast.LENGTH_LONG).show();
                    }


                } else if (callType == CallType.SAVE_FOUNDATION_ITEM) {
                    Gson gson = new Gson();
                    Result response1 = gson.fromJson(response, Result.class);
                    if (response1 != null) {
                        if (response1.getResponseCode() == 1) {
                            Util.showLongToast(getApplicationContext(), response1.getMessage());
                            finish();
                        } else {
                            Util.showLongToast(getApplicationContext(), response1.getMessage());
                        }
                    }
                }

            }
        }
        catch (Exception ex)
        {

        }
    }

    private void hitToServer1()
    {
        //setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(PublicModuleNewEntryProgressList.this,this,
                CallType.GET_FOUNDATION_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getWiringProgrssDataList(section_id);
    }

    @Override
    public void onRefresh() {
        if(section_id!=null)
        {
            hitToServer1();
        }
        else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }



}
