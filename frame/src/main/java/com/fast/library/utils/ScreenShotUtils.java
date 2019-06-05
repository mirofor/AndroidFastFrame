package com.fast.library.utils;

import android.graphics.Bitmap;
import android.view.View;
import java.io.File;
import java.io.FileOutputStream;

/**
 * 说明：截屏
 */
public class ScreenShotUtils {

    public interface ScreenShotListener{
        void onSuccess(File file);
        void onError(Exception e);
    }

    /**
     * 说明：截图
     *
     * @param v 需要进行截图的控件
     * @return 该控件截图的Bitmap对象。
     */
    private static Bitmap captureView(View v) {
        v.setDrawingCacheEnabled(true);
        v.buildDrawingCache();
        return v.getDrawingCache();
    }

    /**
     * 截图
     * @param view
     * @param file
     * @param listener
     */
    public static void captureDecorView(final View view, final File file, final ScreenShotListener listener){
        if (view == null){
            if (listener != null){
                listener.onError(new IllegalArgumentException("view is null!"));
            }
            return;
        }
        if (file == null){
            if (listener != null){
                listener.onError(new IllegalArgumentException("file is null!"));
            }
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                Bitmap bitmap = captureView(view);
                save(view,bitmap,file, Bitmap.CompressFormat.PNG, 100,listener);
            }
        }).start();
    }

    /**
     * 说明：保存图片
     * @param view
     * @param bitmap 位图
     * @param file 图片路径
     * @param format 图片格式
     * @param quality 图片质量
     * @param listener 监听器
     */
    private static void save(View view, Bitmap bitmap, final File file, Bitmap.CompressFormat format,
                             int quality, final ScreenShotListener listener){
        if (bitmap == null || file == null){
            return;
        }
        FileOutputStream fos = null;
        Exception exception = null;
        try {
            if (!file.getParentFile().exists())file.getParentFile().mkdirs();
            fos = new FileOutputStream(file);
            bitmap.compress(format, quality, fos);
            fos.flush();
            ToolUtils.refreshPic(view.getContext(),file);
        }catch (Exception e){
            exception = e;
        }finally {
            FileUtils.closeIO(fos);
        }
        if (listener != null){
            final Exception e1 = exception;
            if (exception != null){
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onError(e1);
                    }
                });
            }else {
                view.post(new Runnable() {
                    @Override
                    public void run() {
                        listener.onSuccess(file);
                    }
                });
            }
        }
    }

}
