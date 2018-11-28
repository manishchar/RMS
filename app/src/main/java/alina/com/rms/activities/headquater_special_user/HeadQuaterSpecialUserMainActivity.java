package alina.com.rms.activities.headquater_special_user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import alina.com.rms.R;
import alina.com.rms.activities.headquaterActivities.HeaduaterBaseActivity;
import alina.com.rms.activities.headquaterActivities.MainActivityHeaduater;
import alina.com.rms.activities.headquater_special_user.ohe_progress.HeadQuaterSpecialUserViewProgressOHEList;
import alina.com.rms.activities.headquater_special_user.report.ReportActivityHeaduater;
import alina.com.rms.activities.headquater_special_user.sp_progress.HeadQuaterSpecialUserViewProgressSPList;
import alina.com.rms.activities.headquater_special_user.ssp_progress.HeadQuaterSpecialUserViewProgressSSPList;
import alina.com.rms.activities.headquater_special_user.tss_progress.HeadQuaterSpecialUserViewProgressTSSList;
import alina.com.rms.activities.headquater_special_user_adapter.HeadQuaterSpecialUserHomeListAdapter;
import alina.com.rms.custom_interface.ServiceClickCallBack;

public class HeadQuaterSpecialUserMainActivity extends HeadQuaterSpecialUserBaseActivity implements ServiceClickCallBack {
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private HeadQuaterSpecialUserHomeListAdapter servicesListAdapter;
    private boolean intentFlag=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            intentFlag=bundle.getBoolean("intentFlag");
            headingText.setText("BOARD DASHBOARD");
        }


        addView();
        initUI();


    }


    @Override
    public void addView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View myView = factory.inflate(R.layout.activity_main, null);
        setView(myView);
    }


    private void initUI() {

        mRecyclerView = (RecyclerView) findViewById(R.id.lvHomeScreenItems);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(2);
        mStaggeredLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
        servicesListAdapter = new HeadQuaterSpecialUserHomeListAdapter(HeadQuaterSpecialUserMainActivity.this, this);
        mRecyclerView.setAdapter(servicesListAdapter);
        //
    }

    @Override
    public void callBack(int position) {

        /*if(position==0)
        {
            Intent intent = new Intent(HeadQuaterSpecialUserMainActivity.this, HeadQuaterSpecialUserViewFoundationList.class);
            startActivity(intent);
        }
        else if(position==1)
        {
            Intent intent = new Intent(HeadQuaterSpecialUserMainActivity.this, HeadQuaterSpecialUserViewMastErictionList.class);
            startActivity(intent);
        }
        else if(position==2)
        {
            Intent intent = new Intent(HeadQuaterSpecialUserMainActivity.this, HeadQuaterSpecialUserViewWiringList.class);
            startActivity(intent);
        }
        else if(position==3)
        {
            Intent intent = new Intent(HeadQuaterSpecialUserMainActivity.this, HeadQuaterSpecialUserPublicMouleListActivity.class);
            startActivity(intent);
        }
        else if(position==4)
        {
            Intent intent = new Intent(HeadQuaterSpecialUserMainActivity.this, PublicModuleProgressSSPList.class);
            startActivity(intent);
        }*/
        if(position==0)
        {
            Intent intent = new Intent(HeadQuaterSpecialUserMainActivity.this, HeadQuaterSpecialUserViewProgressOHEList.class);
            startActivity(intent);
        }
        else if(position==1)
        {
            Intent intent = new Intent(HeadQuaterSpecialUserMainActivity.this, HeadQuaterSpecialUserViewProgressTSSList.class);
            startActivity(intent);
        }
        else if(position==2)
        {
            Intent intent = new Intent(HeadQuaterSpecialUserMainActivity.this, HeadQuaterSpecialUserViewProgressSPList.class);
            startActivity(intent);
        }
        else if(position==3)
        {
            Intent intent = new Intent(HeadQuaterSpecialUserMainActivity.this, HeadQuaterSpecialUserViewProgressSSPList.class);
            startActivity(intent);
        }
        else {
            Intent intent=new Intent(HeadQuaterSpecialUserMainActivity.this, ReportActivityHeaduater.class);
            intent.putExtra("web_View",true);
            intent.putExtra("screen_num",1);
            startActivity(intent);
        }

    }

}
