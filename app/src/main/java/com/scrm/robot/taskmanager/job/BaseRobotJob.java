package com.scrm.robot.taskmanager.job;

import android.accessibilityservice.AccessibilityService;
import android.app.job.JobParameters;
import android.content.Intent;
import android.util.Log;

import com.scrm.robot.RobotApplication;
import com.scrm.robot.taskmanager.RobotAccessibilityContext;
import com.scrm.robot.taskmanager.enums.RobotRunState;
import com.scrm.robot.utils.ApplicationUtil;
import com.scrm.robot.utils.IdUtil;

import java.util.Date;

public abstract class BaseRobotJob implements IRobotJob{
    public BaseRobotJob() {
        this.jobId =  IdUtil.generateTimeId("Job-" );
    }
    public final static String packageName = "com.tencent.wework";
    public RobotApplication robotApplication = (RobotApplication) ApplicationUtil.getApplication();
    public String taskStatus = "";
    public int taskId;
    public Date liveTime = null;
    private String jobId;
    private JobParameters jobParameters;
    private RobotRunState jobState=RobotRunState.STOPPED;
    private Date startTime=null;
    private Date stopTime=null;

    @Override
    public abstract void run() ;

    @Override
    public abstract void process() ;


    @Override
    public  void stop() {
        if (this.jobState == RobotRunState.STARTED) {
            this.setJobState(RobotRunState.STOPPED);
            this.setStopTime(new Date());
            RobotApplication robotApplication = (RobotApplication) ApplicationUtil.getApplication();
            robotApplication.getRobotJobScheduler().getJobSchedulerService().finishJob(this.jobParameters);
        }
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public JobParameters getJobParameters() {
        return jobParameters;
    }

    public void setJobParameters(JobParameters jobParameters) {
        this.jobParameters = jobParameters;
    }

    public RobotRunState getJobState() {
        return jobState;
    }

    public void setJobState(RobotRunState jobState) {
        this.jobState = jobState;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getStopTime() {
        return stopTime;
    }

    public void setStopTime(Date stopTime) {
        this.stopTime = stopTime;

    }
    public String getTaskStatus() {
        return taskStatus;
    }

    public void setTaskStatus(String taskStatus) {
        this.taskStatus = taskStatus;
    }

    public int getTaskId(){
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public void keyBack(){
        RobotAccessibilityContext robotAccessibilityContext = robotApplication.getRobotAccessibilityContext();
        robotAccessibilityContext.getWeWorkAccessibilityService().performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    public void rebootWework(){
        Intent launchIntentForPackage = robotApplication.getPackageManager().getLaunchIntentForPackage(packageName);
        if (launchIntentForPackage != null){
            robotApplication.getJobActivity().startActivity(launchIntentForPackage);
        }
        else{
            System.out.println("启动企业微信失败");
        }
    }

}
