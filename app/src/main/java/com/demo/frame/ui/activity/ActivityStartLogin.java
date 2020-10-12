package com.demo.frame.ui.activity;

import android.view.View;

import com.demo.frame.R;
import com.demo.frame.helper.RouterHelper;
import com.demo.frame.ui.ActivityCommon;
import com.fast.library.ui.ContentView;
import com.fast.library.view.RoundButton;
import com.vondear.rxtool.view.RxToast;

import androidx.annotation.Nullable;
import butterknife.BindView;
import butterknife.OnClick;

/**
 * @author xiaomi
 */
@ContentView(R.layout.activity_smart_start_login)
public class ActivityStartLogin extends ActivityCommon {

    @Nullable
    @BindView(R.id.btn_start_login)
    RoundButton mBtnLogin;

    @Override
    @OnClick({R.id.btn_start_login, R.id.btn_start_register})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_start_login:
                RxToast.success("登录");

                RouterHelper.startHomePage(this);
                break;
            case R.id.btn_start_register:
                RxToast.success("注册");
                break;

            default:
                break;
        }
    }


}
