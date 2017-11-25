package com.prajna.dtboy.http;

import android.content.Context;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * http生命周期钩子
 */

public interface IHTTPHook {

    // 没有网络连接时的通用回调
    void disconnected(Context context);

    // 设置通用的请求头
    List<Header> headers();

    // 所有请求发送前调用的回调
    void pre(Context context);

    // 所有请求结束后调用的回调
    void post(Context context);

    // 请求失败的通用回调
    void fail(Header[] headers, String response, Context context);
}
