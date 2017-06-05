package io.chelizi.amokhttp.query;

import android.content.Context;

import java.util.HashMap;

import io.chelizi.amokhttp.RequestManager;
import io.chelizi.amokhttp.utils.LogUtil;
import okhttp3.CacheControl;

/**
 * Created by Eddie on 2017/5/20.
 * get query
 */
public class AMQuery<T> {

    private String url;
    private CacheControl cacheControl;
    private HashMap<String, String> headers = new HashMap<>();
    private Object tag;
    private int callMethod;

    public AMQuery setUrl(String url) {
        this.url = url;
        return this;
    }


    public AMQuery setCacheControl(CacheControl cacheControl) {
        this.cacheControl = cacheControl;
        return this;
    }

    public AMQuery setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public AMQuery setTag(Object tag) {
        this.tag = tag;
        return this;
    }

    public AMQuery setCallMethod(int callMethod) {
        this.callMethod = callMethod;
        return this;
    }

    public AMQuery openDebug(boolean debug) {
        LogUtil.openDebug(debug);
        return this;
    }

    public void findObjects(Context context, OnFindListener<T> listener) {
        RequestManager.getInstance().find(context,url,cacheControl,headers, tag,callMethod, listener);
    }
}
