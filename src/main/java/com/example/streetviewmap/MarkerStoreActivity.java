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

/**
 * The type Marker store activity.
 */
public class MarkerStoreActivity extends BaseActivity {
    /**
     * The Recycler view marker data.
     */
    ArrayList<RecyclerViewMarkerData> recyclerViewMarkerData;
    /**
     * array of the markers images ids.
     */
    private static int[] markersId={R.drawable.normal_marker,R.drawable.yellow_marker,R.drawable.doge_marker,R.drawable.fire_base_marker};

    private int[] positions;
    /**
     * player score text view
     */
    private TextView playerScoreText;
    /**
     * The constant amountOfMarkers.
     */
    public static int amountOfMarkers=markersId.length;
    /**
     * The Broadcast receiver.
     */
    BroadcastReceiver broadcastReceiver;
    /**
     * The What markers are purchased.
     */
    ArrayList<Integer>whatMarkersArePurchased;
    /**
     * The User marker list.
     */
    boolean[] userMarkerList=new boolean[markersId.length];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_store);
        //get the array from the intent from the mainActivity
        whatMarkersArePurchased=getIntent().getIntegerArrayListExtra("markersBought");
        userMarkerOwnedList();
        RecyclerView markersOptionsToBuy = (RecyclerView) findViewById(R.id.recyclerview);
        setPositionsList();
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
        //broadcastReceiver that update the list of markers bought and refresh the page.
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
            }
        };
        registerReceiver(broadcastReceiver, new IntentFilter("refreshPage"));
    }
    /**
     * set the positions[] list to have the position number in the array in that position
     */
    private void setPositionsList(){
        positions=new int[markersId.length];
        for (int i = 0; i <positions.length ; i++) {
            positions[i]=i;
        }
    }

    /**
     * gets the user document from the data base(fire base) and then set the text of playerScoreText to the user points.
     */
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

    /**
     * set text of the point the player had into playerScoreText
     */
    private void setText(){
         long score=getIntent().getLongExtra("playerScore",0);
         playerScoreText.setText(score+" points");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(broadcastReceiver);
    }
    /**
     * @return updated array of booleans that say which markers the user own.
     */
    private boolean[] userMarkerOwnedList(){
        for(int i =0;i<whatMarkersArePurchased.size();i++){
            userMarkerList[whatMarkersArePurchased.get(i)]=true;
        }
        return userMarkerList;
    }
}