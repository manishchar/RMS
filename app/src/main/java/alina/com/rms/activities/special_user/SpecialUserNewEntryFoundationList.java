package alina.com.rms.activities.special_user;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import alina.com.rms.R;
import alina.com.rms.activities.special_user_adptor.SpecialUserFoundationListAdapter;
import alina.com.rms.activities.superAdminActivities.PublicMouleSuperAdminActivity;
import alina.com.rms.controller.AsyncCompleteListner;
import alina.com.rms.controller.AsyncController;
import alina.com.rms.custom_interface.ServiceClickCallBack;
import alina.com.rms.model.GetFoundationDatum;
import alina.com.rms.model.LoginResultPojo;
import alina.com.rms.model.Result;
import alina.com.rms.model.RkmList;
import alina.com.rms.util.CallType;
import alina.com.rms.util.CameraAndGalleryUtil;
import alina.com.rms.util.LocationProvider;
import alina.com.rms.util.LocationTrack;
import alina.com.rms.util.LoginDB;
import alina.com.rms.util.Util;

public class SpecialUserNewEntryFoundationList extends SpecialUserBaseActivity implements ServiceClickCallBack,AsyncCompleteListner,SwipeRefreshLayout.OnRefreshListener
,LocationProvider.LocationCallback{

    private RecyclerView mRecyclerView;
    private StaggeredGridLayoutManager mStaggeredLayoutManager;
    private SpecialUserFoundationListAdapter servicesListAdapter;
    private List<GetFoundationDatum> getFoundationData=new ArrayList<GetFoundationDatum>();
  //  RelativeLayout relativeLayout;
    private AlertDialog alertDialog;
    private String add_head_quater_name;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<LoginResultPojo> loginResultPojo;
    private LoginResultPojo loginResultPojo1;
    private String login_response;
    private String section_id;
    private Button submitBtn;
    private String date;

    private boolean gps_enabled,network_enabled;

    private LocationTrack locationTrack;

    private CameraAndGalleryUtil cameraAndGalleryUtil;
    private File output1;


    private LatLng l1;
    double longitude = 0;
    double latitude = 0;
    LocationManager lm;

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_super_admin_hqlist);
        Gson gson = new Gson();
        Type type = new TypeToken<List<LoginResultPojo>>() {}.getType();
     /*   String json = gson.toJson(login_response, type);
        System.out.println(json);*/

        lm = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        login_response= LoginDB.getLoginResponseAsJSON(SpecialUserNewEntryFoundationList.this);
        loginResultPojo = gson.fromJson(login_response, type);
        loginResultPojo1=loginResultPojo.get(0);
        Bundle bundle=getIntent().getExtras();
        if(bundle!=null)
        {
            section_id=bundle.getString("section_id");
            date=bundle.getString("date");
        }

        if(locationTrack==null)
        {
            locationTrack = new LocationTrack(SpecialUserNewEntryFoundationList.this);
            // setLocation();
        }

        addView();
        initUI();
    }

    @Override
    public void addView() {
        LayoutInflater factory = LayoutInflater.from(this);
        View myView = factory.inflate(R.layout.activity_super_admin_fooundation_list, null);
        setView(myView);
    }



    private void initUI()
    {
        headingText.setText("Add Foundation List");

        submitBtn=(Button)findViewById(R.id.submitBtn);
        mRecyclerView = (RecyclerView) findViewById(R.id.lvHomeScreenItems);
        mStaggeredLayoutManager = new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL);
        mStaggeredLayoutManager.setSpanCount(1);
        mStaggeredLayoutManager.setGapStrategy(StaggeredGridLayoutManager.GAP_HANDLING_MOVE_ITEMS_BETWEEN_SPANS);
        mRecyclerView.setLayoutManager(mStaggeredLayoutManager);
        servicesListAdapter = new SpecialUserFoundationListAdapter(SpecialUserNewEntryFoundationList.this, this,getFoundationData);
        mRecyclerView.setAdapter(servicesListAdapter);
        // SwipeRefreshLayout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setOnRefreshListener(this);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary,
                android.R.color.holo_green_dark,
                android.R.color.holo_orange_dark,
                android.R.color.holo_blue_dark);

        relativeLayout.setVisibility(View.GONE);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitToserver();
            }
        });
        relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addHeadQuater();
            }
        });
     //   hitToServer();
        //
    }

    private void submitToserver() {

        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(SpecialUserNewEntryFoundationList.this,this,
                CallType.SAVE_FOUNDATION_ITEM,"",true);
       // asyncController.setProgressDialoug(true);
        asyncController.saveFoundationListItem(getFoundationData,date,"");

    }

    private void hitToServer()
    {
        setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(SpecialUserNewEntryFoundationList.this,this,
                CallType.GET_FOUNDATION_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getFoundationList(section_id);

    }


    @Override
    public void callBack(int postion) {

    }



    public void addHeadQuater()
    {

        Intent intent=new Intent(SpecialUserNewEntryFoundationList.this, PublicMouleSuperAdminActivity.class);
        startActivity(intent);
    }


    public void updateHeadQuater(RkmList rkmList)
    {
        Intent intent = new Intent(SpecialUserNewEntryFoundationList.this,PublicMouleSuperAdminActivity.class);
      /*  Bundle bundle=new Bundle();
        bundle.putSerializable("RKM_Data",rkmList);
        intent.putExtras(bundle);*/
        intent.putExtra("crs_id",rkmList.getCrs_id());
        startActivity(intent);

    }



    public void deleteModule(final String id)
    {

        AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(SpecialUserNewEntryFoundationList.this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(SpecialUserNewEntryFoundationList.this);
        }
        builder.setTitle("Delete Public Module")
                .setMessage("Are you sure you want to delete this entry?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        setProgressDialoug(true);
                        AsyncController asyncController=new AsyncController(SpecialUserNewEntryFoundationList.this, SpecialUserNewEntryFoundationList.this,CallType.DELETE_MODULE,"",true);
                        asyncController.setProgressDialoug(true);
                        asyncController.deletePublicModuleForSuperAdmin(id);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // do nothing
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();

    }

    @Override
    public void handleNewLocation(Location location) {
        Log.d("GEt Location", location.toString());

        double currentLatitude = location.getLatitude();
        double currentLongitude = location.getLongitude();
        LatLng latLng = new LatLng(currentLatitude, currentLongitude);

        Log.e("Lat"+latLng.latitude,"Long"+latLng.longitude);


    }

    @Override
    public void asyncCompleteListner(String response, CallType callType) {
        setProgressDialoug(false);
        if(mSwipeRefreshLayout.isRefreshing())
        {
            // Stopping swipe refresh
            mSwipeRefreshLayout.setRefreshing(false);
        }
        //Log.e("response",response);
        ///return;


        if(response!=null)
        {
            if(callType==CallType.GET_FOUNDATION_LIST)
            {

                Gson gson=new Gson();
                Result response1=gson.fromJson(response,Result.class);

                if(response1!=null)
                {
                    if(response1.getGetFoundationData()!=null)
                    {
                        Log.e("String","value");
                        getFoundationData.clear();
                        getFoundationData.addAll(response1.getGetFoundationData());
                        Log.e("value",""+getFoundationData.size());
                        servicesListAdapter.notifyDataSetChanged();
                    if(getFoundationData.size()<1)
                    {
                        submitBtn.setVisibility(View.INVISIBLE);
                    }
                    else {
                        submitBtn.setVisibility(View.VISIBLE);
                    }
                    }
                    else {
                        Toast.makeText(SpecialUserNewEntryFoundationList.this,"no data found",Toast.LENGTH_LONG).show();
                    }
                }
                else {
                    Toast.makeText(SpecialUserNewEntryFoundationList.this,"no data found",Toast.LENGTH_LONG).show();
                }


            }
            else if(callType==CallType.SAVE_FOUNDATION_ITEM)
            {
                Gson gson=new Gson();
                Result response1=gson.fromJson(response,Result.class);
                if(response1!=null)
                {
                    if(response1.getResponseCode()==1)
                    {
                        Util.showLongToast(getApplicationContext(),response1.getMessage());
                        finish();
                    }
                    else {
                        Util.showLongToast(getApplicationContext(),response1.getMessage());
                    }
                }
            }







        }
    }

    private void hitToServer1()
    {
        //setProgressDialoug(true);
        AsyncController asyncController=new AsyncController(SpecialUserNewEntryFoundationList.this,this,
                CallType.GET_FOUNDATION_LIST,"",true);
        asyncController.setProgressDialoug(true);
        asyncController.getFoundationList(section_id);
    }

    @Override
    public void onRefresh() {
        if(section_id!=null)
        {
            hitToServer1();
        }
        else {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkLocationStablity();
        if(section_id!=null)
            hitToServer();
    }



    private void checkLocationStablity()
    {
        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}
        if (!gps_enabled && !network_enabled) {
            // notify user
            android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(new ContextThemeWrapper(this, R.style.DialogTheme));
            alertDialog.setTitle("GPS is not Enabled!");

            alertDialog.setMessage("Do you want to turn on GPS?");
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                    //get gps
                }
            });
            alertDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub

                }
            });
            alertDialog.show();

        }
        else {
            setLocation();
        }
    }
    private void setLocation()
    {
        // locationTrackSecond.canGetLocation()

        if (locationTrack.canGetLocation()) {
            longitude = locationTrack.getLongitude();
            latitude = locationTrack.getLatitude();
            if (latitude != 0 && longitude != 0) {
                l1=new LatLng(latitude,longitude);
            }

            Log.e("service","provider");
        }


    }
    @Override
    public void onDestroy()
    {
        super.onDestroy();
        if (locationTrack!=null)
        {
            locationTrack.stopListener();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CameraAndGalleryUtil.REQUEST_TAKE_PHOTO && resultCode == Activity.RESULT_OK) {

            //cameraAndGalleryUtil.setPic(profile_image1);
            String photoUri = cameraAndGalleryUtil.getPhotoURI();
            //deleteFile();
            output1 = new File(photoUri);
            Uri uri = Uri.fromFile(output1);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                output1 = new File(cameraAndGalleryUtil.saveGalaryImageOnLitkat(bitmap, "rms.jpg"));
                Bitmap bitmap2 = BitmapFactory.decodeFile(output1.getPath());
                //odo_meter_img.setImageBitmap(bitmap2);

                cameraAndGalleryUtil.deleteFile();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo from camera","Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (item==0) {
                    if(cameraAndGalleryUtil ==null)
                    {
                        cameraAndGalleryUtil =new CameraAndGalleryUtil(SpecialUserNewEntryFoundationList.this);
                    }
                    cameraAndGalleryUtil.dispatchTakePictureIntent("rms.jpg");

                }
                else if (item==1) {
                    dialog.dismiss();

                }
            }
        });
        AlertDialog dialog= builder.create();
        //dialog.getWindow().setLayout(800,600);
        dialog.show();


    }

}
