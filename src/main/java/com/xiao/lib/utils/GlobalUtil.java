package com.xiao.lib.utils;

import com.blankj.utilcode.util.SizeUtils;
import com.orhanobut.hawk.Hawk;

/**
 * @author 10488
 * @date 2017-11-03
 * 存储工具
 */

public class GlobalUtil {

    public static final int THUMB_WIDTH = SizeUtils.dp2px(120);

    /**
     * Saves any cmd including any collection, primitive values or custom objects
     *
     * @param key   is required to differentiate the given data
     * @param value is the data that is going to be encrypted and persisted
     * @return true if the operation is successful. Any failure in any step will return false
     */
    public static <T> boolean put(String key, T value) {
        return Hawk.put(key, value);
    }

    /**
     * Gets the original data along with original cmd by the given key.
     * This is not guaranteed operation since Hawk uses serialization. Any change in in the requested
     * data cmd might affect the result. It's guaranteed to return primitive types and String cmd
     *
     * @param key is used to get the persisted data
     * @return the original object
     */
    public static <T> T get(String key) {
        return Hawk.get(key);
    }

    /**
     * Gets the saved data, if it is null, default value will be returned
     *
     * @param key          is used to get the saved data
     * @param defaultValue will be return if the response is null
     * @return the saved object
     */
    public static <T> T get(String key, T defaultValue) {
        return Hawk.get(key, defaultValue);
    }

    /**
     * Removes the given key/value from the storage
     *
     * @param key is used for removing related data from storage
     * @return true if delete is successful
     */
    public static boolean delete(String key) {
        return Hawk.delete(key);
    }

    /**
     * 清除所有资源.
     */
    public static void clearAll() {
        Hawk.deleteAll();
    }
}
