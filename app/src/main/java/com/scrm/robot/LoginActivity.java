package com.scrm.robot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.scrm.robot.utils.HttpConnThread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;

public class LoginActivity extends Activity{
    public EditText accountNum;
    public EditText passWord;
    public Button bt_ok;
    public int currentLayout;
    public LayoutInflater mInflater;
    public ImageView loginImg;
    public View loginView;
    public TextView version;
    public View mainView;
    public static String Account;
    public static String PWd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initViews();
    }
    /**
     * 初始化布局
     */
    protected void initViews() {
        //初始化布局加载器
        mInflater = LayoutInflater.from(this);
        //加载登录页面
        loginView = mInflater.inflate(R.layout.activity_login, null);
        //调用setContentView(View view)方法，传入一个View
        setContentView(loginView);
    }

    /**
     * 调用了setContentView后会回调此方法
     */
    @Override
    public void onContentChanged() {
        super.onContentChanged();
        loginImg = findViewById(R.id.loginImg);
        accountNum = findViewById(R.id.editAccount);
        passWord = findViewById(R.id.editPassword);
        bt_ok = findViewById(R.id.loginButton);
        version = findViewById(R.id.version);
        //set page data
        version.setText(R.string.version);
        loginImg.setImageResource(R.drawable.login);
    }

    public void loginIn(View view) {
        // region HARDCODE
        accountNum.setText("18000000001");
        passWord.setText("a123456");
        // endregion

        if (TextUtils.isEmpty(accountNum.getText().toString())) {
            Logger.w("登录账号不能为空");
            Toast.makeText(this, "账号不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(passWord.getText().toString())) {            Logger.e("登录账号不能为空");
            Logger.w("登录密码不能为空");
            Toast.makeText(this, "请输入密码",Toast.LENGTH_SHORT).show();
            return;
        }
        Account = accountNum.getText().toString();
        PWd = passWord.getText().toString();
        HttpConnThread postThread = new HttpConnThread();
        postThread.start();
        try {
            postThread.join();
        } catch (InterruptedException e) {
            Logger.e("登录失败: %s ",e);
            e.printStackTrace();
        }

        if(HttpConnThread.userName != null){
            Logger.i("登录成功");
            Toast.makeText(this, "登录成功！", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this,MainActivity.class);
            startActivity(intent);
            finish();
        }else {
            Logger.w("登录信息错误");
            Toast.makeText(this, "账号/密码输入不正确，请检查后重试。", Toast.LENGTH_SHORT).show();
        }
    }

    public void weworkLoginIn(View view) {
        Intent intent = new Intent(this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
