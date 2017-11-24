package com.prajna.dtboy.http;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 */

public class Utils {
    public static final String TAG = "[android-sex-http]";

    public static class Logger {
        public static void debug(String message) {
            if (Req.DEBUG) {
                Log.d(TAG, "response:" + message);
            }
        }
    }

    public static List<Header> arrToList(Header[] headers) {
        List<Header> ret = new ArrayList<>();
        for (Header header : headers) {
            ret.add(header);
        }
        return ret;
    }

    public int progress(long bytesWritten, long totalSize) {
        double d = totalSize > 0L ? (double) bytesWritten * 1.0D / (double) totalSize * 100.0D : -1.0D;
        return (int) d;
    }
}
