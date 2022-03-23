package com.scrm.robot.floatwindow;

import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

public class FloatViewTouchListener implements View.OnTouchListener {
    private int x;
    private int y;
    private final WindowManager.LayoutParams layoutParams;
    private final WindowManager windowManager;

    public FloatViewTouchListener(WindowManager.LayoutParams layoutParams, WindowManager windowManager){
        this.layoutParams=layoutParams;
        this.windowManager=windowManager;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getRawX();
                y = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                if(!FloatViewModel.jobStartStop.getValue()){
                    FloatViewModel.jobStartStop.postValue(true);
                }else {
                    FloatViewModel.jobStartStop.postValue(false);
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int nowX = (int) event.getRawX();
                int nowY = (int) event.getRawY();
                int movedX = nowX - x;
                int movedY = nowY - y;
                x = nowX;
                y = nowY;
                layoutParams.x = layoutParams.x + movedX;
                layoutParams.y = layoutParams.y + movedY;
                windowManager.updateViewLayout(view, layoutParams);
                break;
            default:
                break;
        }
        return false;
    }
}