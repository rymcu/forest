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
import com.rymcu.forest.entity.Comment;
import com.rymcu.forest.handler.event.CommentEvent;
import com.rymcu.forest.mapper.CommentMapper;
import com.rymcu.forest.util.Html2TextUtil;
import com.rymcu.forest.util.NotificationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.MessagingException;

/**
 * Created on 2022/8/17 7:38.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 * @packageName com.rymcu.forest.handler
 */
@Slf4j
@Component
public class CommentHandler {

    private static final int MAX_PREVIEW = 200;
    @Resource
    private CommentMapper commentMapper;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_COMMENT)
    public void processCommentCreatedEvent(CommentEvent commentEvent) throws MessagingException {
        log.info("开始执行评论发布事件：[{}]", commentEvent);
        String commentContent = commentEvent.getContent();
        int length = commentContent.length();
        if (length > MAX_PREVIEW) {
            length = 200;
        }
        String commentPreviewContent = commentContent.substring(0, length);
        commentContent = Html2TextUtil.getContent(commentPreviewContent);
        // 判断是否是回复消息
        if (commentEvent.getCommentOriginalCommentId() != null && commentEvent.getCommentOriginalCommentId() != 0) {
            Comment originalComment = commentMapper.selectByPrimaryKey(commentEvent.getCommentOriginalCommentId());
            // 回复消息时,评论者不是上级评论作者则进行消息通知
            if (!commentEvent.getCommentAuthorId().equals(originalComment.getCommentAuthorId())) {
                NotificationUtils.saveNotification(originalComment.getCommentAuthorId(), commentEvent.getIdComment(), NotificationConstant.Comment, commentContent);
            }
        } else {
            // 评论者不是作者本人则进行消息通知
            if (!commentEvent.getCommentAuthorId().equals(commentEvent.getArticleAuthorId())) {
                NotificationUtils.saveNotification(commentEvent.getArticleAuthorId(), commentEvent.getIdComment(), NotificationConstant.Comment, commentContent);
            }
        }
    }
}
