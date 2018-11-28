package alina.com.rms.activities.groupActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import alina.com.rms.R;
import alina.com.rms.adaptor.GroupServicesListAdapter;
import alina.com.rms.custom_interface.ServiceClickCallBack;

public class GroupMainActivity extends GroupBaseActivity implements ServiceClickCallBack {
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private GroupServicesListAdapter servicesListAdapter;
    private boolean intentFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            intentFlag = bundle.getBoolean("intentFlag");
            headingText.setText("REPORT DASHBOARD");
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
        servicesListAdapter = new GroupServicesListAdapter(GroupMainActivity.this, this);
        mRecyclerView.setAdapter(servicesListAdapter);
        //
    }

    @Override
    public void callBack(int postion) {

        if (postion == 0) {

            Intent intent = new Intent(GroupMainActivity.this, GroupWebViewActivity.class);
            startActivity(intent);


        } else if (postion == 1) {
            Intent intent = new Intent(GroupMainActivity.this, GroupPSIMainActivity.class);
            startActivity(intent);
        } else if (postion == 2) {

        } else if (postion == 4) {
            Intent intent = new Intent(GroupMainActivity.this, GroupGroupList.class);
            startActivity(intent);
        }
       /* else if (postion == 5) {
            Intent intent = new Intent(GroupMainActivity.this, SuperAdminGroupList.class);
            startActivity(intent);
        }*/

    }

}
