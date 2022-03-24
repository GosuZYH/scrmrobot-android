package com.scrm.robot;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

public class floatWindowNotice extends Dialog {
    Activity context;
    public ImageView floatWindowNoticeIMG;
    public Button openFloatWindow;
    public ImageButton closeFloatWindowNotice;
    public Window dialogWindow;

    public floatWindowNotice(Activity context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 指定布局

        this.setContentView(R.layout.float_window_dialog);
        floatWindowNoticeIMG = (ImageView) findViewById(R.id.floatWindowNoticeIMG);
        openFloatWindow = (Button) findViewById(R.id.openFloatWindow);
        closeFloatWindowNotice = (ImageButton) findViewById(R.id.closeFloatWindowNotice);
        dialogWindow = this.getWindow();

        WindowManager m = context.getWindowManager();
        Display d = m.getDefaultDisplay();
        WindowManager.LayoutParams p = dialogWindow.getAttributes();
        p.height = (int) (d.getHeight() * 0.35);
        p.width = (int) (d.getWidth() * 0.9);
        dialogWindow.setAttributes(p);
    }
}
