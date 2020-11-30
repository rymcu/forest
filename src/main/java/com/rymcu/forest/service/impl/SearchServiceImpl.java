package com.rymcu.forest.service.impl;

import com.rymcu.forest.core.service.redis.RedisResult;
import com.rymcu.forest.core.service.redis.RedisService;
import com.rymcu.forest.dto.SearchModel;
import com.rymcu.forest.mapper.SearchMapper;
import com.rymcu.forest.service.SearchService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author ronger
 */
@Service
public class SearchServiceImpl implements SearchService {

    @Resource
    private SearchMapper searchMapper;
    @Resource
    private RedisService redisService;

    @Override
    public List<SearchModel> initialSearch() {
        String searchKey = "initialSearch";
        RedisResult<SearchModel> result = redisService.getListResult(searchKey, SearchModel.class);
        if (Objects.nonNull(result.getListResult())) {
            return result.getListResult();
        }
        List<SearchModel> list = new ArrayList<>();
        List<SearchModel> articleSearchModels = searchMapper.searchInitialArticleSearch();
        List<SearchModel> portfolioSearchModels = searchMapper.searchInitialPortfolioSearch();
        List<SearchModel> userSearchModels = searchMapper.searchInitialUserSearch();
        list.addAll(articleSearchModels);
        list.addAll(portfolioSearchModels);
        list.addAll(userSearchModels);
        redisService.set(searchKey, list, 24 * 60 * 60);
        return list;
    }
}
