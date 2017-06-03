package io.chelizi.amokhttp;

import android.text.TextUtils;

import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

/**
 * Created by Eddie on 2017/6/3.
 */

public class RequestBodyFactoryImpl implements IRequestBodyFactory {

    @Override
    public RequestBody buildRequestBody(String params) {
        RequestBody requestBody;
        if (TextUtils.isEmpty(params)){
            throw new NullPointerException("requestBodyFactory build params is null");
        }else{
            MediaType mediaType = MediaType.parse("application/json");
            requestBody = RequestBody.create(mediaType,params);
        }
        return requestBody;
    }

    @Override
    public RequestBody buildRequestBody(HashMap<String, String> params) {
        JSONObject jsonObject = new JSONObject();
        if (params == null){
            throw new NullPointerException("requestBodyFactory build params is null");
        }else{
            try {
                if (params.size() > 0) {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        jsonObject.put(entry.getKey(), entry.getValue());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return buildRequestBody(jsonObject.toString());
    }

    @Override
    public RequestBody buildRequestBody(File file, String fileName, HashMap<String, String> params) {
        MultipartBody.Builder builder = new MultipartBody.Builder();
        if (params == null){
            throw new NullPointerException("requestBodyFactory build params is null");
        }else{
            RequestBody fileBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
            try {
                if (params.size() > 0) {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        builder.addFormDataPart(entry.getKey(), entry.getValue());
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            builder.setType(MultipartBody.FORM);
            builder.addFormDataPart("file", fileName, fileBody);
        }
        return builder.build();
    }
}
