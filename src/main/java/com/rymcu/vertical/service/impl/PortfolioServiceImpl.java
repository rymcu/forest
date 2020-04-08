package com.rymcu.vertical.service.impl;

import com.rymcu.vertical.core.service.AbstractService;
import com.rymcu.vertical.dto.Author;
import com.rymcu.vertical.dto.PortfolioDTO;
import com.rymcu.vertical.dto.UserDTO;
import com.rymcu.vertical.entity.Portfolio;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.mapper.PortfolioMapper;
import com.rymcu.vertical.service.PortfolioService;
import com.rymcu.vertical.service.UserService;
import com.rymcu.vertical.util.UserUtils;
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
    public PortfolioDTO findPortfolioDTOById(Integer id) {
        PortfolioDTO portfolio = portfolioMapper.selectPortfolioDTOById(id);
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

    private PortfolioDTO genPortfolioAuthor(PortfolioDTO portfolioDTO, Author author) {
        portfolioDTO.setPortfolioAuthorAvatarUrl(author.getUserAvatarURL());
        portfolioDTO.setPortfolioAuthorName(author.getUserNickname());
        portfolioDTO.setPortfolioAuthorId(author.getIdUser());
        portfolioDTO.setPortfolioAuthor(author);
        return portfolioDTO;
    }
}
