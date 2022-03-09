package com.scrm.robot.taskmanager;

import static com.scrm.robot.Constants.JOB_INFO_TYPE_KEY;
import static com.scrm.robot.Constants.JOB_INTERVAL_MILL_SECONDS;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.os.Build;

import com.scrm.robot.MainActivity;
import com.scrm.robot.RobotApplication;
import com.scrm.robot.taskmanager.job.BaseRobotJob;
import com.scrm.robot.taskmanager.enums.RobotJobType;
import com.scrm.robot.taskmanager.job.SopAgentSendMomentJob;
import com.scrm.robot.utils.ApplicationUtil;

public class RobotJobFactory {
    public JobInfo buildJobInfo(RobotJobType robotJobType) throws Exception {
        RobotApplication robotApplication = (RobotApplication) ApplicationUtil.getApplication();
        if (robotJobType == RobotJobType.SOP_AGENT_SEND_MOMENT) {

            JobInfo.Builder builder = new JobInfo.Builder(robotApplication.generateJobId(),
                    ((MainActivity)robotApplication.getJobActivity()).getJobScheduleServiceComponent());

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                // android N之后时间必须在15分钟以上
                builder.setMinimumLatency(JOB_INTERVAL_MILL_SECONDS);
                 builder.setOverrideDeadline(10*1000);
                 builder.setBackoffCriteria(10*1000, JobInfo.BACKOFF_POLICY_LINEAR);
            } else {
                builder.setPeriodic(JOB_INTERVAL_MILL_SECONDS);
            }
            builder.setPersisted(true);
            builder.setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY);
            builder.setRequiresDeviceIdle(false);
            builder.setRequiresCharging(false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //指定要运行此作业，设备的电池电量不得过低。
                builder.setRequiresBatteryNotLow(false);
            }
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //指定要运行此作业，设备的可用存储空间不得过低
                builder.setRequiresStorageNotLow(false);
            }
            JobInfo jobInfo = builder.build();
            jobInfo.getExtras().putInt(JOB_INFO_TYPE_KEY, robotJobType.value);
            return jobInfo;
        }
        return null;
    }

    public BaseRobotJob buildRobotJob(RobotJobType robotJobType){
        if(robotJobType==RobotJobType.SOP_AGENT_SEND_MOMENT){
            return new SopAgentSendMomentJob();
        }
        return null;
    }
}
