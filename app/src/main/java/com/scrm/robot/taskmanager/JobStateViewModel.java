package com.scrm.robot.taskmanager;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class JobStateViewModel extends ViewModel {
    public static MutableLiveData<Boolean> isScreenShot=new MutableLiveData<>(false);
    public static MutableLiveData<String> sopType=new MutableLiveData<>("new");
    public static MutableLiveData<Integer> width = new MutableLiveData<>(null);
    public static MutableLiveData<Integer> height = new MutableLiveData<>(null);
    public static MutableLiveData<Integer> x1 = new MutableLiveData<>(null);
    public static MutableLiveData<Integer> y1 = new MutableLiveData<>(null);
    public static MutableLiveData<Integer> x2 = new MutableLiveData<>(null);
    public static MutableLiveData<Integer> y2 = new MutableLiveData<>(null);
}
