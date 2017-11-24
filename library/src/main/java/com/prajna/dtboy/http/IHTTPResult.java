package com.prajna.dtboy.http;

import android.content.Context;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 */
public interface IHTTPResult {
    void success(int status, Header[] headers, String response);

    void cache(String response);

    void fail(Header[] headers, String error, Context context);

    void disconnected(Context context);
}
