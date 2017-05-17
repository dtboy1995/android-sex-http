package com.prajna.dtboy.http;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;

/**
 */

public abstract class HTTPResult<T> implements HTTPResultHandler {

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

    }

    @Override
    public void fail(String errorMsg, Throwable throwable) {
        no(errorMsg);
    }

    @Override
    public void success(int status, Header[] headers, String response) {
        Log.e("success", response);
        T t = HTTPUtil.gson.fromJson(response, getType());
        ok(headers, t);
    }
}
