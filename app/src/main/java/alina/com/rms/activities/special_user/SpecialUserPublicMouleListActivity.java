package alina.com.rms.activities.special_user;

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
import alina.com.rms.activities.special_user_adptor.SpecialUserPublicMouleActivityAdapter;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.custom_interface.ServiceClickCallBack;
import alina.com.rms.model.LoginResultPojo;
import alina.com.rms.model.Result;
import alina.com.rms.model.RkmList;
import alina.com.rms.util.CallType;
import alina.com.rms.util.LoginDB;

public class SpecialUserPublicMouleListActivity extends SpecialUserBaseActivity implements ServiceClickCallBack,AsyncCompleteListner,SwipeRefreshLayout.OnRefreshListener{


    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private SpecialUserPublicMouleActivityAdapter servicesListAdapter;
    private List<RkmList> rkmLists=new ArrayList<RkmList>();
    //  RelativeLayout relativeLayout;
    private AlertDialog alertDialog;
    private String add_head_quater_name;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<LoginResultPojo> loginResultPojo;
    private LoginResultPojo loginResultPojo1;
    private String login_response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_super_admin_hqlist);
        Gson gson = new Gson();
        Type type = new TypeToken<List<LoginResultPojo>>() {}.getType();
     /*   String json = gson.toJson(login_response, type);
        System.out.println(json);*/
        login_response= LoginDB.getLoginResponseAsJSON(SpecialUserPublicMouleListActivity.this);
        loginResultPojo = gson.fromJson(login_response, type);
        loginResultPojo1=loginResultPojo.get(0);
        addView();
        initUI();
    }

    @Override
    public void addView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View myView = factory.inflate(R.layout.activity_super_admin_hqlist, null);
        setView(myView);
    }

    private void hitToServer()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(SpecialUserPublicMouleListActivity.this,this,
                CallType.GET_PUBLIC_MODULE_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getPublicUserList(String.valueOf(loginResultPojo1.getRole()),"");

    }

    private void initUI() {
        headingText.setText("CRS List");

        mRecyclerView = (RecyclerView) findViewById(R.id.lvHomeScreenItems);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(1);
        mStaggeredLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
        servicesListAdapter = new SpecialUserPublicMouleActivityAdapter(SpecialUserPublicMouleListActivity.this, this,rkmLists);
        mRecyclerView.setAdapter(servicesListAdapter);
        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        relativeLayout.setVisibility(View.VISIBLE);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHeadQuater();
            }
        });
        //   hitToServer();
        //
    }

    @Override
    protected void onResume() {
        super.onResume();
        hitToServer();
    }

    @Override
    public void callBack(int postion) {

    }



    public void addHeadQuater()
    {

        Intent intent=new Intent(SpecialUserPublicMouleListActivity.this, SpecialUserPublicMouleActivity.class);
        startActivity(intent);
    }


    public void updateHeadQuater(RkmList rkmList)
    {
        Intent intent = new Intent(SpecialUserPublicMouleListActivity.this,SpecialUserPublicMouleEditActivity.class);
        intent.putExtra("crs_id",rkmList.getCrs_id());
        startActivity(intent);
    }

    public void deleteModule(final String id)
    {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(SpecialUserPublicMouleListActivity.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(SpecialUserPublicMouleListActivity.this);
        }
        builder.setTitle("Delete Public Module")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        setProgressDialoug(true);
                        AsyncController asyncController=new AsyncController(SpecialUserPublicMouleListActivity.this, SpecialUserPublicMouleListActivity.this,CallType.DELETE_MODULE,"",true);
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
        if(response!=null)
        {
            if(callType==CallType.GET_PUBLIC_MODULE_LIST)
            {
                Gson gson=new Gson();
                Result response1=gson.fromJson(response,Result.class);

                if(response1!=null)
                {
                    if(response1.getRkmList()!=null)
                    {
                        Log.e("String","value");
                        rkmLists.clear();
                        rkmLists.addAll(response1.getRkmList());
                        Log.e("value",""+rkmLists.size());
                        servicesListAdapter.notifyDataSetChanged();

                    }
                    else {
                        Toast.makeText(SpecialUserPublicMouleListActivity.this,"no data found",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(SpecialUserPublicMouleListActivity.this,"no data found",Toast.LENGTH_LONG).show();
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

                            Toast.makeText(SpecialUserPublicMouleListActivity.this, response1.getMessage(), Toast.LENGTH_LONG).show();
                            hitToServer();
                        }

                    }
                    else {
                        Toast.makeText(SpecialUserPublicMouleListActivity.this,"Headquater not added successfully",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(SpecialUserPublicMouleListActivity.this,"Headquater not added successfully",Toast.LENGTH_LONG).show();
                }
            }

            else if(callType==CallType.DELETE_MODULE)
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
                            Toast.makeText(SpecialUserPublicMouleListActivity.this, response1.getMessage(), Toast.LENGTH_LONG).show();
                            hitToServer();
                        }

                    }
                    else {
                        Toast.makeText(SpecialUserPublicMouleListActivity.this,"Module not deleted successfully",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(SpecialUserPublicMouleListActivity.this,"Module not deleted successfully",Toast.LENGTH_LONG).show();
                }
            }



        }
    }

    private void hitToServer1()
    {
        AsyncController asyncController=new AsyncController(SpecialUserPublicMouleListActivity.this,this,
                CallType.GET_PUBLIC_MODULE_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getPublicUserList(String.valueOf(loginResultPojo1.getRole()),"");
    }

    @Override
    public void onRefresh() {
        hitToServer1();
    }



}

