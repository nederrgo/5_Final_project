package com.example.streetviewmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;


/**
 * The type Round system.
 */
public class RoundSystem {
    /**
     * The constant roundNum.
     */
    protected static int roundNum=1;
    /**
     * The constant points in game.
     */
    private static int pointsOfGame=0;
    /**
     * the bit map of the markers in the round.
     */
    private static Bitmap customMarker;

    /**
     * check is it the last round.
     *
     * @return the boolean
     */
    public static boolean isItLastRound(){
        return roundNum == 5;
    }

    /**
     * Get points of game.
     *
     * @return the int
     */
    public static int getPointsOfGame(){
        return pointsOfGame;
    }

    /**
     * Sets points of game in to RoundSystem.pointsOfGame.
     *
     * @param pointsOfGame the points of game
     */
    public static void setPointsOfGame(int pointsOfGame) {
        RoundSystem.pointsOfGame = pointsOfGame;
    }

    /**
     * Gets custom marker.
     *
     * @return the custom marker bitMap.
     */
    public static Bitmap getCustomMarker() {
        return customMarker;
    }

    /**
     * Set marker bit map.
     *
     * @param drawable the drawable
     * @param width    the width
     * @param height   the height
     * @param context  the context
     */
    public static void setMarkerBitMap(Drawable drawable , int width, int height, Context context){
        Bitmap imageBitmap  = ((BitmapDrawable)drawable).getBitmap();
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        customMarker=resizedBitmap;
    }
}
