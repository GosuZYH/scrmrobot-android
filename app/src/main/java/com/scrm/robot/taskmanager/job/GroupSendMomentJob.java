package com.scrm.robot.taskmanager.job;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.scrm.robot.RobotApplication;
import com.scrm.robot.taskmanager.JobStateViewModel;
import com.scrm.robot.taskmanager.RobotAccessibilityContext;
import com.scrm.robot.taskmanager.enums.RobotRunState;
import com.scrm.robot.utils.AccessibilityGestureUtil;
import com.scrm.robot.utils.ApplicationUtil;

import java.util.Date;
import java.util.List;

public class GroupSendMomentJob  extends BaseRobotJob {
    private final static String TAG = GroupSendMomentJob.class.getName();
    public AccessibilityGestureUtil accessibilityGestureUtil;
    private final static String packageName="com.tencent.wework";

    public GroupSendMomentJob(){
        super();
        this.setTaskStatus("TASK_START");
        RobotApplication robotApplication = (RobotApplication) ApplicationUtil.getApplication();
    }

    @Override
    public void run() {
        Log.d(TAG, String.format("%s start run", this.getJobId()));
        this.setJobState(RobotRunState.STARTED);
        this.setStartTime(new Date());
    }

    @Override
    public void stop() {
        Log.d(TAG, String.format("%s stop", this.getJobId()));
        super.stop();
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

        this.accessibilityGestureUtil=new AccessibilityGestureUtil(robotAccessibilityContext.getWeWorkAccessibilityService());
        AccessibilityNodeInfo rootNodeInfo = robotAccessibilityContext.getRootNodeInfo();
        if (rootNodeInfo == null) {
            return;
        }
        // TODO 群发任务主流程
        groupSendTask(rootNodeInfo);
    }

    public void groupSendTask(AccessibilityNodeInfo rootNodeInfo){
        switch (this.getTaskStatus()) {
            case "TASK_START":
                findGroupSendHelper(rootNodeInfo);
                break;
        }
    }

    private void findGroupSendHelper(AccessibilityNodeInfo rootNodeInfo){
        //寻找->尝试点击搜索
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.SEARCH);
//        System.out.println("找到'搜索'ui数量"+targetUis.size());
        if(targetUis.size() > 0){
            System.out.println("点击搜索");
            performClick(targetUis.get(0));
            if(!JobStateViewModel.isScreenShot.getValue()){
                JobStateViewModel.isScreenShot.postValue(true); }
        }
    }

    private void performClick(AccessibilityNodeInfo targetInfo) {
        //ui动作:点击
        try {
            targetInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }catch (Exception e){
            System.out.println("点击失败");
        }
    }
}
