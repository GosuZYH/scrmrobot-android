package com.scrm.robot;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.scrm.robot.taskmanager.RobotAccessibilityContext;
import com.scrm.robot.taskmanager.job.BaseRobotJob;
import com.scrm.robot.utils.ApplicationUtil;

public class WeWorkAccessibilityEventService extends IntentService {
    /**
     * @deprecated
     */
    public WeWorkAccessibilityEventService() {
        super("WeWorkAccessibilityEventService");
    }

    public static void startWithEvent(Context context,
                                      AccessibilityEvent event,
                                      AccessibilityNodeInfo rootInfo){
        Intent intent=new Intent(context, WeWorkAccessibilityEventService.class);
        intent.putExtra("event", event);
        intent.putExtra("rootInfo",rootInfo);
        context.startService(intent);
    }



    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            RobotAccessibilityContext robotAccessibilityContext = new RobotAccessibilityContext();

            AccessibilityEvent event = intent.getParcelableExtra("event");
            AccessibilityNodeInfo rootInfo = intent.getParcelableExtra("rootInfo");
            robotAccessibilityContext.setCurrentEvent(event);
            robotAccessibilityContext.setRootNodeInfo(rootInfo);
            RobotApplication application = (RobotApplication) ApplicationUtil.getApplication();
//            application.setRobotAccessibilityContext(robotAccessibilityContext);
            BaseRobotJob job = application.getRobotJobScheduler().getRobotJobExecutor().getCurrentJob();
            if (job != null) {
                job.setRobotAccessibilityContext(robotAccessibilityContext);
                job.process();
            }
        } catch (Exception ex) {
            Logger.e("执行事件错误: %s %s", ex, ex.getStackTrace());
        }
    }
}
