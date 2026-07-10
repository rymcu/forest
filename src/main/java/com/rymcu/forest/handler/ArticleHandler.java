/*
 * Copyright (c) 2020 RYMCU (https://rymcu.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package com.rymcu.forest.handler;

import com.rymcu.forest.config.RabbitMQConfig;
import com.rymcu.forest.core.constant.NotificationConstant;
import com.rymcu.forest.handler.event.ArticleDeleteEvent;
import com.rymcu.forest.handler.event.ArticleEvent;
import com.rymcu.forest.handler.event.ArticleStatusEvent;
import com.rymcu.forest.lucene.service.LuceneService;
import com.rymcu.forest.util.NotificationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;

/**
 * Created on 2022/8/16 20:42.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 */
@Slf4j
@Component
public class ArticleHandler {
    @Resource
    private LuceneService luceneService;

    /**
     * 统一的文章事件入口 —— 三种事件共用一个队列，按类型分发
     */
    @RabbitListener(queues = RabbitMQConfig.QUEUE_ARTICLE)
    public void handleArticleEvent(Object event) throws MessagingException {
        if (event instanceof ArticleEvent) {
            processArticlePostEvent((ArticleEvent) event);
        } else if (event instanceof ArticleDeleteEvent) {
            processArticleDeleteEvent((ArticleDeleteEvent) event);
        } else if (event instanceof ArticleStatusEvent) {
            processArticleStatusEvent((ArticleStatusEvent) event);
        }
    }

    private void processArticlePostEvent(ArticleEvent articleEvent) {
        log.info("执行文章发布相关事件：[{}]", articleEvent);
        // 发送系统通知
        if (articleEvent.getNotification()) {
            NotificationUtils.sendAnnouncement(articleEvent.getIdArticle(), NotificationConstant.Article, articleEvent.getArticleTitle());
        } else {
            // 发送关注通知
            StringBuilder dataSummary = new StringBuilder();
            if (articleEvent.getIsUpdate()) {
                dataSummary.append(articleEvent.getNickname()).append("更新了文章: ").append(articleEvent.getArticleTitle());
                NotificationUtils.sendArticlePush(articleEvent.getIdArticle(), NotificationConstant.UpdateArticle, dataSummary.toString(), articleEvent.getArticleAuthorId());
            } else {
                dataSummary.append(articleEvent.getNickname()).append("发布了文章: ").append(articleEvent.getArticleTitle());
                NotificationUtils.sendArticlePush(articleEvent.getIdArticle(), NotificationConstant.PostArticle, dataSummary.toString(), articleEvent.getArticleAuthorId());
            }
        }
        // 草稿不更新索引
        if (articleEvent.getIsUpdate()) {
            log.info("更新文章索引，id={}", articleEvent.getIdArticle());
            luceneService.updateArticle(articleEvent.getIdArticle());
        } else {
            log.info("写入文章索引，id={}", articleEvent.getIdArticle());
            luceneService.writeArticle(articleEvent.getIdArticle());
        }
        log.info("执行完成文章发布相关事件...id={}", articleEvent.getIdArticle());
    }

    private void processArticleDeleteEvent(ArticleDeleteEvent articleDeleteEvent) {
        log.info("执行文章删除相关事件：[{}]", articleDeleteEvent);
        luceneService.deleteArticle(articleDeleteEvent.getIdArticle());
        log.info("执行完成文章删除相关事件...id={}", articleDeleteEvent.getIdArticle());
    }

    private void processArticleStatusEvent(ArticleStatusEvent articleStatusEvent) throws MessagingException {
        log.info("执行文章状态变更相关事件：[{}]", articleStatusEvent);
        NotificationUtils.saveNotification(articleStatusEvent.getArticleAuthor(), articleStatusEvent.getIdArticle(), NotificationConstant.UpdateArticleStatus, articleStatusEvent.getMessage());
    }
}
