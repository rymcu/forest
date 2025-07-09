package com.rymcu.forest.util;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.beans.BeanCopier;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author jiangjingming
 */
@Slf4j
public class BeanCopierUtil {
    /**
     * beanCopier缓存
     * (A拷贝到B,确定一个beanCopier)
     */
    private static final Map<String, BeanCopier> BEAN_COPIER_MAP = new ConcurrentHashMap<>();

    /**
     * 拷贝方法
     *
     * @param sourceBean 源对象
     * @param targetBean 目标对象
     * @param <S>        源对象类型
     * @param <T>        目标对象类型
     */
    public static <S, T> void copy(S sourceBean, T targetBean) {
        @SuppressWarnings("unchecked")
        Class<S> sourceClass = (Class<S>) sourceBean.getClass();
        @SuppressWarnings("unchecked")
        Class<T> targetClass = (Class<T>) targetBean.getClass();

        BeanCopier beanCopier = getBeanCopier(sourceClass, targetClass);
        beanCopier.copy(sourceBean, targetBean, null);
    }

    /**
     * 转换方法
     *
     * @param sourceBean 源对象
     * @param targetBean 目标对象
     * @param <S>        源对象类型
     * @param <T>        目标对象类型
     * @return 拷贝值后的targetBean
     */
    public static <S, T> T convert(S sourceBean, T targetBean) {
        try {
            assert sourceBean != null;
            copy(sourceBean, targetBean);
            return targetBean;
        } catch (Exception e) {
            log.error("Transform bean error", e);
            throw new RuntimeException(e);
        }
    }


    /**
     * beanCopier获取方法
     * <p>
     * 使用beanCopierMap重用BeanCopier对象
     * <p>
     * 线程安全
     *
     * @param sourceClass 源类型
     * @param targetClass 目标类型
     * @param <S>         源类型
     * @param <T>         目标类型
     * @return BeanCopier实例
     */
    private static <S, T> BeanCopier getBeanCopier(Class<S> sourceClass, Class<T> targetClass) {
        String classKey = sourceClass.getTypeName() + targetClass.getTypeName();
        return BEAN_COPIER_MAP.computeIfAbsent(classKey, key -> BeanCopier.create(sourceClass, targetClass, false));
    }
}
