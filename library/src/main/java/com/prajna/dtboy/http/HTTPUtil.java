package com.prajna.dtboy.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.gson.Gson;
import com.loopj.android.http.AsyncHttpClient;
import com.vincentbrison.openlibraries.android.dualcache.Builder;
import com.vincentbrison.openlibraries.android.dualcache.CacheSerializer;
import com.vincentbrison.openlibraries.android.dualcache.DualCache;
import com.vincentbrison.openlibraries.android.dualcache.JsonSerializer;

import java.security.MessageDigest;

/**
 */

public class HTTPUtil {

    // the base url
    private static String BASE_URL;
    // the http content type
    public static final String CONTENT_TYPE = "application/json";
    // the http port
    private static int HTTP_PORT = 80;
    // the https port
    private static int HTTPS_PORT = 443;
    // the char set
    public static final String CHARSET = "UTF-8";
    // http core
    public static AsyncHttpClient client = new AsyncHttpClient(true, HTTP_PORT, HTTPS_PORT);
    // json tools
    public static Gson gson = new Gson();
    // cache core
    public static DualCache<String> cache;

    // default http setting
    static {
        client.setURLEncodingEnabled(false);
    }

    private static IGlobalResponseHandler globalResponseHandler;
    private static IGlobalRequestHandler globalRequestHandler;

    public static void setGlobalResponseHandler(IGlobalResponseHandler responseHandler) {
        globalResponseHandler = responseHandler;
    }

    public static String getBaseUrl() {
        return BASE_URL;
    }

    public static IGlobalResponseHandler getGlobalResponseHandler() {
        return globalResponseHandler;
    }

    public static IGlobalRequestHandler getGlobalRequestHandler() {
        return globalRequestHandler;
    }

    public static void setGlobalRequestHandler(IGlobalRequestHandler requestHandler) {
        globalRequestHandler = requestHandler;
    }

    private static String CACHE_KEY;

    public static void setCacheKey(String key) {
        CACHE_KEY = key;
    }

    public static void setHttpPort(int httpPort) {
        HTTP_PORT = httpPort;
        client = new AsyncHttpClient(true, HTTP_PORT, HTTPS_PORT);
        client.setURLEncodingEnabled(false);
    }

    public static void setHttpsPort(int httpsPort) {
        HTTPS_PORT = httpsPort;
        client = new AsyncHttpClient(true, HTTP_PORT, HTTPS_PORT);
        client.setURLEncodingEnabled(false);
    }

    public static void setPorts(int httpPort, int httpsPort) {
        HTTP_PORT = httpPort;
        HTTPS_PORT = httpsPort;
        client = new AsyncHttpClient(true, HTTP_PORT, HTTPS_PORT);
        client.setURLEncodingEnabled(false);
    }

    public static void setBaseUrl(String url) {
        BASE_URL = url;
    }

    public static void initHttpCache(Context context) {
        CacheSerializer<String> jsonSerializer = new JsonSerializer<>(String.class);
        cache = new Builder<>("sex-http-cache", 1, String.class)
                .enableLog()
                .useSerializerInRam(500 * 1024, jsonSerializer)
                .useSerializerInDisk(10 * 1024 * 1024, true, jsonSerializer, context)
                .build();
    }

    public static String MD5(String s) {
        char[] hexDigits = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            byte[] btInput = s.getBytes();
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char[] str = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                str[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
                str[(k++)] = hexDigits[(byte0 & 0xF)];
            }
            return new String(str);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }
}
