package com.scrm.robot;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.scrm.robot.taskmanager.JobStateViewModel;
import com.scrm.robot.utils.ApplicationUtil;

public class userSettingActivity extends Activity {
    public static String weworkVersion;
    private  TextView title;
    private  Button back;
    private  Button save;
    private  EditText sopMomentShareBtnXErrorET;
    private  EditText sopMomentShareBtnYErrorET;
    private  EditText sopMomentReceiptBtnXErrorET;
    private  EditText sopMomentReceiptBtnYErrorET;
    private TextView windowWidthTV;
    private TextView windowHeightTV;
    private RadioGroup checkVersion;
    private RadioButton v1;
    private RadioButton v2;

    private WindowManager windowManager;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_settings_activity);
        windowWidthTV=findViewById(R.id.windowWidthTV);
        windowHeightTV=findViewById(R.id.windowHeightTV);

        sopMomentShareBtnXErrorET = findViewById(R.id.sopMomentShareBtnXErrorET);
        sopMomentShareBtnYErrorET = findViewById(R.id.sopMomentShareBtnYErrorET);

        sopMomentReceiptBtnXErrorET = findViewById(R.id.editX2);
        sopMomentReceiptBtnYErrorET = findViewById(R.id.editY2);

        checkVersion = findViewById(R.id.checkVersion);
        v1 = findViewById(R.id.v4_0_0);
        v2 = findViewById(R.id.v4_0_3);

        title = findViewById(R.id.title_text);
        back = findViewById(R.id.bcak);
        save = findViewById(R.id.saveSetting);
        title.setText("用户参数配置");
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getRealMetrics(displayMetrics);
        windowWidthTV.setText(String.valueOf( displayMetrics.widthPixels));
        windowHeightTV.setText(String.valueOf( displayMetrics.heightPixels));

        if(JobStateViewModel.sopMomentShareBtnXError.getValue()!=null){
            sopMomentShareBtnXErrorET.setText(JobStateViewModel.sopMomentShareBtnXError.getValue().toString());}
        if(JobStateViewModel.sopMomentShareBtnYError.getValue()!=null){
            sopMomentShareBtnYErrorET.setText(JobStateViewModel.sopMomentShareBtnYError.getValue().toString());}

        if(JobStateViewModel.sopMomentReceiptBtnXError.getValue()!=null){
            sopMomentReceiptBtnXErrorET.setText(JobStateViewModel.sopMomentReceiptBtnXError.getValue().toString());}
        if(JobStateViewModel.sopMomentReceiptBtnYError.getValue()!=null){
            sopMomentReceiptBtnYErrorET.setText(JobStateViewModel.sopMomentReceiptBtnYError.getValue().toString());}

        if(JobStateViewModel.weworkVersion.getValue()!=null){
            if(JobStateViewModel.weworkVersion.getValue().equals(v1.getText())){
                v1.setChecked(true);
            }else if(JobStateViewModel.weworkVersion.getValue().equals(v2.getText())){
                v2.setChecked(true);
            }
        }

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
                if (!TextUtils.isEmpty(sopMomentShareBtnXErrorET.getText().toString())) {
                    JobStateViewModel.sopMomentShareBtnXError.setValue(Double.parseDouble(sopMomentShareBtnXErrorET.getText().toString()));
                }
                if (!TextUtils.isEmpty(sopMomentShareBtnYErrorET.getText().toString())) {
                    JobStateViewModel.sopMomentShareBtnYError.setValue(Double.parseDouble(sopMomentShareBtnYErrorET.getText().toString()));
                }
                if (!TextUtils.isEmpty(sopMomentReceiptBtnXErrorET.getText().toString())) {
                    JobStateViewModel.sopMomentReceiptBtnXError.setValue(Double.parseDouble(sopMomentReceiptBtnXErrorET.getText().toString()));
                }
                if (!TextUtils.isEmpty(sopMomentReceiptBtnYErrorET.getText().toString())) {
                    JobStateViewModel.sopMomentReceiptBtnYError.setValue(Double.parseDouble(sopMomentReceiptBtnYErrorET.getText().toString()));
                }
                if (v1.isChecked()){
                    JobStateViewModel.weworkVersion.setValue(v1.getText().toString());
                }else if(v2.isChecked()){
                    JobStateViewModel.weworkVersion.setValue(v2.getText().toString());
                }
                RobotApplication application= (RobotApplication) ApplicationUtil.getApplication();
                application.saveAppSettings();

                Toast.makeText(userSettingActivity.this, "参数配置修改成功！", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
