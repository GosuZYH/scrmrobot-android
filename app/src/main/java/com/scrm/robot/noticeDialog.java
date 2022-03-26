package com.scrm.robot;

import android.app.Activity;
import android.app.Dialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Display;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class noticeDialog extends Dialog {
    Activity context;
    public ImageView accessNoticeIMG;
    public Button openAccess;
    public ImageButton closeNotice;
    public Window dialogWindow;

    public noticeDialog(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局

        setContentView(R.layout.notice_dialog);
        accessNoticeIMG = findViewById(R.id.accessNoticeIMG);
        openAccess = findViewById(R.id.openAccess);
        closeNotice = findViewById(R.id.closeNotice);
        dialogWindow = getWindow();

        WindowManager m = context.getWindowManager();
//        FragmentManager F = context.getFragmentManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.height = (int) (d.getHeight() * 0.65);
        p.width = (int) (d.getWidth() * 0.9);
        accessNoticeIMG.setImageResource(R.drawable.qq20220324142956);
        closeNotice.setImageResource(R.drawable.qq20220325192137);
        dialogWindow.setAttributes(p);
    }
}

