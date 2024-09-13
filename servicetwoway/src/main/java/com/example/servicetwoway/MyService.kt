package com.example.servicetwoway

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log

class MyService : Service() {

    private var callback : ICallback? = null

    //实现 AIDL 生产的接口
    private val binder = object : MyInterface.Stub(){
        override fun sendData(data: String?): String? {
            //收到数据后返回给客户端
            Log.i("=====================", "sendData: $data")
            return "111111111111"
        }

        override fun registerCallback(callback: ICallback?) {
            // 注册客户端回调接口
            this@MyService.callback = callback
        }

        override fun unregisterCallback(callback: ICallback?) {
            //反注册回调接口
            if(this@MyService.callback == callback){
                this@MyService.callback = null
            }
        }

    }

    override fun onBind(p0: Intent?): IBinder? {
        return binder
    }
}