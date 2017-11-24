package com.prajna.dtboy.http;

/**
 */

public interface FileResponse {
    void ok();

    void fail(Throwable throwable);

    void progress(int percent);
}
