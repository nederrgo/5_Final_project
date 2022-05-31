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

/**
 * The type Game play activity.
 */
public class GamePlayActivity extends BaseActivity implements OnStreetViewPanoramaReadyCallback, OnMapReadyCallback {

    /**
     * The Street view player vision.
     */
    StreetViewPanoramaView streetViewPlayerVision;
    /**
     * The Map view.
     */
    MapView mapView;
    private LatLng playerPosition;
    private StreetViewPanorama mapStreetView;
    /**
     * google map
     */
    private GoogleMap gMap;
    /**
     * button that close and open the map
     */
    private FloatingActionButton openAndCloseMap;
    /**
     * button that submit the guss of the player
     */
    private Button submitGussButton;
    /**
     * check if we can see the google map
     */
    private boolean isGussMapVisible=false;
    private MarkerOptions markerOptions;
    /**
     * last marker that was marked on the map
     */
    private Marker lastMarked;
    /**
     * progress bar that tell us that the map is loading
     */
    public ProgressBar loadingMap;
    /**
     * The First time loading.
     */
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

    /**
     * active when the street view is ready.
     * @param streetViewPanorama what we see on the google street view.
     */
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

    /**
     * active when google map view is ready
     * @param googleMap google map of the view
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        gMap=googleMap;
        gMap.setOnMapClickListener(latLng -> {
            chosePlaceWithMarker(latLng);
        } );
    }

    /**
     * find views by ids
     */
    private void findViewsByIds(){
        streetViewPlayerVision =findViewById(R.id.mapViewStreet);
        mapView=findViewById(R.id.mapViewChoosePosition);
        openAndCloseMap=findViewById(R.id.openAndCloseMapButton);
        submitGussButton=findViewById(R.id.GussingPlaceButton);
        loadingMap=findViewById(R.id.mapLoding);
    }

    /**
     * set on click events
     */
    private void setClickers(){
        openAndCloseMap.setOnClickListener(view -> {
            changeMapAndGussButtonVisibilityState();
        });
        submitGussButton.setOnClickListener(view -> {
            submitGuss();
        });
    }

    /**
     * make the map and submit button visible and invisible depend on the state of visibility.
     */
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

    /**
     * make a marker and save the location that the player pressed.
     * @param latLng the location the player clicked on the map.
     */
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

    /**
     * submit the player guss
     */
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

    /**
     * sends intent to guss player guss after submit
     * @param latGussed lat of the location the player gussed
     * @param lonGussed lon of the location the player gussed
     * @param lonPosition lon of the player real location
     * @param latPosition lat of the player real location
     * @return the intent
     */
    private Intent CreateIntentOfGuss(double latGussed, double lonGussed, double lonPosition, double latPosition){
        Intent sendToScore= new Intent(getApplicationContext(), RoundStatsActivity.class);
        sendToScore.putExtra("latGussed", latGussed);
        sendToScore.putExtra("lonGussed", lonGussed);
        sendToScore.putExtra("lonPosition",lonPosition);
        sendToScore.putExtra("latPosition",latPosition);
        return sendToScore;
    }



}