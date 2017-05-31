package io.chelizi.amokhttp;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.chelizi.amokhttp.download.OnDownloadListener;
import io.chelizi.amokhttp.entity.FileCard;
import io.chelizi.amokhttp.entity.HttpError;
import io.chelizi.amokhttp.post.OnAddListener;
import io.chelizi.amokhttp.query.OnFindListener;
import io.chelizi.amokhttp.upload.OnUploadListener;
import io.chelizi.amokhttp.upload.ProgressRequestBody;
import io.chelizi.amokhttp.upload.ProgressRequestListener;
import okhttp3.CacheControl;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Eddie on 2017/5/20.
 * ok http manager
 */

public class RequestManager {

    private static final int READ_TIMEOUT = 30;
    private static final int WRITE_TIMEOUT = 60;
    private static final int CONNECT_TIMEOUT = 60;
    private OkHttpClient mOkHttpClient;
    private static volatile RequestManager mInstance;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());


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

    public <T> void upload(Context context, String url, CacheControl cacheControl, HashMap<String, String> headers, File file, Object tag, final OnUploadListener<T> listener){
        try {
            RequestBody requestBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
            final Request request = new Request.Builder()
                    .cacheControl(cacheControl == null ? CacheControl.FORCE_NETWORK : cacheControl)
                    .headers(Headers.of(headers))
                    .tag(tag == null ? context.hashCode() : tag)
                    .url(url)
                    .post(new ProgressRequestBody(requestBody, new ProgressRequestListener() {
                        @Override
                        public void onRequestProgress(final long bytesWritten, final long contentLength, final boolean done) {
                            if (listener != null){
                                mMainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        listener.onRequestProgress(bytesWritten,contentLength,done);
                                    }
                                });
                            }
                        }
                    })).build();
            enqueue(request,listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public <T> void post(Context context, String url, CacheControl cacheControl, HashMap<String, String> headers, HashMap<String, String> params, Object tag, final OnAddListener<T> listener) {
        try {
            RequestBody requestBody = buildRequestBody(params);
            final Request request = new Request.Builder()
                    .cacheControl(cacheControl == null ? CacheControl.FORCE_NETWORK : cacheControl)
                    .headers(Headers.of(headers))
                    .tag(tag == null ? context.hashCode() : tag)
                    .url(url)
                    .post(requestBody)
                    .build();
            enqueue(request, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public <T> void post(Context context, String url, CacheControl cacheControl, HashMap<String, String> headers, String params, Object tag, final OnAddListener<T> listener) {
        try {
            RequestBody requestBody = buildRequestBody(params);
            final Request request = new Request.Builder()
                    .cacheControl(cacheControl == null ? CacheControl.FORCE_NETWORK : cacheControl)
                    .tag(tag == null ? context.hashCode() : tag )
                    .headers(Headers.of(headers))
                    .url(url)
                    .post(requestBody)
                    .build();
            enqueue(request, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public <T> void find(Context context, String url, CacheControl cacheControl, HashMap<String, String> headers, Object tag, OnFindListener<T> listener) {
        try {
            final Request request = new Request.Builder()
                    .tag(tag == null ? context.hashCode() : tag)
                    .url(url)
                    .cacheControl(cacheControl == null ? CacheControl.FORCE_NETWORK : cacheControl)
                    .headers(Headers.of(headers))
                    .build();
            enqueue(request, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public <T> void download(String url, FileCard fileCard, OnDownloadListener<T> listener) {
        Request request = new Request.Builder().url(url).build();
        enqueue(request,listener,fileCard);
    }


    public Handler getMainHandler() {
        return mMainHandler;
    }

    private RequestBody buildRequestBody(HashMap<String, String> params) {
        JSONObject jsonObject = new JSONObject();
        try {
            if (params != null && params.size() > 0) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    jsonObject.put(entry.getKey(), entry.getValue());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buildRequestBody(jsonObject.toString());
    }


    private RequestBody buildRequestBody(String params) {
        MediaType mediaType = MediaType.parse("application/json");
        if (TextUtils.isEmpty(params)) params = "";
        return RequestBody.create(mediaType, params);
    }


    private <T> void enqueue(Request request, final RequestListener<T> listener) {
        enqueue(request, listener, null);
    }

    private <T> void enqueue(Request request, final RequestListener<T> listener, final FileCard fileCard) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null)
                            listener.onFailure(new RuntimeException("network error"));
                    }
                });
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
                                mMainHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (listener != null) {
                                            listener.onResponseError(response.code(), httpError);
                                        }
                                    }
                                });
                            }
                        }
                    }
                }
            }
        });
    }
}
