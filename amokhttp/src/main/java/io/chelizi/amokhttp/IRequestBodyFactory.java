package io.chelizi.amokhttp;

import java.io.File;
import java.util.HashMap;

import okhttp3.RequestBody;

/**
 * Created by Eddie on 2017/6/3.
 */

public interface IRequestBodyFactory {

    RequestBody buildRequestBody(String params);

    RequestBody buildRequestBody(HashMap<String, String> params);

    RequestBody buildRequestBody(File file, String fileName, HashMap<String, String> params);
}
