package com.fast.library.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.os.Handler;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import static com.fast.library.FastFrame.getApplication;

/**
 * 说明：MediaPlayUtils
 */
public class MediaPlayUtils {

    private MediaPlayer mMediaPlayer;

    public MediaPlayUtils(Context context){
        mMediaPlayer = getMediaPlayer(context);
    }

    /**
     *  博客地址： http://blog.csdn.net/ouyang_peng/
     * 获取MediaPlayer  修复bug ( MediaPlayer: Should have subtitle controller already set )
     * </br><a href = "http://stackoverflow.com/questions/20087804/should-have-subtitle-controller-already-set-mediaplayer-error-android/20149754#20149754">
     *     参考链接</a>
     *
     *  </br> This code is trying to do the following from the hidden API
     *   <p>
     * </br> SubtitleController sc = new SubtitleController(context, null, null);
     * </br> sc.mHandler = new Handler();
     * </br> mediaplayer.setSubtitleAnchor(sc, null)</p>
     */
    private MediaPlayer getMediaPlayer(Context context) {
        MediaPlayer mediaplayer = new MediaPlayer();
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.KITKAT) {
            return mediaplayer;
        }
        try {
            Class<?> cMediaTimeProvider = Class.forName("android.media.MediaTimeProvider");
            Class<?> cSubtitleController = Class.forName("android.media.SubtitleController");
            Class<?> iSubtitleControllerAnchor = Class.forName("android.media.SubtitleController$Anchor");
            Class<?> iSubtitleControllerListener = Class.forName("android.media.SubtitleController$Listener");
            Constructor constructor = cSubtitleController.getConstructor(
                    new Class[]{Context.class, cMediaTimeProvider, iSubtitleControllerListener});
            Object subtitleInstance = constructor.newInstance(context, null, null);
            Field f = cSubtitleController.getDeclaredField("mHandler");
            f.setAccessible(true);
            try {
                f.set(subtitleInstance, new Handler());
            } catch (IllegalAccessException e) {
                return mediaplayer;
            } finally {
                f.setAccessible(false);
            }
            Method setsubtitleanchor = mediaplayer.getClass().getMethod("setSubtitleAnchor",
                    cSubtitleController, iSubtitleControllerAnchor);
            setsubtitleanchor.invoke(mediaplayer, subtitleInstance, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return mediaplayer;
    }

    /**
     * 播放
     */
    public void play(String sound){
        try {
            mMediaPlayer.reset();
            AssetFileDescriptor assetFileDescriptor = getApplication().getAssets().openFd(sound);
            mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(),assetFileDescriptor.getStartOffset(),assetFileDescriptor.getLength());
            mMediaPlayer.prepare();
            mMediaPlayer.start();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void destory(){
        try {
            mMediaPlayer.reset();
            mMediaPlayer.release();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

}
