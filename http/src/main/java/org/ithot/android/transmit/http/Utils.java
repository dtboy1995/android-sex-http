package org.ithot.android.transmit.http;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

/**
 * 工具类
 */

public class Utils {

    public static final String TAG = "[android-sex-http]";

    public static class Logger {

        public static void debug(String message) {
            if (Req.DEBUG) {
                Log.d(TAG, "debug:" + message);
            }
        }

        public static void error(String message) {
            if (Req.DEBUG) {
                Log.e(TAG, "debug:" + message);
            }
        }

        public static void info(String message) {
            if (Req.DEBUG) {
                Log.i(TAG, "debug:" + message);
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

    public static int progress(long bytesWritten, long totalSize) {
        double d = totalSize > 0L ? (double) bytesWritten * 1.0D / (double) totalSize * 100.0D : -1.0D;
        return (int) d;
    }
}
