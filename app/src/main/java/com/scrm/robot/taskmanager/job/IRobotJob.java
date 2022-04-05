package com.scrm.robot.taskmanager.job;

public interface IRobotJob {
    void  run() throws InterruptedException;

    void process();

    void pause();

    boolean reRun();

    /**
     * 停止任务
     */
    void finish();

    /**
     * 停止并且重新规划任务
     */
    void finishAndReschedule();
}
