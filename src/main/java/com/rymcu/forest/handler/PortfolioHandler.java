package com.rymcu.forest.handler;

import com.alibaba.fastjson.JSON;
import com.rymcu.forest.handler.event.PortfolioEvent;
import com.rymcu.forest.lucene.model.PortfolioLucene;
import com.rymcu.forest.lucene.util.PortfolioIndexUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

/**
 * Created on 2024/12/22 20:39.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 * @desc : com.rymcu.forest.handler
 */
@Slf4j
@Component
public class PortfolioHandler {

    @TransactionalEventListener
    public void processPortfolioEvent(PortfolioEvent portfolioEvent) {
        log.info("执行作品集发布相关事件：[{}]", JSON.toJSONString(portfolioEvent));
        switch (portfolioEvent.getOperateType()) {
            case ADD:
                log.info("执行完成作品集发布相关事件...id={}", portfolioEvent.getIdPortfolio());
                PortfolioIndexUtil.addIndex(
                        PortfolioLucene.builder()
                                .idPortfolio(portfolioEvent.getIdPortfolio())
                                .portfolioTitle(portfolioEvent.getPortfolioTitle())
                                .portfolioDescription(portfolioEvent.getPortfolioDescription())
                                .build());
                break;
            case UPDATE:
                log.info("执行完成作品集更新相关事件...id={}", portfolioEvent.getIdPortfolio());
                PortfolioIndexUtil.updateIndex(
                        PortfolioLucene.builder()
                                .idPortfolio(portfolioEvent.getIdPortfolio())
                                .portfolioTitle(portfolioEvent.getPortfolioTitle())
                                .portfolioDescription(portfolioEvent.getPortfolioDescription())
                                .build());
                break;
            case DELETE:
                log.info("执行完成作品集删除相关事件...id={}", portfolioEvent.getIdPortfolio());
                PortfolioIndexUtil.deleteIndex(portfolioEvent.getIdPortfolio());
                break;
        }
    }
}
