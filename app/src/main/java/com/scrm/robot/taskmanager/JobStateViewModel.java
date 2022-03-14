package com.scrm.robot.taskmanager;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class JobStateViewModel extends ViewModel {
    public static MutableLiveData<Boolean> isScreenShot=new MutableLiveData<>(false);
    public static MutableLiveData<String> sopType=new MutableLiveData<>("new");
}
