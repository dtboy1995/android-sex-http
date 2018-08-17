package org.ithot.android.transmit.http;

import android.content.Context;

import java.io.File;

public interface IFileRes {

    void done(File file);

    void undone();

    void progress(int rate);

    void disconnected(Context context);

}
