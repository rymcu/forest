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


package com.rymcu.forest.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * RabbitMQ 配置 —— 替代 Spring Event 的消息队列
 * <p>
 * 架构：
 * 1 个 Topic Exchange（forest.events）
 * 5 个持久化 Queue（按业务域划分）
 * 7 个 Routing Key（精确路由不同类型的事件）
 *
 * @author 麦小文
 */
@Configuration
public class RabbitMQConfig {

    // ==================== 交换机 ====================
    public static final String FOREST_EXCHANGE = "forest.events";

    // ==================== 队列名 ====================
    public static final String QUEUE_ARTICLE   = "forest.article";
    public static final String QUEUE_COMMENT   = "forest.comment";
    public static final String QUEUE_FOLLOW    = "forest.follow";
    public static final String QUEUE_PORTFOLIO = "forest.portfolio";
    public static final String QUEUE_ACCOUNT   = "forest.account";

    // ==================== Routing Key ====================
    public static final String RK_ARTICLE_POST   = "article.post";
    public static final String RK_ARTICLE_DELETE = "article.delete";
    public static final String RK_ARTICLE_STATUS = "article.status";
    public static final String RK_COMMENT_CREATE = "comment.create";
    public static final String RK_FOLLOW         = "follow";
    public static final String RK_PORTFOLIO      = "portfolio";
    public static final String RK_ACCOUNT_LOGIN  = "account.login";

    // ==================== 交换机 Bean ====================
    @Bean
    public TopicExchange forestExchange() {
        return new TopicExchange(FOREST_EXCHANGE, true, false);
    }

    // ==================== 队列 Bean ====================
    @Bean
    public Queue articleQueue() {
        return QueueBuilder.durable(QUEUE_ARTICLE).build();
    }

    @Bean
    public Queue commentQueue() {
        return QueueBuilder.durable(QUEUE_COMMENT).build();
    }

    @Bean
    public Queue followQueue() {
        return QueueBuilder.durable(QUEUE_FOLLOW).build();
    }

    @Bean
    public Queue portfolioQueue() {
        return QueueBuilder.durable(QUEUE_PORTFOLIO).build();
    }

    @Bean
    public Queue accountQueue() {
        return QueueBuilder.durable(QUEUE_ACCOUNT).build();
    }

    // ==================== 绑定 ====================
    @Bean
    public Binding articlePostBinding() {
        return BindingBuilder.bind(articleQueue()).to(forestExchange()).with(RK_ARTICLE_POST);
    }

    @Bean
    public Binding articleDeleteBinding() {
        return BindingBuilder.bind(articleQueue()).to(forestExchange()).with(RK_ARTICLE_DELETE);
    }

    @Bean
    public Binding articleStatusBinding() {
        return BindingBuilder.bind(articleQueue()).to(forestExchange()).with(RK_ARTICLE_STATUS);
    }

    @Bean
    public Binding commentBinding() {
        return BindingBuilder.bind(commentQueue()).to(forestExchange()).with(RK_COMMENT_CREATE);
    }

    @Bean
    public Binding followBinding() {
        return BindingBuilder.bind(followQueue()).to(forestExchange()).with(RK_FOLLOW);
    }

    @Bean
    public Binding portfolioBinding() {
        return BindingBuilder.bind(portfolioQueue()).to(forestExchange()).with(RK_PORTFOLIO);
    }

    @Bean
    public Binding accountBinding() {
        return BindingBuilder.bind(accountQueue()).to(forestExchange()).with(RK_ACCOUNT_LOGIN);
    }

    // ==================== JSON 序列化 ====================
    /**
     * 使用 Jackson 序列化事件对象为 JSON 发送到 RabbitMQ
     */
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
