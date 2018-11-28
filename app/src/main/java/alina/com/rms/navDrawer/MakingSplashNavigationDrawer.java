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

public class MakingSplashNavigationDrawer {

    private List<AdapterGetterSetter> arrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    private NavigationRecyleSplashAdapter mAdapter;
    private Context context;

    public MakingSplashNavigationDrawer(Context context , RecyclerView recyclerView)
    {
        this.context=context;

        mAdapter = new NavigationRecyleSplashAdapter(arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this.context.getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setNestedScrollingEnabled(false);
     //   recyclerView.addItemDecoration(new DividerItemDecoration(this.context, LinearLayoutManager.VERTICAL));
        recyclerView.setAdapter(mAdapter);
        prepareMovieData();
    }

    private void prepareMovieData() {
        String [] array=new String[]{"About Railway\nElectrification","Progress of Railway\nElectrification"};
        TypedArray tArray = context.getResources().obtainTypedArray(
                R.array.random_splash_imgs);
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
