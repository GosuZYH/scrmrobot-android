package com.scrm.robot;


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

import java.util.List;

public class WeWorkAccessibilityService extends AccessibilityService {

    private String TAG = "SCRMRobot";
    private final static String packageName="com.tencent.wework";

    @Override
    public void  onServiceConnected(){
        super.onServiceConnected();
    }

    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        int eventType = event.getEventType();
        String className = event.getClassName().toString();
        Log.d(TAG, event.toString());

        AccessibilityNodeInfo nodeInfo = event.getSource();
        AccessibilityNodeInfo rootInfo = getRootInActiveWindow();
        if (packageName.equals(event.getPackageName())) {

        }
    }
    @Override
    public void onInterrupt() {

    }
}
