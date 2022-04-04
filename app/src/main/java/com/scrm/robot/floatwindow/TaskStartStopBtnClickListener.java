package com.scrm.robot.floatwindow;

import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.scrm.robot.RobotApplication;
import com.scrm.robot.taskmanager.JobStateViewModel;
import com.scrm.robot.utils.ApplicationUtil;

public class TaskStartStopBtnClickListener implements View.OnClickListener {

    private final WindowManager.LayoutParams layoutParams;
    private final WindowManager windowManager;


    public TaskStartStopBtnClickListener(WindowManager.LayoutParams layoutParams, WindowManager windowManager){
        this.layoutParams=layoutParams;
        this.windowManager=windowManager;
    }


    @Override
    public void onClick(View view) {
        if(!FloatViewModel.jobStartStop.getValue()){
            FloatViewModel.jobStartStop.postValue(true);
        }else {
            RobotApplication application= (RobotApplication) ApplicationUtil.getApplication();
            application.getRobotJobScheduler().stop();
            if(application.getRobotJobScheduler().getRobotJobExecutor().getCurrentJob()!=null) {
                application.getRobotJobScheduler().getRobotJobExecutor().getCurrentJob().stop();
            }
            FloatViewModel.jobStartStop.postValue(false);
        }
    }
}