package com.scrm.robot.taskmanager.job;

public interface IRobotJob {
    void  run() throws InterruptedException;

    void process();

    void  stop();
}
