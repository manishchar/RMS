package alina.com.rms.activities.superAdminActivities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import alina.com.rms.R;
import alina.com.rms.adaptor.SuperAdminHQListAdapter;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.custom_interface.ServiceClickCallBack;
import alina.com.rms.model.Headquaterlist;
import alina.com.rms.model.Result;
import alina.com.rms.util.CallType;
import alina.com.rms.util.Util;

public class SuperAdminHQList extends SuperAdminBaseActivity implements ServiceClickCallBack,AsyncCompleteListner,SwipeRefreshLayout.OnRefreshListener
{

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private SuperAdminHQListAdapter servicesListAdapter;
    private List<Headquaterlist> headquaterlist=new ArrayList<Headquaterlist>();
  //  RelativeLayout relativeLayout;
    private AlertDialog alertDialog;
    private String add_head_quater_name;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_super_admin_hqlist);
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
        AsyncController asyncController=new AsyncController(SuperAdminHQList.this,this,CallType.GET_HQ_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getHQList();
    }

    private void initUI() {
        headingText.setText("Headquater List");

        mRecyclerView = (RecyclerView) findViewById(R.id.lvHomeScreenItems);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(1);
        mStaggeredLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
        servicesListAdapter = new SuperAdminHQListAdapter(SuperAdminHQList.this, this,headquaterlist);
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
        hitToServer();
        //
    }

    @Override
    public void callBack(int postion) {

    }



    public void addHeadQuater()
    {

        final Dialog dialogView = new Dialog(SuperAdminHQList.this);
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);

// ...Irrelevant code for customizing the buttons and title

         dialogView.setContentView(R.layout.add_headquater_dialog);
        //dialogBuilder.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.edit_add_head_quater);
        Button button_ok=(Button)dialogView.findViewById(R.id.button_ok);
        Button button_cancel=(Button)dialogView.findViewById(R.id.button_cancel);
        dialogView.show();
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().isEmpty())
                {
                    if(editText.getText().toString().trim().length()>0)
                    {
                        add_head_quater_name=editText.getText().toString();
                        setProgressDialoug(true);
                        AsyncController asyncController=new AsyncController(SuperAdminHQList.this,SuperAdminHQList.this,CallType.ADD_HEAD_QUATER,"",true);
                        asyncController.setProgressDialoug(true);
                        asyncController.addHeadQuaterForSuperAdmin(add_head_quater_name);
                        dialogView.dismiss();
                    }
                    else {
                        Util.showLongToast(SuperAdminHQList.this,"Please enter headquater");
                        editText.requestFocus();
                        return;
                    }
                }
                else {
                    Util.showLongToast(SuperAdminHQList.this,"Please enter headquater");
                    editText.requestFocus();
                    return;
                }

            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismiss();
            }
        });

    }


    public void updateHeadQuater(final String id,String add_head_quater_name1)
    {

// ...Irrelevant code for customizing the buttons and title
        final Dialog dialogView = new Dialog(SuperAdminHQList.this);
        dialogView.requestWindowFeature(Window.FEATURE_NO_TITLE);

// ...Irrelevant code for customizing the buttons and title

        dialogView.setContentView(R.layout.add_headquater_dialog);

        final EditText editText = (EditText) dialogView.findViewById(R.id.edit_add_head_quater);
        editText.setText(add_head_quater_name1);
        Button button_ok=(Button)dialogView.findViewById(R.id.button_ok);
        button_ok.setText("Update");
        Button button_cancel=(Button)dialogView.findViewById(R.id.button_cancel);

        dialogView.show();
        button_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!editText.getText().toString().isEmpty())
                {
                    if(editText.getText().toString().trim().length()>0)
                    {   setProgressDialoug(true);
                        add_head_quater_name=editText.getText().toString();
                        AsyncController asyncController=new AsyncController(SuperAdminHQList.this,SuperAdminHQList.this,CallType.UPDATE_HEAD_QUATER,"",true);
                        asyncController.setProgressDialoug(true);
                        asyncController.updateHeadQuaterForSuperAdmin(add_head_quater_name,id);
                        dialogView.dismiss();
                    }
                    else {
                        Util.showLongToast(SuperAdminHQList.this,"Please enter headquater to update");
                        editText.requestFocus();
                        return;
                    }
                }
                else {
                    Util.showLongToast(SuperAdminHQList.this,"Please enter headquater to update");
                    editText.requestFocus();
                    return;
                }

            }
        });
        button_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogView.dismiss();
            }
        });

    }

    public void deleteHQ(final String id)
    {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(SuperAdminHQList.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(SuperAdminHQList.this);
        }
        builder.setTitle("Delete Headquater")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        setProgressDialoug(true);
                        AsyncController asyncController=new AsyncController(SuperAdminHQList.this,SuperAdminHQList.this,CallType.DELETE_HEAD_QUATER,"",true);
                        asyncController.setProgressDialoug(true);
                        asyncController.deleteHeadQuaterForSuperAdmin(id);
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
            if(callType==CallType.GET_HQ_LIST)
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
                        servicesListAdapter.notifyDataSetChanged();

                    }
                    else {
                        Toast.makeText(SuperAdminHQList.this,"no data found",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(SuperAdminHQList.this,"no data found",Toast.LENGTH_LONG).show();
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

                            Toast.makeText(SuperAdminHQList.this, response1.getMessage(), Toast.LENGTH_LONG).show();
                            hitToServer();
                        }

                    }
                    else {
                        Toast.makeText(SuperAdminHQList.this,"Headquater not added successfully",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(SuperAdminHQList.this,"Headquater not added successfully",Toast.LENGTH_LONG).show();
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
                            Toast.makeText(SuperAdminHQList.this, response1.getMessage(), Toast.LENGTH_LONG).show();
                            hitToServer();
                        }

                    }
                    else {
                        Toast.makeText(SuperAdminHQList.this,"Headquater not update successfully",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(SuperAdminHQList.this,"Headquater not update successfully",Toast.LENGTH_LONG).show();
                }
            }

            else if(callType==CallType.DELETE_HEAD_QUATER)
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
                            Toast.makeText(SuperAdminHQList.this, response1.getMessage(), Toast.LENGTH_LONG).show();
                            hitToServer();
                        }

                    }
                    else {
                        Toast.makeText(SuperAdminHQList.this,"Headquater not delete successfully",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(SuperAdminHQList.this,"Headquater not delete successfully",Toast.LENGTH_LONG).show();
                }
            }



        }
    }

    private void hitToServer1()
    {
        AsyncController asyncController=new AsyncController(SuperAdminHQList.this,this,CallType.GET_HQ_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getHQList();
    }

    @Override
    public void onRefresh() {
        hitToServer1();
    }



}
