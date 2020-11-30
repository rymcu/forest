package com.rymcu.forest.service;

import com.rymcu.forest.dto.SearchModel;

import java.util.List;

/**
 * @author ronger
 */
public interface SearchService {
    /**
     * 初始化搜索数据
     * @return
     */
    List<SearchModel> initialSearch();
}
