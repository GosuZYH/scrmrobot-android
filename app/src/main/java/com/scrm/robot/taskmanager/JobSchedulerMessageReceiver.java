package com.scrm.robot.taskmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.scrm.robot.Constants;
import com.scrm.robot.RobotApplication;
import com.scrm.robot.taskmanager.enums.RobotBroadcastType;
import com.scrm.robot.taskmanager.enums.RobotSchedulerJobState;
import com.scrm.robot.utils.ApplicationUtil;

public class JobSchedulerMessageReceiver extends BroadcastReceiver {
    private final static String TAG = JobSchedulerMessageReceiver.class.getName();

    @Override
    public void onReceive(Context context, Intent intent) {
        int broadcastTypeValue = intent.getIntExtra(Constants.BROADCAST_MSG_TYPE_KEY,0);
        RobotBroadcastType broadcastType = RobotBroadcastType.getByValue(broadcastTypeValue);
        if(broadcastType==null){
            return;
        }
        RobotApplication robotApplication = (RobotApplication) ApplicationUtil.getApplication();

        if(broadcastType==RobotBroadcastType.JOB_STATE_BROADCAST) {
            int jobId = intent.getIntExtra(Constants.INTENT_JOB_INFO_ID_KEY, 0);
            int schedulerJobState = intent.getIntExtra(Constants.MSG_SCHEDULER_JOB_STATE_KEY, 0);
            Log.d(TAG, "receive: " + context + " : " + jobId + " : " + schedulerJobState);
            if (RobotSchedulerJobState.FINISH.value == schedulerJobState) {
                robotApplication.getRobotJobScheduler().genNextJob();
            }
        }else if(broadcastType==RobotBroadcastType.SCREENSHOT_FINISH_BROADCAST){
            // 截图完成
            String fileName = intent.getStringExtra(Constants.INTENT_SCREENSHOT_FILE_NAME_KEY);
            Log.d(TAG,"receive screenshot file: "+ fileName);
            JobStateViewModel.isScreenShot.postValue(false);
            // TODO 继续处理
            robotApplication.getRobotJobScheduler().getRobotJobExecutor().getCurrentJob().process();
        }
    }
}
