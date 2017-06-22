package com.prajna.dtboy.http;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.entity.StringEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 */
public class Request {

    String url;
    Context context;
    List<Header> headers = new ArrayList<>();
    String contentType = HTTPUtil.CONTENT_TYPE;
    CachePolicy cachePolicy = CachePolicy.CacheAndRemote;
    //
    Response result;
    ResponseRaw resultRaw;
    Method method = Method.GET;
    HttpEntity body;
    HTTPHandler handler;

    boolean isRawResponse = false;
    boolean isUseBaseUrl = true;

    public Request setIsUseBaseUrl(boolean isUseBaseUrl) {
        this.isUseBaseUrl = isUseBaseUrl;
        return this;
    }

    public boolean getIsUseBaseUrl() {
        return this.isUseBaseUrl;
    }

    public boolean getIsRawResponse() {
        return isRawResponse;
    }

    public ResponseRaw getResponseRaw() {
        return resultRaw;
    }

    public Request setResponseRaw(ResponseRaw resultRaw) {
        this.resultRaw = resultRaw;
        return this;
    }

    public Request setIsRawResponse(boolean isRawResponse) {
        this.isRawResponse = isRawResponse;
        return this;
    }

    public static Request build() {
        Request model = new Request();
        if (HTTPUtil.getGlobalRequestHandler() != null) {
            model.headers.addAll(HTTPUtil.getGlobalRequestHandler().addHeaders());
        }
        model.handler = new HTTPHandler(model);
        return model;
    }

    public Context getContext() {
        return context;
    }

    public Request setContext(Context context) {
        this.context = context;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public Request setUrl(String url) {
        if (getIsUseBaseUrl()) {
            if (HTTPUtil.getBaseUrl() == null) {
                this.url = url;
            } else {
                this.url = String.format("%s%s", HTTPUtil.getBaseUrl(), url);
            }
        } else {
            this.url = url;
        }
        return this;
    }

    public HttpEntity getBody() {
        return body;
    }

    public Request setBody(HttpEntity body) {
        this.body = body;
        return this;
    }

    public Request setBody(Map<String, Object> map) {
        String json = HTTPUtil.gson.toJson(map);
        this.body = new StringEntity(json, HTTPUtil.CHARSET);
        return this;
    }


    public Header[] getHeaders() {
        Header[] results = new Header[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            results[i] = headers.get(i);
        }

        return results;
    }

    public Request setHeaders(Map<String, String> headers) {
        this.headers.clear();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            Header h = new BasicHeader(header.getKey(), header.getValue());
            this.headers.add(h);
        }
        return this;
    }

    public Request addHeader(String key, String value) {
        this.headers.add(new BasicHeader(key, value));
        return this;
    }

    public String getContentType() {
        return contentType;
    }

    public Request setContentType(String contentType) {
        this.contentType = contentType;
        return this;
    }

    public Method getMethod() {
        return method;
    }

    public Request setMethod(Method method) {
        this.method = method;
        return this;
    }

    public HTTPHandler getHandler() {
        return handler;
    }

    public Request setHandler(HTTPHandler handler) {
        this.handler = handler;
        return this;
    }

    public Response getResult() {
        return result;
    }

    public Request setResponse(Response result) {
        this.result = result;
        return this;
    }

    public CachePolicy getCachePolicy() {
        return cachePolicy;
    }

    public Request setCachePolicy(CachePolicy cachePolicy) {
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
                    HTTPUtil.client.post(getContext(), getUrl(), getHeaders(), getBody(), getContentType(), getHandler());
                }
                break;
            case PUT:
                if (!HTTPUtil.isNetworkConnected(getContext())) {
                    getResult().disconnected(getContext());
                } else {
                    HTTPUtil.client.put(getContext(), getUrl(), getHeaders(), getBody(), getContentType(), getHandler());
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
