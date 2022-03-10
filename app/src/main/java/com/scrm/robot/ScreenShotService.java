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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ServiceLifecycleDispatcher;

import com.scrm.robot.floatwindow.FloatViewModel;
import com.scrm.robot.taskmanager.JobStateViewModel;
import com.scrm.robot.taskmanager.RobotAccessibilityContext;
import com.scrm.robot.utils.FileUtil;

import android.os.AsyncTask;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;


public class ScreenShotService extends Service implements LifecycleOwner{
    private final static String TAG = ScreenShotService.class.getName();
    
    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;

    private static Intent mResultData = null;


    private ImageReader mImageReader;
    private WindowManager mWindowManager;

    private int mScreenWidth;
    private int mScreenHeight;
    private int mScreenDensity;

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

    private void startScreenShot() {

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

    boolean savingImage=false;
    private void startCapture() {
        if(savingImage==false) {
            Image image = mImageReader.acquireLatestImage();
            if (image == null) {
                startScreenShot();
            } else {
                savingImage=true;
                SaveTask mSaveTask = new SaveTask();
                mSaveTask.execute(image);
            }
        }else {
            startScreenShot();
        }
    }

//    private final ServiceLifecycleDispatcher mDispatcher = new ServiceLifecycleDispatcher(this);

    private final LifecycleRegistry lifecycleRegistry=new LifecycleRegistry(this);

    @NonNull
    @Override
    public Lifecycle getLifecycle() {
        return this.lifecycleRegistry;
    }


    public class SaveTask extends AsyncTask<Image, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Image... params) {
            if (params == null || params.length < 1 || params[0] == null) {
                return null;
            }

            Image image = params[0];

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
            File fileImage = null;
            if (bitmap != null) {
                try {
                    fileImage = new File(FileUtil.getScreenShotsName());
                    if (!fileImage.exists()) {
                        fileImage.createNewFile();
                    }
                    FileOutputStream out = new FileOutputStream(fileImage);
                    if (out != null) {
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                        out.flush();
                        out.close();
                        Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                        Uri contentUri = Uri.fromFile(fileImage);
                        media.setData(contentUri);
                        sendBroadcast(media);
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    fileImage = null;
                } catch (IOException e) {
                    e.printStackTrace();
                    fileImage = null;
                }finally {
                    savingImage=false;
                    mImageReader.close();
                }
            }

            if (fileImage != null) {
                return bitmap;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
        }
    }

    private void virtualDisplay() {
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                mScreenWidth, mScreenHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);
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

}
