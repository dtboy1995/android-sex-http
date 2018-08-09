/*
 * Copyright 2014 Vincent Brison.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ithot.android.transmit.cache;

import android.util.Log;

/**
 * This class provide a logging instance to the library.
 */
final class Logger {

    private static final String DEFAULT_LOG_TAG = "dualcache";
    private final boolean isLogEnable;

    Logger(boolean isLogEnable) {
        this.isLogEnable = isLogEnable;
    }

    private void log(int lvl, String tag, String msg) {
        if (isLogEnable) {
            Log.println(lvl, tag, msg);
        }
    }

    /**
     * Log with level info.
     * @param tag is the tag to used.
     * @param msg is the msg to log.
     */
    void logInfo(String tag, String msg) {
        log(Log.INFO, tag, msg);
    }

    /**
     * Default log info using tag {@link #DEFAULT_LOG_TAG}.
     * @param msg is the msg to log.
     */
    void logInfo(String msg) {
        log(Log.INFO, DEFAULT_LOG_TAG, msg);
    }

    /**
     * Log with level verbose and tag {@link #DEFAULT_LOG_TAG}.
     * @param msg is the msg to log.
     */
    void logVerbose(String msg) {
        log(Log.VERBOSE, DEFAULT_LOG_TAG, msg);
    }

    /**
     * Log with level warning and tag {@link #DEFAULT_LOG_TAG}.
     * @param msg is the msg to log.
     */
    void logWarning(String msg) {
        log(Log.WARN, DEFAULT_LOG_TAG, msg);
    }

    /**
     * Log with level error and tag {@link #DEFAULT_LOG_TAG}.
     * @param error is the error to log.
     */
    void logError(Throwable error) {
        if (isLogEnable) {
            Log.e(DEFAULT_LOG_TAG, "error : ", error);
        }
    }
}
