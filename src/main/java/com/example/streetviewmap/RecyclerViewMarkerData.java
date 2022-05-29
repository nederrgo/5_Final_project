package com.example.streetviewmap;

import android.util.Log;

import java.util.ArrayList;

public class RecyclerViewMarkerData {
    int idOfMarkerDrawble;
    boolean isPurchased;
    int position;
    public RecyclerViewMarkerData(int idOfMarkerDrawble,boolean isPurchased,int position) {
        this.idOfMarkerDrawble = idOfMarkerDrawble;
        this.isPurchased=isPurchased;
        this.position=position;
    }

    public int getIdOfMarkerDrawble() {
        return idOfMarkerDrawble;
    }

    public static ArrayList<RecyclerViewMarkerData> createMarkersList(int[] markersDrawble,boolean[] isSPurchased,int[] positions){
        ArrayList<RecyclerViewMarkerData> recyclerViewMarkerData =new ArrayList<RecyclerViewMarkerData>();
        for(int i = 0; i <markersDrawble.length; i++){
            Log.i("banana",+markersDrawble.length+","+isSPurchased.length+","+positions.length);
            recyclerViewMarkerData.add(new RecyclerViewMarkerData(markersDrawble[i],isSPurchased[i],positions[i]));
        }
        return recyclerViewMarkerData;
    }
}
