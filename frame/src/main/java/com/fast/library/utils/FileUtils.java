package com.fast.library.utils;

import android.app.Activity;
import android.content.Context;
import android.content.CursorLoader;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 说明：文件与流处理工具类
 */
public final class FileUtils {

    /**
     * 说明：将文件保存到本地
     *
     * @param fileData   保存数据
     * @param folderPath 保存路径
     * @param fileName 文件名称
     */
    public static void saveFileCache(byte[] fileData, String folderPath, String fileName) {
        File folder = getSaveFolder(folderPath.trim());
        File file = new File(folder, fileName.trim());
        ByteArrayInputStream bis = new ByteArrayInputStream(fileData);
        OutputStream os = null;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        try {
            os = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 2];
            int len = 0;
            while ((len = bis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            throw new RuntimeException(FileUtils.class.getName(), e);
        } finally {
            closeIO(bis, os);
        }
    }

    /**
     * 说明：将文件保存到本地
     *
     * @param fileData 保存数据
     * @param file     文件
     */
    public static void saveFileCache(byte[] fileData, File file) {
        ByteArrayInputStream bis = new ByteArrayInputStream(fileData);
        OutputStream os = null;
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
        try {
            os = new FileOutputStream(file);
            byte[] buffer = new byte[1024 * 2];
            int len = 0;
            while ((len = bis.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }
            os.flush();
        } catch (Exception e) {
            throw new RuntimeException(FileUtils.class.getName(), e);
        } finally {
            closeIO(bis, os);
        }
    }

    /**
     * 说明：从指定文件夹获取文件
     *
     * @return 如果文件不存在则创建, 如果如果无法创建文件或文件名为空则返回null
     */
    public static File getSaveFile(String folderPath, String fileNmae) {
        File file = new File(getSavePath(folderPath) + File.separator
                + fileNmae);
        try {
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    /**
     * 说明：获取SD卡下指定文件夹的绝对路径
     *
     * @return 返回SD卡下的指定文件夹的绝对路径
     */
    public static String getSavePath(String folderName) {
        return getSaveFolder(folderName).getAbsolutePath();
    }

    /**
     * 说明：获取文件夹对象
     *
     * @return 返回SD卡下的指定文件夹对象，若文件夹不存在则创建
     */
    public static File getSaveFolder(String folderName) {
        File file = new File(folderName);
        file.mkdirs();
        return file;
    }

    /**
     * 说明：输入流转byte[]
     *
     * @param is
     * @return
     */
    public static final byte[] input2byte(InputStream is) {
        if (is == null) {
            return null;
        }
        byte[] buf = null;
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = 0;
        try {
            while (-1 != (len = bis.read())) {
                baos.write(len);
            }
            buf = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            closeIO(is, bis, baos);
        }
        return buf;
    }

    /**
     * 说明：将uri专为file对象
     *
     * @param activity
     * @param uri
     * @return
     */
    public static File uri2File(Activity activity, Uri uri) {
        if (AndroidInfoUtils.getSDKVersion() < 11) {
            //在API11以下可以使用
            String[] proj = {MediaStore.Images.Media.DATA};
            @SuppressWarnings("deprecation")
            Cursor cursor = activity.managedQuery(uri, proj, null, null, null);
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(index);
            cursor.close();
            return new File(path);
        } else {
            //在API11以上可以使用
            String[] proj = {MediaStore.Images.Media.DATA};
            CursorLoader loader = new CursorLoader(activity, uri, proj, null, null, null);
            Cursor cursor = loader.loadInBackground();
            int index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(index);
            cursor.close();
            return new File(path);
        }
    }

    /**
     * 说明：复制文件
     *
     * @param from
     * @param to
     */
    public static void copyFile(File from, File to) {
        if (null == from || !from.exists()) {
            return;
        }
        if (null == to) {
            return;
        }
        FileInputStream is = null;
        FileOutputStream os = null;
        try {
            is = new FileInputStream(from);
            if (!to.exists()) {
                to.createNewFile();
            }
            os = new FileOutputStream(to);
            copyFileFast(is, os);
        } catch (Exception e) {
            throw new RuntimeException(FileUtils.class.getClass().getName(), e);
        } finally {
            closeIO(is, os);
        }
    }

    /**
     * 说明：快速复制文件（采用nio操作）
     *
     * @param is 数据来源
     * @param os 数据目标
     * @throws IOException
     */
    public static void copyFileFast(FileInputStream is, FileOutputStream os)
            throws IOException {
        FileChannel in = is.getChannel();
        FileChannel out = os.getChannel();
        in.transferTo(0, in.size(), out);
    }
    /**
     * 说明：图片写入文件
     *
     * @param bitmap   图片
     * @param filePath 文件路径
     * @return 是否写入成功
     */
    public static boolean bitmapToFile(Bitmap bitmap, String filePath) {
        boolean isSuccess = false;
        if (bitmap == null) {
            return isSuccess;
        }
        File file = new File(filePath.substring(0,
                filePath.lastIndexOf(File.separator)));
        if (!file.exists()) {
            file.mkdirs();
        }

        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(filePath),
                    16 * 1024);
            isSuccess = bitmap.compress(CompressFormat.PNG, 100, out);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            closeIO(out);
        }
        return isSuccess;
    }

    /**
     * 说明：从文件中读取文本
     *
     * @param filePath
     * @return
     */
    public static String readFile(String filePath) {
        InputStream is = null;
        try {
            is = new FileInputStream(filePath);
        } catch (Exception e) {
            throw new RuntimeException(FileUtils.class.getName()
                    + "readFile---->" + filePath + " not found");
        }
        return inputStream2String(is);
    }

    /**
     * 说明：输入流转字符串
     *
     * @param is
     * @return 一个流中的字符串
     */
    public static String inputStream2String(InputStream is) {
        if (null == is) {
            return null;
        }
        StringBuilder resultSb = null;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(is));
            resultSb = new StringBuilder();
            String len;
            while (null != (len = br.readLine())) {
                resultSb.append(len);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            closeIO(is);
        }
        return null == resultSb ? null : resultSb.toString();
    }

    /**
     * 说明：从assets中读取文本
     *
     * @param name
     * @return
     */
    public static String readFileFromAssets(Context context, String name) {
        InputStream is = null;
        try {
            is = context.getResources().getAssets().open(name);
        } catch (Exception e) {
            throw new RuntimeException(FileUtils.class.getName()
                    + ".readFileFromAssets---->" + name + " not found");
        }
        return inputStream2String(is);
    }

    /**
     * 说明：关闭流
     *
     * @param closeables
     */
    public static void closeIO(Closeable... closeables) {
        if (null != closeables && closeables.length > 0) {
            for (Closeable closeable : closeables) {
                try {
                    if (closeable != null) {
                        closeable.close();
                    }
                } catch (Exception e) {
                    throw new RuntimeException(FileUtils.class.getName(), e);
                }
            }
        }
    }

    /**
     * 说明：删除指定文件
     * @param file
     * @return
     */
    public static boolean delete(File file){
        boolean flag = true;
        if (file != null && file.exists()){
            flag = file.delete();
        }
        return flag;
    }

    /**
     * 说明：删除指定文件夹下所有文件
     *
     * @param file
     */
    public static boolean deleteAllFile(File file) {
        try {
            if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (File f : files) {
                        f.delete();
                    }
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 说明：判断file是否是type类型
     * @param file
     * @param type
     * @return
     */
    public static boolean isFileType(File file,String type){
        String name = file.getName();
        int index = name.lastIndexOf(".");
        return index > 0 && StringUtils.isEqualsIgnoreCase(type,name.substring(index,name.length()));
    }

    /**
     * 说明：创建文件夹
     * @param directory
     * @return  true创建成功
     */
    public static boolean mkdirs(File directory){
        try {
            forceMkdir(directory);
            return true;
        }catch (IOException e){
            LogUtils.e(e);
            return false;
        }
    }

    private static void forceMkdir(File directory) throws IOException{
        if (directory == null){
            throw new IOException("message = "+ " directory is null !");
        }
        if (directory.exists()){
            if (!directory.isDirectory()){
                throw new IOException("message = "+directory+" exists and is not BaseContract directory!");
            }
        }else {
            if (!directory.mkdirs()){
                if (!directory.isDirectory()){
                    throw new IOException("Unable to create directory "+directory);
                }
            }
        }
    }

    /**
     * 说明：根据url获取文件名
     * @param url
     * @return
     */
    public static String getFileNameByUrl(String url){
        String suffixes="avi|mpeg|3gp|mp3|mp4|wav|jpeg|gif|jpg|png|apk|exe|txt|html|zip|java|doc";
        Pattern pat=Pattern.compile("[\\w]+[\\.]("+suffixes+")");//正则判断
        Matcher mc=pat.matcher(url);//条件匹配
        String name = "";
        while(mc.find()){
            name = mc.group();//截取文件名后缀名
        }
        return name;
    }

}

