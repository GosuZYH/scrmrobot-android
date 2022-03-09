package com.scrm.robot.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

public class DateUtil {
    final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    public static String getLogDate(){
        return simpleDateFormat.format(new Date());
    }
}
