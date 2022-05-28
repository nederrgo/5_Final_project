package com.example.streetviewmap;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class GetNotificationDissmisBroadcast extends BroadcastReceiver {
    public GetNotificationDissmisBroadcast() {

    }
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