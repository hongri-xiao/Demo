package com.xiao.lib.download;

import com.blankj.utilcode.util.LogUtils;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

import static java.net.HttpURLConnection.HTTP_OK;
import static java.net.HttpURLConnection.HTTP_PARTIAL;


public class DownloadFileTask implements  Runnable{

    boolean isExit=false;
    private String targetFile;
    private DownloadLinstener listener;
    private long size;
    private String md5;
    private String downloadFileUrl;
    RandomAccessFile rwd = null;
    private long start=0;

    public DownloadFileTask(String downloadFileUrl, final String targetFile, long size, String md5, DownloadLinstener listener) {
        this.isExit = isExit;
        this.targetFile = targetFile;
        this.listener = listener;
        this.size = size;
        this.md5 = md5;
        this.downloadFileUrl=downloadFileUrl;
    }

    public  void exit(){
        isExit=true;

        LogUtils.e("DownloadFileTask =====xgh=====,exit():");
    }



    @Override
    public void run() {

        try {

            File file = new File(targetFile);
            rwd = new RandomAccessFile(file, "rwd");
            File downloadDir=file.getParentFile();

            if (!downloadDir.exists()){
                downloadDir.mkdirs();
            }

            if (file.exists()){
                start=file.length();
                //从文件的某一位置开始写入
                rwd.seek(start);
            }


            LogUtils.e("DownloadFileTask =====xgh=====,run():start="+start+",size="+size);

            URL url = new URL(downloadFileUrl);
            HttpURLConnection  connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setDoInput(true);
            connection.setConnectTimeout(60*1000*3);
            connection.setReadTimeout(60*1000*3);
            connection.setRequestProperty("Range", "bytes=" + start + "-" + size);//设置下载范围

            LogUtils.e("DownloadFileTask =====xgh=====,run():connectTimeOut="+connection.getConnectTimeout());

            if (connection.getResponseCode() == HTTP_PARTIAL || connection.getResponseCode() ==HTTP_OK){

                int tempProgress=0; //记录临时的进度
               // long lastPosition=start;
                long load=start;


                InputStream inputStream = null;
               // OutputStream fos = null;

                byte[] buffer = new byte[4096];
                int len = -1;


                //start download
                listener.onStart(targetFile);

                try {

                    inputStream = connection.getInputStream();

                    while ((len = inputStream.read(buffer)) > 0 && !isExit )  {

                        rwd.write(buffer, 0, len);
                        load += len;

                        int progress = (int) (load * 100 / size);

                        if(progress > tempProgress){

                            //LogUtils.e("DownloadFileTask =====xgh=====,run():progress="+progress+",tempProgress="+tempProgress);

                            if(progress>0 && progress<=100){

                                listener.onUpdateProgress(targetFile, progress);
                                tempProgress=(int) progress;
                            }
                        }


                    }


                    LogUtils.e("DownloadFileTask =====xgh=====,run():size="+size+",fileLength="+file.length());
                    if( file.length()==size){
                        

                        listener.onComplete(targetFile,downloadFileUrl);

                    }else{
                        listener.onError(targetFile,downloadFileUrl,1,"file size error, size="+size+",file.length()="+file.length()+",isExit="+isExit);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onError(targetFile,downloadFileUrl,1,"connect timeout: "+e.getMessage());
                } finally {
                    if (inputStream != null) {
                        inputStream.close();
                    }

                    try {
                        if (rwd != null) {
                            rwd.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }


            }else{
                listener.onError(targetFile,downloadFileUrl,1,"http code error, http code="+connection.getResponseCode());
            }

        } catch (Exception e) {

            LogUtils.e("DownloadFileTask =====xgh=====,run():下载错误, "+e.getMessage());
            listener.onError(targetFile,downloadFileUrl,1,"IOException:"+e.getMessage());
            e.printStackTrace();
        }




    }

    @NotNull
    public String getDownloadUrl() {
        return downloadFileUrl;
    }


    public interface DownloadLinstener {
        public static final int ERROR_ILLEGAL_FILENAME = 1;
        public static final int ERROR_NO_SPACE = 2;

        void onUpdateProgress(String fileName, int percent);

        void onStart(String fileName);

        void onComplete(String fileName,String url);

        void onError(String fileName,String url, int errorCode,String errorMsg);


    }

    @Override
    public String toString() {
        return "DownloadFileTask{" +
                "isExit=" + isExit +
                ", targetFile='" + targetFile + '\'' +
                ", size=" + size +
                ", md5='" + md5 + '\'' +
                ", downloadFileUrl='" + downloadFileUrl + '\'' +
                ", start=" + start +
                '}';
    }
}
