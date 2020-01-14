package com.rymcu.vertical.util;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.concurrent.*;

/**
 * @author ronger
 */
public class BaiDuUtils {

    @Value("${baidu.data.token}")
    private static String token;
    @Value("${baidu.data.site}")
    private static String site;

    public static void sendSEOData(String articlePermalink) {
        if (StringUtils.isBlank(articlePermalink) || StringUtils.isBlank(token)) {
            return;
        }
        ExecutorService executor= new ThreadPoolExecutor(1,1,0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<Runnable>());
        CompletableFuture.supplyAsync(()-> {
            try {
                HttpResponse response = HttpRequest.post("http://data.zz.baidu.com/urls?site=" + site + "&token=" + token).
                        header("User-Agent", "curl/7.12.1").
                        header("Host", "data.zz.baidu.com").
                        header("Content-Type", "text/plain").
                        header("Connection", "close").body(articlePermalink.getBytes(), "text/plain").timeout(30000).send();
                response.charset("UTF-8");
            } catch (Exception e){
                e.printStackTrace();
            }
            return 0;
        },executor);
    }
}
