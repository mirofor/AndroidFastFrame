package com.fast.library.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import com.fast.library.utils.UIUtils;

/**
 * 说明：对话框基类
 */
public abstract class BaseDialog extends Dialog {

    private View mDialogView;
    public Context mContext;

    public BaseDialog(Context context, int themeResId) {
        super(context, themeResId);
        this.mContext = context;
        init(setDialogView());
    }

    public BaseDialog(Context context, int themeResId,int layoutId) {
        super(context, themeResId);
        this.mContext = context;
        init(layoutId);
    }

    /**
     * 说明：初始化
     */
    private void init(int layoutId) {
        initViewBefore();
        mDialogView = UIUtils.inflate(layoutId);
        if (isFullScreen()){
            getDialogView().setMinimumWidth(UIUtils.screenWidth());
            getDialogView().setMinimumHeight(UIUtils.screenHeight());
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
            addContentView(mDialogView, params);
        }else if (setParams() != null){
            addContentView(mDialogView, setParams());
        }else {
            setContentView(mDialogView);
        }
        if (addWindowAnimations() > 0){
            getWindow().setWindowAnimations(addWindowAnimations());
        }
        onInit();
    }

    public int addWindowAnimations(){
        return 0;
    }

    public boolean isFullScreen(){
        return false;
    }

    public LinearLayout.LayoutParams setParams(){
        return null;
    }

    /**
     * 说明：初始化
     */
    public abstract void onInit();

    public void initViewBefore(){}

    /**
     * 说明：设置自定义布局
     * @return
     */
    public abstract int setDialogView();

    /**
     * 说明：获取自定义布局
     * @return
     */
    public View getDialogView(){
        return mDialogView;
    }

    /**
     * <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
     */
    public void isSystemAlert(){
        getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
    }

    public void onDestroy(){
        this.mContext = null;
    }
}
