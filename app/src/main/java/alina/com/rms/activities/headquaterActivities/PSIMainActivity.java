package alina.com.rms.activities.headquaterActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import alina.com.rms.R;
import alina.com.rms.adaptor.PSIServicesListAdapter;
import alina.com.rms.custom_interface.ServiceClickCallBack;

public class PSIMainActivity extends HeaduaterBaseActivity implements ServiceClickCallBack {
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private PSIServicesListAdapter servicesListAdapter;
    private boolean intentFlag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            intentFlag=bundle.getBoolean("intentFlag");
            headingText.setText("REPORT PSI");
        }
        else {
            headingText.setText("Master");
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
        servicesListAdapter = new PSIServicesListAdapter(PSIMainActivity.this, this);
        mRecyclerView.setAdapter(servicesListAdapter);
        //
    }

    @Override
    public void callBack(int postion) {

     if (postion == 0) {


             Intent intent = new Intent(PSIMainActivity.this, HeadquaterMasterListActivity.class);
            intent.putExtra("value", postion);
            startActivity(intent);

        }
        if (postion == 1) {

                Intent intent = new Intent(PSIMainActivity.this, HeadquaterMasterListActivity.class);
                intent.putExtra("value", postion);
                startActivity(intent);

        }
        if (postion == 2) {

                Intent intent = new Intent(PSIMainActivity.this, HeadquaterMasterListActivity.class);
                intent.putExtra("value", postion);
                startActivity(intent);

        }

    }

}
