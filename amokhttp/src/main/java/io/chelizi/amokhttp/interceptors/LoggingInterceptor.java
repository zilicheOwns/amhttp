package io.chelizi.amokhttp.interceptors;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

import io.chelizi.amokhttp.utils.log.AMLogger;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;

/**
 * Created by Eddie on 2017/6/5.
 */
public class LoggingInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        final Request request = chain.request();
        final Response response = chain.proceed(request);

        final ResponseBody responseBody = response.body();
        final long contentLength = responseBody.contentLength();

        BufferedSource source = responseBody.source();
        source.request(Long.MAX_VALUE); // Buffer the entire body.
        Buffer buffer = source.buffer();

        Charset charset = Charset.forName("UTF-8");
        MediaType contentType = responseBody.contentType();
        if (contentType != null) {
            try {
                charset = contentType.charset(charset);
            } catch (UnsupportedCharsetException e) {
                AMLogger.e("");
                AMLogger.e("Couldn't decode the response body; charset is likely malformed.");
                return response;
            }
        }

        if (contentLength != 0) {
            AMLogger.json(buffer.clone().readString(charset));
        }

        return response;
    }
}
