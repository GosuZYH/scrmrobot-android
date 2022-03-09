package com.scrm.robot.utils;

import android.app.Application;

public class ApplicationUtil {
    private volatile static ApplicationUtil applicationUtil;

    private Application application;

    private ApplicationUtil(Application application){
        this.application = application;
    }

    public static void init(Application application){
        if(applicationUtil==null){
            synchronized (ApplicationUtil.class){
                if(applicationUtil==null){
                    applicationUtil=new ApplicationUtil(application);
                }
            }
        }
    }

    public static Application getApplication(){
        if(applicationUtil==null){
            throw new NullPointerException("ApplicationUtil not init");
        }
        return applicationUtil.application;
    }
}
