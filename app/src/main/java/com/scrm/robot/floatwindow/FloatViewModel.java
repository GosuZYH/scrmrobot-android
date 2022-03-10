package com.scrm.robot.floatwindow;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class FloatViewModel extends ViewModel {
    public static MutableLiveData<Boolean> isFloatWindowShow=new MutableLiveData<>(false);
}
