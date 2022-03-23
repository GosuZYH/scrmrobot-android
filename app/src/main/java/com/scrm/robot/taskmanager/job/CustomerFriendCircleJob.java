package com.scrm.robot.taskmanager.job;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.provider.Settings;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Toast;

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
    public static Boolean turnPageFlag = true;
    public static Boolean notificationFlag = true;
    private static int pastDay = 0;
    private static int hourLimit = 0;
    private static int minLimit = 0;

    public CustomerFriendCircleJob(){
        super();
        this.setTaskId(3);
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

        turnPageFlag = robotAccessibilityContext.getCurrentEvent().getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED;
        notificationFlag = robotAccessibilityContext.getCurrentEvent().getEventType() == AccessibilityEvent.TYPE_VIEW_SCROLLED;

        this.accessibilityGestureUtil=new AccessibilityGestureUtil(robotAccessibilityContext.getWeWorkAccessibilityService());
        AccessibilityNodeInfo rootNodeInfo = robotAccessibilityContext.getRootNodeInfo();
        if (rootNodeInfo == null) {
            return;
        }
        customerFriendCircleTask(rootNodeInfo);
    }

    public String customerFriendCircleTask(AccessibilityNodeInfo rootNodeInfo){
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
                backToMainEnd(rootNodeInfo);
                break;
            case "TASK2_END":
                //for loop all task.
                return "INIT_SOP_TASK";
        }
        return null;
    }

    private void findCustomerFriendCircle(AccessibilityNodeInfo rootNodeInfo){
        //寻找->点击工作台
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CHAT);
        List<AccessibilityNodeInfo> chatUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CHAT);
        List<AccessibilityNodeInfo> backUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.BACK);
        List<AccessibilityNodeInfo> confirmUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CONFIRM_4);
        if (chatUis.size()>0){
            if(targetUis.size() > 0){
                Log.d(TAG,"点击工作台");
                performClick(targetUis.get(0).getChild(3));
                this.setTaskStatus("CUSTOMER_FRIEND_CIRCLE");
            }
        }else if(backUis.size()>0){
            performClick(backUis.get(0));
        }else if(confirmUis.size()>0){
            performClick(confirmUis.get(0));
        }
    }

    private void _findCustomerFriendCircle(AccessibilityNodeInfo rootNodeInfo){
        //寻找->点击客户朋友圈
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByText("客户朋友圈");
        if(targetUis.size() > 0){
            try {
                Log.d(TAG,"点击客户朋友圈");
                sysSleep(500);
                performClick(targetUis.get(0).getParent().getParent());
                this.setTaskStatus("CHECK_NEW");
            }catch (Exception e){
            }
        }
    }

    private void turnToCompanyNotice(AccessibilityNodeInfo rootNodeInfo){
        //打开到企业通知页面
        List<AccessibilityNodeInfo> redPointUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.PYQ_MSG1);
        List<AccessibilityNodeInfo> threePointUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.PYQ_MSG);
        if(redPointUis.size() > 0){
            Log.d(TAG,"点击新消息红点");
            sysSleep(700);
            performClick(redPointUis.get(0));
            this.setTaskStatus("FIND_NEED_PUBLISH_PYQ");
        } else if (threePointUis.size() > 0){
            Log.d(TAG,"点击右上角选项");
            sysSleep(700);
            performClick(threePointUis.get(0));
            this.setTaskStatus("CLICK_COMPANY_NOTICE");
        }
    }

    private void _turnToCompanyNotice(AccessibilityNodeInfo rootNodeInfo){
        //点击企业通知
        if(!notificationFlag){
            return;
        }
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.COMPANY_NOTIFICATION);
        if(targetUis.size() > 0){
            Log.d(TAG,"点击企业通知");
            performClick(targetUis.get(1).getParent());
            this.setTaskStatus("FIND_NEED_PUBLISH_PYQ");
        }
    }

    private void checkNewFriendCircle(AccessibilityNodeInfo rootNodeInfo){
        //寻找需要发送的朋友圈
        if(!turnPageFlag){
            return;
        }
        Log.d(TAG,"当前在企业通知页");
        List<AccessibilityNodeInfo> targetUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.NO_CUSTOMER_PYQ);
        List<AccessibilityNodeInfo> notificationUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.SEND_PAGE);
        List<AccessibilityNodeInfo> timeUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.SEND_TIME);
        List<AccessibilityNodeInfo> sendUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.SEND_BUTTON);
        if(targetUis.size() > 0){
            Log.d(TAG,"当前没有任何企业通知");
            this.setTaskStatus("CUSTOMER_TASK_END");
        }else if(notificationUis.size() > 0){
            Log.d(TAG,"当前页面有企业通知");
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(new Date());
            calendar.add(Calendar.DAY_OF_MONTH, pastDay);  //向前推的天数
            calendar.set(Calendar.HOUR_OF_DAY, hourLimit);  //时
            calendar.set(Calendar.MINUTE, minLimit);   //分
            calendar.set(Calendar.SECOND, 0);   //秒
            Date flagTime = calendar.getTime();
            for(int i=0;i<notificationUis.get(0).getChildCount();i++){
                try {
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf=new SimpleDateFormat("MM月dd日 HH:mm");
                    Date date = sdf.parse(timeUis.get(i).getText().toString());
                    Calendar taskTime = Calendar.getInstance();
                    assert date != null;
                    taskTime.setTime(date);
                    taskTime.set(Calendar.YEAR,calendar.get(Calendar.YEAR));
                    Date executeTime = taskTime.getTime();
                    Log.d(TAG,"当前页面第"+(i+1)+"条客户朋友圈时间为"+executeTime+",状态为:"+sendUis.get(i).getText());
                    if(executeTime.after(flagTime)){
                        if(sendUis.get(i).getText().equals("发表")){
                            performClick(sendUis.get(i));
                        }
                    }else {
                        Log.d(TAG,"当前已截止到任务发送时间:"+flagTime);
                        this.setTaskStatus("CUSTOMER_TASK_END");
                        return;
                    }
                }catch (Exception e){
                    Log.d(TAG,"没有时间："+e);
                }
            }
            System.out.println("翻一整页");
            performScroll(notificationUis.get(0));
        }
        turnPageFlag = false;
    }

    public void initToMain(AccessibilityNodeInfo rootNodeInfo) {
        //返回主界面
        List<AccessibilityNodeInfo> chatUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CHAT);
        List<AccessibilityNodeInfo> backUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.BACK);
        List<AccessibilityNodeInfo> confirmUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CONFIRM_4);
        if (chatUis.size()>0){
        }else if(backUis.size()>0){
            performClick(backUis.get(0));
        }else if(confirmUis.size()>0){
            performClick(confirmUis.get(0));
        }
    }

    public void backToMainEnd(AccessibilityNodeInfo rootNodeInfo) {
        //返回主界面、任务结束
        List<AccessibilityNodeInfo> chatUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CHAT);
        List<AccessibilityNodeInfo> backUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.BACK);
        List<AccessibilityNodeInfo> confirmUis = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.CONFIRM_4);
        if (chatUis.size()>0){
            Log.d(TAG,"已返回到主界面，task2 end..");
            this.setTaskStatus("TASK2_END");
        }else if(backUis.size()>0){
            performClick(backUis.get(0));
        }else if(confirmUis.size()>0){
            performClick(confirmUis.get(0));
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

    public void performScroll(AccessibilityNodeInfo targetInfo) {
        //ui动作:向下滑动
        try {
            targetInfo.performAction(AccessibilityNodeInfo.ACTION_SCROLL_FORWARD);
        }catch (Exception e){
            System.out.println("滑动失败");
        }
    }

    private void sysSleep(int msecond) {
        //睡眠 param:seconds
        try {
            System.out.println("睡眠一秒");
            Thread.sleep(msecond);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
