package com.scrm.robot.taskmanager.enums;

public enum RobotJobType {
    SOP_AGENT_SEND_MOMENT("SOP发送朋友圈",1);

    public String name;
    public int value;

    private RobotJobType(String name, int value){
        this.name=name;
        this.value=value;
    }

    public static RobotJobType getByValue(int value){
        for (RobotJobType robotJobType : RobotJobType.values()) {
            if(robotJobType.value==value){
                return robotJobType;
            }
        }
        return null;
    }
}
