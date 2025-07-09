package com.rymcu.forest.util;

import jodd.http.HttpRequest;
import jodd.http.HttpResponse;
import org.apache.commons.lang.StringUtils;

import java.util.concurrent.*;

/**
 * @author ronger
 */
public class BaiDuUtils {

    private final static String TOKEN = "9cdKR6bVCJzxDEJS";

    private final static String SITE = "https://rymcu.com";

    public static void sendSEOData(String permalink) {
        if (StringUtils.isBlank(permalink) || StringUtils.isBlank(TOKEN)) {
            return;
        }
        ExecutorService executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        CompletableFuture.supplyAsync(() -> {
            try {
                HttpResponse response = HttpRequest.post("http://data.zz.baidu.com/urls?site=" + SITE + "&token=" + TOKEN).
                        header("User-Agent", "curl/7.12.1").
                        header("Host", "data.zz.baidu.com").
                        header("Content-Type", "text/plain").
                        header("Connection", "close").body(permalink.getBytes(), "text/plain").timeout(30000).send();
                response.charset("UTF-8");
                System.out.println(response.bodyText());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }, executor);
    }

    public static void sendUpdateSEOData(String permalink) {
        if (StringUtils.isBlank(permalink) || StringUtils.isBlank(TOKEN)) {
            return;
        }
        ExecutorService executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        CompletableFuture.supplyAsync(() -> {
            try {
                HttpResponse response = HttpRequest.post("http://data.zz.baidu.com/update?site=" + SITE + "&token=" + TOKEN).
                        header("User-Agent", "curl/7.12.1").
                        header("Host", "data.zz.baidu.com").
                        header("Content-Type", "text/plain").
                        header("Connection", "close").body(permalink.getBytes(), "text/plain").timeout(30000).send();
                response.charset("UTF-8");
                System.out.println(response.bodyText());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }, executor);
    }

    public static void deleteSEOData(String permalink) {
        if (StringUtils.isBlank(permalink) || StringUtils.isBlank(TOKEN)) {
            return;
        }
        ExecutorService executor = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        CompletableFuture.supplyAsync(() -> {
            try {
                HttpResponse response = HttpRequest.post("http://data.zz.baidu.com/del?site=" + SITE + "&token=" + TOKEN).
                        header("User-Agent", "curl/7.12.1").
                        header("Host", "data.zz.baidu.com").
                        header("Content-Type", "text/plain").
                        header("Connection", "close").body(permalink.getBytes(), "text/plain").timeout(30000).send();
                response.charset("UTF-8");
                System.out.println(response.bodyText());
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }, executor);
    }

    public static void main(String[] args) {
//        sendUpdateSEOData("https://rymcu.com");
        sendSEOData("https://rymcu.com/article/98");
    }
}
