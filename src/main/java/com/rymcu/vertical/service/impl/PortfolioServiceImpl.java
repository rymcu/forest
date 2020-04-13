package com.rymcu.vertical.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.dto.*;
import com.rymcu.vertical.entity.Portfolio;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.mapper.PortfolioMapper;
import com.rymcu.vertical.service.ArticleService;
import com.rymcu.vertical.service.PortfolioService;
import com.rymcu.vertical.service.UserService;
import com.rymcu.vertical.util.UserUtils;
import com.rymcu.vertical.util.Utils;
import com.rymcu.vertical.web.api.exception.BaseApiException;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ronger
 */
@Service
public class PortfolioServiceImpl extends AbstractService<Portfolio> implements PortfolioService {

    @Resource
    private PortfolioMapper portfolioMapper;
    @Resource
    private UserService userService;
    @Resource
    private ArticleService articleService;

    @Override
    public List<PortfolioDTO> findUserPortfoliosByUser(UserDTO userDTO) {
        List<PortfolioDTO> list = portfolioMapper.selectUserPortfoliosByIdUser(userDTO.getIdUser());
        Author author = new Author();
        author.setIdUser(userDTO.getIdUser());
        author.setUserAvatarURL(userDTO.getAvatarUrl());
        author.setUserNickname(userDTO.getNickname());
        list.forEach(portfolioDTO -> {
            genPortfolioAuthor(portfolioDTO,author);
        });
        return list;
    }

    @Override
    public PortfolioDTO findPortfolioDTOById(Integer idPortfolio, Integer type) {
        PortfolioDTO portfolio = portfolioMapper.selectPortfolioDTOById(idPortfolio,type);
        Author author = userService.selectAuthor(portfolio.getPortfolioAuthorId());
        genPortfolioAuthor(portfolio,author);
        Integer articleNumber = portfolioMapper.selectCountArticleNumber(portfolio.getIdPortfolio());
        portfolio.setArticleNumber(articleNumber);
        return portfolio;
    }

    @Override
    public Portfolio postPortfolio(Portfolio portfolio) throws BaseApiException {
        User user = UserUtils.getWxCurrentUser();
        if (portfolio.getIdPortfolio() == null || portfolio.getIdPortfolio() == 0) {
            portfolio.setPortfolioAuthorId(user.getIdUser());
            portfolio.setCreatedTime(new Date());
            portfolio.setUpdatedTime(portfolio.getCreatedTime());
            portfolioMapper.insertSelective(portfolio);
        } else {
            portfolio.setUpdatedTime(new Date());
            portfolioMapper.updateByPrimaryKeySelective(portfolio);
        }
        return portfolio;
    }

    @Override
    public Map findUnbindArticles(Integer page, Integer rows, String searchText, Integer idPortfolio) throws BaseApiException {
        Map map = new HashMap(1);
        User user = UserUtils.getWxCurrentUser();
        Portfolio portfolio = portfolioMapper.selectByPrimaryKey(idPortfolio);
        if (portfolio == null) {
            map.put("message", "该作品集不存在或已被删除!");
        } else {
            if (!user.getIdUser().equals(portfolio.getPortfolioAuthorId())) {
                map.put("message", "非法操作!");
            } else {
                PageHelper.startPage(page, rows);
                List<ArticleDTO> articles = articleService.selectUnbindArticles(idPortfolio,searchText,user.getIdUser());
                PageInfo<ArticleDTO> pageInfo = new PageInfo(articles);
                map = Utils.getArticlesGlobalResult(pageInfo);
            }
        }
        return map;
    }

    @Override
    public Map bindArticle(PortfolioArticleDTO portfolioArticle) {
        Map map = new HashMap(1);
        Integer count = portfolioMapper.selectCountPortfolioArticle(portfolioArticle.getIdArticle(), portfolioArticle.getIdPortfolio());
        if (count == 0) {
            Integer maxSortNo = portfolioMapper.selectMaxSortNo(portfolioArticle.getIdPortfolio());
            portfolioMapper.insertPortfolioArticle(portfolioArticle.getIdArticle(),portfolioArticle.getIdPortfolio(),maxSortNo);
            map.put("message", "绑定成功!");
        } else {
            map.put("message", "该文章已经在作品集下!!");
        }
        return map;
    }

    @Override
    public Map updateArticleSortNo(PortfolioArticleDTO portfolioArticle) {
        Map map = new HashMap(1);
        if (portfolioArticle.getIdPortfolio() == null || portfolioArticle.getIdPortfolio() == 0) {
            map.put("message", "作品集数据异常!");
        }
        if (portfolioArticle.getIdArticle() == null || portfolioArticle.getIdArticle() == 0) {
            map.put("message", "文章数据异常!");
        }
        if (portfolioArticle.getSortNo() == null) {
            map.put("message", "排序号不能为空!");
        }
        Integer result = portfolioMapper.updateArticleSortNo(portfolioArticle.getIdPortfolio(),portfolioArticle.getIdArticle(),portfolioArticle.getSortNo());
        if (result > 0) {
            map.put("message", "更新成功!");
        } else {
            map.put("message", "更新失败!");
        }
        return map;
    }

    private PortfolioDTO genPortfolioAuthor(PortfolioDTO portfolioDTO, Author author) {
        portfolioDTO.setPortfolioAuthorAvatarUrl(author.getUserAvatarURL());
        portfolioDTO.setPortfolioAuthorName(author.getUserNickname());
        portfolioDTO.setPortfolioAuthorId(author.getIdUser());
        portfolioDTO.setPortfolioAuthor(author);
        return portfolioDTO;
    }
}
