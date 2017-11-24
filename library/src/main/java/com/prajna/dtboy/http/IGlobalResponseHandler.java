package com.prajna.dtboy.http;

import android.content.Context;

import cz.msebera.android.httpclient.Header;

/**
 */

public interface IGlobalResponseHandler {
    void disconnected(Context context);

    void fail(Header[] headers, String response, Context context);
}
