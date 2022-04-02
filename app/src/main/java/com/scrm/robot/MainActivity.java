package com.scrm.robot;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.os.Environment;
import android.os.Messenger;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.scrm.robot.floatwindow.FloatViewModel;
import com.scrm.robot.taskmanager.JobSchedulerMessageHandler;
import com.scrm.robot.taskmanager.JobSchedulerService;
import com.scrm.robot.taskmanager.RobotJobFactory;
import com.scrm.robot.taskmanager.RobotJobScheduler;
import com.scrm.robot.taskmanager.enums.RobotJobType;
import com.scrm.robot.utils.ApplicationUtil;
import com.scrm.robot.utils.LogUtil;
import static com.scrm.robot.utils.LogUtil.appendToFile_One;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainActivity extends FragmentActivity{
    private final static String TAG = MainActivity.class.getName();
    //index page layout
    public LinearLayout mTab1;
    public LinearLayout mTab2;

    //tab button
    public ImageButton mImg1;
    public ImageButton mImg2;

    //tab Fragment
    public Fragment mFrag1;
    public Fragment mFrag2;

    //float notice
    public Dialog noticeDialog;
    public Dialog floatNoticeDialog;
    private ComponentName jobScheduleServiceComponent;
    public static RobotJobScheduler jobScheduler;

    @SuppressLint("SdCardPath")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.jobScheduleServiceComponent = new ComponentName(this, JobSchedulerService.class);
        jobScheduler = new RobotJobScheduler();

        RobotApplication robotApplication = (RobotApplication) ApplicationUtil.getApplication();
        robotApplication.setJobActivity(this);
        robotApplication.setRobotJobScheduler(jobScheduler);
        JobScheduler sysJobScheduler = (JobScheduler) getSystemService(Context.JOB_SCHEDULER_SERVICE);
        jobScheduler.setJobScheduler(sysJobScheduler);
        robotApplication.setRobotJobFactory(new RobotJobFactory());

        this.requestCapturePermission();

        //log文件输出
//        LogUtil.d("主页已加载");
//        LogUtil.appendToFile_Third("/storage/emulated/0/scrm.log","主页已加载");
        selectTab(0);
    }

    public void initLoginView(){

    }

    public void onClickMainPage(View view){
        resetImgs();
        selectTab(0);
    }

    public void onClickMinePage(View view){
        resetImgs();
        selectTab(1);
    }

    //将四个的Fragment隐藏
    private void hideFragments(FragmentTransaction transaction) {
        if (mFrag1 != null) {
            transaction.hide(mFrag1);
        }
        if (mFrag2 != null) {
            transaction.hide(mFrag2);
        }
    }

    //将四个ImageButton置为灰色
    private void resetImgs() {
        mImg1 = findViewById(R.id.id_tab_img1);
        mImg2 = findViewById(R.id.id_tab_img2);
        mImg1.setImageResource(R.drawable.mine_tab_icon_home_default);
        mImg2.setImageResource(R.drawable.hme_tab_icon_mine_default);
    }

    //进行选中Tab的处理
    private void selectTab(int i) {
        //获取FragmentManager对象
        FragmentManager manager = getSupportFragmentManager();
        //获取FragmentTransaction对象
        FragmentTransaction transaction = manager.beginTransaction();
        //先隐藏所有的Fragment
        hideFragments(transaction);
        switch (i) {
            //当选中点击的是第一页的Tab时
            case 0:
                mImg1 = findViewById(R.id.id_tab_img1);
                mImg1.setImageResource(R.drawable.hme_tab_icon_home_select);
                if (mFrag1 == null) {
                    mFrag1 = new MainFragment();
                    transaction.add(R.id.id_content, mFrag1);
                } else {
                    //如果第一页对应的Fragment已经实例化，则直接显示出来
                    transaction.show(mFrag1);
                }
                break;
            case 1:
                mImg2 = findViewById(R.id.id_tab_img2);
                mImg2.setImageResource(R.drawable.mine_tab_icon_mine_select);
                if (mFrag2 == null) {
                    mFrag2 = new MineFragment();
                    transaction.add(R.id.id_content, mFrag2);
                } else {
                    transaction.show(mFrag2);
                }
                break;
        }
        transaction.commit();
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
    }

    @Override
    protected void onStop(){
        stopService(new Intent(this, JobSchedulerService.class));
        super.onStop();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    public void btnOpenWeWorkClick(View view){
        Log.d(TAG, "单点测试");
        try {
            //test
            testForImgRgb();
//            this.openCloseFloatView();
            //设置任务类型为1
//            FloatViewModel.currentOnClickJob.postValue(RobotJobType.SOP_AGENT_SEND_MOMENT);
            Toast.makeText(MainActivity.this, "单点测试", Toast.LENGTH_SHORT).show();
        }catch (Exception ignored){
            Toast.makeText(MainActivity.this, "单点测试失败", Toast.LENGTH_SHORT).show();
        }
    }

    public void groupSendTask(View view){
        Log.d(TAG, "执行1V1私聊群发");
        try {
            if (jobScheduler == null) {
                return;
            }
            if(isCheckPermissionsOk(view)){
                Intent intent = new Intent(this,JobActivity.class);
                startActivity(intent);
//                Toast.makeText(MainActivity.this, "执行群发助手任务", Toast.LENGTH_SHORT).show();
//                this.openCloseFloatView();
//                FloatViewModel.currentOnClickJob.postValue(RobotJobType.GROUP_SEND_MOMENT);
            }
        } catch (Exception ignored) {
            Toast.makeText(MainActivity.this, "error..", Toast.LENGTH_SHORT).show();
        }
    }

    public void customerFriendCircleTask(View view){
        Log.d(TAG, "执行客户朋友圈任务");
        try {
            if (jobScheduler == null) {
                return;
            }
            if(isCheckPermissionsOk(view)){
                Toast.makeText(MainActivity.this, "执行客户朋友圈任务", Toast.LENGTH_SHORT).show();
                this.openCloseFloatView();
                FloatViewModel.currentOnClickJob.postValue(RobotJobType.CUSTOMER_AGENT_SEND_MOMENT);
            }
//            WeWorkAccessibilityService.logView.setText("正在执行客户朋友圈任务");
        } catch (Exception ignored) {
            Toast.makeText(MainActivity.this, "error..", Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("SetTextI18n")
    public void sopFriendCircleTask(View view){
        Log.d(TAG, "执行sop朋友圈任务");
        try {
            if (jobScheduler == null) {
                return;
            }
            if(isCheckPermissionsOk(view)){
                Toast.makeText(MainActivity.this, "执行sop朋友圈任务", Toast.LENGTH_SHORT).show();
                this.openCloseFloatView();
                FloatViewModel.currentOnClickJob.postValue(RobotJobType.SOP_AGENT_SEND_MOMENT);
            }
//            WeWorkAccessibilityService.logView.setText("正在执行sop朋友圈任务");
        } catch (Exception ignored) {
            Toast.makeText(MainActivity.this, "error..", Toast.LENGTH_SHORT).show();
        }
    }

    public void allTask(View view){
        Log.d(TAG, "执行所有任务");
        try {
            if (jobScheduler == null) {
                return;
            }
            if(isCheckPermissionsOk(view)){
                Toast.makeText(MainActivity.this, "循环执行所有任务", Toast.LENGTH_SHORT).show();
                this.openCloseFloatView();
                FloatViewModel.currentOnClickJob.postValue(RobotJobType.ALL_TASK_MOMENT);
            }
//            WeWorkAccessibilityService.logView.setText("正在循环执行所有任务");
        } catch (Exception ignored) {
            Toast.makeText(MainActivity.this, "error..", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isAccessibilitySettingsOn(Context mContext) {
        //检测辅助功能是否开启
        int accessibilityEnabled = 0;
        final String service = getPackageName() + "/" + WeWorkAccessibilityService.class.getCanonicalName();
        try {
            accessibilityEnabled = Settings.Secure.getInt(mContext.getApplicationContext().getContentResolver(),
                    android.provider.Settings.Secure.ACCESSIBILITY_ENABLED);
            Log.v(TAG, "accessibilityEnabled = " + accessibilityEnabled);
        } catch (Settings.SettingNotFoundException e) {
            Log.e(TAG, "Error finding setting, default accessibility to not found: " + e.getMessage());
        }
        TextUtils.SimpleStringSplitter mStringColonSplitter = new TextUtils.SimpleStringSplitter(':');

        if (accessibilityEnabled == 1) {
            Log.v(TAG, "***ACCESSIBILITY IS ENABLED***");
            String settingValue = Settings.Secure.getString(mContext.getApplicationContext().getContentResolver(),
                    Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES);
            if (settingValue != null) {
                mStringColonSplitter.setString(settingValue);
                while (mStringColonSplitter.hasNext()) {
                    String accessibilityService = mStringColonSplitter.next();
                    Log.v(TAG, "-------------- > accessibilityService :: " + accessibilityService + " " + service);
                    if (accessibilityService.equalsIgnoreCase(service)) {
                        Log.v(TAG, "We've found the correct setting - accessibility is switched on!");
                        return true;
                    }
                }
            }
        } else {
            Log.v(TAG, "***ACCESSIBILITY IS DISABLED***");
        }
        return false;
    }

    public void btnAccessSettingClick(View view){
        Log.d(TAG, "跳转到辅助功能");
        try {
            if(!isAccessibilitySettingsOn(getApplicationContext())){
                startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
            }
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
            System.out.println("手机路径:"+Environment.getExternalStorageDirectory());
            fis = new FileInputStream(Environment.getExternalStorageDirectory()+"/test.jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bitmap  = BitmapFactory.decodeStream(fis);
        Color color = bitmap.getColor(623,1321);
        int pixel = bitmap.getPixel(623,1321);
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
        this.openCloseFloatView();
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
        if (jobScheduler == null) {
            return;
        }
        jobScheduler.start();
        jobScheduler.addJob(RobotJobType.SOP_AGENT_SEND_MOMENT);
    }

    private void cancelScheduleJob(){
        Log.d(TAG,"cancel schedule job");
        this.closeFloatView();
        jobScheduler.stop();
    }

    public Boolean isCheckPermissionsOk(View view){
        boolean permissions = true;
        if(!isAccessibilitySettingsOn(getApplicationContext())){
            showNoticeDialog(view);
            permissions = false;
        }
        if (!Settings.canDrawOverlays(this)) {
            showFloatWindowNotice(view);
            permissions = false;
        }
        return permissions;
    }

    /**
     * 辅助功能提示框
     */
    public void showNoticeDialog(View view) {
        noticeDialog = new noticeDialog(this);
        noticeDialog.show();
    }

    public void openAccessibility(View view){
        startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS));
    }

    public void closeNoticeDialog(View view){
        noticeDialog.dismiss();
    }

    /**
     * 悬浮窗功能提示框
     */
    public void showFloatWindowNotice(View view) {
            //没有权限，弹出提示框
        floatNoticeDialog = new floatWindowNotice(this);
        floatNoticeDialog.show();
    }

    public void openFloatWindow(View view){
        if (!Settings.canDrawOverlays(this)) {
            //没有权限，需要申请悬浮球权限
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
            intent.setData(Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, 100);
        } else {
            //已经有权限，可以直接显示悬浮窗
            Toast.makeText(MainActivity.this, "suspension-Window have accessed！", Toast.LENGTH_SHORT).show();
        }
    }

    public void closeFloatWindowNotice(View view){
        floatNoticeDialog.dismiss();
    }


    /**
     * 打开悬浮窗
     */
    public static void openCloseFloatView(){
        if(!FloatViewModel.isFloatWindowShow.getValue()){
            FloatViewModel.isFloatWindowShow.postValue(true);
        }
    }
    public static void closeFloatView(){
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

    public void loginOut(View view){
        Toast.makeText(this, "注销成功！", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,LoginActivity.class);
        startActivity(intent);
        finish();
    }

    public ComponentName getJobScheduleServiceComponent() {
        return jobScheduleServiceComponent;
    }
}