package com.example.streetviewmap;


import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

public class MainActivity extends BaseActivity {
    Button sendToGame;
    Button createPlaceButton;
    Button goToSignInButton;
    Button goToLogInButton;
    Button signOutButton;
    Button goToStore;
    private final FireBaseUtil fireBaseUtil = FireBaseUtil.FireBaseHandlerCreator();
    private final FirebaseFirestore dataBase=FirebaseFirestore.getInstance();
    private FirebaseUser currentUser;
    private FirebaseAuth firebaseAuth;
    boolean[] whatMarkersArePurchased;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        findViewsByIds();
        setClickers();
        if(RoundSystem.getCustomMarker()==null){
            RoundSystem.createMarkerBitMap(ResourcesCompat.getDrawable(getResources(),R.drawable.normal_marker,null),76,98,getApplicationContext());
        }
        String[] role= {"",""};
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser= firebaseAuth.getCurrentUser();
        if(currentUser!=null){
            goToLogInButton.setVisibility(View.INVISIBLE);
            goToSignInButton.setVisibility(View.INVISIBLE);
            signOutButton.setVisibility(View.VISIBLE);
            goToStore.setVisibility(View.VISIBLE);
           whatMarkersArePurchased=fireBaseUtil.getWhatStoreMarkersUserHave(MarkerStoreActivity.amountOfMarkers);
            dataBase.collection("users").document(currentUser.getEmail()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if (documentSnapshot.exists()) {
                    Map<String, Object> data = documentSnapshot.getData();
                    role[0] = String.valueOf(data.get("role"));
                    if(role[0].equals("admin")){
                        createPlaceButton.setVisibility(View.VISIBLE);
                    }
                }
            }
        });
        }
    }
    private void findViewsByIds(){
        sendToGame=findViewById(R.id.StartGameButton);
        createPlaceButton=findViewById(R.id.createANewPlaceButton);
        goToSignInButton =findViewById(R.id.goToSignInButton);
        goToLogInButton=findViewById(R.id.goToLogInButton);
        signOutButton=findViewById(R.id.buttonSighOut);
        goToStore=findViewById(R.id.buttonGotoStore);
    }
    private void setClickers(){
        sendToGame.setOnClickListener(view -> {
         sendToActivity(GamePlayActivity.class);
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
            FirebaseAuth.getInstance().signOut();
            sendToActivity(MainActivity.class);
        });
        goToStore.setOnClickListener(view -> {
            Intent intent=new Intent(this,MarkerStoreActivity.class);
            intent.putExtra("markersBought",whatMarkersArePurchased);
            Log.i("banana", "setClickers: "+whatMarkersArePurchased[0]);
            startActivity(intent);
            finish();
        });
    }
    public void sendToActivity(Object activity){
        Intent intent=new Intent(getApplicationContext(), (Class<?>) activity);
        startActivity(intent);
        finish();
    }

}