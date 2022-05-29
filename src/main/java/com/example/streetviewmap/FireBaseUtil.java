package com.example.streetviewmap;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.StreetViewPanorama;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.SuccessContinuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;


/**
 * The type Fire base util.
 */
public class FireBaseUtil {
    private  final static FirebaseFirestore dataBase=FirebaseFirestore.getInstance();
    private final static Random rng = new Random();
    private static double lat=32.79139242166433;
    private static double lon=34.99034071292447;
    private static FirebaseUser currentUser;
    private static final FirebaseAuth userAuth=FirebaseAuth.getInstance();;

    /**
     * Sets new location in the fire base data.
     *
     * @param lat       the lat of the place
     * @param lon       the lon of the place
     * @param placeName the place name(using place name to have easier time to recognize it in the fire base)
     */
    public static void  setNewLocation(double lat,double lon,String placeName) {
        currentUser= userAuth.getCurrentUser();
                Map<String,Object> data=new HashMap<>();
                data.put("lat",lat);
                data.put("lon",lon);
                data.put("name",placeName);
                dataBase.collection("places").document().set(data);
                }

    /**
     * connect to the data base and take all the place and then set a random place in a street view map .
     *
     * @param streetViewMap the street view map
     */
    public static void setRandomPlace(StreetViewPanorama streetViewMap){
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

    /**
     * Add points to user account.
     *
     * @param pointsToAdd the amount of points to add
     */
    public static void addPoints(int pointsToAdd) {
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

    /**
     * Get what store markers user have and add the places to array list.
     *
     * @param markerAmount the marker amount that the marker store as to offer
     * @return the array list
     */
    public  static ArrayList<Integer> getWhatStoreMarkersUserHave(int markerAmount){
        currentUser= userAuth.getCurrentUser();
        final ArrayList<Integer> isMarkersPurchased=new ArrayList<Integer>();
        //final boolean[] isMarkersPurchased= new boolean[7];
        dataBase.collection("users").document(currentUser.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Map<String, Object> data = documentSnapshot.getData();
                for (int i = 0; i < data.size(); i++) {
                    if(data.get("marker"+i)!=null)
                    isMarkersPurchased.add(i);
                }
            }
        });
        return isMarkersPurchased;
    }


    /**
     * Buy marker and refresh page.
     *
     * @param position the position of the marker in the recycle view
     * @param context  the context of the application
     */
    public static void buyMarkerAndRefreshPage(int position, Context context){
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

    /**
     * send broadcast.
     * @param position the position of the marker in the recycle view
     * @param context  the context of the application
     */
    private static void sendBroadcast(Context context,int position){
        Intent intent = new Intent();
        intent.setAction("refreshPage");
        intent.putExtra("position",position);
        context.sendBroadcast(intent);
    }

    /**
     * Sigh out.
     *
     * @param activity the activity that use the function.
     */
    public static void sighOut(AppCompatActivity activity){
        FirebaseAuth.getInstance().signOut();
        RoundSystem.setMarkerBitMap(activity.getResources().getDrawable(R.drawable.normal_marker,activity.getTheme()),76,98,activity);
        Log.i("work", "onOptionsItemSelected: loged out "+FirebaseAuth.getInstance().getCurrentUser());
        activity.startActivity(new Intent(activity,MainActivity.class));
        activity.finish();
    }
}


