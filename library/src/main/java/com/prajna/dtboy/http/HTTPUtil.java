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

    public static String URL = "";

    private static final int HTTP_PORT = 80;
    private static final int HTTPS_PORT = 443;
    public static final String CHARSET = "UTF-8";

    public static AsyncHttpClient client = new AsyncHttpClient(true, HTTP_PORT, HTTPS_PORT);
    public static Gson gson = new Gson();

    static {
        client.setTimeout(3000);
        client.setURLEncodingEnabled(false);
        client.setMaxRetriesAndTimeout(1, 2000);
    }

    public static DualCache<String> cache;

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
