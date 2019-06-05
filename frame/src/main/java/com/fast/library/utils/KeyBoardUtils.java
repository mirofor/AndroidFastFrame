package com.fast.library.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

/**
 * 说明：软键盘工具类
 */
public class KeyBoardUtils {

    private static int rootViewVisibleHeight;/*纪录根视图的显示高度*/

    public interface OnSoftKeyboardStateChangedListener {
        void onSoftKeyboardShow(int height);
        void onSoftKeyboardHide(int height);
    }

    /**
     * 说明：强制显示软键盘
     * @param et
     */
    public static void showSoftInput(View et) {
        if (et != null) {
            et.requestFocus();
            InputMethodManager inputManager = (InputMethodManager) et
                    .getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.showSoftInput(et, InputMethodManager.SHOW_IMPLICIT);
        }
    }

    /**
     * 说明：强制隐藏软键盘
     */
    public static void hiddenSoftInput(EditText editText) {
        if (editText != null) {
            InputMethodManager inputManager = (InputMethodManager) editText
                    .getContext()
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            if (inputManager.isActive()) {
                inputManager.hideSoftInputFromWindow(editText.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            }
        }
    }

    /**
     * 获取软键盘高度
     * @param paramActivity
     * @return
     */
    public static int getKeyboardHeight(Activity paramActivity) {
        int height = getScreenHeight(paramActivity) - getStatusBarHeight(paramActivity)
                - getAppHeight(paramActivity);
        return height;
    }

    /**
     * 屏幕分辨率高
     * @param paramActivity
     * @return
     */
    public static int getScreenHeight(Activity paramActivity) {
        Display display = paramActivity.getWindowManager().getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        return metrics.heightPixels;
    }

    /**
     * statusBar高度
     * @param paramActivity
     * @return
     */
    public static int getStatusBarHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.top;
    }

    /**
     * 可见屏幕高度
     * @param paramActivity
     * @return
     */
    public static int getAppHeight(Activity paramActivity) {
        Rect localRect = new Rect();
        paramActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(localRect);
        return localRect.height();
    }

    /**
     * 键盘是否在显示
     * @param paramActivity
     * @return
     */
    public static boolean isKeyBoardShow(Activity paramActivity) {
        int height = getScreenHeight(paramActivity) - getStatusBarHeight(paramActivity)
                - getAppHeight(paramActivity);
        return height != 0;
    }

    public static void setOnGlobalLayoutListener(final Activity activity, final OnSoftKeyboardStateChangedListener listener){
        if (listener == null || activity == null)return;
        final View rootView = activity.getWindow().getDecorView();
        rootView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                /*获取当前根视图在屏幕上显示的大小*/
                Rect r = new Rect();
                rootView.getWindowVisibleDisplayFrame(r);
                int visibleHeight = r.height();
                if (rootViewVisibleHeight == 0) {
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }
                /*根视图显示高度没有变化，可以看作软键盘显示／隐藏状态没有改变*/
                if (rootViewVisibleHeight == visibleHeight) {
                    return;
                }
                /*根视图显示高度变小超过200，可以看作软键盘显示了*/
                if (rootViewVisibleHeight - visibleHeight > 200) {
                    if (listener != null) {
                        listener.onSoftKeyboardShow(rootViewVisibleHeight - visibleHeight);
                    }
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }
                /*根视图显示高度变大超过200，可以看作软键盘隐藏了*/
                if (visibleHeight - rootViewVisibleHeight > 200) {
                    if (listener != null) {
                        listener.onSoftKeyboardHide(visibleHeight - rootViewVisibleHeight);
                    }
                    rootViewVisibleHeight = visibleHeight;
                    return;
                }
            }
        });
    }
}
