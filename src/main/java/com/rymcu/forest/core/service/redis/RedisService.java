package com.rymcu.forest.core.service.redis;


import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.Set;

/**
 * Redis 服务接口
 *
 * @author Jimersy Lee
 * 2017-09-18 14:58:21
 */
public interface RedisService {
    /**
     * NX: 当且仅当缓存中特定的key不存在时设定数据
     */
    String NXXX_SET_IF_NOT_EXISTS = "nx";
    /**
     * XX: 当且仅当缓存中特定的key存在时设定数据
     */
    String NXXX_SET_IF_EXISTS = "xx";

    /**
     * EX:缓存失效的时间单位：秒
     */
    String EXPX_SECONDS = "ex";

    /**
     * EX:缓存失效的时间单位：毫秒
     */
    String EXPX_MILLISECOND = "px";

    /**
     * 默认过期时间 ，3600(秒)
     */
    int DEFAULT_EXPIRE_TIME = 3600;

    ObjectMapper om = new ObjectMapper();


    /**
     * 空白占位符
     */
    String BLANK_CONTENT = "__BLANK__";

    /**
     * 初始化操作
     */
    void init();


    void destroy();

    //    /**
    //     * 从连接池里取连接（用完连接后必须销毁）
    //     *
    //     * @return
    //     */
    //     Jedis getResource();

    //    /**
    //     * 用完后，销毁连接（必须）
    //     *
    //     * @param jedis
    //     */
    //    void destroyResource(Jedis jedis);

    /**
     * 根据key取数据
     *
     * @param key
     * @return
     */
    String get(String key);


    /**
     * 根据key取对象数据（不支持Collection数据类型）
     *
     * @param key
     * @param clazz
     * @return
     */
    <T> T get(String key, Class<T> clazz);


    /**
     * 根据key取对象数据（不支持Collection数据类型）
     *
     * @param key
     * @param clazz
     * @param <T>
     * @return
     */
    <T> RedisResult<T> getResult(String key, Class<T> clazz);

    /**
     * 根据key取 Collection 对象数据
     *
     * @param key
     * @param elementClazz 集合元素类型
     * @param <T>
     * @return
     */
    <T> RedisResult<T> getListResult(String key, Class<T> elementClazz);


    /**
     * 写入/修改 缓存内容
     *
     * @param key
     * @param obj
     * @return
     */
    String set(String key, Object obj);


    /**
     * 写入/修改 缓存内容
     *
     * @param key
     * @param value
     * @return
     */
    String set(String key, String value);


    /**
     * 写入/修改 缓存内容(无论key是否存在，均会更新key对应的值)
     *
     * @param key
     * @param obj
     * @param expireTime 缓存内容过期时间 （单位：秒） ，若expireTime小于0 则表示该内容不过期
     * @return
     */
    String set(String key, Object obj, long expireTime);


    /**
     * 写入/修改 缓存内容(无论key是否存在，均会更新key对应的值)
     *
     * @param key
     * @param value
     * @param expireTime 缓存内容过期时间 （单位：秒） ，若expireTime小于0 则表示该内容不过期
     * @return
     */
    String set(String key, String value, long expireTime);

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
    String set(String key, String value, String nxxx, String expx, long expiredTime);

    /**
     * 仅当redis中不含对应的key时，设定缓存内容
     *
     * @param key
     * @param value
     * @param expiredTime 缓存内容过期时间 （单位：秒） ，若expireTime小于0 则表示该内容不过期
     * @return
     */
    String setnx(String key, String value, long expiredTime);

    /**
     * 仅当redis中含有对应的key时，修改缓存内容
     *
     * @param key
     * @param value
     * @param expiredTime 缓存内容过期时间 （单位：秒） ，若expireTime小于0 则表示该内容不过期
     * @return
     */
    String setxx(String key, String value, long expiredTime);


    /**
     * 根据key删除缓存,
     *
     * @param keys
     * @return
     */
    Long delete(String... keys);

    /**
     * 判断对应的key是否存在
     *
     * @param key
     * @return
     */
    boolean exists(String key);


    /**
     * redis 加法运算
     *
     * @param key
     * @param value
     * @return 运算结果
     */
    Long incrBy(String key, long value);

    /**
     * 设定redis 对应的key的剩余存活时间
     *
     * @param key
     * @param seconds
     */
    void setTTL(String key, long seconds);

    /**
     * 根据通配符表达式查询key值的set，通配符仅支持*
     *
     * @param pattern 如 ke6*abc等
     * @return
     */
    Set<String> keys(String pattern);

    /**
     * 将对象转为json字符串。若对象为null，则返回 {@link RedisService#BLANK_CONTENT}
     *
     * @param object
     * @return
     */
    String toJsonString(Object object);

    /**
     * json序列化对象。
     *
     * @param value
     * @return 返回序列化后的字符串。若value为null，则返回 {@link RedisService#BLANK_CONTENT}
     */
    String makeSerializedString(Object value);

    /**
     * 写入/修改 缓存内容(无论key是否存在，均会更新key对应的值)
     *
     * @param cacheName
     * @param key
     * @param value
     * @return
     */
    String put(String cacheName, String key, Object value);

    /**
     * 写入/修改 缓存内容(无论key是否存在，均会更新key对应的值)
     *
     * @param cacheName
     * @param key
     * @param value
     * @param expireTime 缓存 内容过期时间 （单位：秒） ，若expireTime小于0 则表示该内容不过期
     * @return
     */
    String put(String cacheName, String key, Object value, int expireTime);

    Object get(String cacheName, String key);


}
