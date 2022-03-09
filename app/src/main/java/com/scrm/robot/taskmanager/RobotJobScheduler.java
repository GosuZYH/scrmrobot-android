package com.scrm.robot.taskmanager;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.app.job.JobScheduler;
import android.util.Log;

import com.scrm.robot.RobotApplication;
import com.scrm.robot.taskmanager.enums.RobotJobType;
import com.scrm.robot.taskmanager.enums.RobotRunState;
import com.scrm.robot.utils.ApplicationUtil;

import java.util.concurrent.LinkedBlockingDeque;

public class RobotJobScheduler {
    private final static String TAG = RobotJobScheduler.class.getName();
    private final RobotJobExecutor robotJobExecutor;
    private JobScheduler jobScheduler;
    private JobSchedulerService jobSchedulerService;

    private RobotRunState runState;

    public RobotJobScheduler() {
        this.runState=RobotRunState.STOPPED;
        this.robotJobExecutor=new RobotJobExecutor();
    }

    public void start(){
        this.runState = RobotRunState.STARTED;
    }

    public void stop(){
        this.runState = RobotRunState.STOPPED;
    }

    public JobScheduler getJobScheduler() {
        return jobScheduler;
    }

    public void setJobScheduler(JobScheduler jobScheduler) {
        this.jobScheduler = jobScheduler;
    }

    public JobSchedulerService getJobSchedulerService() {
        return jobSchedulerService;
    }

    public void setJobSchedulerService(JobSchedulerService jobSchedulerService) {
        this.jobSchedulerService = jobSchedulerService;
    }

    public void genNextJob(){
        this.addJob(RobotJobType.SOP_AGENT_SEND_MOMENT);
    }

    /**
     *
     * @param jobType
     */
    public void addJob(RobotJobType jobType){
        try {
            JobInfo jobInfo = ((RobotApplication)ApplicationUtil.getApplication()).getRobotJobFactory().buildJobInfo(jobType);
            if(jobInfo!=null) {
                this.jobScheduler.schedule(jobInfo);
            }
        }catch (Exception ex){
            Log.e(TAG, ex.getMessage());
        }
    }

    public JobInfo getJob(int jobId){
        return this.jobScheduler.getPendingJob(jobId);
    }

    public void runJob(JobParameters jobParameters){
        this.robotJobExecutor.run(jobParameters);
    }

    public RobotJobExecutor getRobotJobExecutor() {
        return robotJobExecutor;
    }
}
