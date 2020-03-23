package com.xiao.lib.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.media.ExifInterface;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtil {

    /**
     * 拷贝文件到指定位置,并且按照指定尺寸裁剪

     * @param sourceFile
     * @param targetFile
     * @param width
     * @param height
     */
    public  static void copyImageWithSize(String sourceFile, String targetFile, int width, int height){

        File f=new File(sourceFile);
        if(!f.exists()){
            return;
        }

        try {


        Bitmap bitmap = null;
        Matrix matrix=new Matrix();
        int rotateRange=0;

            Options opt = new BitmapFactory.Options();
            opt.inJustDecodeBounds = true;
            opt.inDither = true;
            opt.inPreferredConfig = Bitmap.Config.ARGB_8888;


            InputStream inStream=new FileInputStream(sourceFile);
            if(inStream.available()<=0){
                inStream.close();
                return;
            }


            ExifInterface exif = null;
            try {
                exif = new ExifInterface(sourceFile);
            } catch (IOException e) {
                e.printStackTrace();
                exif = null;
            }

            if (exif != null) {
                // 读取图片中相机方向信息
                int ori = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                        ExifInterface.ORIENTATION_UNDEFINED);

                if(ori==ExifInterface.ORIENTATION_ROTATE_90){
                    rotateRange=90;
                }else if(ori==ExifInterface.ORIENTATION_ROTATE_180){
                    rotateRange=180;
                }else if(ori==ExifInterface.ORIENTATION_ROTATE_270){
                    rotateRange=270;
                }

                // Log.e(TAG, "~~~~~ execute(), ori="+ori);
            }




            BitmapFactory.decodeStream(inStream, null, opt);
            calcBitmapOptions(opt,width,height);
            inStream.close();

            opt.inJustDecodeBounds = false;


            inStream=new FileInputStream(sourceFile);

            bitmap = BitmapFactory.decodeStream(inStream, null, opt);

            //旋转图片
            matrix.postRotate(rotateRange);
            Bitmap rotateBitmap = Bitmap.createBitmap(bitmap, 0, 0, opt.outWidth, opt.outHeight, matrix, true);

            bitmap = rotateBitmap;
            inStream.close();

            if(bitmap!=null){
                saveThumbBitmap(targetFile,bitmap);
            }


            bitmap.recycle();
        }catch(Exception e){
        }


    }


    private static  void calcBitmapOptions(Options bitmapOptions,int width,int height) {
        if (bitmapOptions.outWidth > width || bitmapOptions.outHeight > height) {
            if (bitmapOptions.outHeight >= bitmapOptions.outWidth) {
                bitmapOptions.inSampleSize = bitmapOptions.outHeight / height;
            } else {
                bitmapOptions.inSampleSize = bitmapOptions.outWidth / width;
            }
            // sample only can accept 1, 2, 4, 8
            if ((bitmapOptions.inSampleSize > 2) && (bitmapOptions.inSampleSize < 4)) {
                bitmapOptions.inSampleSize = 2;
            } else if ((bitmapOptions.inSampleSize > 4) && (bitmapOptions.inSampleSize < 8)) {
                bitmapOptions.inSampleSize = 4;
            } else if (bitmapOptions.inSampleSize > 8) {
                bitmapOptions.inSampleSize = 8;
            }
        } else {
            bitmapOptions.inSampleSize = 1;
        }
    }


    private static void saveThumbBitmap(String targetFile, Bitmap mBitmap)   {

        //创建目录
        File dir=new File(targetFile).getParentFile();
        if (!dir.exists()){
            dir.mkdirs();
        }


        File f = new File(targetFile);

        if(f.exists()){
            f.delete();
        }

        FileOutputStream fOut = null;
        try {

            f.createNewFile();
            fOut = new FileOutputStream(f);

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
        } catch (Exception e1) {
            //创建缩略图出现异常时删除有问题的文件
            if(f.exists()){
                f.delete();
            }

            e1.printStackTrace();
        }

        try {
            fOut.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            fOut.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
