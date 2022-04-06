package com.scrm.robot.taskmanager;

import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.orhanobut.logger.Logger;
import com.scrm.robot.WeWorkAccessibilityService;

import java.lang.ref.WeakReference;

public class RobotAccessibilityContext {
//    private  WeakReference<WeWorkAccessibilityService> weWorkAccessibilityService;
//    private WeakReference<AccessibilityEvent> currentEvent;
//    private WeakReference<AccessibilityNodeInfo> rootNodeInfo;


    private  AccessibilityEvent currentEvent;
    private  AccessibilityNodeInfo rootNodeInfo;

    private String weWorkActivityClassName;

    public AccessibilityEvent getCurrentEvent() {
        return currentEvent;
    }

    public void setCurrentEvent(AccessibilityEvent currentEvent) {
        this.currentEvent =  currentEvent;
        if(currentEvent.getClassName()!=null) {
            String className = currentEvent.getClassName().toString();
            if (className.endsWith("Activity")) {
                this.setWeWorkActivityClassName(className);
            }
        }
    }

    public AccessibilityNodeInfo getRootNodeInfo() {
        return rootNodeInfo;
    }

    public void setRootNodeInfo(AccessibilityNodeInfo rootNodeInfo) {
        this.rootNodeInfo =  rootNodeInfo;
    }

    public String getWeWorkActivityClassName() {
        return weWorkActivityClassName;
    }

    public void setWeWorkActivityClassName(String weWorkActivityClassName) {
        this.weWorkActivityClassName = weWorkActivityClassName;
    }
//
//    public  WeWorkAccessibilityService getWeWorkAccessibilityService() {
//        return weWorkAccessibilityService.get();
//    }
//
//    public void setWeWorkAccessibilityService(WeWorkAccessibilityService weWorkAccessibilityService) {
//        this.weWorkAccessibilityService =new WeakReference<WeWorkAccessibilityService>(weWorkAccessibilityService);
//    }
}
