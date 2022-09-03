package com.rymcu.forest.util;

import org.apache.shiro.cache.Cache;
import org.apache.shiro.cache.CacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Set;

/**
 * Cache工具类
 */
public class CacheUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CacheUtils.class);
	private static final CacheManager CACHE_MANAGER = SpringContextHolder.getBean(CacheManager.class);
	
	private static final String SYS_CACHE = "system";

	/**
	 * 获取SYS_CACHE缓存
	 * @param key 键值
	 * @return 缓存对象
	 */
	public static Object get(String key) {
		return get(SYS_CACHE, key);
	}
	
	/**
	 * 获取SYS_CACHE缓存
	 * @param key 键值
	 * @param defaultValue 默认返回值
	 * @return 缓存对象或默认值
	 */
	public static Object get(String key, Object defaultValue) {
		Object value = get(key);
		return value != null ? value : defaultValue;
	}
	
	/**
	 * 写入SYS_CACHE缓存
	 * @param key 键值
	 * @param value 被缓存对象
	 */
	public static void put(String key, Object value) {
		put(SYS_CACHE, key, value);
	}
	
	/**
	 * 从SYS_CACHE缓存中移除
	 * @param key 键值
	 */
	public static void remove(String key) {
		remove(SYS_CACHE, key);
	}
	
	/**
	 * 获取指定命名空间下的缓存
	 * @param cacheName 缓存命名空间
	 * @param key 键值
	 * @return 该命名空间下的对应键值缓存
	 */
	public static Object get(String cacheName, String key) {
		return getCache(cacheName).get(getKey(key));
	}
	
	/**
	 * 获取指定命名空间下的缓存，并在无缓存对象时返回默认值
	 * @param cacheName 缓存命名空间
	 * @param key 键值
	 * @param defaultValue 默认返回值
	 * @return 缓存对象或默认值
	 */
	public static Object get(String cacheName, String key, Object defaultValue) {
		Object value = get(cacheName, getKey(key));
		return value != null ? value : defaultValue;
	}
	
	/**
	 * 向指定命名空间下存入被缓存对象
	 * @param cacheName 缓存命名空间
	 * @param key 键值
	 * @param value 该命名空间下被缓存对象
	 */
	public static void put(String cacheName, String key, Object value) {
		getCache(cacheName).put(getKey(key), value);
	}

	/**
	 * 将指定命名空间下的缓存移除
	 * @param cacheName 缓存命名空间
	 * @param key 键值
	 */
	public static void remove(String cacheName, String key) {
		getCache(cacheName).remove(getKey(key));
	}

	/**
	 * 清空命名空间下所有缓存
	 * @param cacheName 命名空间
	 */
	public static void removeAll(String cacheName) {
		Cache<String, Object> cache = getCache(cacheName);
		Set<String> keys = cache.keys();
		cache.clear();
		LOGGER.info("清理缓存： {} => {}", cacheName, keys);
	}
	
	/**
	 * 获取缓存键名，多数据源下增加数据源名称前缀
	 * @param key 键值
	 * @return 返回对应数据源前缀拼接键值
	 */
	private static String getKey(String key){
//		String dsName = DataSourceHolder.getDataSourceName();
//		if (StringUtils.isNotBlank(dsName)){
//			return dsName + "_" + key;
//		}
		return key;
	}
	
	/**
	 * 获得指定命名空间的Cache，没有则显示日志。
	 * @param cacheName 命名空间
	 * @return 命名空间对应的Cache
	 */
	private static Cache<String, Object> getCache(String cacheName){
		Cache<String, Object> cache = CACHE_MANAGER.getCache(cacheName);
		if (cache == null){
			throw new RuntimeException("当前系统中没有定义“"+cacheName+"”这个缓存。");
		}
		return cache;
	}

}
