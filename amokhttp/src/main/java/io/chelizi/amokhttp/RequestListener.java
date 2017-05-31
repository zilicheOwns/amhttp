package io.chelizi.amokhttp;

import io.chelizi.amokhttp.entity.FileCard;
import io.chelizi.amokhttp.entity.HttpError;
import okhttp3.Response;

/**
 * To monitor the HTTP request, four methods are enumerated.
 * Request success, request failed, network failure, resolve server response.
 *  Created by chelizi on 17/5/28.
 */

public interface RequestListener<T> {

    /**
     * request success
     * @param response response entity
     */
    void onResponseSuccess(T response);

    /**
     * request failed.this is error from server.
     * @param code status code
     * @param httpError error entity
     * @link io.chelizi.amokhttp.entity.HttpError
     */
    void onResponseError(int code, HttpError httpError);

    /**
     * request failed.(network error)
     * @param e exception
     */
    void onFailure(Exception e);

    /**
     * parse response from server.this contains response header/json.
     * @param response response
     * @throws Throwable ex
     */
    void parseNetworkResponse(Response response, FileCard fileCard) throws Throwable;


}
