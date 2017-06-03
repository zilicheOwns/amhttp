package io.chelizi.amokhttp;

import android.content.Context;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;

import java.io.File;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import io.chelizi.amokhttp.download.OnDownloadListener;
import io.chelizi.amokhttp.entity.FileCard;
import io.chelizi.amokhttp.post.OnAddListener;
import io.chelizi.amokhttp.query.OnFindListener;
import io.chelizi.amokhttp.upload.OnUploadListener;
import io.chelizi.amokhttp.upload.ProgressRequestBody;
import io.chelizi.amokhttp.upload.ProgressRequestListener;
import okhttp3.CacheControl;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by Eddie on 2017/5/20.
 */
public class RequestManager {

    private static final int READ_TIMEOUT = 30;
    private static final int WRITE_TIMEOUT = 60;
    private static final int CONNECT_TIMEOUT = 60;
    private OkHttpClient mOkHttpClient;
    private static volatile RequestManager mInstance;

    public RequestManager() {
        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    public static RequestManager getInstance() {
        if (mInstance == null) {
            synchronized (RequestManager.class) {
                if (mInstance == null) {
                    mInstance = new RequestManager();
                }
            }
        }
        return mInstance;
    }

    public <T> void upload(Context context, String url, CacheControl cacheControl,
                           HashMap<String, String> headers, File file, String fileName, HashMap<String, String> params,
                           Object tag, final OnUploadListener<T> listener) {
        try {
            IRequestBodyFactory requestBodyFactory = new RequestBodyFactoryImpl();
            RequestBody requestBody = requestBodyFactory.buildRequestBody(file, fileName, params);

            final Request request = new Request.Builder()
                    .cacheControl(cacheControl == null ? CacheControl.FORCE_NETWORK : cacheControl)
                    .headers(Headers.of(headers))
                    .tag(tag == null ? context.hashCode() : tag)
                    .url(url)
                    .post(new ProgressRequestBody(requestBody, new ProgressRequestListener() {
                        @Override
                        public void onRequestProgress(final long bytesWritten, final long contentLength, final boolean done) {
                            if (listener != null) {
                                Dispatcher dispatcher = Dispatcher.getDispatcher(Looper.getMainLooper());
                                dispatcher.setRequestListener(listener);
                                Message message = Message.obtain();
                                message.what = MessageConstant.MESSAGE_UPLOAD_PROGRESS;
                                Bundle bundle = new Bundle();
                                bundle.putLong("bytesWritten", bytesWritten);
                                bundle.putLong("contentLength", contentLength);
                                bundle.putBoolean("done", done);
                                message.obj = bundle;
                                dispatcher.sendMessage(message);
                            }
                        }
                    })).build();
            RequestUtils.enqueue(mOkHttpClient, request, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public <T> void post(Context context, String url, CacheControl cacheControl,
                         HashMap<String, String> headers, HashMap<String, String> params,
                         Object tag, int callMethod, final OnAddListener<T> listener) {
        try {
            IRequestBodyFactory requestBodyFactory = new RequestBodyFactoryImpl();
            RequestBody requestBody = requestBodyFactory.buildRequestBody(params);
            final Request request = new Request.Builder()
                    .cacheControl(cacheControl == null ? CacheControl.FORCE_NETWORK : cacheControl)
                    .headers(Headers.of(headers))
                    .tag(tag == null ? context.hashCode() : tag)
                    .url(url)
                    .post(requestBody)
                    .build();
            if (callMethod == CallMethod.SYNC) {
                RequestUtils.execute(mOkHttpClient, request, listener);
            } else {
                RequestUtils.enqueue(mOkHttpClient, request, listener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void post(Context context, String url, CacheControl cacheControl,
                         HashMap<String, String> headers, String params, Object tag, int callMethod, final OnAddListener<T> listener) {
        try {
            IRequestBodyFactory requestBodyFactory = new RequestBodyFactoryImpl();
            RequestBody requestBody = requestBodyFactory.buildRequestBody(params);
            final Request request = new Request.Builder()
                    .cacheControl(cacheControl == null ? CacheControl.FORCE_NETWORK : cacheControl)
                    .tag(tag == null ? context.hashCode() : tag)
                    .headers(Headers.of(headers))
                    .url(url)
                    .post(requestBody)
                    .build();
            if (callMethod == CallMethod.SYNC) {
                RequestUtils.execute(mOkHttpClient, request, listener);
            } else {
                RequestUtils.enqueue(mOkHttpClient, request, listener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public <T> void find(Context context, String url, CacheControl cacheControl,
                         HashMap<String, String> headers, Object tag, int callMethod, OnFindListener<T> listener) {
        try {
            final Request request = new Request.Builder()
                    .tag(tag == null ? context.hashCode() : tag)
                    .url(url)
                    .cacheControl(cacheControl == null ? CacheControl.FORCE_NETWORK : cacheControl)
                    .headers(Headers.of(headers))
                    .build();
            if (callMethod == CallMethod.SYNC) {
                RequestUtils.execute(mOkHttpClient, request, listener);
            } else {
                RequestUtils.enqueue(mOkHttpClient, request, listener);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public <T> void download(String url, FileCard fileCard, OnDownloadListener<T> listener) {
        Request request = new Request.Builder().url(url).build();
        RequestUtils.enqueue(mOkHttpClient, request, listener, fileCard);
    }
}
