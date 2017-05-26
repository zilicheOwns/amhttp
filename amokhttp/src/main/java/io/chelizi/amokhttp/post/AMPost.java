package io.chelizi.amokhttp.post;

import android.content.Context;

import java.util.HashMap;

import io.chelizi.amokhttp.AMOkHttpManager;

/**
 *  Created by Eddie on 2017/5/21.
 */

public class AMPost<T> {


    private String url;
    private HashMap<String,String> query = new HashMap<>();

    public AMPost setUrl(String url){
        this.url = url;
        return this;
    }

    public AMPost addWhereEqualTo(String key, String value){
        query.put(key,value);
        return this;
    }


    public void addObjects(Context context, OnAddListener<T> listener){
        AMOkHttpManager.getInstance().post(url,query,listener);
    }
}
