package com.scrm.robot;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.scrm.robot.taskmanager.JobStateViewModel;

public class userSettingActivity extends Activity {

    private static TextView title;
    private static Button back;
    private static Button save;
    private static EditText x1;
    private static EditText y1;
    private static EditText x2;
    private static EditText y2;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_settings_activity);
        x1 = findViewById(R.id.editX1);
        y1 = findViewById(R.id.editY1);
        x2 = findViewById(R.id.editX2);
        y2 = findViewById(R.id.editY2);
        title = findViewById(R.id.title_text);
        back = findViewById(R.id.bcak);
        save = findViewById(R.id.saveSetting);
        title.setText("用户参数配置");
        if(JobStateViewModel.x1.getValue()!=null){x1.setText(JobStateViewModel.x1.getValue().toString());}
        if(JobStateViewModel.y1.getValue()!=null){y1.setText(JobStateViewModel.y1.getValue().toString());}
        if(JobStateViewModel.x2.getValue()!=null){x2.setText(JobStateViewModel.x2.getValue().toString());}
        if(JobStateViewModel.y2.getValue()!=null){y2.setText(JobStateViewModel.y2.getValue().toString());}
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(userSettingActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!TextUtils.isEmpty(x1.getText().toString())) {
                    JobStateViewModel.x1.setValue(Double.parseDouble(x1.getText().toString()));
                }
                if (!TextUtils.isEmpty(y1.getText().toString())) {
                    JobStateViewModel.y1.setValue(Double.parseDouble(y1.getText().toString()));
                }
                if (!TextUtils.isEmpty(x2.getText().toString())) {
                    JobStateViewModel.x2.setValue(Double.parseDouble(x2.getText().toString()));
                }
                if (!TextUtils.isEmpty(y2.getText().toString())) {
                    JobStateViewModel.y2.setValue(Double.parseDouble(y2.getText().toString()));
                }
                Toast.makeText(userSettingActivity.this, "参数配置修改成功！", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
