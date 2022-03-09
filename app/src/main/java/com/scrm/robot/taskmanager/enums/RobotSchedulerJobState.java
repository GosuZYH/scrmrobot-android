package com.scrm.robot.taskmanager.enums;

public enum RobotSchedulerJobState {
    START("start",0),
    FINISH("finish",1),
    STOP("stop",2);

    public String name;
    public int value;

    private RobotSchedulerJobState(String name, int value){
        this.name=name;
        this.value=value;
    }

}
