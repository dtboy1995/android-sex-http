package com.prajna.dtboy.http;

import com.loopj.android.http.TextHttpResponseHandler;

import cz.msebera.android.httpclient.Header;

import static com.prajna.dtboy.http.Req._cache;
import static com.prajna.dtboy.http.Req.client;
import static com.prajna.dtboy.http.Req.ihttpHook;

/**
 * 请求处理器
 */
public class HTTPHandler extends TextHttpResponseHandler {

    Req req;

    public HTTPHandler(Req req) {
        this.req = req;
    }

    @Override
    public void onSuccess(int status, Header[] headers, final String response) {
        Utils.Logger.debug(response);
        if (ihttpHook != null) {
            ihttpHook.post(req._context);
        }
        if (req._method == Method.GET) {
            switch (req._cachePolicy) {
                case NoCache:
                    req._res.success(status, headers, response);
                    break;
                case CacheAndRemote:
                case IgnoreCache:
                case CacheOnly:
                case CacheOrRemote:
                    req._res.success(status, headers, response);
                    // write cache
                    client.getThreadPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            _cache.put(req.key(), response);
                        }
                    });
                    break;
            }
        } else {
            req._res.success(status, headers, response);
        }
    }

    @Override
    public void onFailure(int i, Header[] headers, String s, Throwable throwable) {
        req._res.fail(headers, s, req._context);
    }
}
