package org.ithot.android.serializer.gson;

import com.google.gson.Gson;

import org.ithot.android.serializerinterface.JSONSerializer;

import java.lang.reflect.Type;

public class JSON extends JSONSerializer {

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
