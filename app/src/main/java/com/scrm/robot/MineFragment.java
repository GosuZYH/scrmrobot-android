package com.scrm.robot;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.scrm.robot.utils.HttpConnThread;

public class MineFragment extends Fragment {
    public ImageView userPhoto;
    public TextView userName;
    public TextView userId;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.mine_fragment, container, false);
        userPhoto = view.findViewById(R.id.userPhoto);
        userName = view.findViewById(R.id.userName);
        userId = view.findViewById(R.id.userId);
        userPhoto.setImageResource(R.drawable.userphoto);
        userName.setText(HttpConnThread.userName);
        userId.setText("ID:"+HttpConnThread.userId);
        return view;
    }
}
