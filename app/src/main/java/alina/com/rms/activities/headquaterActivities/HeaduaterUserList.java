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
import alina.com.rms.adaptor.HeadquaterUserListAdapter;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.custom_interface.ServiceClickCallBack;
import alina.com.rms.model.Response;
import alina.com.rms.model.Result;
import alina.com.rms.model.Userlist;
import alina.com.rms.util.CallType;
import alina.com.rms.util.LoginDB;

public class HeaduaterUserList extends HeaduaterBaseActivity implements ServiceClickCallBack,AsyncCompleteListner,SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private HeadquaterUserListAdapter servicesListAdapter;
    private List<Userlist> headquaterlist=new ArrayList<Userlist>();
    //  RelativeLayout relativeLayout;
    private AlertDialog alertDialog;
    private Response response;
    private List<Response> response1;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_super_admin_group_list);
        Gson gson=new Gson();
        Type type = new TypeToken<List<Response>>() {}.getType();
        response1=gson.fromJson(LoginDB.getLoginResponseAsJSON(HeaduaterUserList.this),type);
        response=response1.get(0);
        addView();
        initUI();
    }
    @Override
    public void onResume()
    {
        super.onResume();
        getUserListFromServer();
    }

    @Override
    public void addView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View myView = factory.inflate(R.layout.activity_super_admin_group_list, null);
        setView(myView);
        //initUI();
    }

    private void initUI() {
        headingText.setText("User List");

        mRecyclerView = (RecyclerView) findViewById(R.id.lvHomeScreenItems);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(1);
        mStaggeredLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
        servicesListAdapter = new HeadquaterUserListAdapter(HeaduaterUserList.this, this,headquaterlist);
        mRecyclerView.setAdapter(servicesListAdapter);
        relativeLayout.setVisibility(View.VISIBLE);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // addHeadQuater();

                Intent intent=new Intent(HeaduaterUserList.this,AddUserForHeaduater.class);
                startActivity(intent);
            }
        });


        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);
        //
    }

    private void getUserListFromServer()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(HeaduaterUserList.this,this,CallType.GET_SUPER_ADMIN_USER_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getHeadquaterUserList(response.getUserId());

       // GET_USER_LIST
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
                .setMessage("Are you sure you want to delete this user?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        setProgressDialoug(true);
                        AsyncController asyncController=new AsyncController(HeaduaterUserList.this, HeaduaterUserList.this,CallType.DELETE_SUPER_ADMIN_USER,"",true);
                        asyncController.setProgressDialoug(true);
                        asyncController.deleteUserForGroup1(id);
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

    Userlist headquateruserlist=headquaterlist.get(postion);
    Intent intent=new Intent(this,AddUserForHeaduater.class);
    Bundle bundle=new Bundle();
    bundle.putSerializable("data",headquateruserlist);
    intent.putExtras(bundle);
    startActivity(intent);
    }


    @Override
    public void callBack(int postion) {

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
            if(callType==CallType.GET_SUPER_ADMIN_USER_LIST)
            {
                Gson gson=new Gson();
                Result response1=gson.fromJson(response,Result.class);

                if(response1!=null)
                {
                    if(response1.getGrouplist()!=null)
                    {
                        Log.e("String","value");
                        headquaterlist.clear();
                        headquaterlist.addAll(response1.getGrouplist());
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
            else if(callType==CallType.DELETE_SUPER_ADMIN_USER)
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
                            getUserListFromServer();
                        }

                    }
                    else {
                        Toast.makeText(this,"User not delete successfully",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(this,"User not delete successfully",Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private void getUserListFromServer1()
    {
        AsyncController asyncController=new AsyncController(HeaduaterUserList.this,this,CallType.GET_SUPER_ADMIN_USER_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getHeadquaterUserList(response.getUserId());

        // GET_USER_LIST
    }
    @Override
    public void onRefresh() {
        getUserListFromServer1();
    }

}
