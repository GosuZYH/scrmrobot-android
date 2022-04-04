package com.scrm.robot.taskmanager.enums;

public enum RobotJobType {
    SOP_AGENT_SEND_MOMENT("SOP发送朋友圈",1),
    GROUP_SEND_MESSAGE("群发助手",2),
    CUSTOMER_AGENT_SEND_MOMENT("客户朋友圈",3),
    ALL_TASK_MOMENT("所有任务",4);

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
