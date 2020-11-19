package com.rymcu.forest.core.service.redis.impl;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

/**
 * Redis Key 辅助类
 */
@Component
@Lazy(false)
public class RedisKeyHelper {
    /**
     * 应用级别前缀
     */
    private static String SYS_PREFIX = "PROJECT_";
    /**
     * 用户信息前缀
     */
    private static String UIC_PREFIX = SYS_PREFIX + "UIC_";
    /**
     * 商品中心信息前缀
     */
    private static String IC_PREFIX = SYS_PREFIX + "IC_";
    /**
     * 交易中心信息前缀
     */
    private static String TC_PREFIX = SYS_PREFIX + "TC_";
    /**
     * 优惠中心信息前缀
     */
    private static String PROM_PREFIX = SYS_PREFIX + "PROM_";
    /**
     * 会员中心信息前缀
     */
    private static String MMB_PREFIX = SYS_PREFIX + "MMB_";
    /**
     * 分布式互斥锁前缀
     */
    private static String LOCK_PREFIX = SYS_PREFIX + "LOCK_";

    /**
     * 单次登录的默认有效时长（单位：秒）
     */
    public static final int DEFAULT_LOGIN_TIMEOUT = 3600 * 24 * 7;

    /**
     * 签到记录的默认有效时长（单位：秒）
     */
    public static final int MMB_SIGN_TIMEOUT = 3600 * 12;
    /**
     * redis中加息券信息的保存时间（单位：秒）
     */
    public static final int PROM_IRC_TIMEOUT = 60 * 3;
    /**
     * redis中借款人信息默认有效效时间（单位：秒）
     */
    public static final int BORROWER_EXPIRE_TIMEOUT = 60 * 3;//redis缓存失效时间


    //商品中心KEY配置==begin========================================================================================
    /**
     * 商品（资产标）前缀
     */
    public static final String IC_ITEM_PREFIX = IC_PREFIX + "ITEM_";
    /**
     * 商品（资产标）列表前缀
     */
    public static final String IC_ITEM_LIST_PREFIX = IC_ITEM_PREFIX + "LIST_";
    /**
     * 商品已投金额或份数前缀
     */
    public static final String IC_ITEM_INVESTED_AMOUNT_PREFIX = IC_PREFIX + "INTESTED_AMT_";
    public static final String IC_ITEM_DEAL_CREDITOR_PREFIX = IC_PREFIX + "DEAL_CREDITOR_";

    //商品中心KEY配置==end==========================================================================================
    //优惠中心KEY配置==begin========================================================================================
    /**
     * 加息券信息前缀
     */
    public static String PROM_IRC_PREFIX = PROM_PREFIX + "IRC_";

    /**
     * 优惠配置信息前缀
     */
    public static String PROM_CONFIG_PREFIX = PROM_PREFIX + "CONFIG_";
    /**
     * 红包列表信息前缀
     */
    public static final String PROM_COUPON_LIST_PREFIX = PROM_PREFIX + "COUPON_LIST_";
    //优惠中心KEY配置==end==========================================================================================
    //交易中心KEY配置==========================================================================================
    public static final String TC_TRANS_ACCOUNT_PREFIX = TC_PREFIX + "ACC_";

    public static final String TC_TRANS_CURRENT_DEAL_CONFIG_PREFIX = TC_PREFIX + "CURRENT_DEAL_CONFIG_";

    public static final String TC_TRANS_EXPERIENCE_MONEY_CONFIG_PREFIX = TC_PREFIX + "EXPERIENCE_MONEY_CONFIG_";

    public static final String TC_TRANS_CURRENT_DEAL_LOAD_PREFIX = TC_PREFIX + "CURRENT_DEAL_LOAD_";

    public static final String TC_TRANS_CONST_DEAL_ORDER_LIST_PREFIX = TC_PREFIX + "CURRENT_CONST_DEAL_ORDER_LIST_";

    /**
     * 流水号redis key前缀
     */
    public static final String TC_TRANS_SEQ_PREFIX = TC_PREFIX + "SEQ_";

    //交易中心KEY配置==end==========================================================================================
    //用户中心KEY配置==========================================================================================
    /**
     * redis中登录用户token的key前缀
     */
    public static String LOGIN_TOKEN_KEY_PREFIX = UIC_PREFIX + "LOGIN_TOKEN_";
    /**
     * redis中登录用户USERID的key前缀
     */
    public static String LOGIN_UID_KEY_PREFIX = UIC_PREFIX + "LOGIN_UID_";
    /**
     * 用户信息前缀（手机号）
     */
    public static String UIC_MOBILE_PREFIX = UIC_PREFIX + "MOB_";
    /**
     * 用户角色前缀
     */
    public static String UIC_ROLE_PREFIX = UIC_PREFIX + "B_R_";
    public static String UIC_ROLE_CANCEL_SUFFIX = UIC_PREFIX + "CNL_";
    //用户中心KEY配置==end==========================================================================================

    /**
     * 构建分布式锁的key
     *
     * @param clazz
     * @param key
     * @return
     */
    public String makeLockKey(Class clazz, String key) {
        return buildKeyString(LOCK_PREFIX, clazz.getSimpleName(), key);
    }


    /**
     * 构建分布式锁的key
     *
     * @param key
     * @return
     */
    public String makeLockKey(String key) {
        return buildKeyString(LOCK_PREFIX, key);
    }

    /**
     * 构造商品信息 redis key
     *
     * @param itemId
     * @return
     */
    public String makeIcItemKey(long itemId) {
        return buildKeyString(IC_ITEM_PREFIX, itemId);
    }

    /**
     * 构造商品信息列表 redis key
     *
     * @param pageSize
     * @param pageNo
     * @return
     */
    public String makeIcItemListKey(int pageSize, int pageNo) {
        return buildKeyString(IC_ITEM_LIST_PREFIX, pageSize, pageNo);
    }

    /**
     * 构造资产标已投金额前缀
     *
     * @param itemId
     * @return
     */
    public String makeInvestedAmountKey(long itemId) {
        return buildKeyString(IC_ITEM_INVESTED_AMOUNT_PREFIX, itemId);
    }

    /**
     * 构造交易中心流水号前缀
     *
     * @param flag
     * @return
     */
    public String makeSeqKey(String flag) {
        return buildKeyString(TC_TRANS_SEQ_PREFIX, flag);
    }

    public static String buildKeyString(Object... objs) {
        if (objs == null || objs.length == 0) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (Object obj : objs) {
            if (isFirst) {
                isFirst = false;
            } else {
                builder.append("_");
            }
            if (obj instanceof Class) {
                builder.append(((Class) obj).getName());
            } else {
                builder.append(obj);
            }
        }
        return builder.toString();
    }


}
