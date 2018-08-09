package com.dtboy.http.test;


import com.google.gson.Gson;

import org.ithot.android.transmit.http.Req;

import java.lang.reflect.Type;

public class GsonJSON extends Req.JSON {

    private Gson gson = new Gson();

    @Override
    public Object parse(String json, Type type) {
        return gson.fromJson(json, type);
    }

    @Override
    public String stringify(Object object) {
        return gson.toJson(object);
    }
}
