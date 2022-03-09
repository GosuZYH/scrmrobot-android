package com.scrm.robot;


import static com.scrm.robot.Constants.WEWORK_PACKAGE_NAME;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.RequiresApi;

import com.scrm.robot.taskmanager.RobotAccessibilityContext;
import com.scrm.robot.taskmanager.job.BaseRobotJob;
import com.scrm.robot.utils.ApplicationUtil;

import java.util.List;

public class WeWorkAccessibilityService extends AccessibilityService {

    private final static String TAG = WeWorkAccessibilityService.class.getName();
    private final static String packageName = WEWORK_PACKAGE_NAME;
    private RobotAccessibilityContext robotAccessibilityContext;

    @Override
    public void onServiceConnected() {
        super.onServiceConnected();
        this.robotAccessibilityContext = new RobotAccessibilityContext();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        if (packageName.equals(event.getPackageName())) {
            Log.d(TAG, event.toString());
            AccessibilityNodeInfo rootInfo = getRootInActiveWindow();
            robotAccessibilityContext.setCurrentEvent(event);
            robotAccessibilityContext.setRootNodeInfo(rootInfo);

            RobotApplication application = (RobotApplication) ApplicationUtil.getApplication();
            application.setRobotAccessibilityContext(robotAccessibilityContext);
            BaseRobotJob job = application.getRobotJobScheduler().getRobotJobExecutor().getCurrentJob();
            if (job != null) {
                job.process();
            }
        }
    }

    @Override
    public void onInterrupt() {

    }
}
