package com.example.streetviewmap;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Map;

/**
 * The type Main activity.
 */
public class MainActivity extends BaseActivity {
    /**
     * The Send to game button.
     */
    Button sendToGameButton;
    /**
     * The Create place button.
     */
    Button createPlaceButton;
    /**
     * The Go to sign in button.
     */
    Button goToSignInButton;
    /**
     * The Go to log in button.
     */
    Button goToLogInButton;
    /**
     * The Sign out button.
     */
    Button signOutButton;
    /**
     * The Go to store.
     */
    Button goToStore;
    /**
     * The Go to set new place by gps button.
     */
    Button goToSetNewPlaceByGPSButton;
    private final FirebaseFirestore dataBase=FirebaseFirestore.getInstance();
    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;
    /**
     * The What markers are purchased array.
     */
    ArrayList<Integer> whatMarkersArePurchased;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        findViewsByIds();
        setClickers();
        if(RoundSystem.getCustomMarker()==null){
            RoundSystem.setMarkerBitMap(ResourcesCompat.getDrawable(getResources(),R.drawable.normal_marker,null),76,98,getApplicationContext());
        }
        String[] role= {"",""};
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser= firebaseAuth.getCurrentUser();
        if(currentUser!=null){
            goToLogInButton.setVisibility(View.INVISIBLE);
            goToSignInButton.setVisibility(View.INVISIBLE);
            signOutButton.setVisibility(View.VISIBLE);
            goToStore.setVisibility(View.VISIBLE);
           whatMarkersArePurchased=FireBaseUtil.getWhatStoreMarkersUserHave(MarkerStoreActivity.amountOfMarkers);
            dataBase.collection("users").document(currentUser.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> data = documentSnapshot.getData();
                    role[0] = String.valueOf(data.get("role"));
                    if(role[0].equals("admin")){
                        createPlaceButton.setVisibility(View.VISIBLE);
                        goToSetNewPlaceByGPSButton.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        }
    }

    /**
     * Find views by ids.
     */
    private void findViewsByIds(){
        sendToGameButton =findViewById(R.id.StartGameButton);
        createPlaceButton=findViewById(R.id.createANewPlaceButton);
        goToSignInButton =findViewById(R.id.goToSignInButton);
        goToLogInButton=findViewById(R.id.goToLogInButton);
        signOutButton=findViewById(R.id.buttonSighOut);
        goToStore=findViewById(R.id.buttonGotoStore);
        goToSetNewPlaceByGPSButton =findViewById(R.id.buttonSendToUserGPS);
    }

    /**
     * set the on click events
     */
    private void setClickers(){
        sendToGameButton.setOnClickListener(view -> {
         sendToActivity(GamePlayActivity.class);
        });
        goToSetNewPlaceByGPSButton.setOnClickListener(view -> {
            sendToActivity(AddAdminLocationActivity.class);
        });
        createPlaceButton.setOnClickListener(view -> {
            sendToActivity(SetANewPlaceActivity.class);
        });
        goToSignInButton.setOnClickListener(view -> {
                sendToActivity(SignInActivity.class);
        });
        goToLogInButton.setOnClickListener(view -> {
            sendToActivity(LogInActivity.class);
        });
        signOutButton.setOnClickListener(view -> {
            FireBaseUtil.sighOut(this);
        });
        goToStore.setOnClickListener(view -> {
            Intent intent=new Intent(this,MarkerStoreActivity.class);
            intent.putExtra("markersBought",whatMarkersArePurchased);
            Log.i("banana", "setClickers: "+whatMarkersArePurchased.get(0));
            startActivity(intent);
            finish();
        });
    }

    /**
     * Send to activity.
     *
     * @param activity the activity
     */
    public void sendToActivity(Object activity){
        Intent intent=new Intent(getApplicationContext(), (Class<?>) activity);
        startActivity(intent);
        finish();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflates the menu XML into view
        getMenuInflater().inflate(R.menu.menu, menu);
        MenuItem item;
        item=menu.findItem(R.id.goToMainActiviyMenu);
        item.setVisible(false);
        if (itemOfMenu == null) {
            itemOfMenu = menu.findItem(R.id.signOutMenu);
        }
        if (currentUser == null) {
            itemOfMenu.setVisible(false);
        }
        return true;
    }
}