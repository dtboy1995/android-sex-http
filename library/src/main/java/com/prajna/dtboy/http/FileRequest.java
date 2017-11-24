package com.prajna.dtboy.http;

import android.content.Context;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.HttpEntity;
import cz.msebera.android.httpclient.message.BasicHeader;

/**
 */
public class FileRequest {

    String url;
    Context context;
    List<Header> headers = new ArrayList<>();
    RequestParams params;
    FileResponse response;
    String contentType;
    HttpEntity body;

    public FileRequest setContentType(String type) {
        this.contentType = type;
        return this;
    }

    public String getContentType() {
        return this.contentType;
    }

    public FileRequest setResponse(FileResponse response) {
        this.response = response;
        return this;
    }

    public FileRequest setParams(RequestParams params) {
        this.params = params;
        return this;
    }

    boolean isUseBaseUrl = true;

    public FileRequest setIsUseBaseUrl(boolean isUseBaseUrl) {
        this.isUseBaseUrl = isUseBaseUrl;
        return this;
    }

    public boolean getIsUseBaseUrl() {
        return this.isUseBaseUrl;
    }


    public static FileRequest build() {
        FileRequest model = new FileRequest();
        if (HTTPUtil.getGlobalRequestHandler() != null) {
            model.headers.addAll(HTTPUtil.getGlobalRequestHandler().addHeaders());
        }
        return model;
    }

    public Context getContext() {
        return context;
    }

    public FileRequest setContext(Context context) {
        this.context = context;
        return this;
    }

    public String getUrl() {
        return url;
    }

    public FileRequest setUrl(String url) {
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

    public Header[] getHeaders() {
        Header[] results = new Header[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            results[i] = headers.get(i);
        }

        return results;
    }

    public FileRequest setHeaders(Map<String, String> headers) {
        this.headers.clear();
        for (Map.Entry<String, String> header : headers.entrySet()) {
            Header h = new BasicHeader(header.getKey(), header.getValue());
            this.headers.add(h);
        }
        return this;
    }

    public FileRequest addHeader(String key, String value) {
        this.headers.add(new BasicHeader(key, value));
        return this;
    }

    public void download(File file) {
        HTTPUtil.client.get(getContext(), getUrl(), getHeaders(), null, new FileAsyncHttpResponseHandler(file) {
            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                file.delete();
                file = null;
                response.fail(throwable);
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, File file) {
                response.ok();
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                double d = Double.valueOf(totalSize > 0L ? (double) bytesWritten * 1.0D / (double) totalSize * 100.0D : -1.0D);
                response.progress((int)d);
            }
        });
    }

    public void upload() {
        HTTPUtil.client.post(getContext(), getUrl(), getHeaders(), params, getContentType(), new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                response.ok();
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                response.fail(error);
            }

            @Override
            public void onProgress(long bytesWritten, long totalSize) {
                double d = Double.valueOf(totalSize > 0L ? (double) bytesWritten * 1.0D / (double) totalSize * 100.0D : -1.0D);
                response.progress((int) (d * 100));
            }
        });
    }
}
