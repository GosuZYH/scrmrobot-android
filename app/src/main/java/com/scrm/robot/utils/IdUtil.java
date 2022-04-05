package com.scrm.robot.utils;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.UUID;

public class IdUtil {
    final static SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public static String generateTimeId(String prefix) {
        return prefix + simpleDateFormat.format(new Date());
    }
}
