package com.xiao.lib.websocket


import android.os.Handler
import com.alibaba.fastjson.JSONObject
import com.blankj.utilcode.util.LogUtils
import okhttp3.*
import okio.ByteString
import org.greenrobot.eventbus.EventBus
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class WebSocketClient private constructor(){




    companion object {

        /**
         * 由外部设置webSocket的地址
         */
        var wsUrl:String=""
        var deviceId:String=""

        //心跳发送的字符
         var MSG_HART:String=""

        private val DELAY_RECONNECT_WS = (20 * 1000).toLong()

        private val INTERVAL_SHARK_HAND = (1000 * 10).toLong()

       // var wsUrl = "ws://47.88.60.3:9502?mobile_id=123456789"  //测试用的服务器
       // var wsUrl = "ws://120.77.82.158:2345?mobile_id=123456789"



        val instance:WebSocketClient by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
            WebSocketClient()
        }


        /**
         * 设置webSocketde url
         */
       /* fun setUrl(url:String){
            wUrl=url

        }*/


        //可以传递参数的单例
       /* private var instance: WebSocketClient? = null

        fun getInstance(): WebSocketClient {

            if (instance == null) {

                //synchronized(WebSocketClient::class.java) {
                @Synchronized
                    if (instance == null) {
                        instance = WebSocketClient()
                    }

               // }
            }

            return instance!!
        }*/
    }


    private var mWebSocketClient:OkHttpClient = OkHttpClient.Builder()
                .readTimeout(30, TimeUnit.SECONDS)//设置读取超时时间
                .writeTimeout(30, TimeUnit.SECONDS)//设置写的超时时间
                .connectTimeout(30, TimeUnit.SECONDS)//设置连接超时时间
                .pingInterval(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true)
                .build()


    private var isRunning: Boolean = false

    private var mWebSocket: WebSocket? = null
    private val mHandler = Handler()
    private val timer = Timer()

    /**
     * 握手
     */
    private var sharkHandTask: SharkHandTask? = null


    /**
     * 重连服务器
     */
    private val reConnectTask = Runnable { reconnectWebSocket() }


    internal var i: Int = 0


    /**
     * inner class  是普通的内部类,可以访问外部类的非static 属性和方法,
     * 如果没有inner,是静态内部类, 不能访问外部类的非static 属性和方法
     */
    internal inner class SharkHandTask : TimerTask() {

        override fun run() {

            if (mWebSocket != null) {
                LogUtils.w("SharkHandTask =====xgh=====,run():send message:${MSG_HART}");
                mWebSocket!!.send(MSG_HART)
            }

        }
    }

    private fun reconnectWebSocket() {
        stop()

        mHandler.postDelayed({ start() }, 200)


    }


    private fun resetReconnectTimer() {

        mHandler.removeCallbacks(reConnectTask)
        mHandler.postDelayed(reConnectTask, DELAY_RECONNECT_WS)

        //LogUtils.w("===WebSocketClient===,resetReconnectTimer: 重置reset的timer");
    }

    fun start() {

        if (wsUrl.isEmpty()){
            throw RuntimeException("wsUrl is null")
        }

        if (deviceId.isEmpty()){
            throw  java.lang.RuntimeException("deviceId is null")
        }


        if (isRunning) {
            return

        }

        isRunning = true

        var jobject=JSONObject()
        jobject["cmd"]=1000
        jobject["data"]=""
        jobject["deviceId"]= deviceId

        MSG_HART=jobject.toJSONString()



        LogUtils.e("===WebSocketClient===,start:开始连接webSocket,MSG_HART=${MSG_HART}")
        val request = Request.Builder()
                .url(wsUrl)
                .build()

        mWebSocketClient.newWebSocket(request, object : WebSocketListener() {
            override fun onOpen(webSocket: WebSocket?, response: Response?) {
                isRunning = true
                mWebSocket = webSocket
                startSharkHand()
                resetReconnectTimer()

                //第一次连接的时候去查询一下新的数据
                val msg = WsEventMsg(WsEventMsg.MSG_CHECK_NEW_AD,"0","", deviceId)

                //将收到的数据转发给MainService集中处理
                EventBus.getDefault().post(msg)

                LogUtils.e("===WebSocketClient===,onOpen: ")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {

                //重置重连定时器
                resetReconnectTimer()

                LogUtils.w("===WebSocketClient===,onMessage:text=" + text)


                if (MSG_HART == text || "{\"result\":1}".equals(text)) { //心跳

                    val msg = WsEventMsg(WsEventMsg.MSG_SHARK_HAND,"0","", deviceId)

                    //将收到的数据转发给MainService集中处理
                    EventBus.getDefault().post(msg)


                } else {

                    try {

                        /**
                         * 服务器返回的数据格式: {cmd=2, data='[{"adPacketMd5":"12346","adPacketName":"sp1.zip","adPacketUrl":"http://ykq.litecolor.cn/sp1.zip","beginTime":1559029053037,"dayPlayTime":"[{\"dayStartTime\":\"36000000\",\"dayStopTime\":\"43800000\"},{\"dayStartTime\":\"28800000\",\"dayStopTime\":\"83400000\"}]","endTime":1559979453037,"htmlPath":"http://aaaa.aaa.aaa.com","id":0,"packetLength":2900251,"serverId":"1559115453068","state":0,"totalTime":10,"weekDays":254}]'}
                         */

                        //先按照有中括号来解析
                        var msgList=JSONObject.parseArray(text,WsEventMsg::class.java)
                        LogUtils.e("WebSocketClient =====xgh=====,onMessage():msgList=${msgList.size},size=${msgList}");

                        //过滤掉相同的指令
                        var cmdDistinctList=ArrayList<WsEventMsg>()
                        if (msgList.size >1){

                            msgList.forEach {
                                if (cmdDistinctList.contains(it)){//重复的指令, 直接回复服务器已经处理

                                    it.cmd = WsEventMsg.MSG_REPLY_TO_SERVER
                                    it.deviceId=deviceId
                                    sendMsg(it)

                                }else{
                                    cmdDistinctList.add(it)
                                }

                            }

                        }else{
                            cmdDistinctList.addAll(msgList)
                        }


                        LogUtils.e("WebSocketClient =====xgh=====,onMessage():去重后的指令:${cmdDistinctList}");
                        cmdDistinctList.forEach { it-> EventBus.getDefault().post(it) }


                    } catch (e: Exception) {

                        try {
                            //只有一条指令
                            val msg = JSONObject.parseObject(text, WsEventMsg::class.java)
                            EventBus.getDefault().post(msg)

                        }catch (e1:java.lang.Exception){
                            e1.printStackTrace()
                        }




                    }

                }


            }


            override fun onMessage(webSocket: WebSocket, bytes: ByteString) {

            }

            override fun onClosed(webSocket: WebSocket?, code: Int, reason: String?) {
                isRunning = false
                mWebSocket = null
                stopSharkHand()
                resetReconnectTimer()
                LogUtils.e("===WebSocketClient===,onClosed: ")
            }

            override fun onFailure(webSocket: WebSocket?, t: Throwable?, response: Response?) {
                isRunning = false
                mWebSocket = null
                stopSharkHand()
                resetReconnectTimer()
                LogUtils.e("===WebSocketClient===,onFailure: t=" + t!!.message)
            }
        })


    }

    private fun startSharkHand() {

        if (sharkHandTask != null) {
            sharkHandTask!!.cancel()
        }
        sharkHandTask = SharkHandTask()
        timer.scheduleAtFixedRate(sharkHandTask, 1000, INTERVAL_SHARK_HAND)
    }

    private fun stopSharkHand() {
        if (sharkHandTask != null) {
            sharkHandTask!!.cancel()
        }
    }


    fun stop() {

        isRunning = false
        mHandler.removeCallbacksAndMessages(null)
        if (mWebSocket != null) {
            mWebSocket!!.close(1000, "")
            mWebSocket = null
        }
    }


    /**
     * 发送webSocket 消息
     * @param msg
     */
    fun sendMsg(msg: WsEventMsg) {

        var jsonStr: String = JSONObject.toJSONString(msg)

        LogUtils.e("WebSocketClient =====xgh=====,sendMsg():msg=" + jsonStr)
        if (mWebSocket != null) {
            mWebSocket!!.send(jsonStr)

        }

    }




}
