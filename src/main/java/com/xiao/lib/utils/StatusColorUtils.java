package com.xiao.lib.utils;

import android.app.Activity;
import androidx.annotation.ColorRes;

import com.gyf.barlibrary.ImmersionBar;
import com.xiao.lib.R;

/**
 * @author 10488
 * @date 2017-08-06
 * 状态栏颜色设置.
 */

public class StatusColorUtils {

    /**
     * 设置状态颜色.
     *
     * @param activity 对应的 activity
     * @param colorId  颜色资源id
     */
    public static void setStatusColor(Activity activity, @ColorRes int colorId) {
        ImmersionBar.with(activity).navigationBarColor(colorId).keyboardEnable(true).fitsSystemWindows(true).init();
    }

    /**
     * 透明状态栏
     *
     * @param activity 对应的activity
     */
    public static void transparentStatus(Activity activity) {
        ImmersionBar.with(activity).transparentStatusBar().keyboardEnable(true).navigationBarColor(R.color.colorPrimaryDark).init();
    }

    /**
     * 透明状态栏
     *
     * @param activity 对应的activity
     */
    public static void transparentStatusAndNav(Activity activity) {
        ImmersionBar.with(activity).transparentStatusBar().transparentNavigationBar().init();
    }

    /**
     * 清除状态栏资源.
     *
     * @param activity 对应的activity
     */
    public static void destroy(Activity activity) {
        ImmersionBar.with(activity).destroy();
    }
}
