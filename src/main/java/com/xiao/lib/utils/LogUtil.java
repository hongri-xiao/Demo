package com.xiao.lib.utils;

import android.util.Log;

import com.xiao.lib.BuildConfig;


/**
 * @author 沈小建
 * @data 2018/5/21
 */
public class LogUtil {


    public static void d(String tag, String message) {

        if(BuildConfig.LOG){

            Log.d(tag, message);
        }

    }

    public static void e(String tag, String message) {
        if(BuildConfig.LOG) {
            Log.e(tag, message);
        }
    }

}
