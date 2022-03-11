package com.scrm.robot.taskmanager.enums;

public enum RobotBroadcastType {
    JOB_STATE_BROADCAST("任务状态广播",1),
    SCREENSHOT_FINISH_BROADCAST("截屏完成广播",2);

    public String name;
    public int value;

    private RobotBroadcastType(String name, int value){
        this.name=name;
        this.value=value;
    }

    public static RobotBroadcastType getByValue(int value){
        for (RobotBroadcastType broadcastType : RobotBroadcastType.values()) {
            if(broadcastType.value==value){
                return broadcastType;
            }
        }
        return null;
    }
}
