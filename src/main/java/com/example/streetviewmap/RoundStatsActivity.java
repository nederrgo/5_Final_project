package com.example.streetviewmap;


import static com.example.streetviewmap.CalculateSystem.*;
import static com.example.streetviewmap.RoundSystem.getCustomMarker;

import androidx.annotation.NonNull;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.icu.text.DecimalFormat;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;

public class RoundStatsActivity extends BaseActivity implements OnMapReadyCallback {
    private double latGussed;
    private double lonGussed;
    private double lonRealPosition;
    private double latRealPosition;
    LatLng gussPosition;
    LatLng realPosition;
    private MapView showDistanceMap;
    private TextView distanceBetweenPlaces;
    private ProgressBar scoreBar;
    private Button startNextRound;
    private TextView roundNumText;
    private TextView pointsInGameText;
    int points;
    int pointsInGame;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score_of_player);
        getIntentData();
        findViewsByIds();
        setClickers();
        showDistanceMap.onCreate(null);
        showDistanceMap.getMapAsync(this);
        double distance =distance(latGussed,latRealPosition,lonGussed,lonRealPosition);
         points= points(distance);
        pointsInGame=RoundSystem.getPointsOfGame();
        RoundSystem.setPointsOfGame(pointsInGame+points);
        roundNumText.setText("round "+RoundSystem.roundNum+"/5");
        pointsInGameText.setText("points collected in game "+(pointsInGame+points));
        scoreBar.setProgress(points*scoreBar.getMax()/5000);
        distanceBetweenPlaces.setText(new DecimalFormat("##.##").format(distance)+"km     "+points+" points");
    }
    @Override
    protected void onStart() {
        super.onStart();
        showDistanceMap.onStart();
    }
    //
    @Override
    protected void onDestroy() {
        super.onDestroy();
       showDistanceMap.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        showDistanceMap.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
      showDistanceMap.onResume();
    }
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        setUpForMap(googleMap);
        addMarkers(googleMap);
    }
    public void findViewsByIds(){
        showDistanceMap=findViewById(R.id.mapViewShowDistance);
        distanceBetweenPlaces=findViewById(R.id.DistanceText);
        scoreBar =findViewById(R.id.progressBarOfScore);
        startNextRound=findViewById(R.id.buttonStartNextRound);
        roundNumText=findViewById(R.id.numOfRoundText);
        pointsInGameText=findViewById(R.id.pointsInGameText);
    }
    public void getIntentData(){
        Intent intentSended=getIntent();
        latGussed=intentSended.getDoubleExtra("latGussed",0);
        lonGussed=intentSended.getDoubleExtra("lonGussed",0);
        lonRealPosition =intentSended.getDoubleExtra("lonPosition",0);
        latRealPosition =intentSended.getDoubleExtra("latPosition",0);
        gussPosition=new LatLng(latGussed,lonGussed);
        realPosition=new LatLng(latRealPosition, lonRealPosition);
    }
    public void setUpForMap(GoogleMap googleMap){
        LatLngBounds.Builder builder=new LatLngBounds.Builder();
        builder.include(gussPosition);
        builder.include(realPosition);
        LatLngBounds bounds = builder.build();
        int padding = 300; // offset from edges of the map in pixels
        CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        googleMap.moveCamera(cu);
    }
    private void addMarkers(GoogleMap googleMap) {
        MarkerOptions markerOptions= new MarkerOptions().position(gussPosition).title("your guss");
       Marker yourGussMarker= googleMap.addMarker(markerOptions);
        if(getCustomMarker()!=null){
            Bitmap marker=getCustomMarker();
            yourGussMarker.setIcon(BitmapDescriptorFactory.fromBitmap(marker));
        }
         markerOptions= new MarkerOptions().position(realPosition).title("where you were");
        Marker realPlaceMarker=googleMap.addMarker(markerOptions);
        if(getCustomMarker()!=null){
            Bitmap marker=getCustomMarker();
            realPlaceMarker.setIcon(BitmapDescriptorFactory.fromBitmap(marker));
        }
        Log.i("banana", "addMarkers: ");
    }
    public void setClickers(){
        if(!RoundSystem.isItLastRound()){
            startNextRound.setOnClickListener(view -> {
                Intent sendToRound=new Intent(getApplicationContext(),GamePlayActivity.class);
                startActivity(sendToRound);
                RoundSystem.roundNum++;
                finish();
            });
        }else{
            startNextRound.setText("finish game");
            startNextRound.setOnClickListener(view -> {
                    if(FirebaseAuth.getInstance().getCurrentUser()!=null) {
                        int pointsToAdd = pointsInGame + points;
                            FireBaseUtil.addPoints(pointsToAdd);
                    }
                    Intent sendToRound=new Intent(getApplicationContext(),MainActivity.class);
                    startActivity(sendToRound);
                    RoundSystem.roundNum=1;
                    RoundSystem.setPointsOfGame(0);
                    finish();
            });
        }

    }

}