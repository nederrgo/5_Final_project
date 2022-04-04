package com.example.streetviewmap;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;


public class MarkerStoreActivity extends BaseActivity {
    ArrayList<RecyclerViewMarkerData> recyclerViewMarkerData;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_store);
        RecyclerView markersOptionsToBuy = (RecyclerView) findViewById(R.id.recyclerview);
        String[] markersNames={"doge marker","fireBase marker"};
        int[] markersId={R.drawable.doge_marker,R.drawable.fire_base_marker};
        recyclerViewMarkerData=RecyclerViewMarkerData.createMarkersList(markersNames,markersId);
        MarkersAdpters markersAdpters= new MarkersAdpters(recyclerViewMarkerData);
        markersOptionsToBuy.setAdapter(markersAdpters);
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        markersOptionsToBuy.setLayoutManager(linearLayoutManager);
    }
}