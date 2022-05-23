package com.example.streetviewmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
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

public class AddAdminLocationActivity extends BaseActivity {
    Button startTrackingButton;
    Location userLocation;
    FusedLocationProviderClient fusedLocationProviderClient;
    private static final int REQUEST = 112;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startService(new Intent(this, UserTrackerByGPS.class));
        setContentView(R.layout.activity_add_admin_location);
        findViewsByIds();
        startTrackingButton.setOnClickListener(view -> {
            userLocation = UserTrackerByGPS.location;
            if (userLocation != null) {
                Toast.makeText(getApplicationContext(), "lat" + userLocation.getAltitude(), Toast.LENGTH_LONG).show();
            } else {
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }
                userLocation=fusedLocationProviderClient.getLastLocation().getResult();
                Toast.makeText(getApplicationContext(), "lat" + userLocation.getAltitude(), Toast.LENGTH_LONG).show();

            }
        });
    }
    private void findViewsByIds(){
        startTrackingButton=findViewById(R.id.buttonStartTracking);
    }
    private static boolean hasPermissions(Context context, String... permissions) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && context != null && permissions != null) {
            for (String permission : permissions) {
                if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
                    return false;
                }
            }
        }
        return true;
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    Toast.makeText(getApplicationContext(), "The app was not allowed to access your location", Toast.LENGTH_LONG).show();
                }
            }
        }
    }
}