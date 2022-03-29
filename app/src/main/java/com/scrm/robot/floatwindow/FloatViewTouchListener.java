package com.scrm.robot.floatwindow;

import android.content.Intent;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;

import com.scrm.robot.Constants;
import com.scrm.robot.MainActivity;

public class FloatViewTouchListener implements View.OnTouchListener {
    private int x;
    private int y;
    private final WindowManager.LayoutParams layoutParams;
    private final WindowManager windowManager;
    public static boolean clickDownFlag = false;
    public static boolean clickUpFlag = false;

    public FloatViewTouchListener(WindowManager.LayoutParams layoutParams, WindowManager windowManager){
        this.layoutParams=layoutParams;
        this.windowManager=windowManager;
    }

    @Override
    public boolean onTouch(View view, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                clickDownFlag = true;
                x = (int) event.getRawX();
                y = (int) event.getRawY();
                break;
            case MotionEvent.ACTION_UP:
                if(clickDownFlag){
                    clickUpFlag = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                int nowX = (int) event.getRawX();
                int nowY = (int) event.getRawY();
                int movedX = nowX - x;
                int movedY = nowY - y;
                x = nowX;
                y = nowY;
                if(movedX>2 || movedX<-2 || movedY>2 || movedY<-2){
                    clickDownFlag = false;
                    clickUpFlag = false;
                }
                layoutParams.x = layoutParams.x + movedX;
                layoutParams.y = layoutParams.y + movedY;
                windowManager.updateViewLayout(view, layoutParams);
                break;
            default:
                break;
        }
        if(clickDownFlag&&clickUpFlag){
            clickDownFlag = false;
            clickUpFlag = false;
            if(!FloatViewModel.jobStartStop.getValue()){
                FloatViewModel.jobStartStop.postValue(true);
            }else {
                FloatViewModel.jobStartStop.postValue(false);
            }

        }
        return false;
    }
}