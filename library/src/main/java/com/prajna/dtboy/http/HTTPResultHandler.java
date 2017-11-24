package com.prajna.dtboy.http;

import android.content.Context;

import cz.msebera.android.httpclient.Header;

/**
 */
public interface HTTPResultHandler {
    void success(int status, Header[] headers, String response);

    void cache(String response);

    void fail(Header[] headers, String errorMsg, Context context);

    void disconnected(Context context);
}
