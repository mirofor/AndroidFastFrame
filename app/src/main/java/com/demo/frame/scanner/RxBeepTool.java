package com.demo.frame.scanner;


import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;

import com.demo.frame.R;
import com.vondear.rxtool.RxVibrateTool;

import java.io.IOException;


/**
 * Created by aimi on 2018/5/2.
 */
@SuppressWarnings("deprecation")
public class RxBeepTool {
    private static final float BEEP_VOLUME = 0.5F;
    private static final int VIBRATE_DURATION = 100;
    private static boolean playBeep = false;
    private static MediaPlayer mediaPlayer;

    public RxBeepTool() {
    }

    public static void playBeep(Activity mContext, boolean vibrate) {
        playBeep = true;
        AudioManager audioService = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        if (audioService.getRingerMode() != 2) {
            playBeep = false;
        }

        if (playBeep && mediaPlayer != null) {
            mediaPlayer.start();
        } else {
            mContext.setVolumeControlStream(3);
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setAudioStreamType(3);
            mediaPlayer.setOnCompletionListener(new OnCompletionListener() {
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mediaPlayer.seekTo(0);
                }
            });
            AssetFileDescriptor file = mContext.getResources().openRawResourceFd(R.raw.beep);

            try {
                mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
                file.close();
                mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
                mediaPlayer.prepare();
            } catch (IOException var5) {
                mediaPlayer = null;
            }
        }

        if (vibrate) {
            RxVibrateTool.vibrateOnce(mContext, VIBRATE_DURATION);
        }
    }
}
