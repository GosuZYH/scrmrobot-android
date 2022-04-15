package com.scrm.robot;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.scrm.robot.floatwindow.FloatViewModel;
import com.scrm.robot.taskmanager.enums.RobotJobType;

public class JobActivity extends Activity {
    public Button startBtn;
    public TextView title;
    public RadioButton todayBtn;
    public RadioButton timeBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_activity);
        initViews();
    }

    @SuppressLint("WrongViewCast")
    private void initViews(){
        startBtn = findViewById(R.id.startBtn);
        title = findViewById(R.id.title_text);
        todayBtn = findViewById(R.id.todayBtn);
        timeBtn = findViewById(R.id.timeBtn);
        todayBtn.setChecked(true);
        if(FloatViewModel.currentOnClickJob.getValue()==RobotJobType.GROUP_SEND_MESSAGE){
            title.setText("一对一私聊群发");
            startBtn.setText("启动企微开始群发");
        }
    }

    public void backToMain(View view){
        Intent intent = new Intent(JobActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void executeTask(View view){
        MainActivity.openCloseFloatView();
    }

}
