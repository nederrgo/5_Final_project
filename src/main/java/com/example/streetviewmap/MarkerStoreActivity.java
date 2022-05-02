package com.example.streetviewmap;

import static com.example.streetviewmap.FireBaseUtil.FireBaseHandlerCreator;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import com.example.streetviewmap.FireBaseUtil;
public class MarkerStoreActivity extends BaseActivity {
    ArrayList<RecyclerViewMarkerData> recyclerViewMarkerData;
    FireBaseUtil fireBaseUtil=FireBaseHandlerCreator();
    private static int[] markersId={R.drawable.normal_marker,R.drawable.doge_marker,R.drawable.fire_base_marker};
    private String[] markersNames={"default marker","doge marker","fireBase marker"};
   public static int amountOfMarkers=markersId.length;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_store);
        boolean[] whatMarkersArePurchased=getIntent().getBooleanArrayExtra("markersBought");
        Log.i("banana", "onCreate: "+whatMarkersArePurchased[0]);
        RecyclerView markersOptionsToBuy = (RecyclerView) findViewById(R.id.recyclerview);
        markersOptionsToBuy.setHasFixedSize(true);
        recyclerViewMarkerData=RecyclerViewMarkerData.createMarkersList(markersNames,markersId,whatMarkersArePurchased);
        MarkersAdpters markersAdpters= new MarkersAdpters(recyclerViewMarkerData);
        markersOptionsToBuy.setAdapter(markersAdpters);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        markersOptionsToBuy.setLayoutManager(linearLayoutManager);
    }
}