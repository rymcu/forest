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


package com.rymcu.forest.auth;


import com.rymcu.forest.handler.event.AccountEvent;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import com.rymcu.forest.config.RabbitMQConfig;
import com.rymcu.forest.event.EventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 通过Redis存储和验证token的实现类
 *
 * @author ScienJus
 * @date 2015/7/31.
 */
@Component
public class RedisTokenManager implements TokenManager {

    @Autowired
    private StringRedisTemplate redisTemplate;
    @Resource
    private EventPublisher eventPublisher;

    /**
     * 生成TOKEN
     */
    @Override
    public String createToken(String id) {
        //使用 account 作为源 token
        String token = Jwts.builder().setId(id).setSubject(id).setIssuedAt(new Date()).signWith(SignatureAlgorithm.HS256, JwtConstants.JWT_SECRET).compact();
        //存储到 redis 并设置过期时间
        redisTemplate.boundValueOps(id).set(token, JwtConstants.TOKEN_EXPIRES_MINUTE, TimeUnit.MINUTES);
        return token;
    }

    @Override
    public TokenModel getToken(String token, String account) {
        return new TokenModel(account, token);
    }

    @Override
    public boolean checkToken(TokenModel model) {
        if (model == null) {
            return false;
        }
        String token = redisTemplate.boundValueOps(model.getUsername()).get();
        if (token == null || !token.equals(model.getToken())) {
            return false;
        }
        StringBuilder key = new StringBuilder();
        key.append(JwtConstants.LAST_ONLINE).append(model.getUsername());
        String result = redisTemplate.boundValueOps(key.toString()).get();
        if (StringUtils.isBlank(result)) {
            // 更新最后在线时间
            eventPublisher.publish(RabbitMQConfig.RK_ACCOUNT_LOGIN, new AccountEvent(model.getUsername()));
            redisTemplate.boundValueOps(key.toString()).set(LocalDateTime.now().toString(), JwtConstants.LAST_ONLINE_EXPIRES_MINUTE, TimeUnit.MINUTES);
        }
        return true;
    }

    @Override
    public void deleteToken(String account) {
        redisTemplate.delete(account);
    }
}
