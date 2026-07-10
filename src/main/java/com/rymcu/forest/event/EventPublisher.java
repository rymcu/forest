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


package com.rymcu.forest.event;

import com.rymcu.forest.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import javax.annotation.Resource;

/**
 * 事件发布器 —— 替代 Spring 的 ApplicationEventPublisher
 * <p>
 * 核心设计：
 * <ul>
 *   <li>如果在事务中 → 注册 TransactionSynchronization，等事务 COMMIT 后再发消息</li>
 *   <li>如果不在事务中 → 直接发送</li>
 * </ul>
 * 完全保持了原来 @TransactionalEventListener(phase = AFTER_COMMIT) 的语义，
 * 避免"消息发出去了但数据库回滚了"的脏数据问题。
 *
 * @author 麦小文
 */
@Slf4j
@Component
public class EventPublisher {

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 发布事件到 RabbitMQ
     *
     * @param routingKey RabbitMQConfig 中定义的 RK_* 常量
     * @param event      事件对象（会被 Jackson 序列化为 JSON）
     */
    public void publish(String routingKey, Object event) {
        if (TransactionSynchronizationManager.isSynchronizationActive()) {
            // 在事务中 → 等事务提交后再发送
            TransactionSynchronizationManager.registerSynchronization(
                    new TransactionSynchronization() {
                        @Override
                        public void afterCommit() {
                            doSend(routingKey, event);
                        }
                    });
        } else {
            // 不在事务中 → 直接发送（如 RedisTokenManager 的登录事件）
            doSend(routingKey, event);
        }
    }

    private void doSend(String routingKey, Object event) {
        try {
            rabbitTemplate.convertAndSend(RabbitMQConfig.FOREST_EXCHANGE, routingKey, event);
            log.info("RabbitMQ 消息已发送: routingKey={}, event={}",
                    routingKey, event.getClass().getSimpleName());
        } catch (Exception e) {
            log.error("RabbitMQ 发送失败: routingKey={}, event={}",
                    routingKey, event.getClass().getSimpleName(), e);
        }
    }
}
