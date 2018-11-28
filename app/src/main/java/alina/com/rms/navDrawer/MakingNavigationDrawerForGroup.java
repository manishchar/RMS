package alina.com.rms.navDrawer;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import alina.com.rms.R;


/**
 * Created by HP on 01-12-2017.
 */

public class MakingNavigationDrawerForGroup {

    private List<AdapterGetterSetter> arrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NavigationRecyleAdapter mAdapter;
    private Context context;

    public MakingNavigationDrawerForGroup(Context context , RecyclerView recyclerView)
    {
        this.context=context;

        mAdapter = new NavigationRecyleAdapter(arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.context.getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
     //   recyclerView.addItemDecoration(new DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        prepareMovieData();
    }

    private void prepareMovieData() {
        String [] array=new String[]{"DashBoard","User List","User Manual","Logout"};
        TypedArray tArray = context.getResources().obtainTypedArray(
                R.array.random_imgs_super_admin);
        //int count = tArray.length();
      //  int[] arr = new int[count];
        AdapterGetterSetter movie ;
        for (int i=0;i<array.length;i++)
        {
            movie = new AdapterGetterSetter();
         //   arr[i] = tArray.getResourceId(i, 0);
            movie.setItem_image(tArray.getResourceId(i, 0));
            movie.setItem_name(array[i]);
            arrayList.add(movie);
        }
        tArray.recycle();
        mAdapter.notifyDataSetChanged();

    }
}
