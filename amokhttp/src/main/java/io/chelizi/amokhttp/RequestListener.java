package io.chelizi.amokhttp;

import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

import io.chelizi.amokhttp.entity.HttpError;
import io.chelizi.amokhttp.utils.ClassUtils;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 *  Created by chelizi on 17/5/28.
 */

public abstract class RequestListener<T> {

    public abstract void onResponseSuccess(T response);

    public abstract void onResponseError(int code, @Nullable HttpError httpError);


    public abstract void onFailure(Exception e);

    public void parseNetworkResponse(Class<?> clazz, Response response) throws Throwable {
        ResponseBody responseBody = response.body();
        String responseStr = null;
        if (responseBody != null){
            responseStr = responseBody.string();
        }
        Type type = ClassUtils.getType(clazz);
        T bean = null;
        if (type != null){
            if (TextUtils.equals(type.toString(),"class java.lang.String")) bean = (T) responseStr;
            else bean = new Gson().fromJson(responseStr, type);
        }
        final T finalBean = bean;
        AMOkHttpManager.getInstance().getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                onResponseSuccess(finalBean);
            }
        });

    }
}
