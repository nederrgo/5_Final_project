package com.example.streetviewmap;

import java.util.ArrayList;

public class RecyclerViewMarkerData {
    int idOfMarkerDrawble;
    String name;

    public RecyclerViewMarkerData(int idOfMarkerDrawble, String name) {
        this.idOfMarkerDrawble = idOfMarkerDrawble;
        this.name = name;
    }

    public int getIdOfMarkerDrawble() {
        return idOfMarkerDrawble;
    }

    public String getName() {
        return name;
    }
    private static int lastMarkerId=0;

    public static ArrayList<RecyclerViewMarkerData> createMarkersList(String[] namesOfMarkers, int[] markersDrawble){
        ArrayList<RecyclerViewMarkerData> recyclerViewMarkerData =new ArrayList<RecyclerViewMarkerData>();
        for(int i = 0; i <namesOfMarkers.length; i++){
            recyclerViewMarkerData.add(new RecyclerViewMarkerData(markersDrawble[i],namesOfMarkers[i]));
        }
        return recyclerViewMarkerData;
    }
}
