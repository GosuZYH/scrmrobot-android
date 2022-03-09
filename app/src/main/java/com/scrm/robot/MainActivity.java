package com.scrm.robot;

import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Messenger;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.scrm.robot.taskmanager.JobSchedulerMessageHandler;
import com.scrm.robot.taskmanager.JobSchedulerService;
import com.scrm.robot.taskmanager.RobotJobFactory;
import com.scrm.robot.taskmanager.RobotJobScheduler;
import com.scrm.robot.taskmanager.enums.RobotJobType;
import com.scrm.robot.utils.ApplicationUtil;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getName();

    private JobSchedulerMessageHandler mJobSchedulerMessageHandler;
    private ComponentName jobScheduleServiceComponent;
    private RobotJobScheduler jobScheduler;
    private Messenger jobScheduleMessenger;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        this.mJobSchedulerMessageHandler = new JobSchedulerMessageHandler(this);
        this.jobScheduleServiceComponent = new ComponentName(this, JobSchedulerService.class);
        this.jobScheduler = new RobotJobScheduler();

        RobotApplication robotApplication = (RobotApplication) ApplicationUtil.getApplication();
        robotApplication.setJobActivity(this);
        robotApplication.setRobotJobScheduler(this.jobScheduler);
        JobScheduler sysJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        this.jobScheduler.setJobScheduler(sysJobScheduler);

        robotApplication.setRobotJobFactory(new RobotJobFactory());
    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent startJobScheduleServiceIntent=new Intent(this, JobSchedulerService.class);
        this.jobScheduleMessenger=new Messenger(this.mJobSchedulerMessageHandler);
        startJobScheduleServiceIntent.putExtra(Constants.MESSENGER_INTENT_KEY, this.jobScheduleMessenger);

        startService(startJobScheduleServiceIntent);
    }

    @Override
    protected void onStop(){
        stopService(new Intent(this, JobSchedulerService.class));
        super.onStop();
    }



    public void btnOpenWeWorkClick(View view){
        Intent intent = getPackageManager().getLaunchIntentForPackage(Constants.WEWORK_PACKAGE_NAME);
        startActivity(intent);
    }

    public void btnAccessSettingClick(View view){
        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    }


    public void btnScheduleJobClick(View view){
        this.btnOpenWeWorkClick(view);
        this.scheduleJob();
    }

    public void btnCancelScheduleJobClick(View view){
        this.cancelScheduleJob();
    }

    private void scheduleJob() {
        Log.d(TAG, "start schedule job");
        if (this.jobScheduler == null) {
            return;
        }
        this.jobScheduler.start();
        this.jobScheduler.addJob(RobotJobType.SOP_AGENT_SEND_MOMENT);
    }

    private void cancelScheduleJob(){
        Log.d(TAG,"cancel schedule job");
        this.jobScheduler.stop();
    }

    public ComponentName getJobScheduleServiceComponent() {
        return jobScheduleServiceComponent;
    }
}