package io.chelizi.amokhttp.upload;

import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;

import java.lang.reflect.Type;

import io.chelizi.amokhttp.Dispatcher;
import io.chelizi.amokhttp.RequestListener;
import io.chelizi.amokhttp.entity.FileCard;
import io.chelizi.amokhttp.utils.ClassUtils;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Eddie on 2017/5/24.
 */

public abstract class OnUploadListener<T> implements RequestListener<T> {

    public abstract void onRequestProgress(long progress, long total, boolean done);

    @Override
    public void parseNetworkResponse(Response response , FileCard fileCard) throws Throwable {
        ResponseBody responseBody = response.body();
        if (responseBody == null){
            throw new NullPointerException("response body is null");
        }else{
            String responseStr = responseBody.string();
            Type type = ClassUtils.getType(OnUploadListener.this.getClass());
            T bean = null;
            if (type != null){
                if (TextUtils.equals(type.toString(),"class java.lang.String"))
                    bean = (T) responseStr;
                else
                    bean = new Gson().fromJson(responseStr, type);
            }
            final T finalBean = bean;
            Dispatcher dispatcher = Dispatcher.getDispatcher(Looper.getMainLooper());
            dispatcher.dispatchToUIThread(new Runnable() {
                @Override
                public void run() {
                    onResponseSuccess(finalBean);
                }
            });
        }
    }
}
