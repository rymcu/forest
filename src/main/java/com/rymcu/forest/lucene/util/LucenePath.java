package com.rymcu.forest.lucene.util;

/**
 * LucenePath lucene索引地址常量
 *
 * @author Suwen
 */
public final class LucenePath {

    /**
     * lucene 目录
     */
    public static final String INDEX_PATH = "lucene/index";

    /**
     * 文章 lucene 目录
     */
    public static final String ARTICLE_INDEX_PATH = INDEX_PATH + "/article";

    /**
     * 文章增量 lucene 目录
     */
    public static final String ARTICLE_INCREMENT_INDEX_PATH =
            ARTICLE_INDEX_PATH + "/index777";

    /**
     * 用户 lucene 目录
     */
    public static final String USER_PATH = INDEX_PATH + "/user";

    /**
     * 用户增量 lucene 目录
     */
    public static final String USER_INCREMENT_INDEX_PATH =
            USER_PATH + "/index777";

    /**
     * 作品集 lucene 目录
     */
    public static final String PORTFOLIO_PATH = INDEX_PATH + "/portfolio";

    /**
     * 作品集增量 lucene 目录
     */
    public static final String PORTFOLIO_INCREMENT_INDEX_PATH =
            PORTFOLIO_PATH + "/index777";
}
