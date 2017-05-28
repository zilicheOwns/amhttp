package io.chelizi.amokhttp.query;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

import io.chelizi.amokhttp.RequestManager;
import io.chelizi.amokhttp.RequestListener;
import io.chelizi.amokhttp.entity.FileCard;
import io.chelizi.amokhttp.utils.ClassUtils;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 *  Created by Eddie on 2017/5/20.
 */

public abstract class OnFindListener<T> implements RequestListener<T> {


    @Override
    public void parseNetworkResponse(Response response, FileCard fileCard) throws Throwable {
        ResponseBody responseBody = response.body();
        String responseStr = null;
        if (responseBody != null){
            responseStr = responseBody.string();
        }
        Type type = ClassUtils.getType(OnFindListener.this.getClass());
        T bean = null;
        if (type != null){
            if (TextUtils.equals(type.toString(),"class java.lang.String")) bean = (T) responseStr;
            else bean = new Gson().fromJson(responseStr, type);
        }
        final T finalBean = bean;
        RequestManager.getInstance().getMainHandler().post(new Runnable() {
            @Override
            public void run() {
                onResponseSuccess(finalBean);
            }
        });

    }
}
