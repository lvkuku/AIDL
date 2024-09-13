package com.example.clienttwoway

import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.RemoteException
import android.util.Log
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.servicetwoway.ICallback
import com.example.servicetwoway.MyInterface

class MainActivity : AppCompatActivity() {
    private var myAidlInterface: MyInterface? = null
//    private val sendMessage : Button = findViewById(R.id.send_message);

    // 实现回调接口，用于接收服务端返回的数据
    private val callback = object : ICallback.Stub() {
        override fun onSendClient(data: String?) {
            Log.i("=====================", "onSendClient: $data")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        // 绑定服务
        try {
            val intent = Intent()
            intent.setPackage("com.example.servicetwoway")
            intent.setAction("com.example.servicetwoway.action")
            bindService(intent, serviceConnection, BIND_AUTO_CREATE)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private val serviceConnection = object : ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val asInterface = MyInterface.Stub.asInterface(p1)
            asInterface?.registerCallback(callback)
            asInterface?.sendData("Hello form client")
        }

        override fun onServiceDisconnected(p0: ComponentName?) {
            myAidlInterface = null
        }

    }
    override fun onDestroy() {
        super.onDestroy()
        // 反注册回调接口，避免内存泄漏
        try {
            myAidlInterface?.unregisterCallback(callback)
        } catch (e: RemoteException) {
            e.printStackTrace()
        }
        unbindService(serviceConnection)
    }
}