package com.example.streetviewmap;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * The type Get notification dissmis broadcast.
 */
public class GetNotificationDissmisBroadcast extends BroadcastReceiver {
    /**
     * Instantiates a new Get notification dissmis broadcast.
     */
    public GetNotificationDissmisBroadcast() {

    }
    /**
     * active when receiving intent
     * @param context the context of the intent
     * @param intent the intent that was sent to the receiver
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        ComponentName componentName = new ComponentName(context,sendNotificationToAFKUser.class);
        JobInfo info = new JobInfo.Builder(123, componentName).setRequiresBatteryNotLow(true).setPersisted(true).setMinimumLatency(24*60*60*1000).build();
        JobScheduler scheduler = (JobScheduler) context.getSystemService(context.JOB_SCHEDULER_SERVICE);
        int resultCode = scheduler.schedule(info);
        if (resultCode == JobScheduler.RESULT_SUCCESS) {
            Log.d("banana", "Job scheduled");
        } else {
            Log.d("banana", "Job scheduling failed");
        }
    }

}