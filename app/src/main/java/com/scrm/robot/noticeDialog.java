package com.scrm.robot;

import android.app.Activity;
import android.app.Dialog;
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

        this.setContentView(R.layout.notice_dialog);
        accessNoticeIMG = (ImageView) findViewById(R.id.accessNoticeIMG);
        openAccess = (Button) findViewById(R.id.openAccess);
        closeNotice = (ImageButton) findViewById(R.id.closeNotice);
        dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.height = (int) (d.getHeight() * 0.65);
        p.width = (int) (d.getWidth() * 0.9);
        dialogWindow.setAttributes(p);
    }
}

