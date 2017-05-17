package com.prajna.dtboy.http;

import android.content.Context;

import cz.msebera.android.httpclient.Header;

/**
 * 自定义请求回调接口
 * |success| 请求成功的回调
 * |cache| 返回cache的回调 如不重写cache 该方法默认回调到ok()上
 * |fail| 请求失败的回调
 * |disconnected| 没有网络的回调
 */
public interface HTTPResultHandler {
    void success(int status, Header[] headers, String response);

    void cache(String response);

    void fail(String errorMsg, Throwable throwable);

    void disconnected(Context context);
}
