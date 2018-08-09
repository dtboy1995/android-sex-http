package org.ithot.android.transmit.http;

import android.content.Context;

import cz.msebera.android.httpclient.Header;

/**
 * 响应接口
 */
public interface IHTTPResult {

    // 成功
    void success(int status, Header[] headers, String response);

    // 缓存
    void cache(String response);

    // 失败
    void fail(Header[] headers, String error, Context context);

    // 没有网络连接
    void disconnected(Context context);
}
