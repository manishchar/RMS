package alina.com.rms.activities.reporting_user;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import alina.com.rms.R;
import alina.com.rms.adaptor.ReportingProjectListAdapter;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.custom_interface.ServiceClickCallBack;
import alina.com.rms.model.Result;
import alina.com.rms.util.CallType;
import alina.com.rms.util.SpacesItemDecoration;

public class ReportingProjectActivity extends AppCompatActivity implements ServiceClickCallBack, AsyncCompleteListner {
    RecyclerView recyclerView;
    List<String> projpairValues = new ArrayList<String>(0);
    List<String> projKeyValues = new ArrayList<String>(0);
    private Dialog openDialog;
    ReportingProjectListAdapter reportingProjectListAdapter;
    ImageView back_btn;
    private Toolbar toolbar;
    String reportType;
    String type = "";
    String screenName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getIntent().getExtras();
        reportType = bundle.getString("reportType");
        type = bundle.getString("type");
        screenName = bundle.getString("screenName");
        setContentView(R.layout.activity_reporting_project);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        TextView header_title = (TextView) findViewById(R.id.header_title);
        header_title.setText(screenName);
        back_btn = (ImageView) findViewById(R.id.back_btn);


        back_btn.setScaleType(ImageView.ScaleType.FIT_CENTER);
        back_btn.setImageResource(R.drawable.back_arrow);
        back_btn.getLayoutParams().width = 60;
        /* back_btn.getLayoutParams().height = 50;*/


        recyclerView = (RecyclerView) findViewById(R.id.typeRecycler);
        GridLayoutManager glmBar = new GridLayoutManager(ReportingProjectActivity.this, 2);
        glmBar.setOrientation(GridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(glmBar);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        recyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        reportingProjectListAdapter = new ReportingProjectListAdapter(this, this, projpairValues);
        recyclerView.setAdapter(reportingProjectListAdapter);
        //dashBoardMenuRecycler.addOnItemTouchListener(new RecyclerItemClickListener(this, dashBoardMenuRecycler, this));
        getProjectList();
        back_btn.setVisibility(View.VISIBLE);
        back_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }


    public void getProjectList() {
        setProgressDialoug(true);
        AsyncController asyncController = new AsyncController(this, this, CallType.GET_PROJECT_LIST, "", true);
        asyncController.setProgressDialoug(true);
        asyncController.getProjectList1();
    }

    @Override
    public void callBack(int postion) {
        Intent intent = new Intent(this, ReportWebViewActivity.class);
        intent.putExtra("screenName", screenName);
        intent.putExtra("type", type);
        intent.putExtra("reportType", reportType);
        intent.putExtra("project_id", projKeyValues.get(postion));
        String pairValue1 = "";
        if (projpairValues.get(postion).trim().toLowerCase().equalsIgnoreCase("all project")) {
            pairValue1 = "MobReportAllProject";
            Log.e("Mob", pairValue1);
        } else if (projpairValues.get(postion).trim().toLowerCase().equalsIgnoreCase("all agency")) {
            pairValue1 = "MobReportAllAgency";
            Log.e("Mob", pairValue1);

        }
        intent.putExtra("project_name", pairValue1);
        startActivity(intent);
    }

    @Override
    public void asyncCompleteListner(String response, CallType callType) {
        setProgressDialoug(false);
        if (callType == CallType.GET_PROJECT_LIST) {
            Gson gson = new Gson();
            Result response1 = gson.fromJson(response, Result.class);

            if (response1 != null) {

                if (response1.getProjectList() != null) {
                    projpairValues.clear();
                    projKeyValues.clear();
                    Log.e("String", "value");
                    for (int i = 0; i < response1.getProjectList().size(); i++) {

                        projpairValues.add(response1.getProjectList().get(i).getProjName());
                        projKeyValues.add(response1.getProjectList().get(i).getProjectId());


                    }
                    reportingProjectListAdapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(this, "no data found", Toast.LENGTH_LONG).show();
                }


            }
        }
    }


    public void setProgressDialoug(boolean flag) {

        if (openDialog == null) {

            openDialog = new Dialog(this);
            openDialog.setContentView(R.layout.progress_dialoug1);
            //openDialog.setTitle("Custom Dialog Box");
            openDialog.setCancelable(false);
            // disable scroll on touch


        }
        if (flag) {
            openDialog.show();
        } else {
            openDialog.dismiss();
        }

    }


}
