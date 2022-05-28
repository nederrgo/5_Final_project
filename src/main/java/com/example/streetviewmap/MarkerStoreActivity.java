package com.example.streetviewmap;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MarkerStoreActivity extends BaseActivity {
    ArrayList<RecyclerViewMarkerData> recyclerViewMarkerData;
    private static int[] markersId={R.drawable.normal_marker,R.drawable.doge_marker,R.drawable.fire_base_marker,R.drawable.normal_marker,R.drawable.doge_marker,R.drawable.normal_marker,R.drawable.doge_marker};
    private int[] positions;
    private TextView playerScoreText;
    public static int amountOfMarkers=markersId.length;
    BroadcastReceiver broadcastReceiver;
    ArrayList<Integer>whatMarkersArePurchased;
    boolean[] userMarkerList=new boolean[markersId.length];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_store);
        whatMarkersArePurchased=getIntent().getIntegerArrayListExtra("markersBought");
        userMarkerOwnedList();
        RecyclerView markersOptionsToBuy = (RecyclerView) findViewById(R.id.recyclerview);
        setPositionsList();
        markersOptionsToBuy.setHasFixedSize(true);
        recyclerViewMarkerData=RecyclerViewMarkerData.createMarkersList(markersId,userMarkerList,positions);
        MarkersAdpters markersAdpters= new MarkersAdpters(recyclerViewMarkerData);
        markersOptionsToBuy.setAdapter(markersAdpters);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        markersOptionsToBuy.setLayoutManager(linearLayoutManager);
        playerScoreText=findViewById(R.id.textPointsPlayerHas);
        if(getIntent().getLongExtra("playerScore",-4)==-4){
            playerScore();
        }else{
            setText();
        }
         broadcastReceiver=new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                int position=intent.getIntExtra("position",4);
                Log.i("banana", "onReceive: "+position);
                Log.i("banana", "onReceive: "+userMarkerList[position]);
                whatMarkersArePurchased.add(position);
                Intent refreshPage=new Intent(context,MarkerStoreActivity.class);
                refreshPage.putExtra("markersBought",whatMarkersArePurchased);
                int price=CalculateSystem.storePointCost(position);
                long scorePlayerHave=Long.parseLong(playerScoreText.getText().toString().substring(0,playerScoreText.getText().toString().indexOf(" ")))-price;
                refreshPage.putExtra("playerScore",scorePlayerHave);
                startActivity(refreshPage);
                finish();
                //playerScoreText.setText(scorePlayerHave-CalculateSystem.storePointCost(position)+" points");
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("refreshPage"));
    }
    private void setPositionsList(){
        positions=new int[markersId.length];
        for (int i = 0; i <positions.length ; i++) {
            positions[i]=i;
        }
    }
    private void playerScore(){
        FirebaseFirestore dataBase=FirebaseFirestore.getInstance();
        FirebaseAuth userAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser= userAuth.getCurrentUser();
        dataBase.collection("users").document(currentUser.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> data = documentSnapshot.getData();
                long playerScore=(long)data.get("points");
                playerScoreText.setText(playerScore+" points");
            }
        });
    }
    private void setText(){
         long score=getIntent().getLongExtra("playerScore",0);
         playerScoreText.setText(score+" points");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
    private boolean[] userMarkerOwnedList(){
        for(int i =0;i<whatMarkersArePurchased.size();i++){
            userMarkerList[whatMarkersArePurchased.get(i)]=true;
        }
        return userMarkerList;
    }
}