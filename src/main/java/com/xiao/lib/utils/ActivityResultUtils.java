package com.xiao.lib.utils;

import android.content.Intent;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

/**
 * @author 沈小建
 * @data 2018/6/15
 */
public class ActivityResultUtils extends Fragment {

    private static final String RESULT_FRAGMENT_ID = "activity_result_key";

    /**
     * 回调.
     */
    private OnActivityResultListener mOnActivityResultListener;

    public static ActivityResultUtils getInstance(FragmentActivity activity) {
        FragmentManager fragmentManager = activity.getSupportFragmentManager();
        ActivityResultUtils resultUtils = null;

        if ((resultUtils = (ActivityResultUtils) fragmentManager.findFragmentByTag(RESULT_FRAGMENT_ID)) == null) {
            resultUtils = new ActivityResultUtils();
            fragmentManager.beginTransaction().add(resultUtils, RESULT_FRAGMENT_ID).commit();
            fragmentManager.executePendingTransactions();
        }

        return resultUtils;
    }

    public ActivityResultUtils setOnActivityResultListener(OnActivityResultListener onActivityResultListener) {
        mOnActivityResultListener = onActivityResultListener;
        return this;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mOnActivityResultListener != null) {
            mOnActivityResultListener.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * 数据回调监听.
     */
    public interface OnActivityResultListener {

        /**
         * 等同onActivityResult
         *
         * @param requestCode
         * @param resultCode
         * @param data
         */
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }
}
