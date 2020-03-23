package com.xiao.lib.websocket

import com.alibaba.fastjson.JSONObject

class WsEventMsg : ParentEventMsg {
    /**
     * 伴生对象, 用于定义该类中的静态属性和静态方法
     */
    companion object {



        val MSG_REPLY_TO_SERVER: Int=999
        /**
         * 握手的消息
         */
        const val MSG_SHARK_HAND = 1000

        /**
         * 需要下载新AD
         */
        const val MSG_CHECK_NEW_AD = 2


        /**
         * 根据serverId 修改AD
         */
        const val MSG_UPDATE_AD = 3

        /**
         * 删除某个AD
         */
        const val MSG_DELETE_AD = 4

        /**
         * 修改显示的布局
         */
        const val MSG_CHANGE_LAYOUT_TYPE = 5

        /**
         * 修改设置的密码
         */
        const val MSG_CHANGE_PWD = 6

        /**
         * 设置设备端的音量, 需要音量的数据0~100
         */
        const val MSG_SET_VOL=7


        /**
         * 重启设备
         */
        const val MSG_REBOOT=8


        /**
         * 设置设备端的开关机时间
         */
        const val MSG_SCHEDULE_ON_OFF=9

        /**
         * 获取设备当前的时间
         */
        const val MSG_TAKE_TIME=10

        /**
         * 翻转屏幕
         */
        const val MSG_FLIP_SCREEN=11

        /**
         * 录制屏幕
         */
        const val MSG_TAKE_SCREEN_VIDEO=12


        /**
         *退出引导模式
         */
        const val MSG_EXIT_GUEST_MODE=13


        /**
         * 截取屏幕
         */
        const val MSG_TAKE_SCREEN_PHOTO=14


        /**
         * 获取设备当前时区
         */
        const val MSG_TAKE_TIME_ZONE=15

        /**
         * 设置时区
         */
        const val MSG_SET_TIME_ZONE=16


        /**
         * 获取终端日志
         */
        const val MSG_TAKE_LOG_CAT =17






        /**
         * 回复服务器某个AD下载完成
         */
        const val MSG_DOWNLOAD_AD_COMPLETE = 21

        /**
         * app内部使用的指令
         */
       // const val MSG_START_DOWNLOAD_AD = 200
    }


    var cmdId:String=""
    var cmd: Int = 0
    var data: String = ""
    var deviceId:String=""

    constructor(_type: Int, _data: String,cmdId:String,deviceId:String) {
        cmd = _type
        data = _data
        this.cmdId=cmdId
        this.deviceId=deviceId
    }




    /**
     * 无参数的构造器, 用于反射
     */
    constructor() {

    }


    /**
     * 获取json字符串,发送给服务器,不能用getXXX方法名,会反射失败
     */
    fun toJsonStr(): String {
        return JSONObject.toJSONString(this)

    }

   /* fun getEventStr():String{
        return "cmdId:${cmdId},cmd:${cmd},data:${data},deviceId:${deviceId}"
    }*/


    override fun toString(): String {
        return "WsEventMsg(cmdId='$cmdId', cmd=$cmd, data='$data', deviceId='$deviceId')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as WsEventMsg

        if (cmd != other.cmd) return false
        if (data != other.data) return false
        if (deviceId != other.deviceId) return false

        return true
    }

    override fun hashCode(): Int {
        var result = cmd
        result = 31 * result + data.hashCode()
        result = 31 * result + deviceId.hashCode()
        return result
    }


}
