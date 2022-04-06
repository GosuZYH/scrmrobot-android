package com.scrm.robot;


import static com.scrm.robot.Constants.WEWORK_PACKAGE_NAME;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;

import com.orhanobut.logger.Logger;
import com.scrm.robot.floatwindow.FloatViewModel;
import com.scrm.robot.floatwindow.FloatViewTouchListener;
import com.scrm.robot.floatwindow.TaskStartStopBtnClickListener;
import com.scrm.robot.taskmanager.JobStateViewModel;
import com.scrm.robot.taskmanager.RobotAccessibilityContext;
import com.scrm.robot.taskmanager.enums.RobotRunState;
import com.scrm.robot.taskmanager.job.BaseRobotJob;
import com.scrm.robot.utils.ApplicationUtil;

public class WeWorkAccessibilityService extends AccessibilityService implements LifecycleOwner {

    private final static String TAG = WeWorkAccessibilityService.class.getName();
    private final static String packageName = WEWORK_PACKAGE_NAME;
    private WindowManager windowManager;
    private View floatRootView;
    private Button startStopBtn;
//    public static TextView logView;

//    private RobotAccessibilityContext robotAccessibilityContext;
    private final LifecycleRegistry lifecycleRegistry=new LifecycleRegistry(this);


    @Override
    public void onCreate(){
        super.onCreate();
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);
        this.initObserve();
    }

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
//        this.robotAccessibilityContext = new RobotAccessibilityContext();
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 主线程，不要执行耗时操作
        if (packageName.equals(event.getPackageName())) {
            Log.d(TAG, event.toString());

            AccessibilityNodeInfo rootInfo = getRootInActiveWindow();
            RobotAccessibilityContext robotAccessibilityContext=new RobotAccessibilityContext();
            robotAccessibilityContext.setCurrentEvent(event);
            robotAccessibilityContext.setRootNodeInfo(rootInfo);

            RobotApplication application = (RobotApplication) ApplicationUtil.getApplication();
            application.setRobotAccessibilityContext(robotAccessibilityContext);
            application.setWeWorkAccessibilityService(this);
            //region 方法1 直接处理
//            BaseRobotJob job = application.getRobotJobScheduler().getRobotJobExecutor().getCurrentJob();
//            if (job != null) {
//                job.process();
//            }
            //endregion

            //region 方案2 后台服务处理，不阻塞主线程
            WeWorkAccessibilityEventService.startWithEvent(getApplicationContext(), event, rootInfo);
            //endregion
        }
    }

    @Override
    public void onInterrupt() {

    }

    private void initObserve() {
        FloatViewModel.isFloatWindowShow.observe(this, new Observer<Boolean>() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    showFloatWindow();
                } else {
                    closeFloatWindow();
                }
            }
        });
        FloatViewModel.jobStartStop.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (startStopBtn != null) {
                    String action = aBoolean ? "启动" : "停止";
                    String taskName = FloatViewModel.currentOnClickJob.getValue().name;
                    Logger.d("%s 任务 %s", action, taskName);

                    try {
                        RobotApplication application = (RobotApplication) ApplicationUtil.getApplication();
                        if (!aBoolean) {
                            startStopBtn.setText("启动");
                        } else {
                            startStopBtn.setText("停止");
                            application.getRobotJobScheduler().startAndRunJob(FloatViewModel.currentOnClickJob.getValue());
                            // 打开企微
                            openWeWork();
                        }

                    } catch (Exception e) {
                        Logger.e("%s错误：%s", action, e);
                    }
                }
            }
        });
    }

    public void openWeWork(){
        //打开企微
        Intent intent = getPackageManager().getLaunchIntentForPackage(Constants.WEWORK_PACKAGE_NAME);
        try {
            startActivity(intent);
        }catch (Exception e){

        }
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return this.lifecycleRegistry;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("ClickableViewAccessibility")
    private void showFloatWindow() {
        Logger.d("开启悬浮窗-启动中...");

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();

        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        JobStateViewModel.width.postValue(displayMetrics.widthPixels);
        JobStateViewModel.height.postValue(displayMetrics.heightPixels);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            }
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        //启停任务悬浮窗初始属性
        layoutParams.x = displayMetrics.widthPixels;
        layoutParams.y = (int)(-displayMetrics.heightPixels*0.3);
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.format = PixelFormat.TRANSPARENT;
        startStopBtn = new Button(getApplicationContext());
        floatRootView = LayoutInflater.from(this).inflate(R.layout.activity_float_item, null);
//        floatRootView.setOnTouchListener(new FloatViewTouchListener(layoutParams, windowManager));

        windowManager.addView(floatRootView, layoutParams);
//        startStopBtn.setOnTouchListener(new FloatViewTouchListener(layoutParams, windowManager));
        startStopBtn.setOnClickListener(new TaskStartStopBtnClickListener(layoutParams, windowManager));
        startStopBtn.setText("启动");
        startStopBtn.setWidth(70);
        startStopBtn.setHeight(70);
        windowManager.addView(startStopBtn, layoutParams);
        Logger.d( "开启悬浮窗-启动成功");

        //log window
//        logView = new TextView(getApplicationContext());
//        logView.setText("");
//        logView.setWidth(500);
//        logView.setHeight(200);
//        windowManager.addView(logView, layoutParams);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("ClickableViewAccessibility")
    private void closeFloatWindow(){
        if(startStopBtn!=null && startStopBtn.getWindowToken()!=null && windowManager!=null){
            Log.d(TAG, "[关闭悬浮窗]关闭中...");
            windowManager.removeView(startStopBtn);
            Log.d(TAG, "[关闭悬浮窗]关闭中成功");
        }
    }

    @Override
    public boolean onUnbind( Intent intent) {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        return super.onUnbind(intent);
    }

    @Override
     public void onDestroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);
        super.onDestroy();
    }

    private void sysSleep(int msecond) {
        //睡眠 param:seconds
        try {
            System.out.println("睡眠一秒");
            Thread.sleep(msecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
