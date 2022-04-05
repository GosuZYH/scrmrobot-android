package com.scrm.robot.taskmanager.job;

public interface IRobotJob {
    void  run() throws InterruptedException;

    void process();

    void pause();

    void reRun();

    void  stop();
}
