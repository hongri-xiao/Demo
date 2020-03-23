package com.xiao.lib.application;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * @author 沈小建
 * @date 2018-03-02
 */

public class AppManager implements Application.ActivityLifecycleCallbacks {

    /**
     * activity栈.
     */
    private static List<Activity> mActivityStack = new ArrayList<>();

    /**
     * 实例.
     */
    private static AppManager instance;

    public static AppManager getAppManager() {
        if (instance == null) {
            instance = new AppManager();
        }
        return instance;
    }

    private AppManager() {

    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mActivityStack.add(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mActivityStack.remove(activity);
    }

    /**
     * 获取当前的activity.
     *
     * @return 当前activity
     */
    public static Activity getNowActivity() {
        return mActivityStack.get(mActivityStack.size() - 1);
    }

    /**
     * 获取某个activity.
     */
    public static <T extends Activity> T  getActivity(Class clazz) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().getName().equals(clazz.getName())) {
                return (T) activity;
            }
        }

        return null;
    }

    /**
     * 是否activity存在.
     *
     * @param clazz
     * @return
     */
    public static boolean isActivityRunning(Class clazz) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().getName().equals(clazz.getName())) {
                return true;
            }
        }

        return false;
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        for (Activity activity : mActivityStack) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
                break;
            }
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = mActivityStack.size(); i < size; i++) {
            if (null != mActivityStack.get(i)) {
                finishActivity(mActivityStack.get(i));
            }
        }
        mActivityStack.clear();
    }
}
