package com.fast.library.view;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Property;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.LinearInterpolator;

import com.fast.library.R;

/**
 * 说明：圈圈ProgressBar
 *
 * @author xiaomi
 */
public class CircleProgressView extends View {

    /**
     * 圈圈的粗细
     */
    private float mBorderWidth;
    /**圈圈角度动画时间*/
    private int angleAnimatorDuration;
    private int sweepAnimatorDuration;
    private int minSweepAngle;
    /**
     * 圈圈颜色
     */
    private int[] mColors;

    private boolean mRunning;

    private int mCurrentColorIndex = 0;
    private int mNextColorIndex = 1;

    private final RectF fBounds = new RectF();
    private Paint mPaint;
    private ObjectAnimator mObjectAnimatorSweep;
    private ObjectAnimator mObjectAnimatorAngle;
    private static final LinearInterpolator ANGLE_INTERPOLATOR = new LinearInterpolator();
    private static final AccelerateDecelerateInterpolator SWEEP_INTERPOLATOR = new AccelerateDecelerateInterpolator();
    private boolean mModeAppearing = true;
    private float mCurrentGlobalAngleOffset;
    private float mCurrentGlobalAngle;
    private float mCurrentSweepAngle;

    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        @SuppressLint("CustomViewStyleable") TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.FastLibCircleProgressBar, defStyleAttr, 0);
        mBorderWidth = array.getDimension(R.styleable.FastLibCircleProgressBar_FastLibborder, 4.0f);
        angleAnimatorDuration = array.getInt(R.styleable.FastLibCircleProgressBar_FastLibangleAnimationDurationMillis, 2000);
        sweepAnimatorDuration = array.getInt(R.styleable.FastLibCircleProgressBar_FastLibsweepAnimationDurationMillis, 900);
        minSweepAngle = array.getInt(R.styleable.FastLibCircleProgressBar_FastLibminSweepAngle, 30);
        int colorArrayId = array.getResourceId(R.styleable.FastLibCircleProgressBar_FastLibcolorSequence, R.array.FastLib_circle_default_color_sequence);
        if (isInEditMode()) {
            mColors = new int[4];
            mColors[0] = getResources().getColor(R.color.FastLib_circular_blue);
            mColors[1] = getResources().getColor(R.color.FastLib_circular_green);
            mColors[2] = getResources().getColor(R.color.FastLib_circular_red);
            mColors[3] = getResources().getColor(R.color.FastLib_circular_yellow);
        } else {
            mColors = getResources().getIntArray(colorArrayId);
        }
        array.recycle();
        mCurrentColorIndex = 0;
        mNextColorIndex = 1;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        //画出图形是空心的
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        //图形的粗细程度
        mPaint.setStrokeWidth(mBorderWidth);
        mPaint.setColor(mColors[mCurrentColorIndex]);

        setupAnimations();
    }

    @Override
    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (visibility == VISIBLE) {
            start();
        } else {
            stop();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        float startAngle = mCurrentGlobalAngle - mCurrentGlobalAngleOffset;
        float sweepAngle = mCurrentSweepAngle;
        if (mModeAppearing) {
            mPaint.setColor(gradient(mColors[mCurrentColorIndex], mColors[mNextColorIndex], mCurrentSweepAngle / (360 - minSweepAngle * 2)));
            sweepAngle += minSweepAngle;
        } else {
            startAngle = startAngle + sweepAngle;
            sweepAngle = 360 - sweepAngle - minSweepAngle;
        }
        canvas.drawArc(fBounds, startAngle, sweepAngle, false, mPaint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        fBounds.left = mBorderWidth / 2f + .5f;
        fBounds.right = w - mBorderWidth / 2f - .5f;
        fBounds.top = mBorderWidth / 2f + .5f;
        fBounds.bottom = h - mBorderWidth / 2f - .5f;
    }

    /**
     * 说明：设置圈圈颜色
     *
     * @param colors
     */
    public void setCircleColos(int... colors) {
        if (colors != null && colors.length > 0) {
            switch (colors.length) {
                case 1:
                    mColors[0] = getResources().getColor(colors[0]);
                    mColors[1] = getResources().getColor(colors[0]);
                    mColors[2] = getResources().getColor(colors[0]);
                    mColors[3] = getResources().getColor(colors[0]);
                    break;
                case 2:
                    mColors[0] = getResources().getColor(colors[0]);
                    mColors[1] = getResources().getColor(colors[1]);
                    mColors[2] = getResources().getColor(colors[0]);
                    mColors[3] = getResources().getColor(colors[0]);
                    break;
                case 3:
                    mColors[0] = getResources().getColor(colors[0]);
                    mColors[1] = getResources().getColor(colors[1]);
                    mColors[2] = getResources().getColor(colors[2]);
                    mColors[3] = getResources().getColor(colors[0]);
                    break;
                case 4:
                    mColors[0] = getResources().getColor(colors[0]);
                    mColors[1] = getResources().getColor(colors[1]);
                    mColors[2] = getResources().getColor(colors[2]);
                    mColors[3] = getResources().getColor(colors[3]);
                    break;
                default:
                    break;
            }
        }
    }

    private static int gradient(int color1, int color2, float p) {
        int r1 = (color1 & 0xff0000) >> 16;
        int g1 = (color1 & 0xff00) >> 8;
        int b1 = color1 & 0xff;
        int r2 = (color2 & 0xff0000) >> 16;
        int g2 = (color2 & 0xff00) >> 8;
        int b2 = color2 & 0xff;
        int newr = (int) (r2 * p + r1 * (1 - p));
        int newg = (int) (g2 * p + g1 * (1 - p));
        int newb = (int) (b2 * p + b1 * (1 - p));
        return Color.argb(255, newr, newg, newb);
    }

    @Override
    protected void onAttachedToWindow() {
        start();
        super.onAttachedToWindow();
    }

    @Override
    protected void onDetachedFromWindow() {
        stop();
        super.onDetachedFromWindow();
    }

    private boolean isRunning() {
        return mRunning;
    }

    /**
     * 说明：开始显示
     */
    private void start() {
        if (isRunning()) {
            return;
        }
        mRunning = true;
        mObjectAnimatorAngle.start();
        mObjectAnimatorSweep.start();
        invalidate();
    }

    /**
     * 说明：停止显示
     */
    private void stop() {
        if (!isRunning()) {
            return;
        }
        mRunning = false;
        mObjectAnimatorAngle.cancel();
        mObjectAnimatorSweep.cancel();
        invalidate();
    }

    /**
     * 说明：设置动画
     */
    private void setupAnimations() {
        mObjectAnimatorAngle = ObjectAnimator.ofFloat(this, mAngleProperty, 360f);
        mObjectAnimatorAngle.setInterpolator(ANGLE_INTERPOLATOR);
        mObjectAnimatorAngle.setDuration(angleAnimatorDuration);
        mObjectAnimatorAngle.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimatorAngle.setRepeatMode(ValueAnimator.RESTART);

        mObjectAnimatorSweep = ObjectAnimator.ofFloat(this, mSweepProperty, 360f - minSweepAngle * 2);
        mObjectAnimatorSweep.setInterpolator(SWEEP_INTERPOLATOR);
        mObjectAnimatorSweep.setDuration(sweepAnimatorDuration);
        mObjectAnimatorSweep.setRepeatMode(ValueAnimator.RESTART);
        mObjectAnimatorSweep.setRepeatCount(ValueAnimator.INFINITE);
        mObjectAnimatorSweep.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                toggleAppearingMode();
            }
        });
    }

    private void toggleAppearingMode() {
        mModeAppearing = !mModeAppearing;
        if (mModeAppearing) {
            mCurrentColorIndex = ++mCurrentColorIndex % mColors.length;
            mNextColorIndex = ++mNextColorIndex % mColors.length;
            mCurrentGlobalAngleOffset = (mCurrentGlobalAngleOffset + minSweepAngle * 2) % 360;
        }
    }

    private Property<CircleProgressView, Float> mSweepProperty = new Property<CircleProgressView, Float>(Float.class, "arc") {
        @Override
        public Float get(CircleProgressView object) {
            return object.getCurrentSweepAngle();
        }

        @Override
        public void set(CircleProgressView object, Float value) {
            object.setCurrentSweepAngle(value);
        }
    };

    private Property<CircleProgressView, Float> mAngleProperty = new Property<CircleProgressView, Float>(Float.class, "angle") {
        @Override
        public Float get(CircleProgressView object) {
            return object.getCurrentGlobalAngle();
        }

        @Override
        public void set(CircleProgressView object, Float value) {
            object.setCurrentGlobalAngle(value);
        }
    };

    public void setCurrentSweepAngle(float currentSweepAngle) {
        mCurrentSweepAngle = currentSweepAngle;
        invalidate();
    }

    public float getCurrentSweepAngle() {
        return mCurrentSweepAngle;
    }

    public float getCurrentGlobalAngle() {
        return mCurrentGlobalAngle;
    }

    public void setCurrentGlobalAngle(float currentGlobalAngle) {
        mCurrentGlobalAngle = currentGlobalAngle;
        invalidate();
    }
}
