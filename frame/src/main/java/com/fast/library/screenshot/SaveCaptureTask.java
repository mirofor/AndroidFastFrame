package com.fast.library.screenshot;

import android.annotation.TargetApi;
import android.graphics.Bitmap;
import android.media.Image;
import android.os.AsyncTask;
import android.os.Build;

import java.io.File;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;

/**
 * 说明：保存截图
 */
@TargetApi(Build.VERSION_CODES.LOLLIPOP)
public class SaveCaptureTask extends AsyncTask<Image, Void, File>{

    private OnCaptureListener onCaptureListener;
    private IScreenCaptureFile screenCaptureFile;

    public SaveCaptureTask(IScreenCaptureFile screenCaptureFile,OnCaptureListener listener){
        this.onCaptureListener = listener;
        this.screenCaptureFile = screenCaptureFile;
    }

    @Override
    protected File doInBackground(Image... params) {
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
                fileImage = screenCaptureFile.createScreenShotsFile();
                if (!fileImage.exists()) {
                    fileImage.createNewFile();
                }
                FileOutputStream out = new FileOutputStream(fileImage);
                if (out != null) {
                    bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                    out.flush();
                    out.close();
                    ScreenCaptureUtils.refreshPic(fileImage);
                }
            } catch (Exception e) {
                e.printStackTrace();
                fileImage = null;
            }
        }
        if (fileImage != null) {
            return fileImage;
        }
        return null;
    }

    @Override
    protected void onPostExecute(File file) {
        super.onPostExecute(file);
        if (onCaptureListener != null){
            if (file != null){
                onCaptureListener.onCaptureSuccesss(file);
            }else {
                onCaptureListener.onCaptureError();
            }
            onCaptureListener.onFinishCapture();
        }
    }
}
