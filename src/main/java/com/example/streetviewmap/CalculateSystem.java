package com.example.streetviewmap;

import android.util.Log;


public class CalculateSystem {
    /**
     *
     * @param lat1
     * @param lat2
     * @param lon1
     * @param lon2
     * @return
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
     * @param distance
     * @return
     */
    public static int points(double distance) {
        return (int) Math.round(5000 * Math.pow(Math.E, -distance / 2000));
    }

    /**
     * @param position
     * @return
     */
    public static int storePointCost(int position) {
        position = position + 1;
        Log.i("banana", "storePointCost: " + position);
        return (int) Math.pow(10, position) * position * 1000;
    }
}
