package com.scrm.robot;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.provider.Settings;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    private Button buttonCallWeWork;
    private Button buttonAccessSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.activity_main);
        super.onCreate(savedInstanceState);

        buttonCallWeWork = findViewById(R.id.button_callwework);
        buttonCallWeWork.setOnClickListener(view -> {
            Intent intent = getPackageManager().getLaunchIntentForPackage("com.tencent.wework");
            startActivity(intent);
        });


        buttonAccessSetting = findViewById(R.id.button_setting);
        buttonAccessSetting.setOnClickListener(view -> startActivity(new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)));


    }




}