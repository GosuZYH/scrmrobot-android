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
    public TextView id;
    public TextView outTime;
    public TextView version;

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.mine_fragment, container, false);
        userPhoto = view.findViewById(R.id.userPhoto);
        userName = view.findViewById(R.id.userName);
        outTime = view.findViewById(R.id.outTime);
        id = view.findViewById(R.id.id);
        version = view.findViewById(R.id.mineVersion);
        //set data
        userPhoto.setImageResource(R.drawable.logo);
        userName.setText(HttpConnThread.userName);
        id.setText("ID:"+HttpConnThread.id);
        outTime.setText("过期时间:"+HttpConnThread.outTime);
        version.setText(R.string.version);
        return view;
    }
}
