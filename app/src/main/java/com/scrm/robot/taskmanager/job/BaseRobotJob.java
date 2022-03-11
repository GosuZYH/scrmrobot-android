package com.scrm.robot.taskmanager.job;

import android.app.job.JobParameters;

import com.scrm.robot.RobotApplication;
import com.scrm.robot.taskmanager.enums.RobotRunState;
import com.scrm.robot.utils.ApplicationUtil;
import com.scrm.robot.utils.IdUtil;

import java.util.Date;

public abstract class BaseRobotJob implements IRobotJob{
    public BaseRobotJob() {
        this.jobId =  IdUtil.generateTimeId("Job-" );
    }
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
}
