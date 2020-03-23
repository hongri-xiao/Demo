package com.xiao.lib.mvp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.blankj.utilcode.util.SizeUtils
import com.shehuan.nicedialog.BaseNiceDialog
import com.shehuan.nicedialog.NiceDialog
import com.xiao.lib.R

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var mContext: Context
    protected lateinit var mHandler:Handler


    protected abstract  fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        mContext = this
        mHandler= Handler()

        onCreateAfter(savedInstanceState)
        initDateAndView()
        intent?.let { onHandleIntent(it, true) }


    }


    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        intent?.let { onHandleIntent(it, true) }
    }


    /**
     * @param fromNew [onNewIntent]
     * */
    open fun onHandleIntent(intent: Intent, fromNew: Boolean = false) {

    }



    /**布局设置之后触发*/
    open fun onCreateAfter(savedInstanceState: Bundle?) {
        //enableLayoutFullScreen()
        with(getLayoutId()) {
            if (this > 0) {
                setContentView(this)
            }
        }
    }


    override fun onResume() {
        super.onResume()
        //requestedOrientation = Constant.orientation
    }

    protected abstract fun initDateAndView()


    lateinit var mNiceDialog:BaseNiceDialog

    open fun showLoadingDialog() {
        mNiceDialog = NiceDialog.init()
                .setLayoutId(R.layout.layout_loading)
                //.setTheme(R.style.LoadDialog)
                .setWidth(SizeUtils.dp2px(150f))
                .setHeight(SizeUtils.dp2px(150f))
                .setOutCancel(false)
                .setDimAmount(0.3f)
                .show(supportFragmentManager)

        //mNiceDialog.isCancelable=true
    }

    open fun dismissLoadingDialog() {
        if (mNiceDialog != null) {
            mNiceDialog.dismissAllowingStateLoss()
        }
    }



}
