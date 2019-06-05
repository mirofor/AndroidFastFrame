package com.fast.library.glide;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;

import java.io.ByteArrayOutputStream;

/**
 * 说明：位图工具
 */
public class BitmapUtils {

    /**
     * 说明：图片圆角
     * @param source
     * @param radius
     * @return
     */
    public static Bitmap roundCorners(final Bitmap source, final float radius) {
        if (source == null){
            throw new NullPointerException("BitmapUtils 处理的 source 为null!");
        }
        int width = source.getWidth();
        int height = source.getHeight();
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);
        Bitmap clipped = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(clipped);
        canvas.drawRoundRect(new RectF(0, 0, width, height), radius, radius, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(source, 0, 0, paint);
        source.recycle();
        return clipped;
    }

    /**
     * 说明：圆形图片
     * @param source
     * @return
     */
    public static Bitmap circleBitmap(Bitmap source) {
        if (source == null){
            throw new NullPointerException("BitmapUtils 处理的 source 为null!");
        }
        // 获取位图尺寸
        int width = source.getWidth();
        int height = source.getHeight();
        // 计算画布边长 取宽和高较小的一个值 并把该值的一半作为圆的半径
        // 原因自己想吧
        int sLenght = Math.min(width, height);
        int radius = sLenght / 2;
        Bitmap target = Bitmap.createBitmap(sLenght, sLenght, Bitmap.Config.ARGB_8888);
        // 根据target创建一块画布
        Canvas canvas = new Canvas(target);
        Paint paint = new Paint();
        paint.setAntiAlias(true);
        // 绘制圆
        canvas.drawCircle(radius, radius, radius, paint);
        // 绘制模式 16 相关资料 Xfermode
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        // 绘制位图
        canvas.drawBitmap(source, 0, 0, paint);
        // 返回经过处理以后的位图
        return target;
    }

    /**
     * 说明：资源文件->bitmap
     * @param context
     * @param resId
     * @return
     */
    public static Bitmap bitmap(Context context,int resId){
        return BitmapFactory.decodeResource(context.getResources(),resId);
    }

    /**
     * 说明：回收Bitmap
     * @param bitmaps
     */
    public static void recycle(Bitmap... bitmaps){
        if (bitmaps == null){
            return;
        }
        for (Bitmap bitmap : bitmaps){
            if (bitmap != null){
                bitmap.recycle();
            }
        }
    }

    /**
     * 说明：bitmap->byte[]
     * @param bitmap
     * @param needRecyle 是否需要回收
     * @return
     */
    public static byte[] bmpToByteArray(Bitmap bitmap,boolean needRecyle){
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, output);
        if (needRecyle) {
            recycle(bitmap);
        }

        byte[] result = output.toByteArray();
        try {
            output.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

}
