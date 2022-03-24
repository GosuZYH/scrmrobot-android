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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;

import com.scrm.robot.floatwindow.FloatViewModel;
import com.scrm.robot.floatwindow.FloatViewTouchListener;
import com.scrm.robot.taskmanager.RobotAccessibilityContext;
import com.scrm.robot.taskmanager.enums.RobotJobType;
import com.scrm.robot.taskmanager.job.BaseRobotJob;
import com.scrm.robot.utils.ApplicationUtil;

public class WeWorkAccessibilityService extends AccessibilityService implements LifecycleOwner {

    private final static String TAG = WeWorkAccessibilityService.class.getName();
    private final static String packageName = WEWORK_PACKAGE_NAME;
    private WindowManager windowManager;
    private View floatRootView;
    private Button startStopBtn;
//    public static TextView logView;

    private RobotAccessibilityContext robotAccessibilityContext;
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
        this.robotAccessibilityContext = new RobotAccessibilityContext();
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (packageName.equals(event.getPackageName())) {
            Log.d(TAG, event.toString());
            AccessibilityNodeInfo rootInfo = getRootInActiveWindow();
//            AccessibilityNodeInfo eventNode = event.getSource();
//            if (eventNode == null) {
//                performGlobalAction(GLOBAL_ACTION_RECENTS); // 打开最近页面
//            }
            robotAccessibilityContext.setCurrentEvent(event);
            robotAccessibilityContext.setRootNodeInfo(rootInfo);
            this.robotAccessibilityContext.setWeWorkAccessibilityService(this);

            RobotApplication application = (RobotApplication) ApplicationUtil.getApplication();
            application.setRobotAccessibilityContext(robotAccessibilityContext);

            //special situation solve
            AccessibilityNodeInfo rootNodeInfo = robotAccessibilityContext.getRootNodeInfo();

            BaseRobotJob job = application.getRobotJobScheduler().getRobotJobExecutor().getCurrentJob();
            if (job != null) {
                job.process();
            }
        }
    }

    @Override
    public void onInterrupt() {

    }

    private void initObserve(){
        FloatViewModel.isFloatWindowShow.observe(this, new Observer<Boolean>() {
            @RequiresApi(api = Build.VERSION_CODES.Q)
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    showWindow();
                }else {
                    if(startStopBtn!=null && startStopBtn.getWindowToken()!=null && windowManager!=null){
                        windowManager.removeView(startStopBtn);
                    }
                }
            }
        });
        FloatViewModel.jobStartStop.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                try {
                    if(!aBoolean){
                        startStopBtn.setText("启动");
                        MainActivity.jobScheduler.stop();
                    }else {
                        startStopBtn.setText("停止");
                        MainActivity.jobScheduler.start();
                        MainActivity.jobScheduler.addJob(FloatViewModel.currentOnClickJob.getValue());
                    }
                }catch (Exception e){
                }
            }
        });
    }

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return this.lifecycleRegistry;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @SuppressLint("ClickableViewAccessibility")
    private void showWindow() {
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();

        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_ACCESSIBILITY_OVERLAY;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                layoutParams.layoutInDisplayCutoutMode = WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES;
            }
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        }
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.format = PixelFormat.TRANSPARENT;
        startStopBtn = new Button(getApplicationContext());
        floatRootView = LayoutInflater.from(this).inflate(R.layout.activity_float_item, null);
        floatRootView.setOnTouchListener(new FloatViewTouchListener(layoutParams, windowManager));

        windowManager.addView(floatRootView, layoutParams);
        startStopBtn.setOnTouchListener(new FloatViewTouchListener(layoutParams, windowManager));
        startStopBtn.setText("启动");
        startStopBtn.setWidth(70);
        startStopBtn.setHeight(70);
        windowManager.addView(startStopBtn, layoutParams);

        //log window
//        logView = new TextView(getApplicationContext());
//        logView.setText("");
//        logView.setWidth(500);
//        logView.setHeight(200);
//        windowManager.addView(logView, layoutParams);
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

}
