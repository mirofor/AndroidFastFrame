package com.fast.library.utils;

import java.util.Random;

/**
 * 说明：随机数工具类
 */

public class RandomUtils {

    public static final String NUMBERS = "0123456789";
    public static final String LETTERS_UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    public static final String LETTERS_LOWERCASE = "abcdefghijklmnopqrstuvwxyz";

    /**
     * 说明：禁止实例化
     */
    private RandomUtils(){}

    private static Random create(){
        return new Random();
    }
    /**
     * 说明：【0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ】随机
     * @param length 随机数长度
     * @return
     */
    public static String getRandomNumbersAndLetters(int length) {
        return getRandom(NUMBERS+LETTERS_LOWERCASE+LETTERS_UPPERCASE, length);
    }

    /**
     * 说明：【0123456789】随机
     * @param length
     * @return
     */
    public static String getRandomNumbers(int length) {
        return getRandom(NUMBERS, length);
    }

    /**
     * 说明：获取随机数根方法
     * @param source 随机数字符串
     * @param length 目标随机数长度
     * @return
     */
    public static String getRandom(String source, int length) {
        return StringUtils.isEmpty(source) ? null : getRandom(source.toCharArray(), length);
    }

    /**
     * 说明：获取随机数根方法
     * @param sourceChar 随机数字符数组
     * @param length 目标随机数长度
     * @return
     */
    public static String getRandom(char[] sourceChar, int length) {
        if (sourceChar == null || sourceChar.length == 0 || length < 0) {
            return null;
        }
        StringBuilder str = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            str.append(sourceChar[create().nextInt(sourceChar.length)]);
        }
        return str.toString();
    }

    /**
     * 说明：返回0-max之间的随机数
     * @param max
     * @return 当max<=0时，返回0
	 */
    public static int getRandom(int max) {
        return getRandom(0, max);
    }

    /**
     * 说明：返回min-max之间的随机数
     * @param min 最小值
     * @param max 最大值
     * @return 当min>max时返回0
     */
    public static int getRandom(int min, int max) {
        if (min > max) {
            return 0;
        }
        if (min == max) {
            return min;
        }
        return min + create().nextInt(max - min);
    }

    /**
     * 说明：随机洗牌
     * @param objArray
     * @return
     */
    public static boolean shuffle(Object[] objArray) {
        if (objArray == null) {
            return false;
        }

        return shuffle(objArray, getRandom(objArray.length));
    }

    /**
     * 说明：随机洗牌
     * @param objArray
     * @param shuffleCount
     * @return
     */
    public static boolean shuffle(Object[] objArray, int shuffleCount) {
        int length;
        if (objArray == null || shuffleCount < 0 || (length = objArray.length) < shuffleCount) {
            return false;
        }

        for (int i = 1; i <= shuffleCount; i++) {
            int random = getRandom(length - i);
            Object temp = objArray[length - i];
            objArray[length - i] = objArray[random];
            objArray[random] = temp;
        }
        return true;
    }

    /**
     * 说明：随机洗牌
     * @param intArray
     * @return
     */
    public static int[] shuffle(int[] intArray) {
        if (intArray == null) {
            return null;
        }

        return shuffle(intArray, getRandom(intArray.length));
    }

    /**
     * 说明：随机洗牌
     * @param intArray
     * @param shuffleCount
     * @return
     */
    public static int[] shuffle(int[] intArray, int shuffleCount) {
        int length;
        if (intArray == null || shuffleCount < 0 || (length = intArray.length) < shuffleCount) {
            return null;
        }

        int[] out = new int[shuffleCount];
        for (int i = 1; i <= shuffleCount; i++) {
            int random = getRandom(length - i);
            out[i - 1] = intArray[random];
            int temp = intArray[length - i];
            intArray[length - i] = intArray[random];
            intArray[random] = temp;
        }
        return out;
    }
}

