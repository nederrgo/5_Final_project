package com.example.streetviewmap;


import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Surface;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


public class FireBaseUtil {
    private static FireBaseUtil fireBaseUtil;
    private  final static FirebaseFirestore dataBase=FirebaseFirestore.getInstance();
    private final static Random rng = new Random();
    private static double lat=32.79139242166433;
    private static double lon=34.99034071292447;
    private static FirebaseUser currentUser;
    private static FirebaseAuth userAuth;
 /*   public FireBaseUtil() {
        userAuth = FirebaseAuth.getInstance();
        currentUser= userAuth.getCurrentUser();
    }
    public static FireBaseUtil FireBaseHandlerCreator(){
        if(fireBaseUtil ==null){
            fireBaseUtil =new FireBaseUtil();
        }
        return fireBaseUtil;
    }*/
    public static void  setNewLocation(double lat,double lon,String placeName) {
        userAuth = FirebaseAuth.getInstance();
        currentUser= userAuth.getCurrentUser();
                Map<String,Object> data=new HashMap<>();
                data.put("lat",lat);
                data.put("lon",lon);
                data.put("name",placeName);
                dataBase.collection("places").document().set(data);
                }
    public static void setRandomPlace(StreetViewPanorama streetViewMap){
        userAuth = FirebaseAuth.getInstance();
        currentUser= userAuth.getCurrentUser();
        dataBase.collection("places").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                List<DocumentSnapshot> myListOfDocuments=task.getResult().getDocuments();
                int bound =myListOfDocuments.size();
                int rngNum=rng.nextInt(bound);
                Log.i("banana", "onComplete: "+rngNum+","+bound);
                //getting the random location and display it on the map
                DocumentSnapshot document = myListOfDocuments.get(rngNum);
                Log.i("banana", "onComplete: "+document.get("name"));
                lat = (double) document.get("lat");
                lon = (double) document.get("lon");
                streetViewMap.setPosition(new LatLng(lat, lon));
            }
        });
    }

    public static void addPoints(int pointsToAdd) {
        userAuth = FirebaseAuth.getInstance();
        currentUser= userAuth.getCurrentUser();
        dataBase.collection("users").document(currentUser.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> data = documentSnapshot.getData();
                    long points = (long)data.get("points");
                    Map<String,Object> pointsUpdate=new HashMap<>();
                    pointsUpdate.put("points",points+pointsToAdd);
                    dataBase.collection("users").document(currentUser.getEmail()).update(pointsUpdate);
                }
            }
        });
    }
    public  static boolean[] getWhatStoreMarkersUserHave(int markerAmount){
        userAuth = FirebaseAuth.getInstance();
        currentUser= userAuth.getCurrentUser();
        final boolean[] isMarkersPurchased= new boolean[3];
        dataBase.collection("users").document(currentUser.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> data = documentSnapshot.getData();
                for (int i = 0; i < isMarkersPurchased.length; i++) {
                    if(data.get("marker"+i)!=null)
                     isMarkersPurchased[i] = (boolean)data.get("marker"+i);
                }
            }
        });
        return isMarkersPurchased;

    }
    public static void buyMarkerAndEditPage(int position, Context context){
        userAuth = FirebaseAuth.getInstance();
        currentUser= userAuth.getCurrentUser();
        dataBase.collection("users").document(currentUser.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> data = documentSnapshot.getData();
                long playerScore=(long)data.get("points");
                int cost=CalculateSystem.storePointCost(position+1);
                if(cost>playerScore){
                    Toast.makeText(currentUser.zza().getApplicationContext(),"you dont have enough points",Toast.LENGTH_LONG).show();
                    return;
                }
                Map<String,Object> updateUserData=new HashMap<>();
                updateUserData.put("points",playerScore-cost);
                updateUserData.put("marker"+position,true);
                //update data base and refresh a page
                dataBase.collection("users").document(currentUser.getEmail()).update(updateUserData).onSuccessTask(new SuccessContinuation<Void, Object>() {
                    @NonNull
                    @Override
                    public Task<Object> then(Void unused) throws Exception {
                        Intent intent=new Intent();
                        intent.putExtra("position",position);
                        sendBroadcast(context,position);
                        return null;
                    }
                });
            }
        });
    }
    private static void sendBroadcast(Context context,int position){
        Intent intent = new Intent();
        intent.setAction("refreshPage");
        intent.putExtra("position",position);
        context.sendBroadcast(intent);
    }
}


