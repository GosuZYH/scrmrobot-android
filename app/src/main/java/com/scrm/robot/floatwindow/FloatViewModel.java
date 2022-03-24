package com.scrm.robot.floatwindow;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.scrm.robot.taskmanager.enums.RobotJobType;

public class FloatViewModel extends ViewModel {
    public static MutableLiveData<Boolean> isFloatWindowShow=new MutableLiveData<>(false);
    public static MutableLiveData<Boolean> isAccessNoticeShow=new MutableLiveData<>(false);
    public static MutableLiveData<Boolean> isFloatNoticeShow=new MutableLiveData<>(false);
    public static MutableLiveData<RobotJobType> currentOnClickJob=new MutableLiveData<>(RobotJobType.GROUP_SEND_MOMENT);
    public static MutableLiveData<Boolean> jobStartStop=new MutableLiveData<>(false);
}
