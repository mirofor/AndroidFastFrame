package com.fast.library.utils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.security.MessageDigest;

/**
 * 说明：MD5工具类
 */

public final class MD5 {

    private MD5() {
    }

    public static String getMD5(String content) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(content.getBytes());
            return getHasnString(digest);
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(content + "转MD5异常");
        }
        return null;
    }

    public static String getMD5Num(String content,int count) {
        try {
            if (count<=0) {
                LogUtils.e("加密次数必须大于0");
            }else {
                String md5 = content;
                for (int i = 0; i < count; i++) {
                    md5 = getMD5(md5);
                }
                return md5;
            }
        } catch (Exception e) {
            e.printStackTrace();
            LogUtils.e(content + "转MD5异常");
        }
        return null;
    }

    private static String getHasnString(MessageDigest digest) {
        StringBuilder builder = new StringBuilder();
        for (byte b : digest.digest()) {
            builder.append(Integer.toHexString((b >> 4) & 0xf));
            builder.append(Integer.toHexString(b & 0xf));
        }
        return builder.toString();
    }

    public static String getFileMd5(File file){
        String md5 = null;
        if (file != null && file.exists() && file.isFile()){
            FileInputStream in = null;
            FileChannel ch = null;
            try {
                in = new FileInputStream(file);
                ch =in.getChannel();
                MappedByteBuffer byteBuffer =ch.map(FileChannel.MapMode.READ_ONLY, 0,file.length());
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                messageDigest.update(byteBuffer);
                md5 = byteArrayToHex (messageDigest.digest());
            } catch (Exception e) {
                e.printStackTrace();
                LogUtils.e(file.getAbsolutePath() + "获取MD5异常");
            }finally {
                FileUtils.closeIO(ch,in);
            }
        }
        return md5;
    }

    private static String byteArrayToHex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1) {
                hs = hs + "0" + stmp;
            } else {
                hs = hs + stmp;
            }
            if (n < b.length - 1) {
                hs = hs + "";
            }
        }
        return hs;
    }
}

