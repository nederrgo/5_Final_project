package com.example.streetviewmap;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.util.Log;

import androidx.core.app.NotificationCompat;

/**
 * The type Send notification to afk user.
 */
@SuppressLint("SpecifyJobSchedulerIdRange")
public class sendNotificationToAFKUser extends JobService {
    /**
     * check if the job cancelled.
     */
    boolean jobCancelled=false;
    /**
     * The constant NOTIFICATION_CHANNEL_ID.
     */
    public static final String NOTIFICATION_CHANNEL_ID = "10001" ;
    /**
     * The constant default_notification_channel_id.
     */
    private final static String default_notification_channel_id = "default" ;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        createNotification();
        jobFinished(jobParameters, false);
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

    /**
     * Creates a notification.
     */
    private void createNotification () {
        NotificationManager mNotificationManager = (NotificationManager) getSystemService( NOTIFICATION_SERVICE ) ;
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext() , default_notification_channel_id ) ;
        mBuilder.setContentTitle( "did you forget me" ) ;
        mBuilder.setContentText( "you didnt play for a long time" ) ;
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
        PendingIntent onDismissPendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 0, onCancelIntent, 0);
        mBuilder.setDeleteIntent(onDismissPendingIntent);
        assert mNotificationManager != null;
        mNotificationManager.notify(( int ) System. currentTimeMillis () , mBuilder.build()) ;
    }
}
