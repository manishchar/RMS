package alina.com.rms.activities;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import alina.com.rms.R;
import alina.com.rms.app.AppController;
import alina.com.rms.navDrawer.MakingSplashNavigationDrawer;
import alina.com.rms.navDrawer.RecycleViewClickListener;
import alina.com.rms.navDrawer.RecyclerTouchListener;
import alina.com.rms.reciver.ConnectivityReceiver;

public class SplashScreen2 extends AppCompatActivity implements View.OnClickListener,
        ConnectivityReceiver.ConnectivityReceiverListener {
    private WebView webView;
    private String postUrl = "http://alinasoftwares.in/testapp/newDesign/htmlabout/";
    private Dialog openDialog;
    private LinearLayout login_linear;
    /*  private List<LoginResultPojo> loginResultPojo;
      private LoginResultPojo loginResultPojo1;
      private String login_response;*/
    public Toolbar toolbar;
    private TextView textViewHeading;
    public DrawerLayout drawer;
    private ImageView navigation_btn;
    private android.widget.ProgressBar progressBar;
    private boolean onBackPressedFlag = false;
    private String about_us = "http://alinasoftwares.in/testapp/api/aboutUs";
    private String ericttion_progress = "http://alinasoftwares.in/testapp/api/electrificationprogess";
    private String sanctionrd_progress = "http://alinasoftwares.in/testapp/api/sanctionedProgress";
    private String agencis = "http://alinasoftwares.in/testapp/api/agencyprogress";
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String TAG = SplashScreen2.class.getName();
    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;
    private static final int PERMISSION_REQUEST_CODE = 10225;
    private ImageView imgView,home_btn;
    private TextView textRefresh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        addNavigationView();
        home_btn = (ImageView)toolbar.findViewById(R.id.home_btn);
        home_btn.setVisibility(View.GONE);
        navigation_btn = (ImageView) toolbar.findViewById(R.id.navigation_btn);
        navigation_btn.setOnClickListener(this);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        progressBar = (android.widget.ProgressBar) findViewById(R.id.progressBar);
        login_linear = (LinearLayout) toolbar.findViewById(R.id.login_linear);
        textViewHeading = (TextView) toolbar.findViewById(R.id.header_title);
        textRefresh = (TextView) findViewById(R.id.textRefresh);
        textRefresh.setOnClickListener(this);
        home_btn.setOnClickListener(this);
        imgView = (ImageView) findViewById(R.id.imgView);
        webView = (WebView) findViewById(R.id.webView);
        login_linear.setVisibility(View.VISIBLE);
        webView.setWebViewClient(new SplashScreen2.MyBrowser());
        webView.setHorizontalScrollBarEnabled(false);
        webView.getSettings().setLoadsImagesAutomatically(true);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        webView.loadUrl(postUrl);
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright, android.R.color.holo_green_light,
                android.R.color.holo_orange_light, android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                if (progressBar.getVisibility() == View.VISIBLE) {
                    progressBar.setVisibility(View.GONE);
                }
                webView.reload();

            }
        });
        textViewHeading.setText("Welcome");
        login_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // This method will be executed once the timer is over
                // Start your app main activity
              /*  if(LoginDB.getLoginFlag(SplashScreen2.this))
                {
                    Gson gson = new Gson();
                    Type type = new TypeToken<List<LoginResultPojo>>() {}.getType();
     *//*   String json = gson.toJson(login_response, type);
        System.out.println(json);*//*
                    login_response=LoginDB.getLoginResponseAsJSON(SplashScreen2.this);
                    loginResultPojo = gson.fromJson(login_response, type);
                    loginResultPojo1=loginResultPojo.get(0);
                    if(loginResultPojo1.getRole()==0)
                    {
                        Intent intent=new Intent(SplashScreen2.this,MainActivitySuperAdmin.class);
                        startActivity(intent);
                    }
                    else if(loginResultPojo1.getRole()==1)
                    {
*//*                        Intent intent=new Intent(SplashScreen22.this,MainActivity.class);
                        startActivity(intent);*//*
                        Intent intent=new Intent(SplashScreen2.this,MainActivityHeaduater.class);
                        startActivity(intent);
                    }
                    else if(loginResultPojo1.getRole()==2)
                    {
                        Intent intent=new Intent(SplashScreen2.this,GroupMainActivity.class);
                        startActivity(intent);
                    }
*//*                    else if(loginResultPojo1.getRole()==3)

                    {
                        Intent intent=new Intent(SplashScreen2.this,MainActivity.class);
                        startActivity(intent);
                    }*//*
                }
                else {*/
                Intent i = new Intent(SplashScreen2.this, LoginActivity.class);
                startActivity(i);
                /*}*/
                // close this activity
                finish();
            }
        });


        //  checkConnection();
    }


    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            boolean isConnected = ConnectivityReceiver.isConnected();
            if (isConnected) {
                if (url.equalsIgnoreCase("http://alinasoftwares.in/")) {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(i);
                } else {
                    if (!mSwipeRefreshLayout.isRefreshing()) {
                        progressBar.setVisibility(View.VISIBLE);
                    }
                    view.loadUrl(url);
                }

            } else {
                imgView.setVisibility(View.VISIBLE);
                textRefresh.setVisibility(View.VISIBLE);
                webView.setVisibility(View.GONE);
                drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            }
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            if (mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(false);

            } else {
                progressBar.setVisibility(View.GONE);
            }
            if(webView.getUrl().equalsIgnoreCase(postUrl))
            {
                home_btn.setVisibility(View.GONE);
            }
            else {
                home_btn.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {


        }
    }


    public void addNavigationView() {
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
     /*   ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();*/
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        new MakingSplashNavigationDrawer(SplashScreen2.this, recyclerView);
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getApplicationContext(), recyclerView, new RecycleViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                //Movie movie = movieList.get(position);
                drawer.closeDrawers();
                //Toast.makeText(getApplicationContext(),  ""+position+" is selected!", Toast.LENGTH_SHORT).show();


                //Changes below on 29/10/19

                onBackPressedFlag = true;
                if (position == 0) {
                    webView.loadUrl(about_us);
                } else if (position == 1) {
                   // webView.loadUrl(agencis);
                    webView.loadUrl(sanctionrd_progress);
                } /*else if (position == 2) {
                    webView.loadUrl(ericttion_progress);
                } else if (position == 3) {
                    webView.loadUrl(sanctionrd_progress);
                }*/
                progressBar.setVisibility(View.VISIBLE);
                //Changes above on 29/10/19

                /*  else if(position==5)
                {

                    // Intent intent=new Intent(GroupSplashScreen2.this,GroupWebViewActivity.class);
                    Intent intent=new Intent(SplashScreen2.this,MainActivity.class);
                    intent.putExtra("intentFlag",true);
                    startActivity(intent);
                }
                else if(position==7)
                {

                    new AlertDialog.Builder(SplashScreen2.this)
                            .setTitle("Are you sure,you want to logout")
                            .setIcon(R.drawable.logo)
                            .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface view,int pos) {
                                    SharedPreferences pref = PreferenceManager
                                            .getDefaultSharedPreferences(SplashScreen2.this);
                                    SharedPreferences.Editor edit = pref.edit();
                                    edit.clear();
                                    edit.commit();
                                    finish();
                                    Intent intent=new Intent(SplashScreen2.this,LoginActivity.class);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("Cancel",null)
                            .show();

                }*/

            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));
    }


    @Override
    public void onClick(View var1) {
        if (var1.getId() == R.id.navigation_btn) {
            if (!drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.openDrawer(GravityCompat.START);
            } else {
                drawer.closeDrawers();
            }
        } else if (var1.getId() == R.id.textRefresh) {
            checkConnection();
        }
        else if(var1.getId() == R.id.home_btn)
        {

            webView.loadUrl(postUrl);
        }

    }

/*    public void onBackPressed()
    {
        if(onBackPressedFlag)
        {
            progressBar.setVisibility(View.VISIBLE);
            onBackPressedFlag=false;
            webView.loadUrl(postUrl);

        }
        else {
            super.onBackPressed();
        }
    }
    */


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (keyCode) {
                case KeyEvent.KEYCODE_BACK:
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawers();

                    }
                    else if(webView.getUrl().equalsIgnoreCase(postUrl))
                    {
                        finish();
                    }
                    else if (webView.canGoBack()) {
                        webView.goBack();
                    } else {
                        if (onBackPressedFlag) {
                            progressBar.setVisibility(View.VISIBLE);
                            onBackPressedFlag = false;
                            webView.loadUrl(postUrl);

                        } else {
                            finish();
                        }
                    }
                    return true;
            }

        }
        return super.onKeyDown(keyCode, event);
    }


    // Method to manually check connection status
    private void checkConnection() {
        boolean isConnected = ConnectivityReceiver.isConnected();
        if (isConnected) {
            imgView.setVisibility(View.GONE);
            textRefresh.setVisibility(View.GONE);
            webView.reload();
            new Handler().postDelayed(new Runnable() {

                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

                @Override
                public void run() {
                    webView.setVisibility(View.VISIBLE);
                }
            }, 2000);

            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            imgView.setVisibility(View.VISIBLE);
            textRefresh.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

        // register connection status listener
        AppController.getInstance().setConnectivityListener(this);
        checkConnection();
    }

    /**
     * Callback will be triggered when there is change in
     * network connection
     */
    @Override
    public void onNetworkConnectionChanged(boolean isConnected) {
        if (isConnected) {
            imgView.setVisibility(View.GONE);
            textRefresh.setVisibility(View.GONE);
            webView.reload();
            new Handler().postDelayed(new Runnable() {

                /*
                 * Showing splash screen with a timer. This will be useful when you
                 * want to show case your app logo / company
                 */

                @Override
                public void run() {
                    webView.setVisibility(View.VISIBLE);
                }
            }, 2000);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);

        } else {
            imgView.setVisibility(View.VISIBLE);
            textRefresh.setVisibility(View.VISIBLE);
            webView.setVisibility(View.GONE);
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }
}
