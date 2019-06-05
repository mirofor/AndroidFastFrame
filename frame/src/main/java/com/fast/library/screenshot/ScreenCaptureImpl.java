package com.fast.library.screenshot;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Handler;
import android.support.v4.os.AsyncTaskCompat;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 说明：ScreenCaptureImpl
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScreenCaptureImpl {

    private final static String SCREEN_MIRROR = "screen-mirror";
    private final static long DELAY_START_CAPTURE = 100;

    private MediaProjection mMediaProjection;
    private VirtualDisplay mVirtualDisplay;
    private ImageReader mImageReader;
    private int mScreenWidth;
    private int mScreenHeight;
    private int mScreenDensity;

    private Handler mHandler = new Handler();

    public ScreenCaptureImpl(Context context,Intent data){
        initScreenCapture(context,data);
    }

    private void initScreenCapture(Context context,Intent data){
        MediaProjectionManager manager= (MediaProjectionManager) context.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        mMediaProjection = manager.getMediaProjection(Activity.RESULT_OK,data);

        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
        mScreenWidth = metrics.widthPixels;
        mScreenHeight = metrics.heightPixels;

        mImageReader = ImageReader.newInstance(mScreenWidth, mScreenHeight, PixelFormat.RGBA_8888, 1);
    }

    /**
     * Start Screen Shot
     * @param listener
     */
    public void startScreenShot(final IScreenCaptureFile screenCaptureFile,
                                final OnCaptureListener listener){
        if (listener != null){
            listener.onStartCapture();
        }
        mHandler.post(new Runnable() {
            @Override
            public void run() {
                startVirtual();
            }
        });
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startCapture(screenCaptureFile,listener);
            }
        },DELAY_START_CAPTURE);
    }

    private void startCapture(IScreenCaptureFile screenCaptureFile,OnCaptureListener listener) {
        Image image = mImageReader.acquireLatestImage();
        if (image == null) {
            startScreenShot(screenCaptureFile,listener);
        } else {
            SaveCaptureTask mSaveTask = new SaveCaptureTask(screenCaptureFile,listener);
            AsyncTaskCompat.executeParallel(mSaveTask, image);
        }
    }

    private void startVirtual() {
        mVirtualDisplay = mMediaProjection.createVirtualDisplay(SCREEN_MIRROR,
                mScreenWidth, mScreenHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                mImageReader.getSurface(), null, null);
    }

    private void tearDownMediaProjection() {
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
    }

    private void stopVirtual() {
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
            mVirtualDisplay = null;
        }
    }

    public void onDestroy() {
        stopVirtual();
        tearDownMediaProjection();
    }

}
