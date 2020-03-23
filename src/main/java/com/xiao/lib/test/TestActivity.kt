package com.xiao.lib.test

import com.blankj.utilcode.util.LogUtils
import com.shehuan.nicedialog.BaseNiceDialog
import com.shehuan.nicedialog.NiceDialog
import com.shehuan.nicedialog.ViewConvertListener
import com.shehuan.nicedialog.ViewHolder
import com.xiao.lib.R
import com.xiao.lib.mvp.BaseActivity
import com.zhouyou.http.EasyHttp
import com.zhouyou.http.callback.DownloadProgressCallBack
import com.zhouyou.http.callback.SimpleCallBack
import com.zhouyou.http.exception.ApiException
import java.io.File

class TestActivity :BaseActivity(){
    override fun getLayoutId(): Int=0

    override fun initDateAndView() {
        testHttpGet{flag,data->

        }
    }


    /**
     * 执行Http的Get请求
     */
    fun testHttpGet(callback:(Boolean,String)->Unit={_,_->}){
        EasyHttp.get("")
                .params("equipment_id", "123456")
                .execute(object : SimpleCallBack<String>() {
                    override fun onSuccess(data: String) {
                        callback.invoke(true, data)
                    }

                    override fun onError(e: ApiException) {
                        callback.invoke(false, "")
                    }

                })
    }


    /**
     * 上传文件
     */
fun testUploadFile() {
        var screenShot = File("")
        EasyHttp
                .post("")
                .params("device_time", "")
                .params("equipment_id", "")
                .params("file_video", screenShot, screenShot.name) { bytesWritten, contentLength, done ->

                    var percent = (bytesWritten * 100 / contentLength).toInt()
                    LogUtils.e("MainService =====xgh=====,onResponseProgress():percent=${percent}")

                }
                .execute(object : SimpleCallBack<String>() {
                    override fun onSuccess(t: String) {
                        LogUtils.e("MainService =====xgh=====,onSuccess():${t},fileName=${screenShot.name}");
                    }

                    override fun onError(e: ApiException) {
                        LogUtils.e("MainService =====xgh=====,:onError: ${e.message}");

                    }

                })
    }


    /**
     * 下载文件
     */

    fun testDownloadFile(){

        var url = "http://61.144.207.146:8081/b8154d3d-4166-4561-ad8d-7188a96eb195/2005/07/6c/076ce42f-3a78-4b5b-9aae-3c2959b7b1ba/kfid/2475751/qqlite_3.5.0.660_android_r108360_GuanWang_537047121_release_10000484.apk";
        EasyHttp.downLoad(url)
                .savePath("/sdcard/test/QQ")
                .saveName("release_10000484.apk")//不设置默认名字是时间戳生成的
                .execute(object : DownloadProgressCallBack<String>() {
                    override fun update(bytesRead: Long, contentLength: Long, done: Boolean) {
                    }

                    override fun onComplete(path: String?) {
                    }

                    override fun onError(e: ApiException?) {
                    }

                    override fun onStart() {
                    }
                });


    }


    /**
     * 显示Dialog
     * 注意: 布局文件需要套上一层, 才能正确的wrapContent
     */

    fun testShowDialog(){
        NiceDialog.init()
                .setLayoutId(R.layout.dialog_two_button)
                .setConvertListener(object :ViewConvertListener(){
                    override fun convertView(holder: ViewHolder, dialog: BaseNiceDialog) {
                        holder.setText(R.id.tv_dialog_title,"Tip")
                        holder.setText(R.id.tv_dialog_msg,"Message")

                        holder.setOnClickListener(R.id.btn_dialog_left){
                            dialog.dismiss()
                        }
                        holder.setOnClickListener(R.id.btn_dialog_right){
                            dialog.dismiss()
                        }

                    }

                })
                .setWidth(-1)//默认是match_parent, -1表示wrap_content
               // .setHeight(0)//默认是wrap_content
                .show(supportFragmentManager)

    }


    /**
     * 通过cmd 提交代码到gitHub
     *
     *  //将当前目录下所有文件添加到缓存区
     *   git add ./
     *
     *  //查看是否还有为跟踪的文件
     *   git status
     *
     *  //提交到本地
     *  git commit -m "init"
     *
     * //关联到远程仓库
     * git remote add origin https://github.com/xiaoguanghong/Demo.git
     *
     * //提交到远程仓库
     * git push -u origin master
     *
     *
     * //修改后添加文件
     *  git commit -m "add file"
     *
     * //push , 回车后输入用户名和密码
     *  git push -u origin master
     */

}