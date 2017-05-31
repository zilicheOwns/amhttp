package io.chelizi.amokhttp.upload;

/**
 * Created by Eddie on 2017/5/24.
 */
public interface ProgressRequestListener {

    void onRequestProgress(long bytesWritten, long contentLength, boolean done);
}