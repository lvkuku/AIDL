package com.example.client

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.service.Data
import com.example.service.DataCallback

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        //绑定服务
        try {
            val intent = Intent()
            intent.setPackage("com.example.service")
            intent.setAction("com.example.service.action")
            bindService(intent, serviceConnection, BIND_AUTO_CREATE)
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }

    //获取代理对象
    private val serviceConnection: ServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(componentName: ComponentName, iBinder: IBinder) {
            val dataCallback: DataCallback = DataCallback.Stub.asInterface(iBinder)
            try {
                dataCallback.register(lister)
            } catch (e: RemoteException) {
                throw RuntimeException(e)
            }
        }
        override fun onServiceDisconnected(componentName: ComponentName) {

        }
    }

    //代理对象中需要的 lister
    private val lister : Data.Stub = object : Data.Stub() {
        override fun asBinder(): IBinder {
            return this
        }

        override fun notify(a: Int, b: String?) {
            /*
                这里就是更新的数据了
             */
            Log.i("+++++++++++++++", "notify: " + a  + "================" + b)
        }
    }

    //反注册
    override fun onDestroy() {
        super.onDestroy()
        if (serviceConnection != null) {
            unbindService(serviceConnection)
        }
    }
}