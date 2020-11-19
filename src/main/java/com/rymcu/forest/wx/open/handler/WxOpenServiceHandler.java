package com.rymcu.forest.wx.open.handler;

import com.rymcu.forest.config.RedisProperties;
import com.rymcu.forest.wx.open.config.WxOpenProperties;
import me.chanjar.weixin.open.api.impl.WxOpenInRedisConfigStorage;
import me.chanjar.weixin.open.api.impl.WxOpenMessageRouter;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Service;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author ronger
 */
@Service
@EnableConfigurationProperties({WxOpenProperties.class, RedisProperties.class})
public class WxOpenServiceHandler extends WxOpenServiceImpl {

    private Logger logger = LoggerFactory.getLogger(getClass());

    @Resource
    private WxOpenProperties wxOpenProperties;
    @Resource
    private RedisProperties redisProperties;
    private static JedisPool pool;
    private WxOpenMessageRouter wxOpenMessageRouter;

    @PostConstruct
    public void init() {
        WxOpenInRedisConfigStorage inRedisConfigStorage = new WxOpenInRedisConfigStorage(getJedisPool());
        inRedisConfigStorage.setComponentAppId(wxOpenProperties.getComponentAppId());
        inRedisConfigStorage.setComponentAppSecret(wxOpenProperties.getComponentSecret());
        inRedisConfigStorage.setComponentToken(wxOpenProperties.getComponentToken());
        inRedisConfigStorage.setComponentAesKey(wxOpenProperties.getComponentAesKey());
        setWxOpenConfigStorage(inRedisConfigStorage);
        wxOpenMessageRouter = new WxOpenMessageRouter(this);
        wxOpenMessageRouter.rule().handler((wxMpXmlMessage, map, wxMpService, wxSessionManager) -> {
            logger.info("\n接收到 {} 公众号请求消息，内容：{}", wxMpService.getWxMpConfigStorage().getAppId(), wxMpXmlMessage);
            return null;
        }).next();
    }

    private JedisPool getJedisPool() {
        if (pool == null) {
            synchronized (WxOpenServiceHandler.class) {
                if (pool == null) {
                    pool = new JedisPool(redisProperties, redisProperties.getHost(),
                            redisProperties.getPort(), redisProperties.getConnectionTimeout(),
                            redisProperties.getSoTimeout(), redisProperties.getPassword(),
                            redisProperties.getDatabase(), redisProperties.getClientName(),
                            redisProperties.isSsl(), redisProperties.getSslSocketFactory(),
                            redisProperties.getSslParameters(), redisProperties.getHostnameVerifier());
                }
            }
        }
        return pool;
    }

    public WxOpenMessageRouter getWxOpenMessageRouter() {
        return wxOpenMessageRouter;
    }
}
