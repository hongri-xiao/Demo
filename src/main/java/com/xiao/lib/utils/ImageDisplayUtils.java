package com.xiao.lib.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import androidx.annotation.Nullable;
import android.text.TextUtils;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;

import com.blankj.utilcode.util.ScreenUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.signature.MediaStoreSignature;
import com.xiao.lib.R;
import com.xiao.lib.application.LibApplication;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;

/**
 * @author 沈小建
 * @data 2018/5/22
 */
public class ImageDisplayUtils {

    private static RequestOptions mPicOptions = new RequestOptions()
            //.placeholder(R.drawable.image)
            //.error(R.drawable.image)
            //.override(320,240)
            .diskCacheStrategy(DiskCacheStrategy.ALL);


    /**
     * 预加载视频的缩略图
     */
    private static RequestOptions mPreLoadOptions = new RequestOptions()
            .placeholder(R.drawable.image)
            //.error(R.drawable.image)
            //.frame(6*1000*1000)
            //.set(FRAME_OPTION, MediaMetadataRetriever.OPTION_CLOSEST)
            .override(ScreenUtils.getScreenWidth(),ScreenUtils.getScreenHeight())
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    private static RequestOptions mthumbOptions = new RequestOptions()
            .placeholder(com.xiao.lib.R.drawable.image)
            .error(com.xiao.lib.R.drawable.image)
            .override(GlobalUtil.THUMB_WIDTH,GlobalUtil.THUMB_WIDTH)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    private static RequestOptions mCircleOptions = new RequestOptions()
            .placeholder(R.drawable.image)
            .error(R.drawable.image)
            .circleCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    /**
     * 头像.
     */
    private static RequestOptions mPortraitOptions = new RequestOptions()
            .placeholder(R.drawable.ic_default_portrait)
            .error(R.drawable.ic_default_portrait)
            .diskCacheStrategy(DiskCacheStrategy.ALL);

    /**
     * 加载本地资源.
     */
    public static void displayLocalRes(ImageView imageView, int resId) {
        Glide.with(imageView.getContext()).load(resId).apply(mPicOptions).into(imageView);
    }


    public static void preLoadVideoThumb(Context context,String url){
        Glide.with(context)
                .load(url)
                .apply(mPreLoadOptions)
                .preload();

    }

    public static void loadVideoThumb(ImageView imageView,String url){
        Glide.with(imageView.getContext())
                .load(url)
                .apply(mPreLoadOptions)
                .into(imageView);

    }


    /**
     * 加载网上资源.
     */
    public static void displayPic(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).apply(mPicOptions).listener(new RequestListener<Drawable>() {
            @Override
            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
             Observable.just(url)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(s -> displayPic(imageView,url));
                return false;
            }

            @Override
            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                return false;
            }
        }).into(imageView);
    }

    /**
     * 加载头像.
     */
    public static void displayPortrait(ImageView imageView,String url) {
        Glide.with(imageView.getContext()).load(url).apply(mPortraitOptions).into(imageView);
    }

    public static void displayThumb(ImageView imageView,String url) {
        Glide.with(imageView.getContext()).load(url).apply(mthumbOptions).into(imageView);
    }

    /**
     * 加载网上资源(圆形).
     */
    public static void displayCirclePic(ImageView imageView, String url) {
        Glide.with(imageView.getContext()).load(url).apply(mCircleOptions).into(imageView);
    }

    /**
     * 清除内存缓存.
     */
    public static void clearBitmapCache() {
        Glide.get(LibApplication.mApplication).clearMemory();
    }

    /**
     * 显示图片带bitmap回调.
     */
    public static void displayUrlWithCallback(ImageView imageView, String url, OnGetBitmapListener onGetBitmapListener) {
        Glide.with(imageView.getContext()).asBitmap().load(url).apply(mPicOptions)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        onGetBitmapListener.onBitmap(resource);
                        return false;
                    }
                }).into(imageView);
    }

    public interface OnGetBitmapListener {
        void onBitmap(Bitmap bitmap);
    }


    public static void updateThumbOptions( String url, long modified){
        if(!TextUtils.isEmpty(url) && url.contains(".")){
            try{
                String tail = url.substring(url.lastIndexOf(".") + 1).toLowerCase();
                String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(tail);
                mPreLoadOptions.signature(new MediaStoreSignature(type, modified, 0));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

}
