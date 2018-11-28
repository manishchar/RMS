package alina.com.rms.activities.headquaterActivities;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import alina.com.rms.R;
import alina.com.rms.activities.SplashScreen2;
import alina.com.rms.navDrawer.MakingNavigationDrawerForHeaduater;
import alina.com.rms.navDrawer.RecycleViewClickListener;
import alina.com.rms.navDrawer.RecyclerTouchListener;

public abstract class HeaduaterBaseActivity extends AppCompatActivity implements View.OnClickListener{
    public Toolbar toolbar;
    public FrameLayout container;
    public DrawerLayout drawer;
    private ImageView navigation_btn;
    public TextView headingText;
    public RelativeLayout relativeLayout;
    private Dialog openDialog,openDialog1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_base);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        headingText=(TextView)toolbar.findViewById(R.id.header_title);
        relativeLayout=(RelativeLayout)toolbar.findViewById(R.id.relAdd);
        addNavigationView();
        navigation_btn=(ImageView) toolbar.findViewById(R.id.navigation_btn);
        navigation_btn.setOnClickListener(this);
    }

    public void setView(View view)
    {

        container=(FrameLayout) findViewById(R.id.container);
        container.addView(view);


    }

    public void addNavigationView()
    {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
     /*   ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/
        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view);
        new MakingNavigationDrawerForHeaduater(HeaduaterBaseActivity.this,recyclerView);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecycleViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Movie movie = movieList.get(position);
                drawer.closeDrawers();
                //Toast.makeText(getApplicationContext(),  ""+position+" is selected!", Toast.LENGTH_SHORT).show();
                if(position==0)
                {
                    Intent intent=new Intent(HeaduaterBaseActivity.this, MainActivityHeaduater.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                }
                else if(position==1)
                {
                    Intent intent = new Intent(HeaduaterBaseActivity.this, HeaduaterGroupList.class);
                    startActivity(intent);
                }
                else if(position==2)
                {
                    Intent intent=new Intent(HeaduaterBaseActivity.this,HeaduaterUserList.class);
                    startActivity(intent);
                }
                else if(position==3)
                {
                    Intent intent = new Intent(HeaduaterBaseActivity.this, HeadquaterSectionListActivity.class);
                    startActivity(intent);
                }
                else if(position==4)
                {
                    Intent intent = new Intent(HeaduaterBaseActivity.this, PSIMainActivity.class);
                    startActivity(intent);
                }
                else if(position==5)
                {

                    Intent intent = new Intent(HeaduaterBaseActivity.this, MainActivityHeaduaterTarget.class);
                    startActivity(intent);
                   // Intent intent=new Intent(GroupBaseActivity.this,GroupWebViewActivity.class);
                  /*  Intent intent=new Intent(GroupBaseActivity.this,GroupMainActivity.class);
                    intent.putExtra("intentFlag",true);
                    startActivity(intent);*/
                }
                else if(position==6)
                {
                    Intent intent=new Intent(HeaduaterBaseActivity.this, MainActivityHeaduater.class);
                    intent.putExtra("web_View",true);
                    intent.putExtra("screen_num",1);
                    startActivity(intent);

                }
                /*else if(position==7)
                {
                    Intent intent=new Intent(HeaduaterBaseActivity.this, MainActivityHeaduater.class);
                    intent.putExtra("web_View",true);
                    intent.putExtra("screen_num",2);
                    startActivity(intent);

                }*/
                else if(position==7)
                {

                    new AlertDialog.Builder(HeaduaterBaseActivity.this)
                            .setTitle("Are you sure,you want to logout")
                            .setIcon(R.drawable.logo)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface view,int pos) {
                                    SharedPreferences pref = PreferenceManager
                                            .getDefaultSharedPreferences(HeaduaterBaseActivity.this);
                                    SharedPreferences.Editor edit = pref.edit();
                                    edit.clear();
                                    edit.commit();
                                    finish();
                                    Intent intent=new Intent(HeaduaterBaseActivity.this,SplashScreen2.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Cancel",null)
                            .show();

                }

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }

    public abstract void addView();

    @Override
    public void onClick(View var1)
    {
        if(var1==navigation_btn)
        {
            if(!drawer.isDrawerOpen(GravityCompat.START))
            {
                drawer.openDrawer(GravityCompat.START);
            }
            else {
                drawer.closeDrawers();
            }
        }
    }

    public void showDialog(String msg)
    {
        new AlertDialog.Builder(this)
                //.setTitle(msg)
                .setMessage(msg)
                .setIcon(R.drawable.logo)
                .setPositiveButton("OK", null)
                .show();


    }

    public void showDialog1(String msg)
    {
        new AlertDialog.Builder(this)
                .setTitle(msg)
                .setIcon(R.drawable.logo)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                })
                .show();


    }

    public void setProgressDialoug(boolean flag)
    {

        if(openDialog==null)
        {

            openDialog= new Dialog(HeaduaterBaseActivity.this);
            openDialog.setContentView(R.layout.progress_dialoug1);
            //openDialog.setTitle("Custom Dialog Box");
            openDialog.setCancelable(false);
            // disable scroll on touch


        }
        if(flag)
        {
            openDialog.show();
        }
        else
        {
            openDialog.dismiss();
        }

    }

    public void setUnderConstuctionDialoug(boolean flag)
    {

        if(openDialog1==null)
        {

            openDialog1= new Dialog(HeaduaterBaseActivity.this);
            openDialog1.setContentView(R.layout.under_constuction_dialoug);
           // openDialog1.setTitle("Custom Dialog Box");
            openDialog1.setCancelable(true);
            // disable scroll on touch


        }
        if(flag)
        {
            openDialog1.show();
        }
        else
        {
            openDialog1.dismiss();
        }

    }

}
