package com.scrm.robot.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileUtil {
    //系统保存截图的路径
    @SuppressLint("SdCardPath")
    public static final String SCREENCAPTURE_PATH = "/sdcard/";
    public static final String SCREENSHOT_NAME = "SS";


    public static String getScreenShotsName() {
        File file = new File(SCREENCAPTURE_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh-mm-ss");
        String date = simpleDateFormat.format(new Date());
        StringBuffer stringBuffer = new StringBuffer(SCREENCAPTURE_PATH);
        stringBuffer.append(SCREENSHOT_NAME);
        stringBuffer.append("_");
        stringBuffer.append(date);
        stringBuffer.append(".png");
        return stringBuffer.toString();
    }
}
