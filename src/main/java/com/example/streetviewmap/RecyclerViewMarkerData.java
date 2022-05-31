package com.example.streetviewmap;

import android.util.Log;

import java.util.ArrayList;

/**
 * The type Recycler view marker data.
 */
public class RecyclerViewMarkerData {
    /**
     * The Id of marker drawble.
     */
    int idOfMarkerDrawable;
    /**
     * The Is purchased.
     */
    boolean isPurchased;
    /**
     * The Position.
     */
    int position;

    /**
     * Instantiates a new Recycler view marker data.
     *
     * @param idOfMarkerDrawable the id of the marker drawable
     * @param isPurchased       the is purchased
     * @param position          the position
     */
    public RecyclerViewMarkerData(int idOfMarkerDrawable,boolean isPurchased,int position) {
        this.idOfMarkerDrawable = idOfMarkerDrawable;
        this.isPurchased=isPurchased;
        this.position=position;
    }

    /**
     * Gets id of marker drawble.
     *
     * @return the id of marker drawble
     */
    public int getIdOfMarkerDrawable()  {
        return idOfMarkerDrawable;
    }

    /**
     * Create markers array list.
     *
     * @param markersDrawable the markers drawable array.
     * @param isPurchased    is the markers purchased array.
     * @param positions      the positions of the markers array.
     * @return the array list
     */
    public static ArrayList<RecyclerViewMarkerData> createMarkersList(int[] markersDrawable,boolean[] isPurchased,int[] positions){
        ArrayList<RecyclerViewMarkerData> recyclerViewMarkerData =new ArrayList<RecyclerViewMarkerData>();
        for(int i = 0; i <markersDrawable.length; i++){
            Log.i("banana",+markersDrawable.length+","+isPurchased.length+","+positions.length);
            recyclerViewMarkerData.add(new RecyclerViewMarkerData(markersDrawable[i],isPurchased[i],positions[i]));
        }
        return recyclerViewMarkerData;
    }
}
