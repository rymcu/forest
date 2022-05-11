package com.rymcu.forest.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.dto.*;
import com.rymcu.forest.entity.Article;
import com.rymcu.forest.entity.Portfolio;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.lucene.model.PortfolioLucene;
import com.rymcu.forest.lucene.util.PortfolioIndexUtil;
import com.rymcu.forest.mapper.PortfolioMapper;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.PortfolioService;
import com.rymcu.forest.service.UserService;
import com.rymcu.forest.util.UserUtils;
import com.rymcu.forest.util.Utils;
import com.rymcu.forest.util.XssUtils;
import com.rymcu.forest.web.api.common.UploadController;
import com.rymcu.forest.web.api.exception.BaseApiException;
import org.apache.commons.lang.StringUtils;
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
        Author author = userService.selectAuthor(userDTO.getIdUser());
        list.forEach(portfolioDTO -> {
            genPortfolioAuthor(portfolioDTO,author);
            Integer articleNumber = portfolioMapper.selectCountArticleNumber(portfolioDTO.getIdPortfolio());
            portfolioDTO.setArticleNumber(articleNumber);
        });
        return list;
    }

    @Override
    public PortfolioDTO findPortfolioDTOById(Integer idPortfolio, Integer type) {
        PortfolioDTO portfolio = portfolioMapper.selectPortfolioDTOById(idPortfolio,type);
        if (portfolio == null) {
            return new PortfolioDTO();
        }
        Author author = userService.selectAuthor(portfolio.getPortfolioAuthorId());
        genPortfolioAuthor(portfolio,author);
        Integer articleNumber = portfolioMapper.selectCountArticleNumber(portfolio.getIdPortfolio());
        portfolio.setArticleNumber(articleNumber);
        return portfolio;
    }

    @Override
    public Portfolio postPortfolio(Portfolio portfolio) throws BaseApiException {
        User user = UserUtils.getCurrentUserByToken();
        if (StringUtils.isNotBlank(portfolio.getHeadImgType())) {
            String headImgUrl = UploadController.uploadBase64File(portfolio.getHeadImgUrl(), 0);
            portfolio.setHeadImgUrl(headImgUrl);
        }
        if (portfolio.getIdPortfolio() == null || portfolio.getIdPortfolio() == 0) {
            portfolio.setPortfolioAuthorId(user.getIdUser());
            portfolio.setCreatedTime(new Date());
            portfolio.setUpdatedTime(portfolio.getCreatedTime());
            portfolio.setPortfolioDescriptionHtml(XssUtils.replaceHtmlCode(portfolio.getPortfolioDescription()));
            portfolioMapper.insertSelective(portfolio);
            PortfolioIndexUtil.addIndex(
                    PortfolioLucene.builder()
                            .idPortfolio(portfolio.getIdPortfolio().toString())
                            .portfolioTitle(portfolio.getPortfolioTitle())
                            .portfolioDescription(portfolio.getPortfolioDescription())
                            .build());
        } else {
            portfolio.setUpdatedTime(new Date());
            portfolioMapper.updateByPrimaryKeySelective(portfolio);
            PortfolioIndexUtil.updateIndex(
                    PortfolioLucene.builder()
                            .idPortfolio(portfolio.getIdPortfolio().toString())
                            .portfolioTitle(portfolio.getPortfolioTitle())
                            .portfolioDescription(portfolio.getPortfolioDescription())
                            .build());
        }
        return portfolio;
    }

    @Override
    public Map findUnbindArticles(Integer page, Integer rows, String searchText, Integer idPortfolio) throws BaseApiException {
        Map map = new HashMap(1);
        User user = UserUtils.getCurrentUserByToken();
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
        if (count.equals(0)) {
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
        if (portfolioArticle.getIdPortfolio() == null || portfolioArticle.getIdPortfolio().equals(0)) {
            map.put("message", "作品集数据异常!");
        }
        if (portfolioArticle.getIdArticle() == null || portfolioArticle.getIdArticle().equals(0)) {
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

    @Override
    public Map unbindArticle(Integer idPortfolio, Integer idArticle) {
        Map map = new HashMap(1);
        if (idPortfolio == null || idPortfolio.equals(0)) {
            map.put("message", "作品集数据异常");
        }
        if (idArticle == null || idArticle.equals(0)) {
            map.put("message", "文章数据异常");
        }
        Integer result = portfolioMapper.unbindArticle(idPortfolio,idArticle);
        if (result > 0) {
            map.put("message", "操作成功!");
        } else {
            map.put("message", "操作失败!");
        }
        return map;
    }

    @Override
    public Map deletePortfolio(Integer idPortfolio) throws BaseApiException {
        Map map = new HashMap(1);
        if (idPortfolio == null || idPortfolio.equals(0)) {
            map.put("message", "作品集数据异常");
        }
        // 鉴权
        User user = UserUtils.getCurrentUserByToken();
        Integer roleWeights = userService.findRoleWeightsByUser(user.getIdUser());
        if (roleWeights > 2) {
            Portfolio portfolio = portfolioMapper.selectByPrimaryKey(idPortfolio);
            if (!user.getIdUser().equals(portfolio.getPortfolioAuthorId())) {
                map.put("message", "非法访问！");
                return map;
            }
        }

        Integer articleNumber = portfolioMapper.selectCountArticleNumber(idPortfolio);
        if (articleNumber > 0) {
            map.put("message", "该作品集已绑定文章不允许删除!");
        } else {
            Integer result = portfolioMapper.deleteByPrimaryKey(idPortfolio);
            if (result.equals(0)) {
                map.put("message", "操作失败!");
            }else {
                PortfolioIndexUtil.deleteIndex(String.valueOf(idPortfolio));
            }
        }

        return map;
    }

    @Override
    public List<PortfolioDTO> findPortfolios() {
        return portfolioMapper.selectPortfolios();
    }

    private PortfolioDTO genPortfolioAuthor(PortfolioDTO portfolioDTO, Author author) {
        portfolioDTO.setPortfolioAuthorAvatarUrl(author.getUserAvatarURL());
        portfolioDTO.setPortfolioAuthorName(author.getUserNickname());
        portfolioDTO.setPortfolioAuthorId(author.getIdUser());
        portfolioDTO.setPortfolioAuthor(author);
        return portfolioDTO;
    }
}
