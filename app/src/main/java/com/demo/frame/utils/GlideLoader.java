package com.demo.frame.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.fast.library.handler.UIHandler;
import com.fast.library.utils.FileUtils;
import com.fast.library.utils.StringUtils;

import java.io.File;
import java.io.FileOutputStream;

/**
 * 说明：Glide工具类
 */
public class GlideLoader {


    private GlideLoader(Context context) {
        if (context == null) {
            throw new NullPointerException("context is null");
        }
    }

    /////////////////////////////////////////////////////

    /**
     * 说明：ImageView显示网络图片
     *
     * @param iv
     * @param url
     */
    public static void into(ImageView iv, String url) {
        if (iv != null && !StringUtils.isEmpty(url)) {
            Glide.with(iv.getContext()).load(url).into(iv);
        }
    }

    /**
     * 说明：ImageView显示资源文件
     *
     * @param iv
     * @param resId
     */
    public static void into(ImageView iv, int resId) {
        if (iv != null && resId > 0) {
            iv.setImageResource(resId);
        }
    }

    /**
     * 说明：ImageView显示文件图片
     *
     * @param iv
     * @param file
     */
    public static void into(ImageView iv, File file) {
        if (iv != null && file != null && file.isFile()) {
            Glide.with(iv.getContext())
                    .load(file)
                    .into(iv);
        }
    }

    /**
     * 说明：ImageView显示文件图片
     *
     * @param iv
     * @param uri
     */
    public static void into(ImageView iv, Uri uri) {
        if (iv != null && uri != null) {
            Glide.with(iv.getContext())
                    .load(uri)
                    .into(iv);
        }
    }

    /**
     * 说明：保存为PNG
     *
     * @param bitmap
     * @param path
     * @param listener
     */
    public static void savePNG(Bitmap bitmap, String path, SaveImageListener listener) {
        save(bitmap, path, Bitmap.CompressFormat.PNG, 100, listener);
    }

    /**
     * 说明：保存为PNG
     *
     * @param bitmap
     * @param path
     * @param listener
     */
    public static void saveJPG(Bitmap bitmap, String path, SaveImageListener listener) {
        save(bitmap, path, Bitmap.CompressFormat.JPEG, 100, listener);
    }

    /**
     * 说明：保存为WEBP
     *
     * @param bitmap
     * @param path
     * @param listener
     */
    public static void saveWEBP(Bitmap bitmap, String path, SaveImageListener listener) {
        save(bitmap, path, Bitmap.CompressFormat.WEBP, 100, listener);
    }

    /**
     * 说明：保存图片-格式PNG
     *
     * @param context
     * @param url
     * @param path
     * @param listener
     */
//    public static void saveImage(Context context, final String url, final String path, final SaveImageListener listener) {
//        if (StringUtils.isEmpty(url) || StringUtils.isEmpty(path)) {
//            return;
//        }
//        bitmap(context, url, new BitmapListener() {
//            @Override
//            public void onStart() {
//                if (listener != null) {
//                    listener.onStart();
//                }
//            }
//
//            @Override
//            public void onSuccess(Bitmap bitmap) {
//                String type = url.substring(url.lastIndexOf('.'));
//                if (StringUtils.isEqualsIgnoreCase(type, "jpg")) {
//                    saveJPG(bitmap, path, listener);
//                } else if (StringUtils.isEqualsIgnoreCase(type, "webp")) {
//                    saveWEBP(bitmap, path, listener);
//                } else {
//                    savePNG(bitmap, path, listener);
//                }
//            }
//
//            @Override
//            public void onFail(Exception e) {
//                if (listener != null) {
//                    listener.onFail(e);
//                }
//            }
//        });
//
//    }

    /**
     * 说明：保存图片
     *
     * @param bitmap   位图
     * @param path     图片路径
     * @param format   图片格式
     * @param quality  图片质量
     * @param listener 监听器
     */
    public static void save(final Bitmap bitmap, final String path,
                            final Bitmap.CompressFormat format, final int quality,
                            final SaveImageListener listener) {
        if (bitmap == null || StringUtils.isEmpty(path)) {
            return;
        }
        UIHandler.subThread(new Runnable() {
            @Override
            public void run() {
                FileOutputStream fos = null;
                try {
                    final File file = new File(path);
                    FileUtils.mkdirs(file.getParentFile());
                    fos = new FileOutputStream(file);
                    bitmap.compress(format, quality, fos);
                    fos.flush();
                    UIHandler.async(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) {
                                listener.onSuccess(file);
                            }
                        }
                    });
                } catch (final Exception e) {
                    UIHandler.async(new Runnable() {
                        @Override
                        public void run() {
                            if (listener != null) {
                                listener.onFail(e);
                            }
                        }
                    });
                } finally {
                    FileUtils.closeIO(fos);
                }
            }
        });
    }

    /**
     * 说明：清理缓存文件
     *
     * @param filePath
     */
    public static long clean(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return 0;
        }
        File file = new File(filePath);
        if (!file.exists() || !file.isDirectory()) {
            return 0;
        } else {
            long size = file.length();
            FileUtils.deleteAllFile(file);
            return size;
        }
    }

    /**
     * 说明：回收资源
     *
     * @param bitmaps
     */
    public static void recycle(Bitmap... bitmaps) {
        if (bitmaps == null) {
            return;
        }
        for (Bitmap bitmap : bitmaps) {
            if (bitmap != null && !bitmap.isRecycled()) {
                bitmap.recycle();
            }
        }
    }

    /////////////////////////////////////////////////////

    public interface GlideListener {
        void onStart();

        void onFail(Exception e);
    }

    /**
     * 说明：Bitmap监听器
     */
    public interface BitmapListener extends GlideListener {
        void onSuccess(Bitmap bitmap);
    }

    /**
     * 说明：保存图片监听器
     */
    public interface SaveImageListener extends GlideListener {
        void onSuccess(File file);
    }

}
