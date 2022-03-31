package com.example.streetviewmap;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity  extends AppCompatActivity {
    MenuItem itemSignOut;
    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        setTitle("geoNeder");
        firebaseAuth = FirebaseAuth.getInstance();
        currentUser= firebaseAuth.getCurrentUser();
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        RoundSystem.roundNum=1;
                        finish();
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflates the menu XML into view
        getMenuInflater().inflate(R.menu.menu,menu);
        if(itemSignOut==null) {
            itemSignOut = menu.findItem(R.id.signOutMenu);
        }
        if(currentUser==null){
            itemSignOut.setVisible(false);
        }
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if(id==R.id.goToMainActiviyMenu){
            Log.i("work", "onOptionsItemSelected: loged out "+FirebaseAuth.getInstance().getCurrentUser());
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        if(id==R.id.signOutMenu){
            FirebaseAuth.getInstance().signOut();
            Log.i("work", "onOptionsItemSelected: loged out "+FirebaseAuth.getInstance().getCurrentUser());
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        return true;
    }
}
