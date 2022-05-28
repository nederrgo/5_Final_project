package com.example.streetviewmap;

import static com.example.streetviewmap.RoundSystem.getCustomMarker;

import androidx.annotation.NonNull;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.OnStreetViewPanoramaReadyCallback;
import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.StreetViewPanoramaView;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class GamePlayActivity extends BaseActivity implements OnStreetViewPanoramaReadyCallback, OnMapReadyCallback {

    StreetViewPanoramaView streetViewPlayerVision;
    MapView mapView;
    private LatLng playerPosition;
    private StreetViewPanorama mapStreetView;
    private GoogleMap gMap;
    private FloatingActionButton openAndCloseMap;
    private Button submitGussButton;
    private boolean isGussMapVisible=false;
    private MarkerOptions markerOptions;
    private Marker lastMarked;
    public ProgressBar loadingMap;
    boolean firstTimeLoading =true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_play);

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
        FireBaseUtil.setRandomPlace(mapStreetView);
        streetViewPanorama.setStreetNamesEnabled(false);
        mapStreetView.setOnStreetViewPanoramaChangeListener(streetViewPanoramaCamera -> {
            streetViewPanoramaCamera=mapStreetView.getLocation();
            playerPosition=streetViewPanoramaCamera.position;
            if(firstTimeLoading){
                loadingMap.setVisibility(View.INVISIBLE);
                openAndCloseMap.setVisibility(View.VISIBLE);
                firstTimeLoading=false;
            }
        });

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap=googleMap;
        gMap.setOnMapClickListener(latLng -> {
            chosePlaceWithMarker(latLng);
        } );
    }
    private void findViewsByIds(){
        streetViewPlayerVision =findViewById(R.id.mapViewStreet);
        mapView=findViewById(R.id.mapViewChoosePosition);
        openAndCloseMap=findViewById(R.id.openAndCloseMapButton);
        submitGussButton=findViewById(R.id.GussingPlaceButton);
        loadingMap=findViewById(R.id.mapLoding);
    }
    private void setClickers(){
        openAndCloseMap.setOnClickListener(view -> {
            changeMapAndGussButtonVisibilityState();
        });
        submitGussButton.setOnClickListener(view -> {
            submitGuss();
        });
    }


    private void changeMapAndGussButtonVisibilityState(){
        if(isGussMapVisible){
            mapView.setVisibility(View.INVISIBLE);
            submitGussButton.setVisibility(View.INVISIBLE);
        }else{
            mapView.setVisibility(View.VISIBLE);
            submitGussButton.setVisibility(View.VISIBLE);
        }

        isGussMapVisible=!isGussMapVisible;
    }
    private void chosePlaceWithMarker(LatLng latLng) {
        if(lastMarked!=null){lastMarked.remove();}else{
            submitGussButton.setEnabled(true);
        }
        markerOptions=new MarkerOptions().position(latLng);
        lastMarked=gMap.addMarker(markerOptions);
        if(getCustomMarker()!=null){
        Bitmap marker=getCustomMarker();
        lastMarked.setIcon(BitmapDescriptorFactory.fromBitmap(marker));
        }
    }

    private void submitGuss() {
        LatLng gussingPosition=markerOptions.getPosition();
        double latGussed=gussingPosition.latitude;
        double lonGussed=gussingPosition.longitude;
        double lonPosition=playerPosition.longitude;
        double latPosition=playerPosition.latitude;
        Intent sendToScore=CreateIntentOfGuss(latGussed,lonGussed,lonPosition,latPosition);
        startActivity(sendToScore);
        finish();
    }
    private Intent CreateIntentOfGuss(double latGussed, double lonGussed, double lonPosition, double latPosition){
        Intent sendToScore= new Intent(getApplicationContext(), RoundStatsActivity.class);
        sendToScore.putExtra("latGussed", latGussed);
        sendToScore.putExtra("lonGussed", lonGussed);
        sendToScore.putExtra("lonPosition",lonPosition);
        sendToScore.putExtra("latPosition",latPosition);
        return sendToScore;
    }



}