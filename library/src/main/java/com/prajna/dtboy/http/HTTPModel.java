package com.prajna.dtboy.http;

import android.content.Context;

import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * || 默认的访问类型是 accessToken
 * || 默认的缓存策略是 noCache
 */
public class HTTPModel {

    String url;
    Context context;
    Header[] headers;
    String contentType;
    HTTPResult result;
    HTTPMethod method;
    HttpEntity entity;
    HTTPHandler handler;

    public static HTTPModel build() {
        HTTPModel model = new HTTPModel();
        model.handler = new HTTPHandler(model);
        return model;
    }

    public Context getContext() {
        return context;
    }

    public HTTPModel setContext(Context context) {
        this.context = context;
        return this;
    }

    CachePolicy cachePolicy;

    public String getUrl() {
        return url;
    }

    public HTTPModel setUrl(String url) {
        this.url = url;
        return this;
    }

    public HttpEntity getEntity() {
        return entity;
    }

    public void setEntity(HttpEntity entity) {
        this.entity = entity;
    }

    public HTTPModel setEntity(Map<String, Object> map) {
        String json = HTTPUtil.gson.toJson(map);
        this.entity = new StringEntity(json, HTTPUtil.CHARSET);
        return this;
    }


    public Header[] getHeaders() {
        return headers;
    }

    public HTTPModel setHeaders(Header[] headers) {
        this.headers = headers;
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public HTTPModel setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public HTTPMethod getMethod() {
        return method;
    }

    public HTTPModel setMethod(HTTPMethod method) {
        this.method = method;
        return this;
    }

    public HTTPHandler getHandler() {
        return handler;
    }

    public HTTPModel setHandler(HTTPHandler handler) {
        this.handler = handler;
        return this;
    }

    public HTTPResult getResult() {
        return result;
    }

    public HTTPModel setResult(HTTPResult result) {
        this.result = result;
        return this;
    }

    public CachePolicy getCachePolicy() {
        return cachePolicy;
    }

    public HTTPModel setCachePolicy(CachePolicy cachePolicy) {
        this.cachePolicy = cachePolicy;
        return this;
    }

    public String getCacheKey() {
        return HTTPUtil.MD5(url);
    }

    public void done() {
        switch (getMethod()) {
            case GET:
                if (true) {
                    switch (getCachePolicy()) {
                        case NoCache:
                            if (HTTPUtil.isNetworkConnected(getContext())) {
                                HTTPUtil.client.get(getContext(), getUrl(), getHeaders(), null, getHandler());
                            } else {
                                getResult().disconnected(getContext());
                            }
                            break;
                        case IgnoreCache:
                            if (HTTPUtil.isNetworkConnected(getContext())) {
                                HTTPUtil.client.get(getContext(), getUrl(), getHeaders(), null, getHandler());
                            } else {
                                getResult().disconnected(getContext());
                            }
                            break;
                        case CacheOnly:
                            String cache = HTTPUtil.cache.get(getCacheKey());
                            if (cache != null) {
                                getResult().cache(cache);
                                if (!HTTPUtil.isNetworkConnected(getContext())) {
                                    getResult().disconnected(getContext());
                                }
                            } else {
                                if (!HTTPUtil.isNetworkConnected(getContext())) {
                                    getResult().disconnected(getContext());
                                } else {
                                    HTTPUtil.client.get(getContext(), getUrl(), getHeaders(), null, getHandler());
                                }
                            }
                            break;
                        case CacheAndRemote:
                            String _cache = HTTPUtil.cache.get(getCacheKey());
                            if (_cache != null) {
                                getResult().cache(_cache);
                                if (HTTPUtil.isNetworkConnected(getContext())) {
                                    HTTPUtil.client.get(getContext(), getUrl(), getHeaders(), null, getHandler());
                                } else {
                                    getResult().disconnected(getContext());
                                }
                            } else {
                                if (HTTPUtil.isNetworkConnected(getContext())) {
                                    HTTPUtil.client.get(getContext(), getUrl(), getHeaders(), null, getHandler());
                                } else {
                                    getResult().disconnected(getContext());
                                }
                            }
                            break;
                        case CacheOrRemote:
                            String _cache_ = HTTPUtil.cache.get(getCacheKey());
                            if (HTTPUtil.isNetworkConnected(getContext())) {
                                HTTPUtil.client.get(getContext(), getUrl(), getHeaders(), null, getHandler());
                            } else {
                                getResult().cache(_cache_);
                                getResult().disconnected(getContext());
                            }
                    }
                }
                break;
            case POST:
                if (!HTTPUtil.isNetworkConnected(getContext())) {
                    getResult().disconnected(getContext());
                } else {
                    HTTPUtil.client.post(getContext(), getUrl(), getHeaders(), getEntity(), getContentType(), getHandler());
                }
                break;
            case PUT:
                if (!HTTPUtil.isNetworkConnected(getContext())) {
                    getResult().disconnected(getContext());
                } else {
                    HTTPUtil.client.put(getContext(), getUrl(), getHeaders(), getEntity(), getContentType(), getHandler());
                }
                break;
            case DELETE:
                if (!HTTPUtil.isNetworkConnected(getContext())) {
                    getResult().disconnected(getContext());
                } else {
                    HTTPUtil.client.delete(getContext(), getUrl(), getHeaders(), getHandler());
                }
                break;
        }
    }
}
