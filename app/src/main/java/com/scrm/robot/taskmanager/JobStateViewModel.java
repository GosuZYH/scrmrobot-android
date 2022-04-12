package com.scrm.robot.taskmanager;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class JobStateViewModel extends ViewModel {
    public static MutableLiveData<Boolean> isScreenShot=new MutableLiveData<>(false);
    public static MutableLiveData<String> sopType=new MutableLiveData<>("new");
    public static MutableLiveData<Integer> width = new MutableLiveData<>(null);
    public static MutableLiveData<Integer> height = new MutableLiveData<>(null);
    public static MutableLiveData<Integer> realWidth = new MutableLiveData<>(null);
    public static MutableLiveData<Integer> realHeight = new MutableLiveData<>(null);
    public static MutableLiveData<Double> sopMomentShareBtnXError = new MutableLiveData<Double>(500.0);
    public static MutableLiveData<Double> sopMomentShareBtnYError = new MutableLiveData<Double>(500.0);
    public static MutableLiveData<Double> sopMomentReceiptBtnXError = new MutableLiveData<>(500.0);
    public static MutableLiveData<Double> sopMomentReceiptBtnYError = new MutableLiveData<>(500.0);
    public static MutableLiveData<Double> sopMomentReceiptRGBXError = new MutableLiveData<>(500.0);
    public static MutableLiveData<Double> sopMomentReceiptRGBYError = new MutableLiveData<>(500.0);
    public static MutableLiveData<String> weworkVersion = new MutableLiveData<>("v4.0.3");
}
