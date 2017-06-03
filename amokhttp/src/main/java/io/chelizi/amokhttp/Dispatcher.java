package io.chelizi.amokhttp;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.lang.ref.WeakReference;

import io.chelizi.amokhttp.entity.HttpError;
import io.chelizi.amokhttp.upload.OnUploadListener;

/**
 * Created by Eddie on 2017/6/3.
 */

public class Dispatcher extends Handler {

    private WeakReference<RequestListener> requestListenerWeakReference;
    private static Dispatcher dispatcher;

    public Dispatcher(Looper looper) {
        super(looper);
    }

    public static Dispatcher getDispatcher(Looper looper){
        if (dispatcher == null){
            synchronized (Dispatcher.class){
                if (dispatcher == null){
                    dispatcher = new Dispatcher(looper);
                }
            }
        }
        return dispatcher;
    }

    public void setRequestListener(RequestListener listener){
        requestListenerWeakReference = new WeakReference<>(listener);
    }

    public void dispatchToUIThread(Runnable dispatcher) {
        post(dispatcher);
    }

    @Override
    public void handleMessage(Message msg) {
        int messageConts = msg.what;
        switch (messageConts) {
            case MessageConstant.MESSAGE_NETWORK_ON_FAILURE: {
                RequestListener listener = requestListenerWeakReference.get();
                listener.onFailure(new RuntimeException("network error"));
                break;
            }
            case MessageConstant.MESSAGE_SERVER_ERROR: {
                RequestListener listener = requestListenerWeakReference.get();
                HttpError httpError = (HttpError) msg.obj;
                int statusCode = msg.arg1;
                listener.onResponseError(statusCode, httpError);
                break;
            }
            case MessageConstant.MESSAGE_UPLOAD_PROGRESS: {
                RequestListener listener = requestListenerWeakReference.get();
                Bundle bundle = (Bundle) msg.obj;
                if (bundle != null) {
                    long progress = bundle.getLong("bytesWritten");
                    long total = bundle.getLong("contentLength");
                    boolean done = bundle.getBoolean("done");
                    if (listener != null && listener instanceof OnUploadListener) {
                        ((OnUploadListener) listener).onRequestProgress(progress, total, done);
                    }
                }
                break;
            }
        }
    }
}
