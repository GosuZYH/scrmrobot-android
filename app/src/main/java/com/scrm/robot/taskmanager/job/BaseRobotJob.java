package com.scrm.robot.taskmanager.job;

import android.app.job.JobParameters;
import android.util.Log;

import com.orhanobut.logger.Logger;
import com.scrm.robot.RobotApplication;
import com.scrm.robot.taskmanager.RobotAccessibilityContext;
import com.scrm.robot.taskmanager.enums.RobotJobType;
import com.scrm.robot.taskmanager.enums.RobotRunState;
import com.scrm.robot.utils.ApplicationUtil;
import com.scrm.robot.utils.IdUtil;

import java.util.Date;

public abstract class BaseRobotJob implements IRobotJob{
    private final static String TAG = BaseRobotJob.class.getName();

    public BaseRobotJob() {
        this.jobId =  IdUtil.generateTimeId("Job-" );
    }
    public String taskStatus = "";
    public int taskId;
    private String jobId;
    private RobotJobType jobType;
    private JobParameters jobParameters;
    private RobotRunState jobState=RobotRunState.FINISH;
    private Date startTime=null;
    private Date finishTime =null;
    private Date processTime=null;
    private RobotAccessibilityContext robotAccessibilityContext;

    @Override
    public void run() {
        Logger.d("运行- %s", this.toString());
        this.setJobState(RobotRunState.STARTED);
        this.setStartTime(new Date());
        this.process();
    }


    @Override
    public  void process() {
        this.processTime=new Date();
    }

    @Override
    public void pause() {
        Logger.d("暂停- %s",this.toString());
        this.setJobState(RobotRunState.WAITING);
    }


    public boolean reRun(){
        if(this.canReRun()) {
            Logger.d("重新启动- %s", this.toString());
            this.run();
            return true;
        }
        return false;
    }

    private boolean canReRun(){
        if(this.getJobState()==RobotRunState.FINISH){
            return false;
        }
        return true;
    }

    @Override
    public  void finish() {
        boolean canFinish = this.canFinish();
        if (canFinish) {
            Logger.d("结束- %s",this.toString());
            this.setJobState(RobotRunState.FINISH);
            this.setFinishTime(new Date());
        }
    }

    @Override
    public void finishAndReschedule() {
        boolean canFinish = this.canFinish();
        if (canFinish) {
            Logger.d("结束并且重新调度- %s", this.toString());

            this.setJobState(RobotRunState.FINISH);
            this.setFinishTime(new Date());
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

    public RobotJobType getJobType() {
        return jobType;
    }

    public void setJobType(RobotJobType jobType) {
        this.jobType = jobType;
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

    public Date getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(Date finishTime) {
        this.finishTime = finishTime;
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


    private boolean canFinish(){
        return this.jobState==RobotRunState.STARTED || this.jobState==RobotRunState.WAITING;
    }

    public RobotAccessibilityContext getRobotAccessibilityContext() {
        return robotAccessibilityContext;
    }

    public void setRobotAccessibilityContext(RobotAccessibilityContext robotAccessibilityContext) {
        this.robotAccessibilityContext = robotAccessibilityContext;
    }

    public boolean canProcess() {
        RobotApplication application= (RobotApplication) ApplicationUtil.getApplication();
        if(!application.getRobotJobScheduler().isRunning()){
            return false;
        }
        return this.jobState == RobotRunState.STARTED && this.robotAccessibilityContext != null;
    }

    @Override
    public String toString() {
        return String.format(" 类型：%s 状态：%s id: %s",
                this.jobType == null ? "[未知]" : this.jobType.name,
                this.jobState == null ? "[未知]" : this.jobState.name,
                this.jobId
        );
    }
}
