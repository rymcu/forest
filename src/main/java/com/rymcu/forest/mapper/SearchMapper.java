package com.rymcu.forest.mapper;

import com.rymcu.forest.dto.SearchModel;

import java.util.List;

/**
 * @author ronger
 */
public interface SearchMapper {
    /**
     * 初始化文章搜索数据
     * @return
     */
    List<SearchModel> searchInitialArticleSearch();

    /**
     * 初始化作品集搜索数据
     * @return
     */
    List<SearchModel> searchInitialPortfolioSearch();

    /**
     * 初始化用户搜索数据
     * @return
     */
    List<SearchModel> searchInitialUserSearch();
}
