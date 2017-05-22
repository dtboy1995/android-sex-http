package com.prajna.dtboy.http;

import android.content.Context;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;

/**
 */

public abstract class Response<T> implements HTTPResultHandler {

    public abstract void ok(Header[] headers, T response);

    public abstract void no(String error);

    Type getType() {
        Type type = getClass().getGenericSuperclass();
        ParameterizedType parameterized = (ParameterizedType) type;
        return parameterized.getActualTypeArguments()[0];
    }

    @Override
    public void cache(String response) {
        T t = HTTPUtil.gson.fromJson(response, getType());
        ok(null, t);
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
        T t = HTTPUtil.gson.fromJson(response, getType());
        ok(headers, t);
    }
}
