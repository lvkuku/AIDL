package com.example.service;
/*
* 注意该导包需要自己手动导入
*/
import com.example.service.Data;

interface DataCallback {
    oneway void register(Data listener);
}