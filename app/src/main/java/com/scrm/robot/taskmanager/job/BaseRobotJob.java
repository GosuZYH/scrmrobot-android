package com.scrm.robot.taskmanager.job;

import android.accessibilityservice.AccessibilityService;
import android.app.job.JobParameters;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.scrm.robot.RobotApplication;
import com.scrm.robot.taskmanager.RobotAccessibilityContext;
import com.scrm.robot.taskmanager.enums.RobotRunState;
import com.scrm.robot.utils.ApplicationUtil;
import com.scrm.robot.utils.IdUtil;

import java.util.Date;

public abstract class BaseRobotJob implements IRobotJob{
    private final static String TAG = BaseRobotJob.class.getName();


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
    private Date processTime=null;
    private RobotAccessibilityContext robotAccessibilityContext;

    @Override
    public void run() {
        Log.d(TAG, String.format("%s start run", this.getJobId()));
        this.setJobState(RobotRunState.STARTED);
        this.setStartTime(new Date());
        // TODO DEBUGING 停止启动后再运行，因为没有事件，无法触发
        this.process();
    }


    @Override
    public  void process() {
        this.processTime=new Date();
    }

    @Override
    public void pause() {
        this.setJobState(RobotRunState.WAITING);
    }


    public void reRun(){
        if(this.getJobState()==RobotRunState.STOPPED){
            return;
        }
        this.run();
    }

    @Override
    public  void stop() {
        boolean canStop = this.canStop();
        if (canStop) {
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

    public Date getProcessTime() {
        return processTime;
    }

    public void setProcessTime(Date processTime) {
        this.processTime = processTime;
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
//        RobotAccessibilityContext robotAccessibilityContext = application.getRobotAccessibilityContext();
        RobotAccessibilityContext robotAccessibilityContext = this.getRobotAccessibilityContext();
        RobotApplication application= (RobotApplication) ApplicationUtil.getApplication();
        application.getWeWorkAccessibilityService().performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
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

    private boolean canStop(){
        return this.jobState==RobotRunState.STARTED || this.jobState==RobotRunState.WAITING;
    }

    public RobotAccessibilityContext getRobotAccessibilityContext() {
        return robotAccessibilityContext;
    }

    public void setRobotAccessibilityContext(RobotAccessibilityContext robotAccessibilityContext) {
        this.robotAccessibilityContext = robotAccessibilityContext;
    }

    public boolean canProcess() {
        return this.jobState == RobotRunState.STARTED && this.robotAccessibilityContext != null;
    }
}
