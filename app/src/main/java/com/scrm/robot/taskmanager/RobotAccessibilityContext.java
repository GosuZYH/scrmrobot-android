package com.scrm.robot.taskmanager;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import java.lang.ref.WeakReference;

public class RobotAccessibilityContext {
    private WeakReference<AccessibilityEvent> currentEvent;
    private WeakReference<AccessibilityNodeInfo> rootNodeInfo;

    public AccessibilityEvent getCurrentEvent() {
        return currentEvent.get();
    }

    public void setCurrentEvent(AccessibilityEvent currentEvent) {
        this.currentEvent = new WeakReference<>(currentEvent);
    }

    public AccessibilityNodeInfo getRootNodeInfo() {
        return rootNodeInfo.get();
    }

    public void setRootNodeInfo(AccessibilityNodeInfo rootNodeInfo) {
        this.rootNodeInfo = new WeakReference<>(rootNodeInfo);
    }
}
