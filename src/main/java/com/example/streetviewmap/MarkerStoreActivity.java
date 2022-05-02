package com.example.streetviewmap;

import static com.example.streetviewmap.FireBaseUtil.FireBaseHandlerCreator;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Map;

import com.example.streetviewmap.FireBaseUtil;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MarkerStoreActivity extends BaseActivity {
    ArrayList<RecyclerViewMarkerData> recyclerViewMarkerData;
    FireBaseUtil fireBaseUtil=FireBaseHandlerCreator();
    private static int[] markersId={R.drawable.normal_marker,R.drawable.doge_marker,R.drawable.fire_base_marker};
    private String[] markersNames={"default marker","doge marker","fireBase marker"};
    private int[] positions;
    private TextView playerScoreText;
    public static int amountOfMarkers=markersId.length;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_store);
        boolean[] whatMarkersArePurchased=getIntent().getBooleanArrayExtra("markersBought");
        Log.i("banana", "onCreate: "+whatMarkersArePurchased[0]);
        RecyclerView markersOptionsToBuy = (RecyclerView) findViewById(R.id.recyclerview);
        setPositionsList();
        markersOptionsToBuy.setHasFixedSize(true);
        recyclerViewMarkerData=RecyclerViewMarkerData.createMarkersList(markersNames,markersId,whatMarkersArePurchased,positions);
        MarkersAdpters markersAdpters= new MarkersAdpters(recyclerViewMarkerData);
        markersOptionsToBuy.setAdapter(markersAdpters);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        markersOptionsToBuy.setLayoutManager(linearLayoutManager);
        playerScoreText=findViewById(R.id.textPointsPlayerHas);
        rightPlayerScore();
    }
    private void setPositionsList(){
        positions=new int[markersId.length];
        for (int i = 0; i <positions.length ; i++) {
            positions[i]=i;
        }
    }
    private void rightPlayerScore(){
        FirebaseFirestore dataBase=FirebaseFirestore.getInstance();
        FirebaseAuth userAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser= userAuth.getCurrentUser();
        dataBase.collection("users").document(currentUser.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> data = documentSnapshot.getData();
                long playerScore=(long)data.get("points");
                playerScoreText.setText(playerScore+" ");
            }
        });
    }
}