package com.example.streetviewmap;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.app.job.JobService;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

public class sendNotificationToAFKUser extends JobService {
    boolean jobCancelled=false;
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    private final static String default_notification_channel_id = "default" ;
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                    createNotification();
                }
                try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                Log.d("work", "Job finished");
                jobFinished(jobParameters, false);
            }
        }).start();
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d("work", "Job cancelled before completion");
        jobCancelled = true;
        return true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
    @RequiresApi(api = Build.VERSION_CODES.S)
    private void createNotification () {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService( NOTIFICATION_SERVICE ) ;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), default_notification_channel_id ) ;
        mBuilder.setContentTitle( "did you forget me" ) ;
        mBuilder.setContentText( "you didn't play for a long time" ) ;
        mBuilder.setSmallIcon(R.drawable. ic_launcher_foreground ) ;
        mBuilder.setAutoCancel( true ) ;
        if (android.os.Build.VERSION. SDK_INT >= android.os.Build.VERSION_CODES. O ) {
            int importance = NotificationManager. IMPORTANCE_HIGH ;
            NotificationChannel notificationChannel = new NotificationChannel( NOTIFICATION_CHANNEL_ID , "NOTIFICATION_CHANNEL_NAME" , importance) ;
            mBuilder.setChannelId( NOTIFICATION_CHANNEL_ID ) ;
            assert mNotificationManager != null;
            mNotificationManager.createNotificationChannel(notificationChannel) ;
        }
        Intent onCancelIntent = new Intent(this, GetNotificationDissmisBroadcast.class);
        PendingIntent onDismissPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(),
                0,
                onCancelIntent,  PendingIntent.FLAG_MUTABLE);
       // mBuilder.setDeleteIntent(onDismissPendingIntent);
        assert mNotificationManager != null;
        mNotificationManager.notify(( int ) System. currentTimeMillis () , mBuilder.build()) ;
    }
}
