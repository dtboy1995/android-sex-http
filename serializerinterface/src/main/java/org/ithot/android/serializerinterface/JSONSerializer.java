package org.ithot.android.serializerinterface;

import java.lang.reflect.Type;

public abstract class JSONSerializer {
    public abstract Object parse(String json, Type type);

    public abstract String stringify(Object object);
}