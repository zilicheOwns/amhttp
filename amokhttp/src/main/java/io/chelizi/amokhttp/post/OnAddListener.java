package io.chelizi.amokhttp.post;

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
 *  Created by Eddie on 2017/5/21.
 */
public abstract class OnAddListener<T> implements RequestListener<T> {

    @Override
    public void parseNetworkResponse(Response response , FileCard fileCard) throws Throwable {
        ResponseBody responseBody = response.body();
        if (responseBody == null){
            throw new NullPointerException("response body is null");
        }else{
            String responseStr = responseBody.string();
            Type type = ClassUtils.getType(OnAddListener.this.getClass());
            T bean = null;
            if (type != null){
                if (TextUtils.equals(type.toString(),"class java.lang.String"))
                    bean = (T) responseStr;
                else
                    bean = new Gson().fromJson(responseStr, type);
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
}
