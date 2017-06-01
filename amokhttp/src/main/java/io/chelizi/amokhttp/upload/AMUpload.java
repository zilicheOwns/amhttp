package io.chelizi.amokhttp.upload;

import android.content.Context;

import java.io.File;
import java.util.HashMap;

import io.chelizi.amokhttp.RequestManager;
import okhttp3.CacheControl;

/**
 * Created by Eddie on 2017/5/24.
 */

public class AMUpload<T> {

    private String url;
    private HashMap<String, String> headers = new HashMap<>();
    private HashMap<String, String> upload = new HashMap<>();
    private CacheControl cacheControl;
    private Object tag;
    private File file;
    private String fileName;

    public AMUpload setUrl(String url) {
        this.url = url;
        return this;
    }

    public AMUpload setCacheControl(CacheControl cacheControl) {
        this.cacheControl = cacheControl;
        return this;
    }

    public AMUpload setHeaders(HashMap<String, String> headers) {
        this.headers = headers;
        return this;
    }

    public AMUpload setTag(Object tag) {
        this.tag = tag;
        return this;
    }

    public AMUpload setFile(File file) {
        this.file = file;
        return this;
    }

    public AMUpload setFileName(String fileName) {
        this.fileName = fileName;
        return this;
    }


    public AMUpload addWhereEqualTo(String key, String value) {
        upload.put(key, value);
        return this;
    }


    public void uploadObjects(Context context, OnUploadListener<T> listener) {
        RequestManager.getInstance().upload(context, url, cacheControl, headers, file, fileName, upload, tag, listener);
    }
}
