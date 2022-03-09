package com.scrm.robot.taskmanager;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.scrm.robot.Constants;
import com.scrm.robot.MainActivity;
import com.scrm.robot.RobotApplication;
import com.scrm.robot.taskmanager.enums.RobotSchedulerJobState;
import com.scrm.robot.utils.ApplicationUtil;

import java.lang.ref.WeakReference;

public class JobSchedulerMessageHandler extends Handler {
    private final static String TAG = JobSchedulerMessageHandler.class.getName();
    private WeakReference<MainActivity> mActivity;

    public JobSchedulerMessageHandler(MainActivity activity){
        super();
        this.mActivity = new WeakReference<MainActivity>(activity);
    }

    @Override
    public void handleMessage(Message message){
        MainActivity mainActivity=this.mActivity.get();
        if(mainActivity==null){
            return;
        }
        JobParameters jobParameters = (JobParameters) message.obj;
        Log.d(TAG,"handle message: "+message.what);
        if(RobotSchedulerJobState.FINISH.value ==message.what) {
           RobotApplication robotApplication= (RobotApplication) ApplicationUtil.getApplication();
           robotApplication.getRobotJobScheduler().genNextJob();
        }
    }
}
