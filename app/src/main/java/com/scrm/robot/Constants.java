package com.scrm.robot;

public class Constants {

    public final static String WEWORK_PACKAGE_NAME = "com.tencent.wework";
    public final static String BROADCAST_MSG_TYPE_KEY = "msgType";

    public final static int JOB_INTERVAL_MILL_SECONDS = 5 * 1000;

    public final static String MESSENGER_INTENT_KEY = "mainMessenger";

    public final static String INTENT_JOB_INFO_ID_KEY = "robotJobId";
    public final static String INTENT_JOB_INFO_TYPE_KEY = "robotJobType";
    public final static String INTENT_SCREENSHOT_FILE_NAME_KEY = "fileName";

    public final static String MSG_SCHEDULER_JOB_STATE_KEY ="robotSchedulerJobState";

    public final static String JOB_SCHEDULER_MSG_RECEIVER = "com.scrm.robot.receiver.jobScheduleMsgReceiver";

    public final static  int REQUEST_MEDIA_PROJECTION=100;

    /**
     * 5000kb, 50mb
     */
    public final static  int MAX_BYTES=50*1000;
}
