package com.scrm.robot;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.scrm.robot.floatwindow.FloatViewModel;
import com.scrm.robot.taskmanager.enums.RobotJobType;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class MainFragment extends Fragment {
    private final static String TAG = MainActivity.class.getName();
    Activity context;
    public Dialog noticeDialog;
    public Dialog floatNoticeDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        return view;
    }

}
