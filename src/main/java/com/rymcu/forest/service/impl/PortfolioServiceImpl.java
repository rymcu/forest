package com.rymcu.forest.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.exception.BusinessException;
import com.rymcu.forest.core.exception.ServiceException;
import com.rymcu.forest.core.exception.UltraViresException;
import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.dto.*;
import com.rymcu.forest.entity.Portfolio;
import com.rymcu.forest.enumerate.FilePath;
import com.rymcu.forest.enumerate.FileDataType;
import com.rymcu.forest.lucene.model.PortfolioLucene;
import com.rymcu.forest.lucene.util.PortfolioIndexUtil;
import com.rymcu.forest.mapper.PortfolioMapper;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.PortfolioService;
import com.rymcu.forest.service.UserService;
import com.rymcu.forest.util.XssUtils;
import com.rymcu.forest.web.api.common.UploadController;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

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
            genPortfolioAuthor(portfolioDTO, author);
            Integer articleNumber = portfolioMapper.selectCountArticleNumber(portfolioDTO.getIdPortfolio());
            portfolioDTO.setArticleNumber(articleNumber);
        });
        return list;
    }

    @Override
    public PortfolioDTO findPortfolioDTOById(Long idPortfolio, Integer type) {
        PortfolioDTO portfolio = portfolioMapper.selectPortfolioDTOById(idPortfolio, type);
        if (portfolio == null) {
            return new PortfolioDTO();
        }
        Author author = userService.selectAuthor(portfolio.getPortfolioAuthorId());
        genPortfolioAuthor(portfolio, author);
        Integer articleNumber = portfolioMapper.selectCountArticleNumber(portfolio.getIdPortfolio());
        portfolio.setArticleNumber(articleNumber);
        return portfolio;
    }

    @Override
    public Portfolio postPortfolio(Portfolio portfolio) {
        if (FileDataType.BASE64.equals(portfolio.getHeadImgType())) {
            String headImgUrl = UploadController.uploadBase64File(portfolio.getHeadImgUrl(), FilePath.PORTFOLIO);
            portfolio.setHeadImgUrl(headImgUrl);
        }
        if (portfolio.getIdPortfolio() == null || portfolio.getIdPortfolio() == 0) {
            portfolio.setCreatedTime(new Date());
            portfolio.setUpdatedTime(portfolio.getCreatedTime());
            portfolio.setPortfolioDescriptionHtml(XssUtils.filterHtmlCode(portfolio.getPortfolioDescription()));
            portfolioMapper.insertSelective(portfolio);
            PortfolioIndexUtil.addIndex(
                    PortfolioLucene.builder()
                            .idPortfolio(portfolio.getIdPortfolio())
                            .portfolioTitle(portfolio.getPortfolioTitle())
                            .portfolioDescription(portfolio.getPortfolioDescription())
                            .build());
        } else {
            portfolio.setUpdatedTime(new Date());
            portfolioMapper.updateByPrimaryKeySelective(portfolio);
            PortfolioIndexUtil.updateIndex(
                    PortfolioLucene.builder()
                            .idPortfolio(portfolio.getIdPortfolio())
                            .portfolioTitle(portfolio.getPortfolioTitle())
                            .portfolioDescription(portfolio.getPortfolioDescription())
                            .build());
        }
        return portfolio;
    }

    @Override
    public PageInfo<ArticleDTO> findUnbindArticles(Integer page, Integer rows, String searchText, Long idPortfolio, Long idUser) {
        Portfolio portfolio = portfolioMapper.selectByPrimaryKey(idPortfolio);
        if (portfolio == null) {
            throw new BusinessException("该作品集不存在或已被删除!");
        } else {
            if (!idUser.equals(portfolio.getPortfolioAuthorId())) {
                throw new UltraViresException("非法操作!");
            } else {
                PageHelper.startPage(page, rows);
                List<ArticleDTO> articles = articleService.selectUnbindArticles(idPortfolio, searchText, idUser);
                return new PageInfo<>(articles);
            }
        }
    }

    @Override
    public boolean bindArticle(PortfolioArticleDTO portfolioArticle) throws ServiceException {
        Integer count = portfolioMapper.selectCountPortfolioArticle(portfolioArticle.getIdArticle(), portfolioArticle.getIdPortfolio());
        if (count.equals(0)) {
            Integer maxSortNo = portfolioMapper.selectMaxSortNo(portfolioArticle.getIdPortfolio());
            Integer result = portfolioMapper.insertPortfolioArticle(portfolioArticle.getIdArticle(), portfolioArticle.getIdPortfolio(), maxSortNo);
            if (result == 0) {
                throw new ServiceException("更新失败!");
            }
        } else {
            throw new BusinessException("该文章已经在作品集下!!");
        }
        return true;
    }

    @Override
    public boolean updateArticleSortNo(PortfolioArticleDTO portfolioArticle) throws ServiceException {
        Integer result = portfolioMapper.updateArticleSortNo(portfolioArticle.getIdPortfolio(), portfolioArticle.getIdArticle(), portfolioArticle.getSortNo());
        if (result == 0) {
            throw new ServiceException("更新失败!");
        }
        return true;
    }

    @Override
    public boolean unbindArticle(Long idPortfolio, Long idArticle) throws ServiceException {
        Integer result = portfolioMapper.unbindArticle(idPortfolio, idArticle);
        if (result == 0) {
            throw new ServiceException("操作失败!");
        }
        return true;
    }

    @Override
    public boolean deletePortfolio(Long idPortfolio, Long idUser, Integer roleWeights) {
        if (idPortfolio == null || idPortfolio == 0) {
            throw new IllegalArgumentException("作品集数据异常！");
        }
        // 鉴权
        if (roleWeights > 2) {
            Portfolio portfolio = portfolioMapper.selectByPrimaryKey(idPortfolio);
            if (!idUser.equals(portfolio.getPortfolioAuthorId())) {
                throw new UltraViresException("非法访问！");
            }
        }

        Integer articleNumber = portfolioMapper.selectCountArticleNumber(idPortfolio);
        if (articleNumber > 0) {
            throw new BusinessException("该作品集已绑定文章不允许删除！");
        } else {
            Integer result = portfolioMapper.deleteByPrimaryKey(idPortfolio);
            if (result.equals(0)) {
                throw new BusinessException("操作失败！");
            }
            PortfolioIndexUtil.deleteIndex(idPortfolio);
            return true;
        }
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
