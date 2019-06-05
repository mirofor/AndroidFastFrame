package com.demo.frame.ui.activity;

import android.view.View;
import android.widget.TextView;

import com.demo.frame.R;
import com.demo.frame.ui.ActivityCommon;
import com.fast.library.ui.ContentView;
import com.vondear.rxtool.view.RxToast;

import butterknife.OnClick;

@ContentView(R.layout.activity_smart_start_login)
public class ActivityStartLogin extends ActivityCommon {

    TextView mTvBtn;
    @Override

    @OnClick({R.id.btn_start_login, R.id.btn_start_register})
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_start_login:// 登录
                RxToast.success("登录");
                mTvBtn.setText("ddd");
                break;
            case R.id.btn_start_register://注册
                RxToast.success("注册");
                break;

            default:
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }


}
