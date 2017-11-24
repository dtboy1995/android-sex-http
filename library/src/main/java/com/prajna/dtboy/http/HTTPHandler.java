package com.prajna.dtboy.http;

import android.util.Log;

import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

/**
 */
public class HTTPHandler extends TextHttpResponseHandler {

    Request model;

    public HTTPHandler(Request model) {
        this.model = model;
    }

    @Override
    public void onSuccess(int status, Header[] headers, String response) {
        Log.e(getClass().getName(), response);
        if (model.getMethod() == Method.GET) {
            switch (model.getCachePolicy()) {
                case NoCache:
                    if (model.getIsRawResponse()) {
                        model.getResponseRaw().success(status, headers, response);
                    } else {
                        model.getResult().success(status, headers, response);
                    }
                    break;
                case IgnoreCache:
                    if (model.getIsRawResponse()) {
                        model.getResponseRaw().success(status, headers, response);
                    } else {
                        model.getResult().success(status, headers, response);
                    }
                    HTTPUtil.cache.put(model.getCacheKey(), response);
                    break;
                case CacheOnly:
                    if (model.getIsRawResponse()) {
                        model.getResponseRaw().success(status, headers, response);
                    } else {
                        model.getResult().success(status, headers, response);
                    }
                    HTTPUtil.cache.put(model.getCacheKey(), response);
                    break;
                case CacheAndRemote:
                    if (model.getIsRawResponse()) {
                        model.getResponseRaw().success(status, headers, response);
                    } else {
                        model.getResult().success(status, headers, response);
                    }
                    HTTPUtil.cache.put(model.getCacheKey(), response);
                    break;
                case CacheOrRemote:
                    if (model.getIsRawResponse()) {
                        model.getResponseRaw().success(status, headers, response);
                    } else {
                        model.getResult().success(status, headers, response);
                    }
            }
        } else {
            if (model.getIsRawResponse()) {
                model.getResponseRaw().success(status, headers, response);
            } else {
                model.getResult().success(status, headers, response);
            }
        }
    }

    @Override
    public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
        if (model.getIsRawResponse()) {
            model.getResponseRaw().fail(headers, s, model.context);
        } else {
            model.getResult().fail(headers, s, model.context);
        }

    }

}
