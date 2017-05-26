package io.chelizi.amokhttp.utils;

import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class ClassUtils {

    public static Type getType(Class cls) {
        try {
            Type superclass = cls.getGenericSuperclass();
            if (!(superclass instanceof ParameterizedType)) {
                throw new RuntimeException("Missing type parameter.");
            }
            ParameterizedType parameterized = (ParameterizedType) superclass;
            Type type = $Gson$Types.canonicalize(parameterized.getActualTypeArguments()[0]);
            return type;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
