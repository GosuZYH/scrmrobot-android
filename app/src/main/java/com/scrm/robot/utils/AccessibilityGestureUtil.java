package com.scrm.robot.utils;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.graphics.Point;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

/**
 * 手势工具类
 */
public class AccessibilityGestureUtil {
    private final static String TAG = "手势工具类";
    private AccessibilityService accessibilityService;

    public AccessibilityGestureUtil(AccessibilityService accessibilityService) {
        this.accessibilityService = accessibilityService;
    }

    /**
     * 手势点击
     *
     * @param x
     * @param y
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void click(int x, int y) {
        Point position = new Point(x, y);

        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path p = new Path();
        p.moveTo(position.x, position.y);
        builder.addStroke(new GestureDescription.StrokeDescription(p, 0, 100L));
        GestureDescription gesture = builder.build();
        boolean isDispatched = this.accessibilityService.dispatchGesture(gesture, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.d(TAG, "onCompleted: 完成..........");
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.d(TAG, "onCompleted: 取消..........");
            }
        }, null);
    }


    /**
     * 手势点击，具有回调
     *
     * @param x
     * @param y
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void click(int x, int y, AccessibilityService.GestureResultCallback callback) {
        Point position = new Point(x, y);

        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path p = new Path();
        p.moveTo(position.x, position.y);
        builder.addStroke(new GestureDescription.StrokeDescription(p, 0, 100L));
        GestureDescription gesture = builder.build();
        boolean isDispatched = this.accessibilityService.dispatchGesture(gesture, callback, null);
    }

    /**
     * 手势滑动
     *
     * @param x
     * @param y
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void swip(int x, int y, int _x, int _y) {
        Point position = new Point(x, y);
        Point _position = new Point(_x, _y);

        GestureDescription.Builder builder = new GestureDescription.Builder();
        Path p = new Path();
        p.moveTo(position.x, position.y);
        p.lineTo(_position.x, _position.y);
        builder.addStroke(new GestureDescription.StrokeDescription(p, 0, 200L));
        GestureDescription gesture = builder.build();
        boolean isDispatched = this.accessibilityService.dispatchGesture(gesture, new AccessibilityService.GestureResultCallback() {
            @Override
            public void onCompleted(GestureDescription gestureDescription) {
                super.onCompleted(gestureDescription);
                Log.d(TAG, "onCompleted: 完成..........");
            }

            @Override
            public void onCancelled(GestureDescription gestureDescription) {
                super.onCancelled(gestureDescription);
                Log.d(TAG, "onCompleted: 取消..........");
            }
        }, null);
    }
}
