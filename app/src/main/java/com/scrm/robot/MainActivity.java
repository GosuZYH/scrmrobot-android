package com.scrm.robot;

import android.annotation.SuppressLint;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Messenger;
import android.provider.Settings;
import android.util.Log;
import android.view.View;

import com.scrm.robot.floatwindow.FloatViewModel;
import com.scrm.robot.taskmanager.JobSchedulerMessageHandler;
import com.scrm.robot.taskmanager.JobSchedulerService;
import com.scrm.robot.taskmanager.RobotJobFactory;
import com.scrm.robot.taskmanager.RobotJobScheduler;
import com.scrm.robot.taskmanager.enums.RobotJobType;
import com.scrm.robot.utils.ApplicationUtil;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private final static String TAG = MainActivity.class.getName();

//    private JobSchedulerMessageHandler mJobSchedulerMessageHandler;
    private ComponentName jobScheduleServiceComponent;
    private RobotJobScheduler jobScheduler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

//        this.mJobSchedulerMessageHandler = new JobSchedulerMessageHandler(this);
        this.jobScheduleServiceComponent = new ComponentName(this, JobSchedulerService.class);
        this.jobScheduler = new RobotJobScheduler();

        RobotApplication robotApplication = (RobotApplication) ApplicationUtil.getApplication();
        robotApplication.setJobActivity(this);
        robotApplication.setRobotJobScheduler(this.jobScheduler);
        JobScheduler sysJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        this.jobScheduler.setJobScheduler(sysJobScheduler);

        robotApplication.setRobotJobFactory(new RobotJobFactory());


        this.requestCapturePermission();


    }

    @Override
    protected void onStart(){
        super.onStart();
        Intent startJobScheduleServiceIntent=new Intent(this, JobSchedulerService.class);
//        Messenger jobScheduleMessenger = new Messenger(this.mJobSchedulerMessageHandler);
//        startJobScheduleServiceIntent.putExtra(Constants.MESSENGER_INTENT_KEY, jobScheduleMessenger);

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
        this.openFloatView();
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
        this.closeFloatView();
        this.jobScheduler.stop();
    }

    /**
     * 打开悬浮窗
     */
    private void openFloatView(){
        FloatViewModel.isFloatWindowShow.postValue(true);
    }
    private void closeFloatView(){
        FloatViewModel.isFloatWindowShow.postValue(false);
    }

    /**
     * 截图服务
     * @return
     */
    @SuppressLint("ObsoleteSdkInt")
    public void requestCapturePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //5.0 之后才允许使用屏幕截图
            return;
        }

        MediaProjectionManager mediaProjectionManager = (MediaProjectionManager)
                getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        startActivityForResult(
                mediaProjectionManager.createScreenCaptureIntent(),
                Constants.REQUEST_MEDIA_PROJECTION);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.REQUEST_MEDIA_PROJECTION:
                if (resultCode == RESULT_OK && data != null) {
                    ScreenShotService.setResultData(data);
//                    startService(new Intent(getApplicationContext(), ScreenShotService.class));
                    startForegroundService(new Intent(getApplicationContext(), ScreenShotService.class));

                }
                break;
        }
    }

    public ComponentName getJobScheduleServiceComponent() {
        return jobScheduleServiceComponent;
    }
}