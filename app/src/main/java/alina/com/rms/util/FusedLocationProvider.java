package alina.com.rms.util;

import android.annotation.SuppressLint;
import android.content.IntentSender;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

/**
 * Created by alina on 08-04-2018.
 */

public class FusedLocationProvider {

    private FusedLocationProviderClient mFusedLocationClient;
    private  AppCompatActivity appCompatActivit;
    private  LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    boolean mRequestingLocationUpdates;

    public FusedLocationProvider(AppCompatActivity appCompatActivity)
    {
        this.appCompatActivit=appCompatActivity;

        setmFusedLocationClient();
        createLocationRequest();


        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    // ...
                }
            };
        };

    }

    @SuppressLint("MissingPermission")
    public void setmFusedLocationClient()
    {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(appCompatActivit);

        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(appCompatActivit, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            Log.e("Last Lattitude"+location.getLatitude(),"Last Longitude"+location.getLongitude());
                        }
                    }
                });
    }

    @SuppressLint("RestrictedApi")
    protected void createLocationRequest() {

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(mLocationRequest);


// ...

        SettingsClient client = LocationServices.getSettingsClient(appCompatActivit);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(builder.build());

        task.addOnSuccessListener(appCompatActivit, new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                // All location settings are satisfied. The client can initialize
                // location requests here.
                // ...
            }
        });

        task.addOnFailureListener(appCompatActivit, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                if (e instanceof ResolvableApiException) {
                    // Location settings are not satisfied, but this can be fixed
                    // by showing the user a dialog.
                    try {
                        // Show the dialog by calling startResolutionForResult(),
                        // and check the result in onActivityResult().
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(appCompatActivit,
                                501);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error.
                    }
                }
            }
        });

    }



    protected void onPause() {
        stopLocationUpdates();
    }

    private void stopLocationUpdates() {
        mFusedLocationClient.removeLocationUpdates(mLocationCallback);
    }

    protected void onResume() {

        /*if (mRequestingLocationUpdates) {
            startLocationUpdates();
        }*/
    }



}
