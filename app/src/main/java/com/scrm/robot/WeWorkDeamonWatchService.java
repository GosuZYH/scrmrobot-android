package com.scrm.robot;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;

import androidx.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.scrm.robot.taskmanager.RobotAccessibilityContext;
import com.scrm.robot.taskmanager.job.BaseRobotJob;
import com.scrm.robot.utils.ApplicationUtil;
import com.scrm.robot.utils.DateUtils;

import java.lang.reflect.Constructor;
import java.util.Date;

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

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent!=null){
            RobotApplication application = (RobotApplication) ApplicationUtil.getApplication();
            while (true) {
                Logger.d("监控服务-任务无事件超时检查-开始");
                if (application.getRobotJobScheduler() != null && application.getRobotJobScheduler().getRobotJobExecutor() != null) {
                    BaseRobotJob job = application.getRobotJobScheduler().getRobotJobExecutor().getCurrentJob();
                    if (job != null && job.canProcess()) {
                        Date jobProcessTime = job.getProcessTime();
                        if (jobProcessTime != null) {
                            Date now = new Date();
                            long delay = now.getTime() - jobProcessTime.getTime();

                            Logger.d("监控服务-任务无事件超时检查：当前时间：%s 任务时间：%s 间隔时间 %s 毫秒", DateUtils.getLogDate(),DateUtils.parseLogDate(jobProcessTime),delay);

                            if (delay > Constants.JOB_TIMEOUT_MILL_SECONDS) {
                                Logger.d("任务已超时 %s 毫秒", delay);
//                                if (event != null) {
//                                    event.recycle();
//                                }
//                                event = this.obtainEvent();
//                                if (event != null) {
                                Logger.d("监控服务-触发事件 %s", event);
                                RobotAccessibilityContext robotAccessibilityContext = job.getRobotAccessibilityContext();
                                WeWorkAccessibilityEventService.startWithEvent(this, robotAccessibilityContext.getCurrentEvent(), robotAccessibilityContext.getRootNodeInfo());
//                                }
                            }
                        }
                    }
                }

                Logger.d("监控服务-任务无事件超时检查-结束");
                try {
                    Thread.sleep(10000);
                }catch (Exception ex){

                }
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
