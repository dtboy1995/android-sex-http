package com.prajna.dtboy.http;

import android.content.Context;

import cz.msebera.android.httpclient.Header;

/**
 */

public abstract class ResponseRaw implements HTTPResultHandler {

    public abstract void ok(Header[] headers, String response);

    public abstract void no(String error);

    @Override
    public void cache(String response) {
        ok(null, response);
    }

    @Override
    public void disconnected(Context context) {
        if (HTTPUtil.globalResponseHandler != null) {
            HTTPUtil.globalResponseHandler.disconnected(context);
        }
    }

    @Override
    public void fail(String errorMsg, Context context) {
        if (HTTPUtil.globalResponseHandler != null) {
            HTTPUtil.globalResponseHandler.fail(errorMsg, context);
        }
        no(errorMsg);
    }

    @Override
    public void success(int status, Header[] headers, String response) {
        ok(headers, response);
    }

}
