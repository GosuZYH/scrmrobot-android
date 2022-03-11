package com.scrm.robot.taskmanager.job;

import android.os.Bundle;
import android.util.Log;
import android.view.accessibility.AccessibilityNodeInfo;

import com.scrm.robot.RobotApplication;
import com.scrm.robot.taskmanager.JobStateViewModel;
import com.scrm.robot.taskmanager.RobotAccessibilityContext;
import com.scrm.robot.taskmanager.enums.RobotRunState;
import com.scrm.robot.utils.AccessibilityNodeFinder;
import com.scrm.robot.utils.ApplicationUtil;

import java.util.Date;

public class SopAgentSendMomentJob extends BaseRobotJob {
    private final static String TAG = SopAgentSendMomentJob.class.getName();

    public SopAgentSendMomentJob(){
        super();
    }

    @Override
    public void run() {
        Log.d(TAG, String.format("%s start run", this.getJobId()));
        this.setJobState(RobotRunState.STARTED);
        this.setStartTime(new Date());
    }

    @Override
    public void process() {
        if(this.getJobState()==RobotRunState.STOPPED){
            Log.d(TAG, String.format("%s processing is [stopped]", this.getJobId()));
            return;
        }
        Log.d(TAG, String.format("%s processing", this.getJobId()));
        RobotApplication application = (RobotApplication) ApplicationUtil.getApplication();
        RobotAccessibilityContext robotAccessibilityContext = application.getRobotAccessibilityContext();

        if (robotAccessibilityContext == null) {
            return;
        }
        AccessibilityNodeInfo rootNodeInfo = robotAccessibilityContext.getRootNodeInfo();
        if (rootNodeInfo == null) {
            return;
        }


        // 搜索按钮
        AccessibilityNodeInfo searchBtn = AccessibilityNodeFinder.findNodeByViewId(rootNodeInfo, "com.tencent.wework:id/kci");
        if (searchBtn != null) {
//            return;
            searchBtn.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }

        // 搜索输入框
        AccessibilityNodeInfo searchInput = AccessibilityNodeFinder.findNodeByViewId(rootNodeInfo, "com.tencent.wework:id/iqz");
        if (searchInput != null) {
            searchInput.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            Bundle arguments = new Bundle();
            // TODO @zyh low app名称配置
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, "SMR-代开发-test");
            searchInput.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        }

        // app点击
        AccessibilityNodeInfo appViewGroup = AccessibilityNodeFinder.findNodeByViewId(rootNodeInfo, "com.tencent.wework:id/fyu");
        if (appViewGroup != null) {
            AccessibilityNodeInfo parent = appViewGroup.getParent();
            parent.performAction(AccessibilityNodeInfo.ACTION_CLICK);
            // 截图
            if(JobStateViewModel.isScreenShot.getValue().equals(false)) {
                JobStateViewModel.isScreenShot.postValue(true);
            }
            this.stop();
        }
    }


    @Override
    public void stop() {
        Log.d(TAG, String.format("%s stop", this.getJobId()));
        super.stop();
    }
}
