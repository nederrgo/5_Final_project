package com.example.streetviewmap;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Map;

public class AddAdminLocationActivity extends BaseActivity {
    Button startTrackingButton;
    double lat;
    double lon;
    @SuppressLint("MissingPermission")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin_location);
        findViewsByIds();
        FusedLocationProviderClient fusedLocationProviderClient=new FusedLocationProviderClient(getApplicationContext());
        ActivityResultLauncher<String[]> requestPermissionsLauncher=registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                if(result!=null){
                    boolean fine=result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                    boolean coarse=result.get(Manifest.permission.ACCESS_COARSE_LOCATION);
                    if(fine&&coarse){
                        startTrackingButton.setText("hello");
                        fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY,null).addOnSuccessListener(AddAdminLocationActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location!=null){
                                    lat=location.getLatitude();
                                    lon=location.getLongitude();
                                }
                            }
                        });
                    }
                }
            }
        });
        String[] permissions={Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};
        requestPermissionsLauncher.launch(permissions);
        startTrackingButton.setOnClickListener(v -> {
            fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY,null).addOnSuccessListener(AddAdminLocationActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location!=null){
                        lat=location.getLatitude();
                        lon=location.getLongitude();
                        Toast.makeText(getApplicationContext(),lat+","+lon,Toast.LENGTH_LONG).show();

                    }
                }
            });
        });
    }
    private void findViewsByIds() {
        startTrackingButton = findViewById(R.id.buttonStartTracking);
    }


}