package com.example.streetviewmap;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Map;

public class AddAdminLocationActivity extends BaseActivity implements OnStreetViewPanoramaReadyCallback {
    Button startTrackingButton;
    double lat;
    double lon;
    Button setNewLocationButton;
    private StreetViewPanorama mapStreetView;
    StreetViewPanoramaView streetViewPlayerVision;
    private StreetViewPanoramaLocation cameraPov;
    @SuppressLint("MissingPermission")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_admin_location);
        findViewsByIds();
        streetViewPlayerVision.onCreate(null);
        streetViewPlayerVision.getStreetViewPanoramaAsync(this);
        FusedLocationProviderClient fusedLocationProviderClient=new FusedLocationProviderClient(getApplicationContext());
        ActivityResultLauncher<String[]> requestPermissionsLauncher=registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), new ActivityResultCallback<Map<String, Boolean>>() {
            @SuppressLint("MissingPermission")
            @Override
            public void onActivityResult(Map<String, Boolean> result) {
                if(result!=null){
                    boolean fine=result.get(Manifest.permission.ACCESS_FINE_LOCATION);
                    boolean coarse=result.get(Manifest.permission.ACCESS_COARSE_LOCATION);
                    if(fine&&coarse){
                        fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY,null).addOnSuccessListener(AddAdminLocationActivity.this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if (location!=null){
                                    lat=location.getLatitude();
                                    lon=location.getLongitude();
                                }
                            }
                        });
                    }else {
                        Toast.makeText(getApplicationContext(),"this page needs your location to work",Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        String[] permissions={Manifest.permission.ACCESS_COARSE_LOCATION,Manifest.permission.ACCESS_FINE_LOCATION};
        requestPermissionsLauncher.launch(permissions);
        startTrackingButton.setOnClickListener(v -> {
            requestPermissionsLauncher.launch(permissions);
            fusedLocationProviderClient.getCurrentLocation(LocationRequest.PRIORITY_HIGH_ACCURACY,null).addOnSuccessListener(AddAdminLocationActivity.this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location!=null){
                        lat=location.getLatitude();
                        lon=location.getLongitude();
                        Toast.makeText(getApplicationContext(),lat+","+lon,Toast.LENGTH_LONG).show();
                        mapStreetView.setPosition(new LatLng(lat, lon));
                    }
                }
            });
        });
        setNewLocationButton.setOnClickListener(view -> {
            cameraPov=mapStreetView.getLocation();
            if(cameraPov==null){
                Toast.makeText(getApplicationContext(),"this isnt a place with a streetView",Toast.LENGTH_LONG).show();
            }else{
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Name");
                // set the custom layout
                final View customLayout = getLayoutInflater().inflate(R.layout.set_a_new_place_dialog, null);
                builder.setView(customLayout);
                // add a button
                builder.setPositiveButton("set place", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // send data from the AlertDialog to the Activity
                        TextView editText = customLayout.findViewById(R.id.setNewPlaceText);
                        FireBaseUtil.setNewLocation( cameraPov.position.latitude,cameraPov.position.longitude, editText.getText().toString());
                    }
                });
                builder.setNegativeButton("close",new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        dialog.dismiss();
                    }
                });
                // create and show the alert dialog
                AlertDialog dialog = builder.create();
                dialog.show();
            }});
    }
    @Override
    protected void onStart() {
        super.onStart();
        streetViewPlayerVision.onStart();
    }

    @Override
    protected void onPause() {
        streetViewPlayerVision.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        streetViewPlayerVision.onStop();
        super.onStop();
    }

    @Override
    protected void onResume() {
        super.onResume();
        streetViewPlayerVision.onResume();
    }

    private void findViewsByIds() {
        startTrackingButton = findViewById(R.id.buttonStartTracking);
        streetViewPlayerVision=findViewById(R.id.mapViewStreetAddAdminLocation);
        setNewLocationButton=findViewById(R.id.buttonSetAdminPlace);
    }


    @Override
    public void onStreetViewPanoramaReady(@NonNull StreetViewPanorama streetViewPanorama) {
        mapStreetView=streetViewPanorama;
        streetViewPanorama.setPosition(new LatLng(lat, lon));
    }
}