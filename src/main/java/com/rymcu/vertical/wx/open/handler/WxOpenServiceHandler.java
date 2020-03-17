package com.rymcu.vertical.wx.open.handler;

import com.rymcu.vertical.core.service.props.DynProps4FilesService;
import com.rymcu.vertical.wx.open.config.WxOpenProperties;
import me.chanjar.weixin.open.api.impl.WxOpenInRedisConfigStorage;
import me.chanjar.weixin.open.api.impl.WxOpenMessageRouter;
import me.chanjar.weixin.open.api.impl.WxOpenServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;

/**
 * @author ronger
 */
@Service
public class WxOpenServiceHandler extends WxOpenServiceImpl {

    private Logger logger = LoggerFactory.getLogger(getClass());


    @Resource
    private WxOpenProperties wxOpenProperties;
    @Resource
    private WxOpenMessageRouter wxOpenMessageRouter;
    @Resource
    private DynProps4FilesService dynProps4Files;
    @Resource
    private Environment env;
    private static JedisPool pool;

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
                    JedisPoolConfig config = new JedisPoolConfig();
                    config.setMaxIdle(dynProps4Files.getInt("REDIS_MAX_IDLE", JedisPoolConfig.DEFAULT_MAX_IDLE));
                    config.setMaxTotal(dynProps4Files.getInt("REDIS_MAX_TOTAL", JedisPoolConfig.DEFAULT_MAX_TOTAL));
                    config.setMaxWaitMillis(dynProps4Files.getLong("REDIS_MAX_WAIT", JedisPoolConfig.DEFAULT_MAX_WAIT_MILLIS));
                    config.setTestOnBorrow(true);
                    pool = new JedisPool(config, env.getProperty("spring.redis.host"),
                            dynProps4Files.getInt("REDIS_PORT", 6379), dynProps4Files.getInt(
                            "REDIS_MAX_WAIT", 1000), dynProps4Files.getProperty("REDIS_PASSWORD", env.getProperty("spring.redis.password")));
                }
            }
        }
        return pool;
    }

    public WxOpenMessageRouter getWxOpenMessageRouter() {
        return wxOpenMessageRouter;
    }
}
