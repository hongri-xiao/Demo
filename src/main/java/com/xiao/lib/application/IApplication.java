package com.xiao.lib.application;

import android.app.Application;

/**
 * @author 沈小建
 * @data 2018/4/25
 */
public interface IApplication {

    void onCreate(Application application);

    public void onTrimMemory(int level);

    public void onLowMemory();

    void onTerminate();
}
