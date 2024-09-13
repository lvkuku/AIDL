package com.example.servicetwoway;
import com.example.servicetwoway.ICallback;
/**
*   双向通信
*/

interface MyInterface {
    // 定义要实现的接口方法，用于从客户端传递数据到服务端
    String sendData(String data);
    // 定义一个回调方法，用于服务端传递数据给客户端
    void registerCallback(ICallback callback);
    // 反注册回调接口，避免内存泄漏
    void unregisterCallback(ICallback callback);
}