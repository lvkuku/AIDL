// ICallback.aidl
package com.example.servicetwoway;

interface ICallback {
   // 服务端通过此方法将数据回传给客户端
    void onSendClient(String data);
}