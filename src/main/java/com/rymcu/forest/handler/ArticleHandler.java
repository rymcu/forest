package com.rymcu.forest.handler;

import com.alibaba.fastjson.JSON;
import com.rymcu.forest.core.constant.NotificationConstant;
import com.rymcu.forest.handler.event.ArticleDeleteEvent;
import com.rymcu.forest.handler.event.ArticleEvent;
import com.rymcu.forest.handler.event.ArticleStatusEvent;
import com.rymcu.forest.lucene.service.LuceneService;
import com.rymcu.forest.util.NotificationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;

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

    @TransactionalEventListener
    public void processArticlePostEvent(ArticleEvent articleEvent) {
        log.info(String.format("执行文章发布相关事件：[%s]", JSON.toJSONString(articleEvent)));
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

    @TransactionalEventListener
    public void processArticleDeleteEvent(ArticleDeleteEvent articleDeleteEvent) {
        log.info(String.format("执行文章删除相关事件：[%s]", JSON.toJSONString(articleDeleteEvent)));
        luceneService.deleteArticle(articleDeleteEvent.getIdArticle());
        log.info("执行完成文章删除相关事件...id={}", articleDeleteEvent.getIdArticle());
    }

    @TransactionalEventListener
    public void processArticleStatusEvent(ArticleStatusEvent articleStatusEvent) throws MessagingException {
        log.info(String.format("执行文章删除相关事件：[%s]", JSON.toJSONString(articleStatusEvent)));
        NotificationUtils.saveNotification(articleStatusEvent.getArticleAuthor(), articleStatusEvent.getIdArticle(), NotificationConstant.UpdateArticleStatus, articleStatusEvent.getMessage());
    }
}
