package com.prajna.dtboy.http;

import android.util.Log;

import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 * HTTP请求处理器
 */
public class HTTPHandler extends TextHttpResponseHandler {

    HTTPModel model;

    public HTTPHandler(HTTPModel model) {
        this.model = model;
    }

    @Override
    public void onSuccess(int status, Header[] headers, String response) {
        Log.e(getClass().getName(), response);
        if (model.getMethod() == HTTPMethod.GET) {
            switch (model.getCachePolicy()) {
                case NoCache:
                    model.getResult().success(status, headers, response);
                    break;
                case IgnoreCache:
                    model.getResult().success(status, headers, response);
                    HTTPUtil.cache.put(model.getCacheKey(), response);
                    break;
                case CacheOnly:
                    model.getResult().success(status, headers, response);
                    HTTPUtil.cache.put(model.getCacheKey(), response);
                    break;
                case CacheAndRemote:
                    model.getResult().success(status, headers, response);
                    HTTPUtil.cache.put(model.getCacheKey(), response);
                    break;
                case CacheOrRemote:
                    model.getResult().success(status, headers, response);
            }
        } else {
            model.getResult().success(status, headers, response);
        }
    }

    @Override
    public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
        Log.e(getClass().getName(), s+"");
        model.getResult().fail(s, throwable);
    }

}
