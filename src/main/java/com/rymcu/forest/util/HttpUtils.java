package com.rymcu.forest.util;

import com.alibaba.fastjson.JSON;
import jodd.http.HttpRequest;
import jodd.http.HttpResponse;

import java.util.Map;

/**
 * @author ronger
 */
public class HttpUtils {

    public static String sendGet(String url) {
        HttpRequest request = HttpRequest.get(url);
        HttpResponse response = request.send();
        return response.bodyText();
    }

    public static String sendPost(String url, Map params) {
        HttpRequest request = HttpRequest.post(url);
        request.contentType("application/json");
        request.charset("utf-8");

        //参数详情
        if (params != null) {
            request.body(JSON.toJSONString(params));
        }

        HttpResponse response = request.send();
        String respJson = response.bodyText();
        return respJson;
    }
}
