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

public class GroupSendMomentJob  extends BaseRobotJob {
    private final static String TAG = GroupSendMomentJob.class.getName();
    public AccessibilityGestureUtil accessibilityGestureUtil;
    private final static String packageName="com.tencent.wework";
    private static boolean afterClickGroupSend=false;
    private static int pastGroupSendDay;
    private static int groupSendHourLimit;
    private static int groupSendMinLimit;

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
            case "NO_MSG":
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
            try{
                String time = targetUis.get(0).getChild(0).getChild(0).getChild(1).getText().toString();
                //create flag time
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(new Date());
                calendar.add(Calendar.DAY_OF_MONTH, pastGroupSendDay);  //向前推的天数
                calendar.set(Calendar.HOUR_OF_DAY, groupSendHourLimit);  //时
                calendar.set(Calendar.MINUTE, groupSendMinLimit);   //分
                Date flagTime = calendar.getTime();
                //create task time
                Calendar taskTime = Calendar.getInstance();
                taskTime.setTime(new Date());
                if (time.contains(":")){
                    List<String> list = Arrays.asList(time.split(":"));
                    taskTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(list.get(0)));  //时
                    taskTime.set(Calendar.MINUTE, Integer.parseInt(list.get(1)));   //分
                }else if (time.contains("月")){
                    List<String> list1 = Arrays.asList(time.split("月"));
                    taskTime.set(Calendar.MONTH, Integer.parseInt(list1.get(0))-1);  //月
                    taskTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(list1.get(1).substring(0,list1.get(1).indexOf("日"))));  //天
                }else if (time.contains("昨天")){
                    taskTime.add(Calendar.DAY_OF_MONTH,-1);  //天-1d
                }else if (time.contains("上午") || time.contains("下午") || time.contains("刚刚") || time.contains("分钟前")){
                    taskTime.add(Calendar.HOUR_OF_DAY, -3);  //时-3h
                }
                Date executeTime = taskTime.getTime();
                Log.d(TAG,"截至发送时间线："+flagTime+",当前最新任务时间："+executeTime);
                if(executeTime.after(flagTime)){
                    performClick(targetUis.get(0).getChild(0).getChild(2));
                }else{
                    Log.d(TAG,"当前已无满足时间的群发任务");
                    this.setTaskStatus("NO_MSG");
                }
            }catch (Exception e){
                Log.d(TAG,"出现小错误："+e);
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
