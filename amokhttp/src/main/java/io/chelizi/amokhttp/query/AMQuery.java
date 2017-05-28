package io.chelizi.amokhttp.query;

import android.content.Context;

import io.chelizi.amokhttp.AMOkHttpManager;

/**
 * Created by Eddie on 2017/5/20.
 * get query
 */

public class AMQuery<T> {

    private String url;

    public AMQuery setUrl(String url) {
        this.url = url;
        return this;
    }


    public void findObjects(Context context, OnFindListener<T> listener) {
        AMOkHttpManager.getInstance().find(context, url, listener);
    }
}
