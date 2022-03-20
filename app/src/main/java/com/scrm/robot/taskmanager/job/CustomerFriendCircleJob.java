package com.scrm.robot.taskmanager.job;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.scrm.robot.RobotApplication;
import com.scrm.robot.taskmanager.RobotAccessibilityContext;
import com.scrm.robot.taskmanager.enums.RobotRunState;
import com.scrm.robot.utils.AccessibilityGestureUtil;
import com.scrm.robot.utils.ApplicationUtil;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomerFriendCircleJob extends BaseRobotJob {
    private final static String TAG = GroupSendMomentJob.class.getName();
    public AccessibilityGestureUtil accessibilityGestureUtil;

    public CustomerFriendCircleJob(){
        super();
        this.setTaskStatus("START_CUSTOMER_TASK");
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

        this.accessibilityGestureUtil=new AccessibilityGestureUtil(robotAccessibilityContext.getWeWorkAccessibilityService());
        AccessibilityNodeInfo rootNodeInfo = robotAccessibilityContext.getRootNodeInfo();
        if (rootNodeInfo == null) {
            return;
        }
        customerFriendCircleTask(rootNodeInfo);
    }

    public void customerFriendCircleTask(AccessibilityNodeInfo rootNodeInfo){
        switch (this.getTaskStatus()) {
            case "START_CUSTOMER_TASK":
                findCustomerFriendCircle(rootNodeInfo);
                break;
            case "CUSTOMER_FRIEND_CIRCLE":
                _findCustomerFriendCircle(rootNodeInfo);
                break;
            case "CHECK_NEW":
                turnToCompanyNotice(rootNodeInfo);
                break;
            case "CLICK_COMPANY_NOTICE":
                _turnToCompanyNotice(rootNodeInfo);
                break;
            case "FIND_NEED_PUBLISH_PYQ":
                checkNewFriendCircle(rootNodeInfo);
                break;
            case "CUSTOMER_TASK_END":
                backToMain(rootNodeInfo);
                break;
            case "TASK2_END":
                break;
        }
    }

    private void findCustomerFriendCircle(AccessibilityNodeInfo rootNodeInfo){
        //寻找->点击工作台
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CHAT);
        if(targetUis.size() > 0){
            Log.d(TAG,"点击工作台");
            performClick(targetUis.get(0).getChild(3));
            this.setTaskStatus("CUSTOMER_FRIEND_CIRCLE");
        }
    }

    private void _findCustomerFriendCircle(AccessibilityNodeInfo rootNodeInfo){
        //寻找->点击客户朋友圈
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByText("客户朋友圈");
        if(targetUis.size() > 0){
            Log.d(TAG,"点击客户朋友圈");
            performClick(targetUis.get(0).getParent().getParent());
            this.setTaskStatus("CHECK_NEW");
        }
    }

    private void turnToCompanyNotice(AccessibilityNodeInfo rootNodeInfo){
        //打开到企业通知页面
        List<AccessibilityNodeInfo> redPointUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.PYQ_MSG1);
        List<AccessibilityNodeInfo> threePointUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.PYQ_MSG);
        if(redPointUis.size() > 0){
            Log.d(TAG,"点击新消息红点");
            performClick(redPointUis.get(0));
            this.setTaskStatus("FIND_NEED_PUBLISH_PYQ");
        } else if (threePointUis.size() > 0){
            Log.d(TAG,"点击右上角选项");
            performClick(threePointUis.get(0));
            this.setTaskStatus("CLICK_COMPANY_NOTICE");
        }
    }

    private void _turnToCompanyNotice(AccessibilityNodeInfo rootNodeInfo){
        //点击企业通知
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.COMPANY_NOTIFICATION);
        if(targetUis.size() > 0){
            Log.d(TAG,"点击企业通知");
            performClick(targetUis.get(0).getParent());
            this.setTaskStatus("FIND_NEED_PUBLISH_PYQ");
        }
    }

    private void checkNewFriendCircle(AccessibilityNodeInfo rootNodeInfo){
        //寻找需要发送的朋友圈
        Log.d(TAG,"当前在企业通知页");
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.NO_CUSTOMER_PYQ);
        if(targetUis.size() > 0){
            Log.d(TAG,"当前没有任何企业通知");
            this.setTaskStatus("CUSTOMER_TASK_END");
        }
    }

    public void backToMain(AccessibilityNodeInfo rootNodeInfo) {
        //返回主界面
        List<AccessibilityNodeInfo> userUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.USER);
        List<AccessibilityNodeInfo> chatUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CHAT);
        List<AccessibilityNodeInfo> backUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.BACK);
        List<AccessibilityNodeInfo> confirmUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CONFIRM_4);
        if (userUis.size()>0 && chatUis.size()>0){
            Log.d(TAG,"已返回到主界面，task2 end..");
            this.setTaskStatus("TASK2_END");
        }else {
            if(backUis.size()>0){
                performClick(backUis.get(0));
            }
            if(confirmUis.size()>0){
                performClick(confirmUis.get(0));
            }
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
