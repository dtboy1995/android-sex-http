package com.prajna.dtboy.http;

import android.content.Context;

/**
 */

public interface IGlobalResponseHandler {
    void disconnected(Context context);

    void fail(String response, Context context);
}
