package io.chelizi.amokhttp.download;

import java.io.File;
import java.io.InputStream;

import io.chelizi.amokhttp.RequestListener;
import io.chelizi.amokhttp.RequestManager;
import io.chelizi.amokhttp.entity.FileCard;
import io.chelizi.amokhttp.utils.FileUtils;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by Eddie on 2017/5/24.
 */

public abstract class OnDownloadListener<T> implements RequestListener<T>{

    public abstract void onProgressChanged(long progress, long total);

    @Override
    public void parseNetworkResponse(final Response response, FileCard fileCard) throws Throwable {
        ResponseBody body = response.body();
        if (body == null){
            throw new NullPointerException("response body is null");
        }else{
            InputStream is = body.byteStream();
            long contentLength = body.contentLength();
            final File file = FileUtils.saveFile(is,
                    contentLength, fileCard, new OnSaveListener() {
                @Override
                public void OnProgress(final long progress, final long total) {
                    RequestManager.getInstance().getMainHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            onProgressChanged(progress,total);
                        }
                    });
                }
            });
            RequestManager.getInstance().getMainHandler().post(new Runnable() {
                @Override
                public void run() {
                    onResponseSuccess((T)file);
                }
            });

        }

    }
}
