package com.prajna.dtboy.http;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 */

public class Body {

    private Map<String, Object> kvs = new HashMap<>();

    public static Body build() {
        Body body = new Body();
        return body;
    }

    public Body addKvs(String key, Object value) {
        kvs.put(key, value);
        return this;
    }

    public Map<String, Object> done() {
        return this.kvs;
    }
}
