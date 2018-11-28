package alina.com.rms.activities.special_user;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import alina.com.rms.R;
import alina.com.rms.activities.special_user_adptor.SpecialUserHomeListAdapter;
import alina.com.rms.custom_interface.ServiceClickCallBack;

public class SpecialUserMainActivity extends  SpecialUserBaseActivity  implements ServiceClickCallBack {
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private SpecialUserHomeListAdapter servicesListAdapter;
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
        servicesListAdapter = new SpecialUserHomeListAdapter(SpecialUserMainActivity.this, this);
        mRecyclerView.setAdapter(servicesListAdapter);
        //
    }

    @Override
    public void callBack(int position) {

        if(position==0)
        {
            Intent intent = new Intent(SpecialUserMainActivity.this, SpecialUserViewFoundationList.class);
            startActivity(intent);
        }
        else if(position==1)
        {
            Intent intent = new Intent(SpecialUserMainActivity.this, SpecialUserViewMastErictionList.class);
            startActivity(intent);
        }
        else if(position==2)
        {
            Intent intent = new Intent(SpecialUserMainActivity.this, SpecialUserViewWiringList.class);
            startActivity(intent);
        }
        else if(position==3)
        {
            Intent intent = new Intent(SpecialUserMainActivity.this, SpecialUserPublicMouleListActivity.class);
            startActivity(intent);
        }

    }

}
