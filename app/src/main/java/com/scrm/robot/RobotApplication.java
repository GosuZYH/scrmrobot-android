package com.scrm.robot;

import android.app.Activity;
import android.app.Application;
import android.view.accessibility.AccessibilityNodeInfo;

import com.scrm.robot.taskmanager.RobotAccessibilityContext;
import com.scrm.robot.taskmanager.RobotJobFactory;
import com.scrm.robot.taskmanager.RobotJobScheduler;
import com.scrm.robot.utils.ApplicationUtil;

public class RobotApplication extends Application {
   private RobotAccessibilityContext robotAccessibilityContext;

    private RobotJobScheduler robotJobScheduler;
    private RobotJobFactory robotJobFactory;
    private Activity jobActivity;
    private int jobId=0;

    @Override
    public void onCreate() {
        super.onCreate();
        ApplicationUtil.init(this);
    }

    public RobotAccessibilityContext getRobotAccessibilityContext() {
        return robotAccessibilityContext;
    }

    public void setRobotAccessibilityContext(RobotAccessibilityContext robotAccessibilityContext) {
        this.robotAccessibilityContext = robotAccessibilityContext;
    }

    public int generateJobId(){
        return jobId+=1;
    }


    public RobotJobScheduler getRobotJobScheduler() {
        return robotJobScheduler;
    }

    public void setRobotJobScheduler(RobotJobScheduler robotJobScheduler) {
        this.robotJobScheduler = robotJobScheduler;
    }

    public Activity getJobActivity() {
        return jobActivity;
    }

    public void setJobActivity(Activity jobActivity) {
        this.jobActivity = jobActivity;
    }

    public RobotJobFactory getRobotJobFactory() {
        return robotJobFactory;
    }

    public void setRobotJobFactory(RobotJobFactory robotJobFactory) {
        this.robotJobFactory = robotJobFactory;
    }
}
