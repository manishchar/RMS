package alina.com.rms.activities.groupActivities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import alina.com.rms.R;
import alina.com.rms.adaptor.GroupUserListAdapter;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.custom_interface.ServiceClickCallBack;
import alina.com.rms.model.GroupResponse;
import alina.com.rms.model.GroupResponseOfResponse;
import alina.com.rms.model.Result;
import alina.com.rms.model.Userlist;
import alina.com.rms.util.CallType;
import alina.com.rms.util.LoginDB;

public class GroupGroupList extends GroupBaseActivity implements ServiceClickCallBack, AsyncCompleteListner {
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private GroupUserListAdapter servicesListAdapter;
    private List<Userlist> headquaterlist = new ArrayList<Userlist>();
    //  RelativeLayout relativeLayout;
    private AlertDialog alertDialog;
    GroupResponse groupResponse;
    GroupResponseOfResponse groupResponseOfResponse;
    List<GroupResponseOfResponse> responseOfResponses = new ArrayList<GroupResponseOfResponse>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_super_admin_group_list);
        addView();
        initUI();
    }

    @Override
    public void onResume() {
        super.onResume();
        Gson gson = new Gson();
        groupResponse = gson.fromJson(LoginDB.getUSerResponseAsJSON(GroupGroupList.this), GroupResponse.class);
        responseOfResponses.clear();
        responseOfResponses.addAll(groupResponse.getResponse());
        groupResponseOfResponse = responseOfResponses.get(0);
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
        servicesListAdapter = new GroupUserListAdapter(GroupGroupList.this, this, headquaterlist);
        mRecyclerView.setAdapter(servicesListAdapter);
        relativeLayout.setVisibility(View.VISIBLE);
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // addHeadQuater();

                Intent intent = new Intent(GroupGroupList.this, AddUserForGroup.class);
                startActivity(intent);
            }
        });

        //
    }

    private void getUserListFromServer() {
        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(GroupGroupList.this, this, CallType.GET_GROUP_USER, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getUser(groupResponseOfResponse.getUserId(), groupResponseOfResponse.getGroup());

        // GET_USER_LIST
    }


    public void deleteHQ(final String id) {
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
                        AsyncController asyncController = new AsyncController(GroupGroupList.this, GroupGroupList.this, CallType.DELETE_SUPER_ADMIN_USER, "", true);
                        asyncController.setProgressDialoug(true);
                        asyncController.deleteUserForGroup(id);
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

        Userlist headquateruserlist = headquaterlist.get(postion);
        Intent intent = new Intent(this, AddUserForGroup.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("data", headquateruserlist);
        intent.putExtras(bundle);
        startActivity(intent);
    }


    @Override
    public void callBack(int postion) {

    }


    @Override
    public void asyncCompleteListner(String response, CallType callType) {
        setProgressDialoug(false);
        if (response != null) {
            if (callType == CallType.GET_GROUP_USER) {
                Gson gson = new Gson();
                Result response1 = gson.fromJson(response, Result.class);

                if (response1 != null) {
                    if (response1.getUserlist() != null) {
                        Log.e("String", "value");
                        headquaterlist.clear();
                        headquaterlist.addAll(response1.getUserlist());
                        Log.e("value", "" + headquaterlist.size());
                        servicesListAdapter.notifyDataSetChanged();

                    } else {
                        Toast.makeText(this, "No user available", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                }
            } else if (callType == CallType.DELETE_SUPER_ADMIN_USER) {
                Gson gson = new Gson();
                Result response1 = gson.fromJson(response, Result.class);

                if (response1 != null) {
                    if (response1.getResponseCode() != null) {
                        if (response1.getResponseCode() == 1) {
                            // Log.e("String", "value");
                            Toast.makeText(this, response1.getMessage(), Toast.LENGTH_LONG).show();
                            getUserListFromServer();
                        }

                    } else {
                        Toast.makeText(this, "User not delete successfully", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(this, "User not delete successfully", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

}
