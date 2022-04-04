package com.scrm.robot.taskmanager;


import android.annotation.SuppressLint;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.orhanobut.logger.Logger;
import com.scrm.robot.Constants;
import com.scrm.robot.RobotApplication;
import com.scrm.robot.taskmanager.enums.RobotBroadcastType;
import com.scrm.robot.taskmanager.enums.RobotSchedulerJobState;
import com.scrm.robot.utils.ApplicationUtil;
import com.scrm.robot.utils.DateUtils;

@SuppressLint("SpecifyJobSchedulerIdRange")
public class JobSchedulerService extends JobService {
    private final static String TAG = JobSchedulerService.class.getName();
//    private Messenger mMessenger;
//
//    private LocalBroadcastManager localBroadcastManager;
//    private static JobSchedulerMessageReceiver receiver;

    public JobSchedulerService(){
        super();
    }

    @Override
    public void onCreate(){
        super.onCreate();
//        this.localBroadcastManager= LocalBroadcastManager.getInstance(this);
//        if(receiver==null) {
//           receiver = new JobSchedulerMessageReceiver();
//           this.localBroadcastManager.registerReceiver(receiver, new IntentFilter(Constants.JOB_SCHEDULER_MSG_RECEIVER));
//        }
        RobotApplication robotApplication = (RobotApplication) ApplicationUtil.getApplication();
        robotApplication.getRobotJobScheduler().setJobSchedulerService(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
//        mMessenger = intent.getParcelableExtra(Constants.MESSENGER_INTENT_KEY);
        return START_NOT_STICKY;
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Logger.d("任务运行: %s %s", jobParameters.getJobId() , DateUtils.getLogDate());
        RobotApplication robotApplication = (RobotApplication) ApplicationUtil.getApplication();
        try {
            robotApplication.getRobotJobScheduler().runJob(jobParameters);
        } finally {

        }
        return true;
    }

    public void finishJob(JobParameters jobParameters){
        jobFinished(jobParameters, false);
        this.sendBroadcast(RobotSchedulerJobState.FINISH, jobParameters);
    }

    /**
     * 当系统接收到一个取消请求时，系统会调用onStopJob方法取消正在等待执行的任务。
     * 其实onStopJob在jobFinished正常调用结束一个job时，也是不会调用的，
     * 只有在该job没有被执行完，就被cancel掉的时候回调到，
     * 比如某个job还没有执行就被JobScheduler给Cancel掉时，
     * 或者在某个运行条件不满足时，比如原来在Wifi环境允许的某个任务，执行过程中切换到了非Wifi场景，那也会调用该方法。
     * 改方法也返回一个boolean值，返回true表示会重新放到JobScheduler里reScheduler，false表示直接忽略。
     *
     * @param jobParameters
     * @return
     */
    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG,"job stop: "+jobParameters.getJobId());
        this.sendBroadcast(RobotSchedulerJobState.STOP, jobParameters);
        return false;
    }

//    /**
//     * 服务端给客户端发消息
//     * @param schedulerJobState
//     * @param jobParameters
//     */
//    private void sendMessage(RobotSchedulerJobState schedulerJobState,
//                             JobParameters jobParameters){
//        if(this.mMessenger ==null){
//            return;
//        }
//        Message message= Message.obtain();
//        message.what=schedulerJobState.value;
//        message.obj =jobParameters;
//        try {
//            mMessenger.send(message);
//        } catch (RemoteException e) {
//            Log.e(TAG, e.getMessage());
//            e.printStackTrace();
//        }
//
//    }

    /**
     * 服务端给客户端广播消息
     * @param schedulerJobState
     * @param jobParameters
     */
    private void sendBroadcast(RobotSchedulerJobState schedulerJobState,
                             JobParameters jobParameters){

        Intent intent=new Intent(Constants.JOB_SCHEDULER_MSG_RECEIVER);
        intent.putExtra(Constants.BROADCAST_MSG_TYPE_KEY, RobotBroadcastType.JOB_STATE_BROADCAST.value);
        intent.putExtra(Constants.INTENT_JOB_INFO_ID_KEY, jobParameters.getJobId());
        intent.putExtra(Constants.MSG_SCHEDULER_JOB_STATE_KEY, schedulerJobState.value);
        LocalBroadcastManager localBroadcastManager = ((RobotApplication)ApplicationUtil.getApplication()).getLocalBroadcastManager();
        localBroadcastManager.sendBroadcast(intent);
    }
}
