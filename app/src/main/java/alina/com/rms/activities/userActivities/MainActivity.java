package alina.com.rms.activities.userActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import alina.com.rms.R;
import alina.com.rms.activities.headquaterActivities.PSIMainActivity;
import alina.com.rms.adaptor.ServicesListAdapter;
import alina.com.rms.custom_interface.ServiceClickCallBack;

public class MainActivity extends BaseActivity implements ServiceClickCallBack {
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private ServicesListAdapter servicesListAdapter;
    private boolean intentFlag=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            intentFlag=bundle.getBoolean("intentFlag");
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
        servicesListAdapter = new ServicesListAdapter(MainActivity.this, this);
        mRecyclerView.setAdapter(servicesListAdapter);
        //
    }

    @Override
    public void callBack(int postion) {
        /*if (postion < 8) {
            Intent intent = new Intent(getActivity(), SelectedListActivity.class);
            intent.putExtra("value", postion);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.on_back_in, R.anim.on_back_out);
        } else if (postion == 8) {
            StatusCode.LIKE_POST=1;
            Intent intent = new Intent(getActivity(), StoryActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.on_back_in, R.anim.on_back_out);
        } else if (postion == 9) {
            StatusCode.LIKE_POST=2;
            Intent intent = new Intent(getActivity(), RecipiesActivity.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.on_back_in, R.anim.on_back_out);
        } else if (postion == 10) {
            StatusCode.LIKE_POST=3;
            Intent intent = new Intent(getActivity(), ArticalMain.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.on_back_in, R.anim.on_back_out);
        } else if (postion == 11) {
            Intent intent = new Intent(getActivity(), Q_and_A.class);
            startActivity(intent);
            getActivity().overridePendingTransition(R.anim.on_back_in, R.anim.on_back_out);
        }*/
        if (postion == 0) {
            if(intentFlag)
            {
                Intent intent = new Intent(MainActivity.this, WebViewActivity.class);
                startActivity(intent);
            }
            else {
                Intent intent = new Intent(MainActivity.this, OHEActivity.class);
                startActivity(intent);
            }


        }
        else if (postion == 1) {
            Intent intent = new Intent(MainActivity.this, PSIMainActivity.class);
            if(intentFlag) {
                intent.putExtra("intentFlag", intentFlag);
            }
            startActivity(intent);
        }

    }

}
