package alina.com.rms.activities.headquaterActivities;

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
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import alina.com.rms.R;
import alina.com.rms.adaptor.HeadquaterMasterListAdapter;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.custom_interface.ServiceClickCallBack;
import alina.com.rms.model.Response;
import alina.com.rms.model.Result;
import alina.com.rms.model.Sectionlist;
import alina.com.rms.util.CallType;
import alina.com.rms.util.LoginDB;
import alina.com.rms.util.URL;

public class HeadquaterMasterListActivity extends HeaduaterBaseActivity implements ServiceClickCallBack,AsyncCompleteListner,SwipeRefreshLayout.OnRefreshListener
{
        private RecyclerView mRecyclerView;
        private StaggeredGridLayoutManager mStaggeredLayoutManager;
        private HeadquaterMasterListAdapter servicesListAdapter;
        private List<Sectionlist> headquaterlist=new ArrayList<Sectionlist>();
        //  RelativeLayout relativeLayout;
        private AlertDialog alertDialog;
        private Response response;
        private List<Response> response1;
        int screenposition=0;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    String type;
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_super_admin_group_list);
            Bundle bundle=getIntent().getExtras();
            if(bundle.containsKey("value"))
            {
                screenposition=bundle.getInt("value");
            }

        Gson gson=new Gson();
        Type type = new TypeToken<List<Response>>() {}.getType();
        response1=gson.fromJson(LoginDB.getLoginResponseAsJSON(HeadquaterMasterListActivity.this),type);
        response=response1.get(0);
        addView();
        initUI();
    }
        @Override
        public void onResume()
        {
            super.onResume();
            if(screenposition==0)
            {
                headingText.setText("SP Master List");
                type="SP";

            }
            else if(screenposition==1)
            {
                headingText.setText("SSP Master List");
                type="SSP";
            }
            else if(screenposition==2)
            {
                headingText.setText("TSS Master List");
                type="TSS";
            }
            getMasterListFromServer();
        }

        @Override
        public void addView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View myView = factory.inflate(R.layout.activity_section, null);
        setView(myView);
        //initUI();
    }

    private void initUI() {

        mRecyclerView = (RecyclerView) findViewById(R.id.lvHomeScreenItems);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(1);
        mStaggeredLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
        servicesListAdapter = new HeadquaterMasterListAdapter(HeadquaterMasterListActivity.this, this,headquaterlist);
        mRecyclerView.setAdapter(servicesListAdapter);
        relativeLayout.setVisibility(View.VISIBLE);

        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // addHeadQuater();

                Intent intent=new Intent(HeadquaterMasterListActivity.this,AddMasterActivity.class);
                intent.putExtra("screenPosition",screenposition);
                startActivity(intent);
            }
        });

        //
    }

    private void getMasterListFromServer()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(HeadquaterMasterListActivity.this,this, CallType.GET_MASTER_LIST,"",true);
        asyncController.setProgressDialoug(true);
        if(screenposition==0)
        {
            asyncController.getHeadquaterMasterList(response.getHeadquater(), URL.get_sp);
        }
        else if(screenposition==1)
        {
            asyncController.getHeadquaterMasterList(response.getHeadquater(), URL.get_ssp);
        }
        else if(screenposition==2)
        {
            asyncController.getHeadquaterMasterList(response.getHeadquater(), URL.get_tss);
        }

        // GET_USER_LIST
    }

    @Override
    public void callBack(int postion) {

    }

    public void deleteHQ(final String id)
    {
        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        builder.setTitle("Delete User")
                .setMessage("Are you sure you want to delete ?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        setProgressDialoug(true);
                        AsyncController asyncController=new AsyncController(HeadquaterMasterListActivity.this, HeadquaterMasterListActivity.this,CallType.DELETE_MASTER,"",true);
                        asyncController.setProgressDialoug(true);
                        asyncController.deleteMasterForHeadquater(id,HeadquaterMasterListActivity.this.type);
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

    public void updateHeadQuater(int postion) {

        Sectionlist headquateruserlist=headquaterlist.get(postion);
        Intent intent=new Intent(HeadquaterMasterListActivity.this,AddMasterActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("data",headquateruserlist);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public void asyncCompleteListner(String response, CallType callType) {
        setProgressDialoug(false);
        if(mSwipeRefreshLayout.isRefreshing())
        {
            // Stopping swipe refresh
            mSwipeRefreshLayout.setRefreshing(false);
        }
        if(response!=null)
        {
            if(callType==CallType.GET_MASTER_LIST)
            {
                Gson gson=new Gson();
                Result response1=gson.fromJson(response,Result.class);

                if(response1!=null)
                {
                    if(response1.getSectionlist()!=null)
                    {
                        Log.e("String","value");
                        headquaterlist.clear();
                        headquaterlist.addAll(response1.getSectionlist());
                        Log.e("value",""+headquaterlist.size());
                        servicesListAdapter.notifyDataSetChanged();

                    }
                    else {
                        Toast.makeText(this,"no data found",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(this,"no data found",Toast.LENGTH_LONG).show();
                }


            }
            else if(callType==CallType.ADD_HEAD_QUATER)
            {
                Gson gson=new Gson();
                Result response1=gson.fromJson(response,Result.class);

                if(response1!=null)
                {
                    if(response1.getResponseCode()!=null)
                    {
                        if(response1.getResponseCode()==1)
                        {

                            Toast.makeText(this, response1.getMessage(), Toast.LENGTH_LONG).show();
                            //hitToServer();
                        }

                    }
                    else {
                        Toast.makeText(this,"Section not added successfully",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(this,"Section not added successfully",Toast.LENGTH_LONG).show();
                }
            }
            else if(callType==CallType.UPDATE_HEAD_QUATER)
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
                            Toast.makeText(this, response1.getMessage(), Toast.LENGTH_LONG).show();
                           // hitToServer();
                        }

                    }
                    else {
                        Toast.makeText(this,"Section not update successfully",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(this,"Section not update successfully",Toast.LENGTH_LONG).show();
                }
            }

            else if(callType==CallType.DELETE_MASTER)
            {
                Gson gson=new Gson();
                Result response1=gson.fromJson(response,Result.class);

                if(response1!=null)
                {
                    if(response1.getResponseCode()!=null)
                    {
                        if(response1.getResponseCode()==1)
                        {
                            // Log.e("String", "value");
                            Toast.makeText(this, response1.getMessage(), Toast.LENGTH_LONG).show();
                            getMasterListFromServer();
                        }
                        else {
                            showDialog(response1.getMessage());
                            // Toast.makeText(this,"Section not delete successfully",Toast.LENGTH_LONG).show();
                        }

                    }

                }
                else {
                    Toast.makeText(this,"Network wrror please try again",Toast.LENGTH_LONG).show();
                }
            }



        }
    }


    private void getMasterListFromServer1()
    {

        AsyncController asyncController=new AsyncController(HeadquaterMasterListActivity.this,this, CallType.GET_MASTER_LIST,"",true);
        asyncController.setProgressDialoug(true);
        if(screenposition==0)
        {
            asyncController.getHeadquaterMasterList(response.getHeadquater(), URL.get_sp);
        }
        else if(screenposition==1)
        {
            asyncController.getHeadquaterMasterList(response.getHeadquater(), URL.get_ssp);
        }
        else if(screenposition==2)
        {
            asyncController.getHeadquaterMasterList(response.getHeadquater(), URL.get_tss);
        }

        // GET_USER_LIST
    }
    @Override
    public void onRefresh() {
        getMasterListFromServer1();
    }
}
