package io.chelizi.amokhttp.download;

/**
 * Created by Eddie on 2017/5/28.
 */

public interface OnSaveListener {

    void OnProgress(long progress, long total);
}
