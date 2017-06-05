package io.chelizi.amokhttp.post;

import android.content.Context;
import android.text.TextUtils;

import java.util.HashMap;

import io.chelizi.amokhttp.RequestManager;
import io.chelizi.amokhttp.utils.LogUtil;
import okhttp3.CacheControl;

/**
 *  Created by Eddie on 2017/5/21.
 */

public class AMPost<T> {


    private String url;
    private HashMap<String,String> query = new HashMap<>();
    private HashMap<String, String> headers = new HashMap<>();
    private CacheControl cacheControl;
    private String params;
    private Object tag;
    private int callMethod;

    public AMPost setUrl(String url){
        this.url = url;
        return this;
    }

    public AMPost addWhereEqualTo(String key, String value){
        query.put(key,value);
        return this;
    }


    public AMPost setCacheControl(CacheControl cacheControl) {
        this.cacheControl = cacheControl;
        return this;
    }

    public AMPost setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public AMPost setTag(Object tag) {
        this.tag = tag;
        return this;
    }

    public AMPost setParams(String params) {
        this.params = params;
        return this;
    }

    public AMPost setCallMethod(int callMethod) {
        this.callMethod = callMethod;
        return this;
    }

    public AMPost openDebug(boolean debug) {
        LogUtil.openDebug(debug);
        return this;
    }

    public void addObjects(Context context, OnAddListener<T> listener){
        if (query.size() > 0){
            RequestManager.getInstance().post(context,url,cacheControl,headers,query,tag,callMethod,listener);
        }else if (!TextUtils.isEmpty(params)){
            RequestManager.getInstance().post(context,url,cacheControl,headers,params,tag,callMethod,listener);
        }
    }
}
