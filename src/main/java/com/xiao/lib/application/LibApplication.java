package com.xiao.lib.application;

import android.annotation.SuppressLint;
import android.app.Application;
import android.text.TextUtils;

import com.blankj.utilcode.util.PhoneUtils;
import com.blankj.utilcode.util.Utils;
import com.bumptech.glide.Glide;
import com.orhanobut.hawk.Hawk;

/**
 * @author 沈小建
 * @data 2018/4/25
 */
public class LibApplication implements IApplication {

    public static Application mApplication;

    /**
     * DeviceID.
     */
    private static String DEVICE_ID;

    @Override
    public void onCreate(Application application) {
        mApplication = application;

        application.registerActivityLifecycleCallbacks(AppManager.getAppManager());

        Utils.init(application);
        Hawk.init(application).build();

        //初始化网络监测的广播
        //NetStateChangeReceiver.init(mApplication);

       /* MobclickAgent.UMAnalyticsConfig config = new MobclickAgent.UMAnalyticsConfig(mApplication, "5b91e3cbf29d981e160000fb", "googlePlay");
        MobclickAgent.startWithConfigure(config);*/
    }

    @SuppressLint("MissingPermission")
    public static String getDeviceId() {
        if (TextUtils.isEmpty(DEVICE_ID)) {
            DEVICE_ID = PhoneUtils.getDeviceId();
        }

        return DEVICE_ID;
    }

    @Override
    public void onTrimMemory(int level) {
        Glide.get(mApplication).onTrimMemory(level);
    }

    @Override
    public void onLowMemory() {
        Glide.get(mApplication).onLowMemory();
    }


    @Override
    public void onTerminate() {
        // 取消BroadcastReceiver注册
       // NetStateChangeReceiver.deInit();
    }

}
