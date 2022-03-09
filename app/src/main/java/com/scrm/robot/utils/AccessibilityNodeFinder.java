package com.scrm.robot.utils;

import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.scrm.robot.exception.AccessibilityNodeNotFoundException;

import java.util.Date;
import java.util.List;

public class AccessibilityNodeFinder {
    private final static String TAG = AccessibilityNodeFinder.class.getName();

    /**
     * 默认查找超时 2s
     */
    private final static int defaultFindNodeTimeOut = 2000;
    private final static int loopThreadSleepTime = 100;

    public static AccessibilityNodeInfo findNodeByViewId(AccessibilityNodeInfo source,
                                                         String id) {
        List<AccessibilityNodeInfo> accessibilityNodeInfoList = source.findAccessibilityNodeInfosByViewId(id);
        if (accessibilityNodeInfoList.size() > 0) {
            return accessibilityNodeInfoList.get(0);
        }
        Log.w(TAG, String.format("view id %s not found", id));
        return null;
    }

    public static AccessibilityNodeInfo findNodeByViewId(AccessibilityNodeInfo source,
                                                         String id,
                                                         int timeout) {
        long start = new Date().getTime();

        while ((new Date().getTime() - start) < timeout) {
            Log.d(TAG, DateUtil.getLogDate()+" finding node..");

            AccessibilityNodeInfo nodeInfo = findNodeByViewId(source, id);
            if (nodeInfo != null) {
                return nodeInfo;
            }
            try {
                Thread.sleep(4000);
            } catch (Exception ignored) {
            }
        }
        Log.w(TAG, String.format("view id %s not found", id));
        return null;
    }
}
