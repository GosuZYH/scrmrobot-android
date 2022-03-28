package com.scrm.robot;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.scrm.robot.taskmanager.JobSchedulerMessageReceiver;
import com.scrm.robot.taskmanager.JobStateViewModel;
import com.scrm.robot.taskmanager.RobotAccessibilityContext;
import com.scrm.robot.taskmanager.enums.RobotBroadcastType;
import com.scrm.robot.utils.AccessibilityGestureUtil;
import com.scrm.robot.utils.ApplicationUtil;

import com.scrm.robot.utils.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;


public class ScreenShotService extends Service implements LifecycleOwner{
    private final static String TAG = ScreenShotService.class.getName();

    public AccessibilityGestureUtil accessibilityGestureUtil;
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;

    private static Intent mResultData = null;


    private ImageReader mImageReader;
    private WindowManager mWindowManager;

    private int mScreenWidth;
    private int mScreenHeight;
    private int mScreenDensity;

//
//    private LocalBroadcastManager localBroadcastManager;
//    private static JobSchedulerMessageReceiver receiver;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }


    @Override
    public void onCreate() {
        super.onCreate();
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_CREATE);

        this.createNotification();
        this.initBroadcast();
        this.initWindow();
        this.initObserve();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId){
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_START);
        return super.onStartCommand(intent, flags, startId);
    }



    public static Intent getResultData() {
        return mResultData;
    }
    public static void setResultData(Intent mResultData) {
        ScreenShotService.mResultData = mResultData;
    }

    private void  initBroadcast(){
//        this.localBroadcastManager= LocalBroadcastManager.getInstance(this);
//        if(receiver==null) {
//            receiver = new JobSchedulerMessageReceiver();
//            this.localBroadcastManager.registerReceiver(receiver, new IntentFilter(Constants.JOB_SCHEDULER_MSG_RECEIVER));
//        }
    }

    private void initWindow(){
        mWindowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;
        createImageReader();
    }

    private void initObserve(){
        JobStateViewModel.isScreenShot.observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    startScreenShot();
                }else {
                }
            }
        });
    }
    @SuppressLint("WrongConstant")
    private void createImageReader() {
        mImageReader = ImageReader.newInstance(mScreenWidth, mScreenHeight, PixelFormat.RGBA_8888, 1);
    }

    private void
    startScreenShot() {

        Handler handler1 = new Handler();
        handler1.postDelayed(new Runnable() {
            public void run() {
                //start virtual
                startVirtual();
            }
        }, 5);

        handler1.postDelayed(new Runnable() {
            public void run() {
                //capture the screen
                startCapture();
            }
        }, 30);
    }

    public void startVirtual() {
        if (mMediaProjection != null) {
            virtualDisplay();
        } else {
            setUpMediaProjection();
            virtualDisplay();
        }
    }
    public void setUpMediaProjection() {
        if (mResultData == null) {
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            startActivity(intent);
        } else {
            mMediaProjection = getMediaProjectionManager().getMediaProjection(Activity.RESULT_OK, mResultData);
        }
    }

    private MediaProjectionManager getMediaProjectionManager() {

        return (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
    }

    boolean isSavingImage =false;
    private void startCapture() {
        if(!isSavingImage) {
            Image image = mImageReader.acquireLatestImage();
            if (image == null) {
                startScreenShot();
            } else {
                isSavingImage =true;
                SaveTask mSaveTask = new SaveTask();
                mSaveTask.execute(image);
            }
        }else {
            startScreenShot();
        }
    }

    private final LifecycleRegistry lifecycleRegistry=new LifecycleRegistry(this);

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return this.lifecycleRegistry;
    }

    public class SaveTask extends AsyncTask<Image, Void, Bitmap> {



        @RequiresApi(api = Build.VERSION_CODES.Q)
        @Override
        protected Bitmap doInBackground(Image... params) {
            if (params == null || params.length < 1 || params[0] == null) {
                return null;
            }
            RobotApplication application = (RobotApplication) ApplicationUtil.getApplication();
            RobotAccessibilityContext robotAccessibilityContext = application.getRobotAccessibilityContext();
            accessibilityGestureUtil=new AccessibilityGestureUtil(robotAccessibilityContext.getWeWorkAccessibilityService());

            JobStateViewModel.sopType.postValue("new");
            Image image = params[0];

            String sopType = "loading";
            int width = image.getWidth();
            int height = image.getHeight();
            final Image.Plane[] planes = image.getPlanes();
            final ByteBuffer buffer = planes[0].getBuffer();
            //每个像素的间距
            int pixelStride = planes[0].getPixelStride();
            //总的间距
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * width;
            Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
            image.close();
            Log.d(TAG,"x:"+width+"height"+height);
//            Color color = bitmap.getColor(933,1733);
//            int pixel = bitmap.getPixel(933,1733);
            //for xiaoMi
            Color color = bitmap.getColor((int)(0.8639*width),(int)(0.857*height));
//            Color color = bitmap.getColor(623,1321);
            Log.d(TAG,"color："+color);
//            int pixel = bitmap.getPixel(623,1321);
            if (color.red() >0.52 && color.red()<0.56 && color.green()>0.65 && color.green()<0.69 && color.blue()>0.84 &color.blue()<0.88){
                //已回执
                accessibilityGestureUtil.swip((int)(width*0.5),(int)(height*0.5),(int)(width*0.5),(int)(height*0.4));
                sopType = "noneed";
            }else if (color.red() > 0.20 && color.red() < 0.24 && color.green() > 0.43 && color.green() < 0.47 && color.blue() > 0.75 & color.blue() < 0.79) {
                //未回执
                accessibilityGestureUtil.click((int)(0.5*width), (int)(0.968*height));
                accessibilityGestureUtil.click((int)(0.5*width), (int)(1.007*height));
//                this.accessibilityGestureUtil.click(540, 2070);
                sopType = "need";
            }else {
                //加载未完成
                accessibilityGestureUtil.swip((int)(width*0.5),(int)(height*0.5),(int)(width*0.5),(int)(height*0.4));
                sopType = "loading";
            }
            //for test
//            this.accessibilityGestureUtil.click(540, 2070);
//            sopType = "need";
            sendBroadcast(sopType);
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            isSavingImage =false;
        }
    }

    private void virtualDisplay() {
        if(mVirtualDisplay==null) {
            mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                    mScreenWidth, mScreenHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    mImageReader.getSurface(), null, null);
        }
    }


    private void stopVirtual() {
        if (mVirtualDisplay == null) {
            return;
        }
        mVirtualDisplay.release();
        mVirtualDisplay = null;
    }


    private void tearDownMediaProjection() {
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }

    @Override
    public boolean onUnbind( Intent intent) {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_STOP);
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        lifecycleRegistry.handleLifecycleEvent(Lifecycle.Event.ON_DESTROY);

        super.onDestroy();
        stopVirtual();
        tearDownMediaProjection();
    }


    private String NOTIFICATION_CHANNEL_ID = "ScreenShotService_nofity";
    private String NOTIFICATION_CHANNEL_NAME = "ScreenShotService";
    private String NOTIFICATION_CHANNEL_DESC = "ScreenShotService";
    private int NOTIFICATION_ID = 1000;
    private static final String NOTIFICATION_TICKER = "RecorderApp";

    public void createNotification() {
        Log.i(TAG, "notification: " + Build.VERSION.SDK_INT);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Call Start foreground with notification
            Intent notificationIntent = new Intent(this, ScreenShotService.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher_foreground))
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setContentTitle("Starting Service")
                    .setContentText("Starting monitoring service")
                    .setTicker(NOTIFICATION_TICKER)
                    .setContentIntent(pendingIntent);
            Notification notification = notificationBuilder.build();
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, NOTIFICATION_CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(NOTIFICATION_CHANNEL_DESC);
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(channel);
            startForeground(NOTIFICATION_ID, notification);
            //notificationManager.notify(NOTIFICATION_ID, notification);
        }
    }

    /**
     * 服务端给客户端广播消息
     */
    private void sendBroadcast(String msg) {

        Intent intent = new Intent(Constants.JOB_SCHEDULER_MSG_RECEIVER);
        intent.putExtra(Constants.BROADCAST_MSG_TYPE_KEY, RobotBroadcastType.SCREENSHOT_FINISH_BROADCAST.value);
//        intent.putExtra(Constants.INTENT_SCREENSHOT_FILE_NAME_KEY, msg);
        intent.putExtra("sopType",msg);
//        this.localBroadcastManager.sendBroadcast(intent);
        LocalBroadcastManager localBroadcastManager = ((RobotApplication) ApplicationUtil.getApplication()).getLocalBroadcastManager();
        localBroadcastManager.sendBroadcast(intent);
    }
}
