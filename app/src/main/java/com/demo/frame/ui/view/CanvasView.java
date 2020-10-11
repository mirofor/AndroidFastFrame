package com.demo.frame.ui.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;

import com.fast.library.utils.LogUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;

public class CanvasView extends View {

    public static final int STROKE_WIDTH = 5; // dp
    private final Paint paint = new Paint();
    private final Path path = new Path();
    private final int specW;
    private final int specH;

    public CanvasView(Context context) {
        this(context, null);
    }

    public CanvasView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHieht = getResources().getDisplayMetrics().heightPixels;
        specW = MeasureSpec.makeMeasureSpec(screenWidth, MeasureSpec.EXACTLY);
        specH = MeasureSpec.makeMeasureSpec(screenHieht, MeasureSpec.EXACTLY);
    }

    public CanvasView(Context context, AttributeSet attrs, int defAttr) {
        super(context, attrs, defAttr);
        initView();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int screenHieht = getResources().getDisplayMetrics().heightPixels;
        specW = MeasureSpec.makeMeasureSpec(screenWidth, MeasureSpec.EXACTLY);
        specH = MeasureSpec.makeMeasureSpec(screenHieht, MeasureSpec.EXACTLY);
    }

    private void initView() {
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, STROKE_WIDTH, getResources().getDisplayMetrics()));
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setAntiAlias(true);
        setBackgroundColor(Color.WHITE);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawPath(path, paint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                path.moveTo(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(x, y);
                break;
        }
        invalidate();
        return true;
    }

    public void clean() {
        path.reset();
        invalidate();
    }
//    public Bitmap getSignatureBitmap() {
//        Bitmap originalBitmap = getTransparentSignatureBitmap();
//        Bitmap whiteBgBitmap = Bitmap.createBitmap(originalBitmap.getWidth(), originalBitmap.getHeight(),
//                Bitmap.Config.ARGB_8888);
//        Canvas canvas = new Canvas(whiteBgBitmap);
//        canvas.drawColor(Color.WHITE);
//        canvas.drawBitmap(originalBitmap, 0, 0, null);
//        return whiteBgBitmap;
//    }
//    private void ensureSignatureBitmap() {
//        if (mSignatureBitmap == null) {
//            mSignatureBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
//            mSignatureBitmapCanvas = new Canvas(mSignatureBitmap);
//        }
//    }


    public Bitmap createBitmap(View v) {

        int w = v.getWidth();
        int h = v.getHeight();

        LogUtils.d("CanvasView width * height = " + w + " * " + h);
        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(bmp);
//        c.drawColor(Color.WHITE);
        c.drawColor(0x00000000);
//        c.drawBitmap(bmp, 0, 0, null);
        v.layout(0, 0, w, h);
        v.draw(c);

//        return compressScale(rotateToDegrees(bmp,-90));、
        return compressScale(bmp);

    }

    public static Bitmap rotateToDegrees(Bitmap tmpBitmap, float degrees) {
        Matrix matrix = new Matrix();
        matrix.reset();
        matrix.setRotate(degrees);
        Bitmap rBitmap = Bitmap.createBitmap(tmpBitmap, 0, 0, tmpBitmap.getWidth(), tmpBitmap.getHeight(), matrix,
                true);
        return rBitmap;
    }

    /**
     * 图片按比例大小压缩方法
     *
     * @param image （根据Bitmap图片压缩）
     * @return
     */
    public static Bitmap compressScale(Bitmap image) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);

        // 判断如果图片大于1M,进行压缩避免在生成图片（BitmapFactory.decodeStream）时溢出
        if (baos.toByteArray().length / 1024 > 1024) {
            baos.reset();// 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.PNG, 90, baos);// 这里压缩50%，把压缩后的数据存放到baos中
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        // 开始读入图片，此时把options.inJustDecodeBounds 设回true了
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap= BitmapFactory.decodeStream(isBm, null, newOpts);
        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;//原始宽高
        int h = newOpts.outHeight;

        //设置保存的手签照片大小
        float ww = 400f;//这里设置宽度为400f
        float hh = 300f;//这里设置高度为300f

        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放,缩放比为1/be
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0) {
            be = 1;
        }
        newOpts.inSampleSize = be; // 设置缩放比例
        // newOpts.inPreferredConfig = Config.RGB_565;//降低图片从ARGB888到RGB565

        // 重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了
        isBm = new ByteArrayInputStream(baos.toByteArray());
        bitmap = BitmapFactory.decodeStream(isBm, null, newOpts);

//        return compressImage(bitmap,5);// 压缩好比例大小后再进行质量压缩
        return bitmap;
    }

    /**
     * 质量压缩方法
     *
     * @param image size(kb)
     * @return
     */
    public static Bitmap compressImage(Bitmap image, int size) {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, baos);// 质量压缩方法，这里100表示不压缩，把压缩后的数据存放到baos中
        int options = 80;

        while (baos.toByteArray().length / 1024 > size) { // 循环判断如果压缩后图片是否大于size,大于继续压缩

            if (options < 10) {
                options = 10;
            }

            baos.reset(); // 重置baos即清空baos
            image.compress(Bitmap.CompressFormat.PNG, options, baos);// 这里压缩options%，把压缩后的数据存放到baos中
            options -= 10;// 每次都减少10
        }
        ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());// 把压缩后的数据baos存放到ByteArrayInputStream中
        Bitmap bitmap = BitmapFactory.decodeStream(isBm, null, null);// 把ByteArrayInputStream数据生成图片
        return bitmap;
    }
}
