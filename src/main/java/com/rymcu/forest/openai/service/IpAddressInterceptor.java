package com.rymcu.forest.openai.service;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;

/**
 * OkHttp Interceptor that adds an ip address header
 * @author ronger
 */
public class IpAddressInterceptor implements Interceptor {

    private final String ip;

    IpAddressInterceptor(String ip) {
        this.ip = ip;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request()
                .newBuilder()
                .header("x-forwarded-for", ip)
                .build();
        return chain.proceed(request);
    }
}
