package com.example.streetviewmap;

import android.util.Log;


/**
 * The type Calculate system.
 */
public class CalculateSystem {
    /**
     * Distance.
     *
     * @param lat1 lat of the place the player gussed.
     * @param lat2 lat of the place the player really was in .
     * @param lon1 lon of the place the player gussed.
     * @param lon2 lon of the place the player really was in.
     * @return the distance between the place the player gussed to where he really was.
     */
    public static double distance(double lat1, double lat2, double lon1, double lon2) {
        final int R = 6371; // Radius of the earth
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    /**
     * Points int.
     *
     * @param distance get the distance between the place the player gussed to where he really was.
     * @return the amount of points the player gets for this distance.
     */
    public static int points(double distance) {
        return (int) Math.round(5000 * Math.pow(Math.E, -distance / 2000));
    }

    /**
     * Store point cost int.
     *
     * @param position the position that the marker is in the recycleView.
     * @return the amount of points the player needs to be able to buy the marker.
     */
    public static int storePointCost(int position) {
        position = position + 1;
        Log.i("banana", "storePointCost: " + position);
        return (int) Math.pow(10, position) * position * 1000;
    }
}
