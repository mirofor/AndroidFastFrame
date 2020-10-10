package com.fast.library.ui;

import android.app.Activity;

import com.fast.library.utils.ToolUtils;

import java.lang.reflect.Method;

import androidx.fragment.app.Fragment;

/**
 * 说明：注解工具类[Activity,Fragment]
 */

public class AnnotateViewUtils {

    /**
     * 说明：activity绑定view
     * @param activity
     */
    private static String initContentView(Activity activity){
        Class<? extends Activity> clazz = activity.getClass();
        ContentView bindView = clazz.getAnnotation(ContentView.class);
        String response = "";
        if (bindView != null){
            response = "";
            int layoutId = bindView.value();
            try {
                Method method = clazz.getMethod("setContentView",int.class);
                method.setAccessible(true);
                method.invoke(activity,layoutId);
            }catch (Exception e){
                response = ToolUtils.collectErrorInfo(e);
            }
        }
        return response;
    }

    /**
     * 说明：fragment绑定view
     * @param fragment
     */
    private static String initContentView(SupportFragment fragment){
        Class<? extends Fragment> clazz = fragment.getClass();
        ContentView bindView = clazz.getAnnotation(ContentView.class);
        String response = "";
        if (bindView != null){
            response = "";
            int layoutId = bindView.value();
            try {
                Method method = clazz.getMethod("setRootViewResID",int.class);
                method.setAccessible(true);
                method.invoke(fragment,layoutId);
            }catch (Exception e){
                response = ToolUtils.collectErrorInfo(e);
            }
        }
        return response;
    }

    /**
     * @param currentClass
     *            当前类，一般为Activity或Fragment
     */
    public static String init(Object currentClass) {
        if (currentClass instanceof Activity){
            return initContentView((Activity) currentClass);
        }else if (currentClass instanceof SupportFragment){
            return initContentView((SupportFragment) currentClass);
        }else {
            throw new IllegalArgumentException(currentClass + " must be Activity or SupportFragment or FrameFragment!!!");
        }
    }

}

