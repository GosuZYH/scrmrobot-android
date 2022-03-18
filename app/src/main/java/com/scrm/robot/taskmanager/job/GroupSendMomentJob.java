package com.scrm.robot.taskmanager.job;

import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.scrm.robot.RobotApplication;
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
    private static boolean afterClickGroupSend=false;

    public GroupSendMomentJob(){
        super();
        this.setTaskStatus("TASK_START");
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
        AccessibilityEvent currentEvent = robotAccessibilityContext.getCurrentEvent();
        afterClickGroupSend = currentEvent.getEventType() == AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED;

        this.accessibilityGestureUtil=new AccessibilityGestureUtil(robotAccessibilityContext.getWeWorkAccessibilityService());
        AccessibilityNodeInfo rootNodeInfo = robotAccessibilityContext.getRootNodeInfo();
        if (rootNodeInfo == null) {
            return;
        }
        groupSendTask(rootNodeInfo);
    }

    public void groupSendTask(AccessibilityNodeInfo rootNodeInfo){
        switch (this.getTaskStatus()) {
            case "TASK_START":
                findGroupSendHelper(rootNodeInfo);
                break;
            case "GROUP_HELPER":
                _findGroupSendHelper(rootNodeInfo);
                break;
            case "FIND_MSG":
                checkGroupSendMsg(rootNodeInfo);
                break;
            case "TODO_SEND":
                sendMsg(rootNodeInfo);
                break;
        }
    }

    private void findGroupSendHelper(AccessibilityNodeInfo rootNodeInfo){
        //寻找->点击工作台
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CHAT);
        if(targetUis.size() > 0){
            System.out.println("点击工作台");
            performClick(targetUis.get(0).getChild(3));
            this.setTaskStatus("GROUP_HELPER");
        }
    }

    private void _findGroupSendHelper(AccessibilityNodeInfo rootNodeInfo){
        //寻找->点击群发助手
        List<AccessibilityNodeInfo> groupSendUis = rootNodeInfo.findAccessibilityNodeInfosByText("群发助手");
        if(groupSendUis.size() > 0){
            System.out.println("点击群发助手");
            performClick(groupSendUis.get(0).getParent().getParent());
            this.setTaskStatus("FIND_MSG");
        }
    }

    private void checkGroupSendMsg(AccessibilityNodeInfo rootNodeInfo){
        //检测有无待发消息
        if(afterClickGroupSend){
            List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.SEND_FLAG);
            if(targetUis.size() > 0){
                System.out.println("点击待发送入口");
                performClick(targetUis.get(0));
                this.setTaskStatus("TODO_SEND");
            }else {
                System.out.println("当前没有待发送消息");
                this.setTaskStatus("NO_MSG");
            }
        }
    }

    private void sendMsg(AccessibilityNodeInfo rootNodeInfo){
        //处理待发送消息
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.SEND_PAGE);
        if(targetUis.size() > 0){
            String time = targetUis.get(0).getChild(0).getChild(0).getChild(1).getText().toString();
            //TODO time限制
            //点击发送
            performClick(targetUis.get(0).getChild(0).getChild(2));
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
