package com.scrm.robot.taskmanager.job;

import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.RequiresApi;

import com.scrm.robot.RobotApplication;
import com.scrm.robot.taskmanager.RobotAccessibilityContext;
import com.scrm.robot.taskmanager.enums.RobotRunState;
import com.scrm.robot.utils.AccessibilityGestureUtil;
import com.scrm.robot.utils.ApplicationUtil;

import java.util.Date;

public class AllTaskJob extends BaseRobotJob {

    private final static String TAG = GroupSendMomentJob.class.getName();
    private final SopAgentSendMomentJob task1 = new SopAgentSendMomentJob();
    private final GroupSendMomentJob task2 = new GroupSendMomentJob();
    private final CustomerFriendCircleJob task3 = new CustomerFriendCircleJob();
    public AccessibilityGestureUtil accessibilityGestureUtil;

    public AllTaskJob(){
        super();
        this.setTaskId(1);
        this.setTaskStatus("START_SOP_TASK");
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

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void process() {
        if(this.getJobState()==RobotRunState.STOPPED){
            Log.d(TAG, String.format("%s processing is [stopped]", this.getJobId()));
            return;
        }
        Log.d(TAG, String.format("%s processing", this.getJobId()));
        RobotApplication application = (RobotApplication) ApplicationUtil.getApplication();
        RobotAccessibilityContext robotAccessibilityContext = application.getRobotAccessibilityContext();

        accessibilityGestureUtil=new AccessibilityGestureUtil(robotAccessibilityContext.getWeWorkAccessibilityService());

        SopAgentSendMomentJob.tagFindFlag = robotAccessibilityContext.getCurrentEvent().getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED;
        SopAgentSendMomentJob.selectAllCustomerFlag = robotAccessibilityContext.getCurrentEvent().getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        GroupSendMomentJob.afterClickGroupSend = robotAccessibilityContext.getCurrentEvent().getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;
        CustomerFriendCircleJob.turnPageFlag = robotAccessibilityContext.getCurrentEvent().getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED;
        CustomerFriendCircleJob.notificationFlag = robotAccessibilityContext.getCurrentEvent().getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED;

        AccessibilityNodeInfo rootNodeInfo = robotAccessibilityContext.getRootNodeInfo();
        if (rootNodeInfo == null) {
            return;
        }
        executeAllTask(rootNodeInfo);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void executeAllTask(AccessibilityNodeInfo rootNodeInfo){
        switch (this.getTaskId()) {
            case 1:
                String res1 = task1.SopFriendCircle(rootNodeInfo);
                if("START_GROUP_TASK".equals(res1)){
                    this.setTaskId(2);
                    task2.accessibilityGestureUtil = accessibilityGestureUtil;
                    task2.setTaskStatus("START_GROUP_TASK");
                    this.setTaskStatus("START_GROUP_TASK");
                }
                break;
            case 2:
                String res2 = task2.groupSendTask(rootNodeInfo);
                if("START_CUSTOMER_TASK".equals(res2)){
                    this.setTaskId(3);
                    task3.accessibilityGestureUtil = accessibilityGestureUtil;
                    task3.setTaskStatus("START_CUSTOMER_TASK");
                    this.setTaskStatus("START_CUSTOMER_TASK");
                }
                break;
            case 3:
                String res3 = task3.customerFriendCircleTask(rootNodeInfo);
                if("INIT_SOP_TASK".equals(res3)){
                    this.setTaskId(1);
                    task1.accessibilityGestureUtil = accessibilityGestureUtil;
                    task1.setTaskStatus("INIT_SOP_TASK");
                    this.setTaskStatus("INIT_SOP_TASK");
                }
                break;
        }
    }
}
