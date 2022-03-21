package com.scrm.robot;

import android.annotation.SuppressLint;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Messenger;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.scrm.robot.floatwindow.FloatViewModel;
import com.scrm.robot.taskmanager.JobSchedulerMessageHandler;
import com.scrm.robot.taskmanager.JobSchedulerService;
import com.scrm.robot.taskmanager.RobotJobFactory;
import com.scrm.robot.taskmanager.RobotJobScheduler;
import com.scrm.robot.taskmanager.enums.RobotJobType;
import com.scrm.robot.utils.ApplicationUtil;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

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
    protected void onResume() {
        super.onResume();
        suspensionWindowPermissionCheck();
    }

    @Override
    protected void onStop(){
        stopService(new Intent(this, JobSchedulerService.class));
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void btnOpenWeWorkClick(View view){
        Log.d(TAG, "打开企业微信");
        try {
            testForImgRgb();
//            openWework();
            Toast.makeText(MainActivity.this, "打开企业微信", Toast.LENGTH_SHORT).show();
        }catch (Exception ignored){
            Toast.makeText(MainActivity.this, "启动企业微信失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void groupSendTask(View view){
        Log.d(TAG, "执行1V1私聊群发");
        try {
            Toast.makeText(MainActivity.this, "执行群发助手任务", Toast.LENGTH_SHORT).show();
            if (this.jobScheduler == null) {
                return;
            }
            this.jobScheduler.start();
            this.jobScheduler.addJob(RobotJobType.GROUP_SEND_MOMENT);
            openWework();
        } catch (Exception ignored) {
            Toast.makeText(MainActivity.this, "error..", Toast.LENGTH_SHORT).show();
        }
    }

    public void customerFriendCircleTask(View view){
        Log.d(TAG, "执行客户朋友圈任务");
        try {
            Toast.makeText(MainActivity.this, "执行客户朋友圈任务", Toast.LENGTH_SHORT).show();
            if (this.jobScheduler == null) {
                return;
            }
            this.jobScheduler.start();
            this.jobScheduler.addJob(RobotJobType.CUSTOMER_AGENT_SEND_MOMENT);
            openWework();
        } catch (Exception ignored) {
            Toast.makeText(MainActivity.this, "error..", Toast.LENGTH_SHORT).show();
        }
    }

    public void sopFriendCircleTask(View view){
        Log.d(TAG, "执行sop朋友圈任务");
        try {
            Toast.makeText(MainActivity.this, "执行sop朋友圈任务", Toast.LENGTH_SHORT).show();
            if (this.jobScheduler == null) {
                return;
            }
            this.jobScheduler.start();
            this.jobScheduler.addJob(RobotJobType.SOP_AGENT_SEND_MOMENT);
            openWework();
        } catch (Exception ignored) {
            Toast.makeText(MainActivity.this, "error..", Toast.LENGTH_SHORT).show();
        }
    }

    public void allTask(View view){
        Log.d(TAG, "执行所有任务");
        try {
            Toast.makeText(MainActivity.this, "循环执行所有任务", Toast.LENGTH_SHORT).show();
            openWework();
        } catch (Exception ignored) {
            Toast.makeText(MainActivity.this, "error..", Toast.LENGTH_SHORT).show();
        }
    }


    public void btnAccessSettingClick(View view){
        Log.d(TAG, "跳转到辅助功能");
        try {
            startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            Toast.makeText(MainActivity.this, "Accessibility service", Toast.LENGTH_SHORT).show();
        } catch (Exception ignored) {
            Toast.makeText(MainActivity.this, "can not open Accessibility service", Toast.LENGTH_SHORT).show();
        }
    }

    public void suspendedBallSetting(View view){
        Log.d(TAG, "跳转到悬浮球功能");
        try {
            Toast.makeText(MainActivity.this, "check suspension-Window permission", Toast.LENGTH_SHORT).show();
            if (!Settings.canDrawOverlays(this)) {
                //没有权限，需要申请悬浮球权限
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                intent.setData(Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 100);
            } else {
                //已经有权限，可以直接显示悬浮窗
                Toast.makeText(MainActivity.this, "suspension-Window have accessed！", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception ignored) {
            Toast.makeText(MainActivity.this, "check suspension-Window permission error", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("SdCardPath")
    private void testForImgRgb(){
        FileInputStream fis = null;
        try {
            fis = new FileInputStream("/sdcard/test.png");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap  = BitmapFactory.decodeStream(fis);
        Color color = bitmap.getColor(933,1733);
        int pixel = bitmap.getPixel(933,1733);
        System.out.println("red:"+color.red());
        System.out.println("green:"+color.green());
        System.out.println("blue:"+color.blue());
        System.out.println("获取图片中该像素Color:"+color);
        System.out.println("获取图片中该像素Pixel:"+pixel);
    }

    private void suspensionWindowPermissionCheck() {
        if (!Settings.canDrawOverlays(this)) {
            //没有权限，需要申请悬浮球权限
            Toast.makeText(MainActivity.this, "请点击检查悬浮球权限是否已打开", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void btnScheduleJobClick(View view){
        Log.d(TAG, "定时任务开始");
        this.openFloatView();
        this.btnOpenWeWorkClick(view);
        this.scheduleJob();
    }

    public void openWework(){
        //打开企微
        Intent intent = getPackageManager().getLaunchIntentForPackage(Constants.WEWORK_PACKAGE_NAME);
        startActivity(intent);
    }

    public void btnCancelScheduleJobClick(View view){
        Log.d(TAG, "停止定时任务");
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