package com.scrm.robot;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

public class JobActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.job_activity);
    }

    public void backToMain(View view){
        Intent intent = new Intent(JobActivity.this,MainActivity.class);
        startActivity(intent);
        finish();
    }
}
