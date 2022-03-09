package com.scrm.robot.taskmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.scrm.robot.Constants;
import com.scrm.robot.RobotApplication;
import com.scrm.robot.taskmanager.enums.RobotSchedulerJobState;
import com.scrm.robot.utils.ApplicationUtil;

public class JobSchedulerMessageReceiver extends BroadcastReceiver {
    private final static String TAG = JobSchedulerMessageReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        int jobId = intent.getIntExtra(Constants.JOB_INFO_ID_KEY,0);
        int schedulerJobState=intent.getIntExtra(Constants.MSG_SCHEDULER_JOB_STATE_KEY,0);
        Log.d(TAG, "receive: " + context+" : "+jobId+" : "+schedulerJobState);
        if(RobotSchedulerJobState.FINISH.value==schedulerJobState){
            RobotApplication robotApplication= (RobotApplication) ApplicationUtil.getApplication();
            robotApplication.getRobotJobScheduler().genNextJob();
        }
    }
}
