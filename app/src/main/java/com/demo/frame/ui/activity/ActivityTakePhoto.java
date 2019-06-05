package com.demo.frame.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.fast.frame.event.EventCenter;
import com.fast.library.utils.FileUtils;
import com.fast.library.utils.StringUtils;
import com.demo.frame.utils.XContant;

import org.devio.takephoto.app.TakePhoto;
import org.devio.takephoto.app.TakePhotoActivity;
import org.devio.takephoto.compress.CompressConfig;
import org.devio.takephoto.model.CropOptions;
import org.devio.takephoto.model.LubanOptions;
import org.devio.takephoto.model.TImage;
import org.devio.takephoto.model.TResult;
import org.devio.takephoto.model.TakePhotoOptions;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;

/**
 * 说明：ActivityTakePhoto
 */
public class ActivityTakePhoto extends TakePhotoActivity {

    private static final String TAKE_PHOTO_PATH = "take_photo_path";//图片上传选择
    public static final String ID_AUTH_USER_INFO = "auth_user_info";
    public static final String ID_HAND_SIGNATURE = "ID_HAND_SIGNATURE";
    public static final String ID_USER_SIGNATURE = "ID_USER_SIGNATURE"; // 用户签署文件图片

    public static final String DO_SIGNING_PIC_ONE = "DO_SIGNING_PICC_ONE"; // 用户签署文件第一张照片
    public static final String DO_SIGNING_PIC_TWO = "DO_SIGNING_PICC_TWO"; // 用户签署文件第二张照片
    public static final String DO_SIGNING_PIC_THREE = "DO_SIGNING_PICC_THREE"; // 用户签署文件第三张照片

    private static final String PHOTO_TYPE = "photoType";
    private static final String PHOTO_ID = "photoID";

    private static final int TYPE_TAKE = 0x111;//拍照
    private static final int TYPE_SELECTED = 0x112;//选择

    private TakePhoto mPhoto;
    private int type;
    private String id;

    public static void take(Context context, String id) {
        start(context, TYPE_TAKE, id);
    }

    public static void select(Context context, String id) {
        start(context, TYPE_SELECTED, id);
    }

    private static void start(Context context, int type, String id) {
        Intent intent = new Intent(context, ActivityTakePhoto.class);
        intent.putExtra(PHOTO_TYPE, type);
        intent.putExtra(PHOTO_ID, id);
        context.startActivity(intent);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (mPhoto != null) {
            mPhoto.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPhoto = getTakePhoto();
        mPhoto.onCreate(savedInstanceState);

        type = getIntent().getIntExtra(PHOTO_TYPE, TYPE_SELECTED);
        id = getIntent().getStringExtra(PHOTO_ID);

        File file = getUploadPicCacheFile();
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        Uri imageUri = Uri.fromFile(file);

//        configCompress(mPhoto);
        configLuBanCompress(mPhoto);
        configTakePhotoOption(mPhoto);

        switch (type) {
            case TYPE_TAKE:
                if (id.equals(ID_AUTH_USER_INFO) || id.equals(ID_HAND_SIGNATURE)) {
                    mPhoto.onPickFromCaptureWithCrop(imageUri, getAuthImgCropOptions());
                } else if (id.equals(DO_SIGNING_PIC_ONE) || id.equals(DO_SIGNING_PIC_TWO) || id.equals(DO_SIGNING_PIC_THREE)) {
                    mPhoto.onPickFromCaptureWithCrop(imageUri, getSignUserFaceCropOptions());
                } else {
//                    mPhoto.onPickFromCapture(imageUri);
                    mPhoto.onPickFromCaptureWithCrop(imageUri, getCropOptions());
                }
                break;
            case TYPE_SELECTED:
                if (id.equals(ID_AUTH_USER_INFO) || id.equals(ID_HAND_SIGNATURE)) {
                    mPhoto.onPickFromGalleryWithCrop(imageUri, getAuthImgCropOptions());
                } else if (id.equals(DO_SIGNING_PIC_ONE) || id.equals(DO_SIGNING_PIC_TWO) || id.equals(DO_SIGNING_PIC_THREE)) {
                    mPhoto.onPickFromGalleryWithCrop(imageUri, getSignUserFaceCropOptions());
                } else {
                    mPhoto.onPickFromGalleryWithCrop(imageUri, getCropOptions());
//                    mPhoto.onPickFromGallery();
                }
                break;
            default:
                finish();
                break;
        }
    }

    /**
     * 拍照设置
     *
     * @param takePhoto
     */
    private void configTakePhotoOption(TakePhoto takePhoto) {
        TakePhotoOptions.Builder builder = new TakePhotoOptions.Builder();
        builder.setCorrectImage(true);
        takePhoto.setTakePhotoOptions(builder.create());

    }

    /**
     * 压缩信息配置
     *
     * @param takePhoto
     */
    private void configCompress(TakePhoto takePhoto) {
        int maxSize = 1024 * 300;//压缩到的最大大小，单位B
        int maxPixel = 1400; //长或宽不超过的最大像素,单位px
        CompressConfig config = new CompressConfig.Builder().setMaxSize(maxSize).setMaxPixel(maxPixel).create();
        takePhoto.onEnableCompress(config, false);

    }

    private void configLuBanCompress(TakePhoto takePhoto) {
        int maxSize = 1024 * 300;//压缩到的最大大小，单位B
        CompressConfig config;

        LubanOptions option = new LubanOptions.Builder().setMaxHeight(1000).setMaxWidth(800).setMaxSize(maxSize).create();
        config = CompressConfig.ofLuban(option);

        takePhoto.onEnableCompress(config, false);
    }


    /**
     * 签署头像照片
     */
    private CropOptions getSignUserFaceCropOptions() {
        int height = 1000;
        int width = 800;
        boolean withWonCrop = false;
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setAspectX(width).setAspectY(height);
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }

    /**
     * 剪裁配置
     */
    private CropOptions getCropOptions() {
        int height = 1200;
        int width = 1400;
        boolean withWonCrop = false;
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setAspectX(width).setAspectY(height);
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }

    private CropOptions getAuthImgCropOptions() {
        int height = 300;
        int width = 300;
        boolean withWonCrop = false;
        CropOptions.Builder builder = new CropOptions.Builder();
        builder.setAspectX(width).setAspectY(height);
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
        finish();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        finish();
    }


    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
        ArrayList<TImage> images = result.getImages();
        if (images != null && !images.isEmpty()) {
            EventBus.getDefault().post(new EventCenter<>(TAKE_PHOTO_PATH, id, new File(images.get(0).getCompressPath())));
        }
        finish();
    }

    public static boolean isTakePhotoType(EventCenter<File> eventCenter, String id) {
        return TAKE_PHOTO_PATH.equals(eventCenter.type) && StringUtils.isEquals(id, eventCenter.what);
    }

    /**
     * 获取上传图片缓存文件
     *
     * @return
     */
    public static File getUploadPicCacheFile() {
        return new File(XContant.UPLOAD_IMAGE_CACHE + System.currentTimeMillis() + ".jpg");
    }

    public static void cleanCache() {
        File file = new File(XContant.UPLOAD_IMAGE_CACHE);
        FileUtils.deleteAllFile(file);
    }
}