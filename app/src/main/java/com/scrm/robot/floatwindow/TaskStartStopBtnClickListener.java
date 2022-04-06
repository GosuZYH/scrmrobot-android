package com.scrm.robot.floatwindow;

import android.view.View;
import android.view.WindowManager;

import com.orhanobut.logger.Logger;
import com.scrm.robot.RobotApplication;
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
            Logger.d("控制按钮点击: 启动");
            FloatViewModel.jobStartStop.postValue(true);
        }else {
            Logger.d("控制按钮点击: 停止");
            RobotApplication application= (RobotApplication) ApplicationUtil.getApplication();
            application.getRobotJobScheduler().stop();
            FloatViewModel.jobStartStop.postValue(false);
        }
    }
}