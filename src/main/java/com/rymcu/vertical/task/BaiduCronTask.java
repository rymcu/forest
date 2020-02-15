package com.rymcu.vertical.task;

import com.rymcu.vertical.util.BaiDuUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class BaiduCronTask {

    @Value("${resource.domain}")
    private String domain;

    /**
     *  定时推送首页更新
     * */
    @Scheduled(cron = "0 0 10,14,18 * * ?")
    public void pushHome() {
        BaiDuUtils.updateSEOData(domain);
    }

}
