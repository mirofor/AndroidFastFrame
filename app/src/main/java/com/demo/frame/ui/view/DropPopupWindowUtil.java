package com.demo.frame.ui.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.PopupWindow;

/**
 * Created by aimi on 2018/6/7.
 */
public class DropPopupWindowUtil {
    public DropPopupWindowUtil() {
    }

    public static PopupWindow showAsDropDown(Activity activity, View contentView, float widthScale, float heightScale, View anchor, int xoff, int yoff) {
        DisplayMetrics outMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int mScreenHeight = outMetrics.heightPixels;
        int mScreenWidth = outMetrics.widthPixels;
        PopupWindow popupWindow = new PopupWindow(contentView, (int) ((float) mScreenWidth * widthScale), (int) ((float) mScreenHeight * heightScale), true);
        popupWindow.setTouchable(true);
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        popupWindow.setOutsideTouchable(true);
        popupWindow.showAsDropDown(anchor, xoff, yoff);
        return popupWindow;
    }

    public static PopupWindow showAsDropDown(Activity activity, View contentView, float widthScale, float heightScale, View anchor) {
        return showAsDropDown(activity, contentView, widthScale, heightScale, anchor, 0, 0);
    }
}
