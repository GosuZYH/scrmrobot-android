package com.scrm.robot.taskmanager;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class JobStateViewModel extends ViewModel {
    public static MutableLiveData<Boolean> isScreenShot=new MutableLiveData<>(false);
    public static MutableLiveData<String> sopType=new MutableLiveData<>("new");
    public static MutableLiveData<Integer> width = new MutableLiveData<>(null);
    public static MutableLiveData<Integer> height = new MutableLiveData<>(null);
    public static MutableLiveData<Double> x1 = new MutableLiveData<>(null);
    public static MutableLiveData<Double> y1 = new MutableLiveData<>(null);
    public static MutableLiveData<Double> x2 = new MutableLiveData<>(null);
    public static MutableLiveData<Double> y2 = new MutableLiveData<>(null);
}
