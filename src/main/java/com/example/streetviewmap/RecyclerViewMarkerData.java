package com.example.streetviewmap;

import java.util.ArrayList;

public class RecyclerViewMarkerData {
    int idOfMarkerDrawble;
    String name;
    boolean isPurchased;
    int position;
    public RecyclerViewMarkerData(int idOfMarkerDrawble, String name,boolean isPurchased,int position) {
        this.idOfMarkerDrawble = idOfMarkerDrawble;
        this.name = name;
        this.isPurchased=isPurchased;
        this.position=position;
    }

    public int getIdOfMarkerDrawble() {
        return idOfMarkerDrawble;
    }

    public String getName() {
        return name;
    }
    private static int lastMarkerId=0;

    public static ArrayList<RecyclerViewMarkerData> createMarkersList(String[] namesOfMarkers, int[] markersDrawble,boolean[] isSPurchased,int[] positions){
        ArrayList<RecyclerViewMarkerData> recyclerViewMarkerData =new ArrayList<RecyclerViewMarkerData>();
        for(int i = 0; i <namesOfMarkers.length; i++){
            recyclerViewMarkerData.add(new RecyclerViewMarkerData(markersDrawble[i],namesOfMarkers[i],isSPurchased[i],positions[i]));
        }
        return recyclerViewMarkerData;
    }
}
