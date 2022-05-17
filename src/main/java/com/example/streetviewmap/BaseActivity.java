package com.example.streetviewmap;

import static android.service.controls.ControlsProviderService.TAG;

import android.app.AlertDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
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
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleEventObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class BaseActivity  extends AppCompatActivity implements LifecycleEventObserver {
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
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
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
            RoundSystem.setMarkerBitMap(getResources().getDrawable(R.drawable.normal_marker,getTheme()),76,98,this);
            Log.i("work", "onOptionsItemSelected: loged out "+FirebaseAuth.getInstance().getCurrentUser());
            startActivity(new Intent(getApplicationContext(),MainActivity.class));
            finish();
        }
        return true;
    }
    public void scheduleJob() {
        ComponentName componentName = new ComponentName(this, sendNotificationToAFKUser.class);
        JobInfo info = new JobInfo.Builder(123, componentName).setRequiresBatteryNotLow(true).setPersisted(true).setMinimumLatency(24*60*60*1000).build();
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("banana", "Job scheduled");
        } else {
            Log.d("banana", "Job scheduling failed");
        }
    }
    public void cancelJob() {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(123);
        Log.d(TAG, "Job cancelled");
    }
    @Override
    public void onStateChanged(@NonNull LifecycleOwner source, @NonNull Lifecycle.Event event) {
        if(Lifecycle.Event.ON_STOP.equals(event)){
            Log.d("banana", "Background"+event.name());
            scheduleJob();
        } else if(Lifecycle.Event.ON_START.equals(event)){
            cancelJob();
        }
    }
}
