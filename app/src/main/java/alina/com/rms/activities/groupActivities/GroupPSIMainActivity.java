package alina.com.rms.activities.groupActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;

import alina.com.rms.R;
import alina.com.rms.adaptor.PSIServicesListAdapter;
import alina.com.rms.custom_interface.ServiceClickCallBack;

public class GroupPSIMainActivity extends GroupBaseActivity implements ServiceClickCallBack {
    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private PSIServicesListAdapter servicesListAdapter;
    private boolean intentFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_main);
        headingText.setText("REPORT PSI");

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
        servicesListAdapter = new PSIServicesListAdapter(GroupPSIMainActivity.this, this);
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

            Intent intent = new Intent(GroupPSIMainActivity.this, GroupTSSWebViewActivity.class);
            intent.putExtra("screen_name", "TSS");
            startActivity(intent);


        }
        if (postion == 1) {

            Intent intent = new Intent(GroupPSIMainActivity.this, GroupWebViewActivity.class);
            intent.putExtra("screen_name", "SP");
            startActivity(intent);


        }
        if (postion == 2) {

            Intent intent = new Intent(GroupPSIMainActivity.this, GroupWebViewActivity.class);
            intent.putExtra("screen_name", "SSP");
            startActivity(intent);

        }

    }

}
