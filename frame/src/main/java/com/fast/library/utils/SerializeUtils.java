package com.fast.library.utils;

/**
 * 说明：序列化和反序列化工具类
 * <p/>
 * 作者：fanly
 * <p/>
 * 时间：2015/10/28 21:32
 * <p/>
 * 版本：verson 1.0
 */

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * 说明：序列化和反序列化工具类
 */

public class SerializeUtils {

    /**
     * 说明：禁止实例化
     */
    private SerializeUtils() {
    }

    /**
     * 说明：反序列化，文件->对象
     *
     * @param filePath
     *            文件路径
     * @return
     */
    public static Object deserialization(String filePath) {
        ObjectInputStream in = null;
        try {
            in = new ObjectInputStream(new FileInputStream(filePath));
            Object o = in.readObject();
            in.close();
            return o;
        } catch (FileNotFoundException e) {
            throw new RuntimeException("【异常：文件找不到】", e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("【异常：类找不到】", e);
        } catch (IOException e) {
            throw new RuntimeException("【异常：IO异常】", e);
        } finally {
            FileUtils.closeIO(in);
        }
    }

    /**
     * 说明：序列化，对象->文件
     *
     * @param filePath
     *            文件路径
     * @param obj
     *            对象
     */
    public static void serialization(String filePath, Object obj) {
        ObjectOutputStream out = null;
        try {
            out = new ObjectOutputStream(new FileOutputStream(filePath));
            out.writeObject(obj);
            out.close();
        } catch (FileNotFoundException e) {
            throw new RuntimeException("【异常：文件找不到】", e);
        } catch (IOException e) {
            throw new RuntimeException("【异常：IO异常】", e);
        } finally {
            FileUtils.closeIO(out);
        }
    }

}

