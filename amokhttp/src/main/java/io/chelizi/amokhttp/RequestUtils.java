package io.chelizi.amokhttp;

import android.os.Looper;
import android.os.Message;

import com.google.gson.Gson;

import java.io.IOException;

import io.chelizi.amokhttp.entity.FileCard;
import io.chelizi.amokhttp.entity.HttpError;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Eddie on 2017/6/3.
 */

public class RequestUtils {

    public static <T> void enqueue(OkHttpClient client, Request request, RequestListener<T> listener) {
        enqueue(client, request, listener, null);
    }

    public static <T> void enqueue(OkHttpClient client, Request request, final RequestListener<T> listener, final FileCard fileCard) {
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                Dispatcher dispatcher = Dispatcher.getDispatcher(Looper.getMainLooper());
                dispatcher.setRequestListener(listener);
                Message message = Message.obtain();
                message.what = MessageConstant.MESSAGE_NETWORK_ON_FAILURE;
                dispatcher.sendMessage(message);
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                if (response == null) {
                    throw new NullPointerException("okHttp response is null");
                } else {
                    if (response.isSuccessful()) {
                        try {
                            if (listener != null) {
                                listener.parseNetworkResponse(response, fileCard);
                            }
                        } catch (Throwable throwable) {
                            throwable.printStackTrace();
                        }
                    } else {
                        ResponseBody body = response.body();
                        if (body == null) {
                            throw new NullPointerException("response boy is null");
                        } else {
                            String error = body.string();
                            final HttpError httpError = new Gson().fromJson(error, HttpError.class);
                            if (httpError != null) {
                                Dispatcher dispatcher = Dispatcher.getDispatcher(Looper.getMainLooper());
                                dispatcher.setRequestListener(listener);
                                Message message = Message.obtain();
                                message.what = MessageConstant.MESSAGE_NETWORK_ON_FAILURE;
                                message.obj = httpError;
                                message.arg1 = response.code();
                                dispatcher.sendMessage(message);
                            }
                        }
                    }
                }
            }
        });
    }

    public static <T> void execute(OkHttpClient client, Request request, RequestListener<T> listener){
        try {
            Response response = client.newCall(request).execute();
            if (response == null){
                listener.onFailure(new HandleException("execute request is failed"));
            }else{
                if (response.isSuccessful()){
                    try {
                        listener.parseNetworkResponse(response,null);
                    } catch (Throwable throwable) {
                        throwable.printStackTrace();
                    }
                }else{
                    ResponseBody body = response.body();
                    if (body == null){
                        throw new NullPointerException("body is null");
                    }else{
                        String error = body.string();
                        final HttpError httpError = new Gson().fromJson(error, HttpError.class);
                        if (httpError != null){
                            listener.onResponseError(response.code(),httpError);
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
