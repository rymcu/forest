package com.rymcu.forest.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.exception.BusinessException;
import com.rymcu.forest.core.exception.ServiceException;
import com.rymcu.forest.core.exception.UltraViresException;
import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.dto.*;
import com.rymcu.forest.entity.Portfolio;
import com.rymcu.forest.enumerate.FileDataType;
import com.rymcu.forest.enumerate.FilePath;
import com.rymcu.forest.enumerate.OperateType;
import com.rymcu.forest.handler.event.PortfolioEvent;
import com.rymcu.forest.mapper.PortfolioMapper;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.PortfolioService;
import com.rymcu.forest.service.UserService;
import com.rymcu.forest.util.XssUtils;
import com.rymcu.forest.web.api.common.UploadController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Resource
    private ApplicationEventPublisher applicationEventPublisher;

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
    @Transactional(rollbackFor = Exception.class)
    public Portfolio postPortfolio(Portfolio portfolio) {
        boolean isUpdate = portfolio.getIdPortfolio() != null && portfolio.getIdPortfolio() > 0;
        String headImgUrl = "";
        if (FileDataType.BASE64.equals(portfolio.getHeadImgType())) {
            headImgUrl = UploadController.uploadBase64File(portfolio.getHeadImgUrl(), FilePath.PORTFOLIO);
            portfolio.setHeadImgUrl(headImgUrl);
        }
        if (isUpdate) {
            Portfolio oldPortfolio = portfolioMapper.selectByPrimaryKey(portfolio.getIdPortfolio());
            oldPortfolio.setUpdatedTime(new Date());
            if (StringUtils.isNotBlank(headImgUrl)) {
                oldPortfolio.setHeadImgUrl(headImgUrl);
            }
            oldPortfolio.setPortfolioTitle(portfolio.getPortfolioTitle());
            oldPortfolio.setPortfolioDescriptionHtml(XssUtils.filterHtmlCode(portfolio.getPortfolioDescription()));
            oldPortfolio.setPortfolioDescription(portfolio.getPortfolioDescription());
            portfolioMapper.updateByPrimaryKeySelective(oldPortfolio);
        } else {
            portfolio.setCreatedTime(new Date());
            portfolio.setUpdatedTime(portfolio.getCreatedTime());
            portfolio.setPortfolioDescriptionHtml(XssUtils.filterHtmlCode(portfolio.getPortfolioDescription()));
            portfolioMapper.insertSelective(portfolio);
        }
        applicationEventPublisher.publishEvent(new PortfolioEvent(portfolio.getIdPortfolio(), portfolio.getPortfolioTitle(), portfolio.getPortfolioDescription(), isUpdate ? OperateType.UPDATE : OperateType.ADD));
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
    @Transactional(rollbackFor = Exception.class)
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
    @Transactional(rollbackFor = Exception.class)
    public boolean updateArticleSortNo(PortfolioArticleDTO portfolioArticle) throws ServiceException {
        Integer result = portfolioMapper.updateArticleSortNo(portfolioArticle.getIdPortfolio(), portfolioArticle.getIdArticle(), portfolioArticle.getSortNo());
        if (result == 0) {
            throw new ServiceException("更新失败!");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean unbindArticle(Long idPortfolio, Long idArticle) throws ServiceException {
        Integer result = portfolioMapper.unbindArticle(idPortfolio, idArticle);
        if (result == 0) {
            throw new ServiceException("操作失败!");
        }
        return true;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
            applicationEventPublisher.publishEvent(new PortfolioEvent(idPortfolio, null, null, OperateType.DELETE));
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
