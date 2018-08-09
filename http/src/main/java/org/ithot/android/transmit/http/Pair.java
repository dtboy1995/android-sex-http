package org.ithot.android.transmit.http;

import java.util.HashMap;
import java.util.Map;

/**
 * 构建map的工具类
 */

public class Pair {

    private Map<String, Object> kvs = new HashMap<>();

    public static Pair build() {
        Pair body = new Pair();
        return body;
    }

    public Pair kvs(String key, Object value) {
        kvs.put(key, value);
        return this;
    }

    public Map<String, Object> go() {
        return this.kvs;
    }
}
