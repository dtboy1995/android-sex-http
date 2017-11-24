package com.prajna.dtboy.http;

import android.content.Context;

import cz.msebera.android.httpclient.Header;

/**
 */

public abstract class ResponseRaw implements HTTPResultHandler {

    public abstract void ok(Header[] headers, String response);

    public abstract void no(Header[] headers, String error);

    @Override
    public void cache(String response) {
        ok(null, response);
    }

    @Override
    public void disconnected(Context context) {
        if (HTTPUtil.getGlobalResponseHandler() != null) {
            HTTPUtil.getGlobalResponseHandler().disconnected(context);
        }
    }

    @Override
    public void fail(Header[] headers, String errorMsg, Context context) {
        if (HTTPUtil.getGlobalResponseHandler() != null) {
            HTTPUtil.getGlobalResponseHandler().fail(headers, errorMsg, context);
        }
        no(headers, errorMsg);
    }

    @Override
    public void success(int status, Header[] headers, String response) {
        ok(headers, response);
    }

}
