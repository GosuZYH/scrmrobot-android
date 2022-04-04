package com.scrm.robot.taskmanager.job;

import android.os.Build;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.RequiresApi;

import com.orhanobut.logger.Logger;
import com.scrm.robot.RobotApplication;
import com.scrm.robot.taskmanager.RobotAccessibilityContext;
import com.scrm.robot.utils.AccessibilityGestureUtil;
import com.scrm.robot.utils.ApplicationUtil;

public class AllTaskJob extends BaseRobotJob {

    private final static String TAG = GroupSendMessageJob.class.getName();
    private static final SopAgentSendMomentJob task1 = new SopAgentSendMomentJob();
    private static final GroupSendMessageJob task2 = new GroupSendMessageJob();
    private static final CustomerFriendCircleJob task3 = new CustomerFriendCircleJob();
    public AccessibilityGestureUtil accessibilityGestureUtil;

    public AllTaskJob(){
        super();
        this.setTaskId(1);
        this.setTaskStatus("START_SOP_TASK");
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void run() {
//        Log.d(TAG, String.format("%s start run", this.getJobId()));
//        this.setJobState(RobotRunState.STARTED);
//        this.setStartTime(new Date());
//        this.process();
        super.run();
    }

    @Override
    public void stop() {
        Log.d(TAG, String.format("%s stop", this.getJobId()));
        super.stop();
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public void process() {
        super.process();
        if(!this.canProcess()){
            Logger.d( "全部任务 %s 处理，任务不可运行 状态: %s", this.getJobId(),this.getJobState());
            return;
        }
        Logger.d( "全部任务-任务处理中 %s", this.getJobId());

        RobotApplication application = (RobotApplication) ApplicationUtil.getApplication();
//        RobotAccessibilityContext robotAccessibilityContext = application.getRobotAccessibilityContext();
        RobotAccessibilityContext robotAccessibilityContext = this.getRobotAccessibilityContext();

        accessibilityGestureUtil=new AccessibilityGestureUtil(application.getWeWorkAccessibilityService());

//        accessibilityGestureUtil=new AccessibilityGestureUtil(robotAccessibilityContext.getWeWorkAccessibilityService());
        
        AccessibilityNodeInfo rootNodeInfo = robotAccessibilityContext.getRootNodeInfo();
        if (rootNodeInfo == null) {
            return;
        }
        SopAgentSendMomentJob.tagFindFlag = robotAccessibilityContext.getCurrentEvent().getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED;
        SopAgentSendMomentJob.selectAllCustomerFlag = robotAccessibilityContext.getCurrentEvent().getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED;
        if(GroupSendMessageJob.findMsg){
            if(robotAccessibilityContext.getCurrentEvent().getEventType() == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED){
                GroupSendMessageJob.afterClickGroupSend ++;
            }
        }
        CustomerFriendCircleJob.turnPageFlag = robotAccessibilityContext.getCurrentEvent().getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED;
        CustomerFriendCircleJob.notificationFlag = robotAccessibilityContext.getCurrentEvent().getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED;

        executeAllTask(rootNodeInfo);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    private void executeAllTask(AccessibilityNodeInfo rootNodeInfo){
        switch (this.getTaskId()) {
            case 1:
                task1.accessibilityGestureUtil = accessibilityGestureUtil;
                String res1 = task1.SopFriendCircle(rootNodeInfo);
                if("START_GROUP_TASK".equals(res1)){
                    this.setTaskId(2);
                    task2.setTaskStatus("START_GROUP_TASK");
                    this.setTaskStatus("START_GROUP_TASK");
                }
                break;
            case 2:
                task2.accessibilityGestureUtil = accessibilityGestureUtil;
                String res2 = task2.groupSendTask(rootNodeInfo);
                if("START_CUSTOMER_TASK".equals(res2)){
                    this.setTaskId(3);
                    task3.setTaskStatus("START_CUSTOMER_TASK");
                    this.setTaskStatus("START_CUSTOMER_TASK");
                }
                break;
            case 3:
                task3.accessibilityGestureUtil = accessibilityGestureUtil;
                String res3 = task3.customerFriendCircleTask(rootNodeInfo);
                if("INIT_SOP_TASK".equals(res3)){
                    this.setTaskId(1);
                    task1.setTaskStatus("INIT_SOP_TASK");
                    this.setTaskStatus("INIT_SOP_TASK");
                }
                break;
        }
    }
}
