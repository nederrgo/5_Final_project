package com.example.streetviewmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.StreetViewPanoramaCamera;
import com.google.android.gms.maps.model.StreetViewPanoramaLocation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class SetANewPlaceActivity extends BaseActivity implements OnMapReadyCallback, OnStreetViewPanoramaReadyCallback {

    StreetViewPanoramaView streetViewPlayerVision;
    MapView mapView;
    private StreetViewPanorama mapStreetView;
    private GoogleMap gMap;
    private FloatingActionButton openAndCloseMap;
    private Button sendToPlaceButton;
    private boolean isGussMapVisible=false;
    private MarkerOptions markerOptions;
    private Marker lastMarked;
    public ProgressBar loadingMap;
    private final FireBaseUtil fireBaseUtil = FireBaseUtil.FireBaseHandlerCreator();
    boolean firstTimeLoading =true;
    private ImageButton goBackToManuButton;
    private Button setNewPlaceButton;
    private StreetViewPanoramaLocation cameraPov;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_anew_place);
        findViewsByIds();
        mapView.onCreate(null);
        mapView.getMapAsync(this);
        streetViewPlayerVision.onCreate(null);
        streetViewPlayerVision.getStreetViewPanoramaAsync(this);
        setClickers();
    }
    @Override
    protected void onStart() {
        super.onStart();
        streetViewPlayerVision.onStart();
        mapView.onStart();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        streetViewPlayerVision.onDestroy();
        mapView.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        streetViewPlayerVision.onPause();
        mapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        streetViewPlayerVision.onResume();
        mapView.onResume();
    }
    @Override
    public void onStreetViewPanoramaReady(@NonNull StreetViewPanorama streetViewPanorama) {
        mapStreetView=streetViewPanorama;
        streetViewPanorama.setPosition(new LatLng(-19,12));
        streetViewPanorama.setStreetNamesEnabled(false);
        mapStreetView.setOnStreetViewPanoramaChangeListener(streetViewPanoramaCamera -> {
            Log.i("banana", "onStreetViewPanoramaReady: ");
            if(firstTimeLoading){
                loadingMap.setVisibility(View.INVISIBLE);
                openAndCloseMap.setVisibility(View.VISIBLE);
                goBackToManuButton.setVisibility(View.VISIBLE);
                firstTimeLoading=false;
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap=googleMap;
        Log.i("banan","working");
        gMap.setOnMapClickListener(latLng -> {
            Log.i("banan","working");
            chosePlaceWithMarker(latLng);
        } );
    }
    private void findViewsByIds(){
        streetViewPlayerVision =findViewById(R.id.mapViewStreetAdminSetActivity);
        mapView=findViewById(R.id.mapViewChoosePositionAdminSetActivity);
        openAndCloseMap=findViewById(R.id.openAndCloseMapButtonAdminSetActivity);
        sendToPlaceButton =findViewById(R.id.sendMeToThatPlaceButton);
        loadingMap=findViewById(R.id.mapLodingAdminSetActivity);
        goBackToManuButton =findViewById(R.id.goBackMenuButtonAdminSetActivity);
        setNewPlaceButton=findViewById(R.id.setPlaceButton);
    }
    private void setClickers(){
        openAndCloseMap.setOnClickListener(view -> {
            changeMapAndGussButtonVisibilityState();
        });
        sendToPlaceButton.setOnClickListener(view -> {
            mapStreetView.setPosition(markerOptions.getPosition());
        });
        goBackToManuButton.setOnClickListener(view -> {
            Intent sendToMain=new Intent(getApplicationContext(),MainActivity.class);
            startActivity(sendToMain);
            finish();
        });
        setNewPlaceButton.setOnClickListener(view -> {
            cameraPov=mapStreetView.getLocation();
            if(cameraPov==null){
                Toast.makeText(getApplicationContext(),"this isnt a place with a streetView",Toast.LENGTH_LONG).show();
            }else{
            LatLng newPosition=markerOptions.getPosition();
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
                    fireBaseUtil.setNewLocation(newPosition.latitude, newPosition.longitude, editText.getText().toString());
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

    private void changeMapAndGussButtonVisibilityState(){
        if(isGussMapVisible){
            mapView.setVisibility(View.INVISIBLE);
            sendToPlaceButton.setVisibility(View.INVISIBLE);
            setNewPlaceButton.setVisibility(View.INVISIBLE);
        }else{
            mapView.setVisibility(View.VISIBLE);
            sendToPlaceButton.setVisibility(View.VISIBLE);
            setNewPlaceButton.setVisibility(View.VISIBLE);
        }
        isGussMapVisible=!isGussMapVisible;
    }
    private void chosePlaceWithMarker(LatLng latLng) {
        Log.i("banan","working");
        if(lastMarked!=null){lastMarked.remove();}else{
            sendToPlaceButton.setEnabled(true);
        }
        markerOptions=new MarkerOptions().position(latLng);
        lastMarked=gMap.addMarker(markerOptions);
    }

}