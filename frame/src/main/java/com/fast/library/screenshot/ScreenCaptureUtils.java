package com.fast.library.screenshot;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import com.fast.library.FastFrame;
import java.io.File;

/**
 * 说明：ScreenCaptureUtils（5.0 之后才允许使用屏幕截图）
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class ScreenCaptureUtils {

    private static final int REQUEST_MEDIA_PROJECTION = 1000;

    private Context mContext;
    private ScreenCaptureImpl mScreenCaptureImpl;

    /**
     * 5.0 之后才允许使用屏幕截图
     */
    public ScreenCaptureUtils(@NonNull Activity activity){
        mContext = activity;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            MediaProjectionManager mediaProjectionManager = (MediaProjectionManager)
                    activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            activity.startActivityForResult(
                    mediaProjectionManager.createScreenCaptureIntent(),
                    REQUEST_MEDIA_PROJECTION);
        }
    }

    /**
     * onActivityResult
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public boolean onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_MEDIA_PROJECTION && data != null){
            mScreenCaptureImpl = new ScreenCaptureImpl(mContext,data);
            return true;
        }else {
            return false;
        }
    }

    /**
     * 刷新图库
     * @param file
     */
    public static void refreshPic(File file){
        try {
            Intent media = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri contentUri = Uri.fromFile(file);
            media.setData(contentUri);
            FastFrame.getApplication().sendBroadcast(media);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * 截图
     * @param listener
     */
    public void startScreenShot(IScreenCaptureFile screenCaptureFile,final OnCaptureListener listener){
        if (mScreenCaptureImpl != null){
            mScreenCaptureImpl.startScreenShot(screenCaptureFile,listener);
        }
    }

    public void onDestory(){
        if (mScreenCaptureImpl != null){
            mScreenCaptureImpl.onDestroy();
        }
    }

}
