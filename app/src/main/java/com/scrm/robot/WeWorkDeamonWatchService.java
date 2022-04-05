package com.scrm.robot;

import android.accessibilityservice.AccessibilityService;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.orhanobut.logger.Logger;
import com.scrm.robot.taskmanager.RobotAccessibilityContext;
import com.scrm.robot.taskmanager.enums.RobotRunState;
import com.scrm.robot.taskmanager.job.BaseRobotJob;
import com.scrm.robot.taskmanager.job.ResourceId;
import com.scrm.robot.utils.ApplicationUtil;
import com.scrm.robot.utils.DateUtils;

import java.lang.reflect.Constructor;
import java.util.Date;
import java.util.List;

public class WeWorkDeamonWatchService extends IntentService {
    /**
     * @deprecated
     */
    public WeWorkDeamonWatchService() {
        super("WeWorkDeamonWatchService");
    }
    private AccessibilityEvent event;

    public static void startWatch(Context context){
        Logger.d("监控服务-启动");
        Intent intent=new Intent(context, WeWorkDeamonWatchService.class);
        context.startService(intent);
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            RobotApplication application = (RobotApplication) ApplicationUtil.getApplication();
            while (true) {
                Logger.d("监控服务-任务无事件超时检查-开始");
                if (application.getRobotJobScheduler() != null && application.getRobotJobScheduler().getRobotJobExecutor() != null) {
                    Date jobProcessTime = application.getLastEventTime();
                    if (jobProcessTime != null) {
                        Date now = new Date();
                        long delay = now.getTime() - jobProcessTime.getTime();

                        Logger.d("监控服务-任务无事件超时检查：当前时间：%s 任务时间：%s 间隔时间 %s 毫秒", DateUtils.getLogDate(), DateUtils.parseLogDate(jobProcessTime), delay);

                        if (delay > Constants.JOB_TIMEOUT_MILL_SECONDS) {
                            Logger.d("任务已超时 %s 毫秒", delay);
//                                if (event != null) {
//                                    event.recycle();
//                                }
//                                event = this.obtainEvent();
//                                if (event != null) {
                            Logger.d("监控服务-触发事件 %s", event);
                            BaseRobotJob job = application.getCurrentJob();
                            if (job != null ) {
                                if( job.canProcess()) {
                                    job.pause();
                                }
                                // TODO NOW 回到主页
                                if(job.getJobState()!= RobotRunState.STOPPED) {
                                    backToMain();
                                }
                            }
                            job = application.getCurrentJob();
                            if (job != null) {
                                job.reRun();
                            }
//                                RobotAccessibilityContext robotAccessibilityContext = job.getRobotAccessibilityContext();
//                                WeWorkAccessibilityEventService.startWithEvent(this, robotAccessibilityContext.getCurrentEvent(), robotAccessibilityContext.getRootNodeInfo());
//                                }
                        }
                    }
                }

                Logger.d("监控服务-任务无事件超时检查-结束");
                try {
                    Thread.sleep(10000);
                } catch (Exception ex) {

                }
            }
        }
    }

    /**
     * 回到主界面, 且点击到第一个 消息 页面
     */
    private void  backToMain(){
        RobotApplication application = (RobotApplication) ApplicationUtil.getApplication();

        RobotAccessibilityContext robotAccessibilityContext=application.getRobotAccessibilityContext();
        WeWorkAccessibilityService weWorkAccessibilityService= application.getWeWorkAccessibilityService();
        if(robotAccessibilityContext!=null){
            weWorkAccessibilityService.openWeWork();
            while (!ResourceId.WEWORK_MAIN_UI_CLASS_NAME.equals(application.getCurrentWeWorkActivityClassName())){
                // 返回上个页面
                weWorkAccessibilityService.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                weWorkAccessibilityService= application.getWeWorkAccessibilityService();
                weWorkAccessibilityService.openWeWork();
            }
            Logger.d("监控服务-已经处于主页面，准备切换到第一个功能");
            // 点击消息按钮
            AccessibilityNodeInfo rootNodeInfo = weWorkAccessibilityService.getRootInActiveWindow();
            if(rootNodeInfo!=null) {
                // 底部导航按钮
                List<AccessibilityNodeInfo> barNodeParents = rootNodeInfo.findAccessibilityNodeInfosByViewId(ResourceId.BOTTOM_NAVIGATE_BAR);
                if (barNodeParents.size() > 0 ) {
                    AccessibilityNodeInfo barNodeParent = barNodeParents.get(0);
                    if(barNodeParent.getChildCount()==5) {
                        Logger.d("监控服务-切换到导航栏第一个功能");
                        // 点击第5个
                        barNodeParent.getChild(4).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        // 再点击第一个
                        barNodeParent.getChild(0).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    }else {
                        Logger.d("监控服务-无法找到导航栏");
                    }
                }
            }else {
                Logger.d("监控服务-无法找到任何节点");
            }
        }

    }

    public  AccessibilityEvent obtainEvent() {
        RobotApplication application = (RobotApplication) ApplicationUtil.getApplication();
        BaseRobotJob job = application.getRobotJobScheduler().getRobotJobExecutor().getCurrentJob();
        try {
            if (job != null) {
                RobotAccessibilityContext robotAccessibilityContext = job.getRobotAccessibilityContext();
                if (robotAccessibilityContext != null) {
                    String className = robotAccessibilityContext.getWeWorkActivityClassName();
                    if (className != null) {
                        Constructor<AccessibilityEvent> constructor = AccessibilityEvent.class.getDeclaredConstructor();
                        constructor.setAccessible(true);
                        AccessibilityEvent accessibilityEvent = constructor.newInstance();
                        accessibilityEvent.setEventType(AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED);
                        accessibilityEvent.setClassName(className);
                        return accessibilityEvent;
                    }
                }
            }
        } catch (Exception ex) {
            Logger.e("监控服务-获取事件错误：%s", ex);
        }
        return null;
    }
}
