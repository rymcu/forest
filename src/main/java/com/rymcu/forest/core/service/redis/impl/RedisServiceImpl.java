package com.rymcu.forest.core.service.redis.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.rymcu.forest.config.RedisProperties;
import com.rymcu.forest.core.service.redis.RedisResult;
import com.rymcu.forest.core.service.redis.RedisService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.annotation.Resource;
import java.io.IOException;
import java.util.*;

/**
 * Redis 服务接口实现类
 *
 * @author liwei
 * 16/10/30 下午5:28
 */
@Component("redisService")
@EnableConfigurationProperties({RedisProperties.class})
public class RedisServiceImpl implements RedisService {

    private static final Logger logger = LoggerFactory.getLogger(RedisServiceImpl.class);

    private static JedisPool pool = null;

    @Resource
    private RedisProperties redisProperties;


    /**
     * 初始化操作
     */
    @Override
    @PostConstruct
    public void init() {
        if (pool != null) {
            return;
        }
        pool = getJedisPool();
    }

    private JedisPool getJedisPool() {
        if (pool == null) {
            synchronized (RedisServiceImpl.class) {
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

    @Override
    @PreDestroy
    public void destroy() {
        try {
            if (pool != null) {
                pool.destroy();
            }

        } catch (Exception e) {
            //do nothing
        }
    }

    /**
     * 从连接池里取连接（用完连接后必须销毁）
     *
     * @return
     */
    private Jedis getResource() {
        return pool.getResource();

    }

    /**
     * 用完后，销毁连接（必须）
     *
     * @param jedis
     */
    private void destroyResource(Jedis jedis) {
        if (jedis == null) {
            return;
        }
        jedis.close();
    }

    /**
     * 根据key取数据
     *
     * @param key
     * @return
     */
    @Override
    public String get(String key) {

        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return StringUtils.EMPTY;
        }
        Jedis jedis = this.getResource();
        try {
            return jedis.get(key);
        } finally {
            this.destroyResource(jedis);
        }

    }

    /**
     * 根据key取对象数据（不支持Collection数据类型）
     *
     * @param key
     * @param clazz
     * @return
     */
    @Override
    public <T> T get(String key, Class<T> clazz) {
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return null;
        }
        if (clazz == null) {
            logger.warn("Params clazz is null!");
            return null;
        }
        String value = get(key);
        if (StringUtils.isBlank(value) || StringUtils.equalsIgnoreCase(value, BLANK_CONTENT)) {
            return null;
        }
        T obj = null;
        try {
            obj = om.readValue(value, clazz);
        } catch (IOException e) {
            logger.error("Can not unserialize obj to [{}] with string [{}]", clazz.getName(), value);
        }
        return obj;
    }

    /**
     * 写入/修改 缓存内容(无论key是否存在，均会更新key对应的值)
     *
     * @param key
     * @param obj
     * @param expireTime 缓存 内容过期时间 （单位：秒） ，若expireTime小于0 则表示该内容不过期
     * @return
     */
    @Override
    public String set(String key, Object obj, long expireTime) {
        String value = RedisService.BLANK_CONTENT;
        if (obj != null) {
            try {
                value = RedisService.om.writeValueAsString(obj);
            } catch (IOException e) {
                logger.error("Can not write object to redis:" + obj.toString(), e);
            }
        }
        return set(key, value, expireTime);
    }

    /**
     * 写入/修改 缓存内容(无论key是否存在，均会更新key对应的值)
     *
     * @param key
     * @param value
     * @param expireTime 缓存 内容过期时间 （单位：秒） ，若expireTime小于0 则表示该内容不过期
     * @return
     */
    @Override
    public String set(String key, String value, long expireTime) {
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return null;
        }

        if (value == null) {

            logger.warn("Params value is null!");
            return null;
        }

        Jedis jedis = this.getResource();
        try {
            String result = jedis.set(key, value);
            if (expireTime > 0) {
                jedis.expire(key, expireTime);
            }
            return result;
        } finally {
            this.destroyResource(jedis);
        }
    }

    /**
     * 根据key取对象数据（不支持Collection数据类型）
     *
     * @param key
     * @param clazz
     * @return
     */
    @Override
    public <T> RedisResult<T> getResult(String key, Class<T> clazz) {
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return null;
        }
        if (clazz == null) {
            logger.warn("Params clazz is null!");
            return null;
        }
        RedisResult<T> redisResult = new RedisResult<T>();

        String value = get(key);
        if (StringUtils.isBlank(value)) {
            redisResult.setExist(false);
            return redisResult;
        }
        //到此步，则表明redis中存在key
        redisResult.setExist(true);
        if (StringUtils.equalsIgnoreCase(value, BLANK_CONTENT)) {
            return redisResult;
        }
        T obj = null;
        try {
            obj = om.readValue(value, clazz);
            redisResult.setResult(obj);
        } catch (IOException e) {
            logger.error("Can not unserialize obj to [{}] with string [{}]", clazz.getName(), value);
            //到此步直接视为无值
            redisResult.setExist(false);
        }
        return redisResult;
    }

    /**
     * 根据key取 Collection 对象数据
     *
     * @param key
     * @param elementClazz 集合元素类型
     * @return
     */
    @Override
    public <T> RedisResult<T> getListResult(String key, Class<T> elementClazz) {
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return null;
        }

        if (elementClazz == null) {
            logger.warn("Params elementClazz is null!");
            return null;
        }
        RedisResult<T> redisResult = new RedisResult<T>();

        String value = get(key);
        if (StringUtils.isBlank(value)) {
            redisResult.setExist(false);
            return redisResult;
        }

        //到此步，则表明redis中存在key
        redisResult.setExist(true);
        if (StringUtils.equalsIgnoreCase(value, BLANK_CONTENT)) {
            return redisResult;
        }

        List<T> list = null;
        try {
            list = om.readValue(value, getCollectionType(List.class, elementClazz));
            redisResult.setListResult(list);
        } catch (IOException e) {
            logger.error("Can not unserialize list to [{}] with string [{}]", elementClazz.getName(), value);
            //到此步直接视为无值
            redisResult.setExist(false);
        }

        return redisResult;
    }

    /**
     * 写入/修改 缓存内容
     *
     * @param key
     * @param obj
     * @return
     */
    @Override
    public String set(String key, Object obj) {
        String value = RedisService.BLANK_CONTENT;
        if (obj != null) {
            try {
                value = RedisService.om.writeValueAsString(obj);
            } catch (IOException e) {
                logger.error("Can not write object to redis:" + obj.toString(), e);
            }
        }
        return set(key, value);
    }

    private static <T> JavaType getCollectionType(Class<? extends Collection> collectionClazz,
                                                  Class<T> elementClazz) {
        return om.getTypeFactory().constructCollectionType(collectionClazz, elementClazz);
    }


    /**
     * 写入/修改 缓存内容(默认有过期时间 1小时)
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public String set(String key, String value) {
        return this.set(key, value, DEFAULT_EXPIRE_TIME);
    }


    /**
     * 写入/修改 缓存内容
     *
     * @param key
     * @param value
     * @param nxxx        缓存写入值模式 详见 {@link RedisService#NXXX_SET_IF_EXISTS}, {@link RedisService#NXXX_SET_IF_NOT_EXISTS}
     * @param expx        缓存超时时间单位 详见{@link RedisService#EXPX_SECONDS}, {@link RedisService#EXPX_MILLISECOND}
     * @param expiredTime 缓存存活时长，必须 大于0
     * @return
     */
    @Override
    public String set(String key, String value, String nxxx, String expx, long expiredTime) {
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return null;
        }

        if (value == null) {
            logger.warn("Params value is null!");
            return null;
        }

        Jedis jedis = this.getResource();
        try {
            return jedis.set(key, value);
        } finally {
            this.destroyResource(jedis);
        }

    }

    /**
     * 仅当redis中不含对应的key时，设定缓存内容
     *
     * @param key
     * @param value
     * @param expiredTime 缓存内容过期时间 （单位：秒） ，expireTime必须大于0
     * @return
     */
    @Override
    public String setnx(String key, String value, long expiredTime) {
        return this.set(key, value, NXXX_SET_IF_NOT_EXISTS, EXPX_SECONDS, expiredTime);
    }

    /**
     * 仅当redis中含有对应的key时，修改缓存内容
     *
     * @param key
     * @param value
     * @param expiredTime 缓存内容过期时间 （单位：秒） ，expireTime必须大于0
     * @return
     */
    @Override
    public String setxx(String key, String value, long expiredTime) {
        return this.set(key, value, NXXX_SET_IF_EXISTS, EXPX_SECONDS, expiredTime);
    }

    /**
     * 根据key删除缓存
     *
     * @param keys
     * @return
     */
    @Override
    public Long delete(String... keys) {
        if (keys == null || keys.length == 0) {
            logger.warn("Params keys is null or 0 length!");
            return -1L;
        }
        Jedis jedis = this.getResource();
        try {
            return jedis.del(keys);
        } finally {
            this.destroyResource(jedis);
        }
    }

    /**
     * 判断对应的key是否存在
     *
     * @param key
     * @return
     */
    @Override
    public boolean exists(String key) {
        if (StringUtils.isBlank(key)) {
            //不接受空值
            return false;
        }
        Jedis jedis = this.getResource();
        try {
            return jedis.exists(key);
        } finally {
            this.destroyResource(jedis);
        }


    }

    /**
     * redis 加法运算
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public Long incrBy(String key, long value) {
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return null;
        }
        Jedis jedis = this.getResource();
        try {
            return jedis.incrBy(key, value);
        } finally {
            this.destroyResource(jedis);
        }
    }

    /**
     * 设定redis 对应的key的剩余存活时间
     *
     * @param key
     * @param seconds
     */
    @Override
    public void setTTL(String key, long seconds) {
        if (seconds < 0) {
            return;
        }
        if (StringUtils.isBlank(key)) {
            logger.warn("Params key is blank!");
            return;
        }
        Jedis jedis = this.getResource();
        try {
            jedis.expire(key, seconds);
        } finally {
            this.destroyResource(jedis);
        }
    }

    /**
     * 根据通配符表达式查询key值的set，通配符仅支持*
     *
     * @param pattern 如 ke6*abc等
     * @return
     */
    @Override
    public Set<String> keys(String pattern) {

        if (StringUtils.isBlank(pattern)) {
            logger.warn("Params pattern is blank!");
            return Collections.emptySet();
        }
        Jedis jedis = this.getResource();
        try {
            return jedis.keys(pattern);
        } finally {
            this.destroyResource(jedis);
        }
    }


    /**
     * 将对象转为json字符串。若对象为null，则返回 {@link RedisService#BLANK_CONTENT}
     *
     * @param object
     * @return
     */
    @Override
    public String toJsonString(Object object) {
        if (object == null) {
            return BLANK_CONTENT;
        }

        if ((object instanceof Collection) && CollectionUtils.isEmpty((Collection) object)) {
            return BLANK_CONTENT;
        }

        if ((object instanceof Map) && CollectionUtils.isEmpty((Map) object)) {
            return BLANK_CONTENT;
        }

        try {
            return om.writeValueAsString(object);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public String makeSerializedString(Object value) {
        if (value == null) {
            return BLANK_CONTENT;
        }

        if ((value instanceof Collection) && ((Collection) value).size() == 0) {
            return BLANK_CONTENT;
        }

        if ((value instanceof Map) && ((Map) value).size() == 0) {
            return BLANK_CONTENT;
        }


        return JSON.toJSONString(value);
    }

    @Override
    public String put(String cacheName, String key, Object value) {
        String result = get(cacheName);
        Map map = new HashMap();
        if (StringUtils.isNotBlank(result)) {
            map = JSON.parseObject(result, new TypeReference<Map>() {
            });
        }
        map.put(key, value);
        return set(cacheName, map);
    }

    @Override
    public String put(String cacheName, String key, Object value, int expireTime) {
        String result = get(cacheName);
        Map map = new HashMap();
        if (StringUtils.isNotBlank(result)) {
            map = JSON.parseObject(result, new TypeReference<Map>() {
            });
        }
        map.put(key, value);
        return set(cacheName, map, expireTime);
    }

    @Override
    public Object get(String cacheName, String key) {
        String result = get(cacheName);
        if (StringUtils.isNotBlank(result)) {
            Map map = JSON.parseObject(result, new TypeReference<Map>() {
            });
            return map.get(key);
        }
        return null;
    }
}
