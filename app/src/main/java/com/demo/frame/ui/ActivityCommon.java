package com.demo.frame.ui;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.Loader;
import android.text.TextUtils;

import com.demo.frame.R;
import com.vondear.rxtool.view.RxToast;


/**
 * 说明：ActivityCommon
 */
public abstract class ActivityCommon extends ActivityBase {

    @Override
    public Loader onCreateLoader(int id, Bundle args) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader loader, Object data) {

    }

    private String resolvePermission(String permission) {
        if (TextUtils.isEmpty(permission))
            throw new RuntimeException("oh,permission is empty?");
        String result;
        if (permission.equals(Manifest.permission.CAMERA)) {
            result = getString(R.string.permission_CAMERA);
        } else if (permission.equals(Manifest.permission.CALL_PHONE)) {
            result = getString(R.string.permission_CALL_PHONE);
        } else if (permission.equals(Manifest.permission.RECORD_AUDIO)) {
            result = getString(R.string.permission_RECORD_AUDIO);
        } else if (permission.equals(Manifest.permission.ACCESS_FINE_LOCATION)) {
            result = getString(R.string.permission_ACCESS_FINE_LOCATION);
        } else if (permission.equals(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            result = getString(R.string.permission_ACCESS_COARSE_LOCATION);
        } else if (permission.equals(Manifest.permission.READ_CONTACTS)) {
            result = getString(R.string.permission_READ_CONTACTS);
        } else if (permission.equals(Manifest.permission.SEND_SMS)) {
            result = getString(R.string.permission_SEND_SMS);
        } else if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
            result = getString(R.string.permission_READ_EXTERNAL_STORAGE);
        } else if (permission.equals(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            result = getString(R.string.permission_WRITE_EXTERNAL_STORAGE);
        } else {
            result = "";
        }
        return result;
    }

    /**
     * 跳转到设置界面（不确保所有机型成功，失败会有提示）
     */
    protected void toSetPermission() {
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        try {
            startActivity(intent);
            RxToast.success(getString(R.string.permission_setting_tip));
        } catch (Exception e) {
            e.printStackTrace();
            RxToast.success(getString(R.string.permission_cannot_go_to_setting));
        }
    }
}
