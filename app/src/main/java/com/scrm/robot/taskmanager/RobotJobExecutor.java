package com.scrm.robot.taskmanager;

import static com.scrm.robot.Constants.INTENT_JOB_INFO_TYPE_KEY;

import android.app.job.JobInfo;
import android.app.job.JobParameters;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.scrm.robot.RobotApplication;
import com.scrm.robot.taskmanager.enums.RobotRunState;
import com.scrm.robot.taskmanager.job.BaseRobotJob;
import com.scrm.robot.taskmanager.enums.RobotJobType;
import com.scrm.robot.utils.ApplicationUtil;

public class RobotJobExecutor {
    private BaseRobotJob currentJob;
    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void run(JobParameters jobParameters){
        RobotApplication application = (RobotApplication) ApplicationUtil.getApplication();
        if(currentJob!=null && this.currentJob.getJobState()== RobotRunState.STARTED){
            return;
        }
        JobInfo jobInfo= application.getRobotJobScheduler().getJob(jobParameters.getJobId());
        if(jobInfo!=null) {
            int robotJobTypeValue = jobInfo.getExtras().getInt(INTENT_JOB_INFO_TYPE_KEY);
            RobotJobType robotJobType = RobotJobType.getByValue(robotJobTypeValue);
            BaseRobotJob robotJob = application.getRobotJobFactory().buildRobotJob(robotJobType);
            if (robotJob != null) {
                robotJob.setJobParameters(jobParameters);
                this.currentJob = robotJob;
                robotJob.run();
            }
        }
    }

    public BaseRobotJob getCurrentJob() {
        return currentJob;
    }
}
