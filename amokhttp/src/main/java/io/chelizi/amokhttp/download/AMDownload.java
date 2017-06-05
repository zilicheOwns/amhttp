package io.chelizi.amokhttp.download;

import android.content.Context;

import io.chelizi.amokhttp.RequestManager;
import io.chelizi.amokhttp.entity.FileCard;
import io.chelizi.amokhttp.utils.LogUtil;

/**
 * Created by Eddie on 2017/5/24.
 */

public class AMDownload<T> {

    private String url;
    private FileCard fileCard;

    public AMDownload setUrl(String url) {
        this.url = url;
        return this;
    }

    public AMDownload setFileCard(FileCard fileCard) {
        this.fileCard = fileCard;
        return this;
    }

    public AMDownload openDebug(boolean debug) {
        LogUtil.openDebug(debug);
        return this;
    }

    public void downloadObjects(Context context, OnDownloadListener<T> listener){
        RequestManager.getInstance().download(url,fileCard,listener);
    }

}
