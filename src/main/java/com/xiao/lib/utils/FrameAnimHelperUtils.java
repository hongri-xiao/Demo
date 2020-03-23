package com.xiao.lib.utils;

import android.annotation.SuppressLint;
import android.content.res.XmlResourceParser;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import android.widget.ImageView;

import org.xmlpull.v1.XmlPullParser;

import java.util.ArrayList;
import java.util.List;


/**
 * @author 沈小建
 * @data 2018/5/14
 */
public class FrameAnimHelperUtils {

    private static final String TAG_ITEM = "item";
    private static final String TAG_DRAWABLE = "drawable";
    private static final String TAG_DURATION = "duration";

    /**
     * 对应的图片控件.
     */
    private ImageView mImageView;

    /**
     * 对应的资源id.
     */
    private int mDrawableId;

    /**
     * 动画的集合.
     */
    private List<AnimBean> mAnimBeanList;

    /**
     * 同意bitmap空间.
     */
    private BitmapFactory.Options mBitmapOption = new BitmapFactory.Options();

    /**
     * 现在播放的图片位置.
     */
    private int mNowPlayPicPosition;

    /**
     * 延时更改动画.
     */
    @SuppressLint("HandlerLeak")
    private Handler mChangeAnimHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            mBitmapOption.inBitmap = BitmapFactory.decodeResource(mImageView.getResources(), mAnimBeanList.get(mNowPlayPicPosition).resId, mBitmapOption);
            mImageView.setImageBitmap(mBitmapOption.inBitmap);
            mNowPlayPicPosition++;

            if (mNowPlayPicPosition == mAnimBeanList.size()) {
                mNowPlayPicPosition = 0;
            }
            sendEmptyMessageDelayed(0, mAnimBeanList.get(mNowPlayPicPosition).duration);
        }
    };


    public FrameAnimHelperUtils(@NonNull ImageView imageView, @DrawableRes int drawableId) {
        mImageView = imageView;
        mDrawableId = drawableId;

        if (Build.VERSION.SDK_INT < 19) {
            mImageView.setBackgroundDrawable(imageView.getResources().getDrawable(drawableId));
            ((AnimationDrawable) mImageView.getBackground()).start();
        } else {
            mBitmapOption.inMutable = true;
        }

        xmlParserAnim();
    }

    /**
     * 解析xml.
     */
    private void xmlParserAnim() {
        try {
            @SuppressLint("ResourceType")
            XmlResourceParser xmlPullParser = mImageView.getResources().getXml(mDrawableId);

            int eventType = xmlPullParser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                switch (eventType) {
                    case XmlPullParser.START_DOCUMENT:
                        mAnimBeanList = new ArrayList<>();
                        break;
                    case XmlPullParser.START_TAG:
                        if (xmlPullParser.getName().equals(TAG_ITEM)) {
                            AnimBean animBean = new AnimBean();
                            for (int i = 0; i < xmlPullParser.getAttributeCount(); i++) {
                                String attributeName = xmlPullParser.getAttributeName(i);
                                if (attributeName.equals(TAG_DRAWABLE)) {
                                    animBean.resId = Integer.parseInt(xmlPullParser.getAttributeValue(i).replace("@", ""));
                                }

                                if (attributeName.equals(TAG_DURATION)) {
                                    animBean.duration = xmlPullParser.getAttributeIntValue(i, 0);
                                }
                            }
                            mAnimBeanList.add(animBean);
                        }
                        break;
                    case XmlPullParser.END_TAG:
                        break;
                    default:
                        break;
                }

                eventType = xmlPullParser.next();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 开始执行动画.
     */
    public void startAnim() {
        if (Build.VERSION.SDK_INT >= 19) {
            mChangeAnimHandler.sendEmptyMessageDelayed(mNowPlayPicPosition, mAnimBeanList.get(0).duration);
        }
    }

    /**
     * 暂停动画.
     */
    public void pauseAnim() {
        if (Build.VERSION.SDK_INT >= 19) {
            mChangeAnimHandler.removeCallbacksAndMessages(null);
        }
    }

    /**
     * 释放资源动画.
     */
    public void release() {
        mChangeAnimHandler.removeCallbacksAndMessages(null);
        mImageView = null;
        if (Build.VERSION.SDK_INT >= 19) {
            if(mBitmapOption.inBitmap != null) {
                mBitmapOption.inBitmap.recycle();
            }
        }
    }

    /**
     * 还原到最后的位置.
     */
    public void releaseToLastPic(){
        mChangeAnimHandler.removeCallbacksAndMessages(null);
        if(mImageView != null) {
            mImageView.setImageResource(mAnimBeanList.get(mAnimBeanList.size() - 1).resId);
            mImageView = null;
        }
    }

    class AnimBean {
        public int resId;
        public int duration;
    }
}
