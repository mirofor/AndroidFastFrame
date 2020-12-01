package com.demo.frame.ui.activity;

import android.content.Intent;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.frame.R;
import com.demo.frame.helper.RouterHelper;
import com.demo.frame.ui.ActivityCommon;
import com.fast.library.tools.ViewTools;
import com.fast.library.ui.ContentView;
import com.fast.library.utils.AndroidInfoUtils;
import com.fast.library.utils.LogUtils;
import com.fast.library.view.RoundButton;

import androidx.annotation.Nullable;
import butterknife.BindView;
import me.salmonzhg.easypermission.EasyPermission;

@ContentView(R.layout.activity_smart_start)
public class ActivityStart extends ActivityCommon {

    @Nullable
    @BindView(R.id.iv_img_start)
    ImageView ivBgImg;

    // 版本号
    @Nullable
    @BindView(R.id.tv_start_version_name)
    TextView tvVersionName;

    @Nullable
    @BindView(R.id.rb_skip)
    RoundButton rbSkip;

    String pushExtra;

    private int ss1 = 3;
    private Handler handler = new Handler();
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            ss1 -= 1;
//            String text = String.format("跳过%d秒", ss1);
//            ViewTools.setText(rbSkip, text);
            if (ss1 == 0) {
                RouterHelper.startLoginTip(ActivityStart.this);
                finish();
            } else {
                handler.postDelayed(runnable, 1000);
            }
        }
    };

    @Override
    public void getIntentData(Intent intent) {
        super.getIntentData(intent);
        pushExtra = getIntent().getStringExtra("aliReceiverExtra");
    }

    @Override
    public void onInitStart() {
        super.onInitStart();
        LogUtils.e("【ActivityStart onInitStart】");
        EasyPermission.initialize(this);
        ViewTools.setText(tvVersionName, AndroidInfoUtils.versionName());
        rbSkip.setOnClickListener(v -> {

            RouterHelper.startLoginTip(this);
            finish();
        });
        handlerAction();

    }

    private void handlerAction() {
        handler.postDelayed(runnable, 0);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (handler != null && runnable != null) {
            handler.removeCallbacks(runnable);
        }
    }
}
