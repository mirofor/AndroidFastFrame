package com.fast.library.view;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.AttributeSet;

import com.fast.library.R;

import androidx.annotation.ColorRes;
import androidx.appcompat.widget.AppCompatTextView;

/**
 * 说明：RoundButton
 */
public class RoundButton extends AppCompatTextView {

    private ColorStateList solidColor,pressedColor,disEnableColor;
    private RoundDrawable rd;

    public RoundButton(Context context) {
        this(context,null);
    }

    public RoundButton(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public RoundButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.Frame_RoundButton);
        boolean isCircle = array.getBoolean(R.styleable.Frame_RoundButton_rbCircle,false);
        pressedColor = array.getColorStateList(R.styleable.Frame_RoundButton_rbPressedColor);
        disEnableColor = array.getColorStateList(R.styleable.Frame_RoundButton_rbDisEnableColor);
        solidColor = array.getColorStateList(R.styleable.Frame_RoundButton_rbSolidColor);
        ColorStateList textColor = array.getColorStateList(R.styleable.Frame_RoundButton_rbTextColor);
        ColorStateList textPressedColor = array.getColorStateList(R.styleable.Frame_RoundButton_rbTextPressedColor);
        int cornerRadius = array.getDimensionPixelSize(R.styleable.Frame_RoundButton_rbCornerRadius,0);
        int strokeColor = array.getColor(R.styleable.Frame_RoundButton_rbStrokeColor,0x0);
        int strokeWidth = array.getDimensionPixelSize(R.styleable.Frame_RoundButton_rbStrokeWidth,0);
        int strokeDashWidth = array.getDimensionPixelSize(R.styleable.Frame_RoundButton_rbStrokeDashWidth,0);
        int strokeDashGap = array.getDimensionPixelSize(R.styleable.Frame_RoundButton_rbStrokeDashGap,0);
//        圆角
        int leftTopCorner = array.getDimensionPixelSize(R.styleable.Frame_RoundButton_rbLeftTopCorner,0);
        int rightTopCorner = array.getDimensionPixelSize(R.styleable.Frame_RoundButton_rbRightTopCorner,0);
        int leftBottomCorner = array.getDimensionPixelSize(R.styleable.Frame_RoundButton_rbLeftBottomCorner,0);
        int rightBottomCorner = array.getDimensionPixelSize(R.styleable.Frame_RoundButton_rbRightBottomCorner,0);

        array.recycle();

        rd = new RoundDrawable(isCircle);

        rd.setStroke(strokeWidth,strokeColor,strokeDashWidth,strokeDashGap);
//        设置背景颜色
        setSolidColors(isEnabled());
//        设置文字颜色
        if (textColor == null){
            textColor = ColorStateList.valueOf(0);
        }
        if (textColor.isStateful()){
            setTextColor(textColor);
        }else if (textPressedColor != null){
            setTextColor(createStateListColor(textColor,textPressedColor));
        }else {
            setTextColor(textColor.getDefaultColor());
        }
//        设置圆角，1，如果设置圆形，四个角无效
        if (!isCircle){
            if (cornerRadius != 0){
                rd.setCornerRadius(cornerRadius);
            }else {
                rd.setCornerRadii(new float[]{leftTopCorner,leftTopCorner,rightTopCorner,rightTopCorner,
                        rightBottomCorner,rightBottomCorner,leftBottomCorner,leftBottomCorner});
            }
        }
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (rd != null){
            setSolidColors(enabled);
        }
    }

    /**
     * 设置四个角度
     * @param leftTopCorner
     * @param rightTopCorner
     * @param rightBottomCorner
     * @param leftBottomCorner
     */
    public void setCorner(float leftTopCorner,float rightTopCorner,float rightBottomCorner,float leftBottomCorner){
        rd.setCornerRadii(new float[]{leftTopCorner,leftTopCorner,rightTopCorner,rightTopCorner,
                rightBottomCorner,rightBottomCorner,leftBottomCorner,leftBottomCorner});
    }

    public void setSolidColor(@ColorRes int color){
        solidColor = getResources().getColorStateList(color);
        setSolidColors(isEnabled());
    }

    public void setPressedColor(@ColorRes int color){
        pressedColor = getResources().getColorStateList(color);
        setSolidColors(isEnabled());
    }

    private void setSolidColors(boolean enabled){
        if (solidColor == null){
            solidColor = ColorStateList.valueOf(0);
        }
        if (enabled){
            if (solidColor.isStateful()){
                rd.setSolidColors(solidColor);
            }else if (pressedColor != null){
                rd.setSolidColors(createStateListColor(solidColor,pressedColor));
            }else {
                rd.setColor(solidColor.getDefaultColor());
            }
        }else {
            disEnableColor = disEnableColor == null ? solidColor : disEnableColor;
            rd.setColor(disEnableColor.getDefaultColor());
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN){
            setBackground(rd);
        }else {
            setBackgroundDrawable(rd);
        }
    }

    private ColorStateList createStateListColor(ColorStateList normal, ColorStateList pressed){
        int[][] states = new int[][]{{android.R.attr.state_pressed},{}};
        int[] colors = new int[]{pressed.getDefaultColor(),normal.getDefaultColor()};
        return new ColorStateList(states,colors);
    }

    private class RoundDrawable extends GradientDrawable{
        boolean isCircle;

        private ColorStateList mSolidColors;
        private int mFillColor;
        public RoundDrawable(boolean circle){
            this.isCircle = circle;
        }
        public void setSolidColors(ColorStateList colors){
            mSolidColors = colors;
            setColor(colors.getDefaultColor());
        }

        @Override
        protected void onBoundsChange(Rect r) {
            super.onBoundsChange(r);
            if (isCircle){//圆形
                RectF rectF = new RectF(getBounds());
                setCornerRadius(((rectF.height() > rectF.width() ? rectF.width() : rectF.height())) / 2);
            }
        }

        @Override
        public void setColor(int argb) {
            mFillColor = argb;
            super.setColor(argb);
        }

        @Override
        protected boolean onStateChange(int[] stateSet) {
            if (mSolidColors != null){
                int newColor = mSolidColors.getColorForState(stateSet,0);
                if (mFillColor != newColor){
                    setColor(newColor);
                    return true;
                }
            }
            return false;
        }

        @Override
        public boolean isStateful() {
            return super.isStateful() || (mSolidColors != null && mSolidColors.isStateful());
        }
    }

}
