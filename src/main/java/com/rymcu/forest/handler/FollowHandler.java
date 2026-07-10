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
import com.rymcu.forest.handler.event.FollowEvent;
import com.rymcu.forest.util.NotificationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;

/**
 * Created on 2023/4/28 16:07.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 * @desc : com.rymcu.forest.handler
 */
@Slf4j
@Component
public class FollowHandler {
    @RabbitListener(queues = RabbitMQConfig.QUEUE_FOLLOW)
    public void processFollowEvent(FollowEvent followEvent) throws MessagingException {
        log.info("执行关注相关事件: [{}]", followEvent);
        // 发送系统通知
        NotificationUtils.saveNotification(followEvent.getFollowingId(), followEvent.getIdFollow(), NotificationConstant.Follow, followEvent.getSummary());
        log.info("执行完成关注相关事件...");
    }
}
