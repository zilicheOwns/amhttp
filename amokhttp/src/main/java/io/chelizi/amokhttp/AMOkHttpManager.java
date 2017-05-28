package io.chelizi.amokhttp;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.chelizi.amokhttp.entity.HttpError;
import io.chelizi.amokhttp.post.OnAddListener;
import io.chelizi.amokhttp.query.OnFindListener;
import okhttp3.Call;
import okhttp3.Callback;
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

public class AMOkHttpManager {

    private static final int READ_TIMEOUT = 30;
    private static final int WRITE_TIMEOUT = 60;
    private static final int CONNECT_TIMEOUT = 60;
    private OkHttpClient mOkHttpClient;
    private static volatile AMOkHttpManager mInstance;
    private Handler mMainHandler = new Handler(Looper.getMainLooper());


    public AMOkHttpManager() {
        mOkHttpClient = new OkHttpClient.Builder()
                .readTimeout(READ_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(WRITE_TIMEOUT, TimeUnit.SECONDS)
                .connectTimeout(CONNECT_TIMEOUT, TimeUnit.SECONDS)
                .build();
    }

    public static AMOkHttpManager getInstance() {
        if (mInstance == null) {
            synchronized (AMOkHttpManager.class) {
                if (mInstance == null) {
                    mInstance = new AMOkHttpManager();
                }
            }
        }
        return mInstance;
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

    public <T> void post(String url, HashMap<String, String> params, final OnAddListener<T> listener) {
        try {
            RequestBody requestBody = buildRequestBody(params);
            final Request request = new Request.Builder().url(url).post(requestBody).build();
            enqueue(request, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public <T> void find(String url, final OnFindListener<T> listener) {
        try {
            final Request request = new Request.Builder().url(url).build();
            enqueue(request, listener);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private <T> void enqueue(Request request, final RequestListener<T> listener) {
        mOkHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@Nullable Call call, @Nullable final IOException e) {
                mMainHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null)
                            listener.onFailure(new RuntimeException("network error"));
                    }
                });
            }

            @Override
            public void onResponse(@Nullable Call call, @Nullable final Response response) throws IOException {
                if (response == null) {
                    throw new NullPointerException("okHttp response is null");
                } else {
                    if (response.isSuccessful()) {
                        try {
                            if (listener != null) {
                                listener.parseNetworkResponse(listener.getClass(), response);
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
