package com.example.streetviewmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import androidx.core.content.ContextCompat;

import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;

public class RoundSystem {
    protected static int roundNum=1;
    private static int pointsOfGame=0;
    public static boolean isItLastRound(){
        return roundNum == 5;
    }
    public static int getPointsOfGame(){
        return pointsOfGame;
    }

    public static void setPointsOfGame(int pointsOfGame) {
        RoundSystem.pointsOfGame = pointsOfGame;
    }
    public static Bitmap resizeMapIcons(int iconId,int width, int height,Context context){
        Drawable d= ContextCompat.getDrawable(context,iconId);
        Bitmap imageBitmap  = ((BitmapDrawable)d).getBitmap();
        Bitmap resizedBitmap = Bitmap.createScaledBitmap(imageBitmap, width, height, false);
        return resizedBitmap;
    }

}
