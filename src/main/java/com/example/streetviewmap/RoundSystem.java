package com.example.streetviewmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;


public class RoundSystem {
    protected static int roundNum=1;
    private static int pointsOfGame=0;
    private static Bitmap customMarker;
    public static boolean isItLastRound(){
        return roundNum == 5;
    }
    public static int getPointsOfGame(){
        return pointsOfGame;
    }

    public static void setPointsOfGame(int pointsOfGame) {
        RoundSystem.pointsOfGame = pointsOfGame;
    }

    public static Bitmap getCustomMarker() {
        return customMarker;
    }
    public static void setMarkerBitMap(Drawable drawable , int width, int height, Context context){
        Bitmap imageBitmap  = ((BitmapDrawable)drawable).getBitmap();
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        customMarker=resizedBitmap;
    }

}
