package com.example.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.RemoteCallbackList
import android.os.RemoteException
import android.util.Log

class MyService : Service() {
    /*
        管理跨进程回调的类
        可以自动处理客户端的注册和注销
        并且能够在客户端进程意外终止时自动移除回调
        他是线程安全的 需要与 beginBroadcast() 和 finishBroadcast() 结合使用
     */
    var remoteCallbackList: RemoteCallbackList<Data> = RemoteCallbackList()

    /*
        用于与Service绑定的mBinder
     */
    private val mBinder: DataCallback.Stub = object : DataCallback.Stub() {
        override fun register(listener: Data?) {
            remoteCallbackList.register(listener)
            notifyClients(0,"ForestLux")
        }

    }

    /*
        更新 Data 数据
     */
    fun notifyClients(a: Int, b: String) {
        val n: Int = remoteCallbackList.beginBroadcast()
        for (i in 0 until n) {
            try {
                remoteCallbackList.getBroadcastItem(i).notify(a, b)
            } catch (e: RemoteException) {
                e.printStackTrace()
            }
        }
        remoteCallbackList.finishBroadcast()
    }



    /*
        绑定 Service 的形式
     */
    override fun onBind(p0: Intent?): IBinder? {
        return mBinder;
    }

    override fun onDestroy() {
        remoteCallbackList.kill()
        super.onDestroy()
    }
}