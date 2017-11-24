package com.prajna.dtboy.http;

import android.content.Context;

import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 */

public interface IHTTPHook {

    void disconnected(Context context);

    List<Header> headers();

    void pre(Context context);

    void post(Context context);

    void fail(Header[] headers, String response, Context context);
}
