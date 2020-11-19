package com.rymcu.forest.task;

import com.rymcu.forest.core.constant.ProjectConstant;
import com.rymcu.forest.util.BaiDuUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 * @author ronger
 */
@Component
@Slf4j
public class BaiDuCronTask {

    @Value("${resource.domain}")
    private String domain;
    @Value(("${env}"))
    private String env;

    /**
     *  定时推送首页更新
     * */
    @Scheduled(cron = "0 0 10,14,18 * * ?")
    public void pushHome() {
        if (!ProjectConstant.ENV.equals(env)) {
            BaiDuUtils.sendUpdateSEOData(domain);
        }
    }

}
