package org.ithot.android.transmit.http;

import android.content.Context;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import cz.msebera.android.httpclient.Header;

import static org.ithot.android.transmit.http.Req.ihttpHook;
import static org.ithot.android.transmit.http.Req.json;

/**
 * 响应
 */

public abstract class Res<T> implements IHTTPResult {

    public abstract void ok(Header[] headers, T response);

    public abstract void no(Header[] headers, String error);

    private Type getType() {
        Type type = getClass().getGenericSuperclass();
        ParameterizedType parameter = (ParameterizedType) type;
        return parameter.getActualTypeArguments()[0];
    }

    @Override
    public void cache(String response) {
        Type type;
        try {
            type = getType();
        } catch (Exception e) {
            type = null;
        }
        if (type != null && type == String.class) {
            ok(null, (T) response);
            return;
        }
        T t = (T) json().parse(response, type);
        ok(null, t);
    }

    @Override
    public void disconnected(Context context) {
        if (ihttpHook != null) {
            ihttpHook.post(context);
            ihttpHook.disconnected(context);
        }
    }

    @Override
    public void fail(Header[] headers, String error, Context context) {
        if (ihttpHook != null) {
            ihttpHook.post(context);
            ihttpHook.fail(headers, error, context);
        }
        no(headers, error);
    }

    @Override
    public void success(int status, Header[] headers, String response) {
        Type type;
        try {
            type = getType();
        } catch (Exception e) {
            type = null;
        }
        if (type != null && type == String.class) {
            ok(null, (T) response);
            return;
        }
        T t = (T) json().parse(response, type);
        ok(headers, t);
    }
}
