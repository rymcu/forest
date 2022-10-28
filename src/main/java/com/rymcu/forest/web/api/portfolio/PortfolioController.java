package com.rymcu.forest.web.api.portfolio;

import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.exception.ServiceException;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.core.service.security.annotation.AuthorshipInterceptor;
import com.rymcu.forest.dto.PortfolioArticleDTO;
import com.rymcu.forest.dto.PortfolioDTO;
import com.rymcu.forest.entity.Portfolio;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.enumerate.Module;
import com.rymcu.forest.service.PortfolioService;
import com.rymcu.forest.service.UserService;
import com.rymcu.forest.util.UserUtils;
import com.rymcu.forest.web.api.exception.BaseApiException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/portfolio")
public class PortfolioController {

    @Resource
    private PortfolioService portfolioService;
    @Resource
    private UserService userService;

    @GetMapping("/detail/{idPortfolio}")
    public GlobalResult<PortfolioDTO> detail(@PathVariable Long idPortfolio, @RequestParam(defaultValue = "0") Integer type) {
        if (idPortfolio == null || idPortfolio == 0) {
            throw new IllegalArgumentException("作品集主键参数异常!");
        }
        return GlobalResultGenerator.genSuccessResult(portfolioService.findPortfolioDTOById(idPortfolio, type));
    }

    @PostMapping("/post")
    public GlobalResult<Portfolio> add(@RequestBody Portfolio portfolio) throws BaseApiException {
        User user = UserUtils.getCurrentUserByToken();
        portfolio.setPortfolioAuthorId(user.getIdUser());
        portfolio = portfolioService.postPortfolio(portfolio);
        return GlobalResultGenerator.genSuccessResult(portfolio);
    }

    @PutMapping("/post")
    @AuthorshipInterceptor(moduleName = Module.PORTFOLIO)
    public GlobalResult<Portfolio> update(@RequestBody Portfolio portfolio) throws BaseApiException {
        if (portfolio.getIdPortfolio() == null || portfolio.getIdPortfolio() == 0) {
            throw new IllegalArgumentException("作品集主键参数异常!");
        }
        User user = UserUtils.getCurrentUserByToken();
        portfolio.setPortfolioAuthorId(user.getIdUser());
        portfolio = portfolioService.postPortfolio(portfolio);
        return GlobalResultGenerator.genSuccessResult(portfolio);
    }

    @GetMapping("/{idPortfolio}/unbind-articles")
    @AuthorshipInterceptor(moduleName = Module.PORTFOLIO)
    public GlobalResult<PageInfo> unbindArticles(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows, @RequestParam(defaultValue = "") String searchText, @PathVariable Long idPortfolio) throws BaseApiException {
        if (idPortfolio == null || idPortfolio == 0) {
            throw new IllegalArgumentException("作品集主键参数异常!");
        }
        User user = UserUtils.getCurrentUserByToken();
        PageInfo pageInfo = portfolioService.findUnbindArticles(page, rows, searchText, idPortfolio, user.getIdUser());
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    @PostMapping("/bind-article")
    @AuthorshipInterceptor(moduleName = Module.PORTFOLIO)
    public GlobalResult<Boolean> bindArticle(@RequestBody PortfolioArticleDTO portfolioArticle) throws ServiceException {
        if (portfolioArticle.getIdPortfolio() == null || portfolioArticle.getIdPortfolio() == 0) {
            throw new IllegalArgumentException("作品集主键参数异常!");
        }
        boolean flag = portfolioService.bindArticle(portfolioArticle);
        return GlobalResultGenerator.genSuccessResult(flag);
    }

    @PutMapping("/update-article-sort-no")
    @AuthorshipInterceptor(moduleName = Module.PORTFOLIO)
    public GlobalResult<Boolean> updateArticleSortNo(@RequestBody PortfolioArticleDTO portfolioArticle) throws ServiceException {
        if (portfolioArticle.getIdPortfolio() == null || portfolioArticle.getIdPortfolio() == 0) {
            throw new IllegalArgumentException("作品集主键参数异常!");
        }
        if (portfolioArticle.getIdArticle() == null || portfolioArticle.getIdArticle() == 0) {
            throw new IllegalArgumentException("文章主键参数异常!");
        }
        if (portfolioArticle.getSortNo() == null) {
            throw new IllegalArgumentException("排序号不能为空!");
        }
        boolean flag = portfolioService.updateArticleSortNo(portfolioArticle);
        return GlobalResultGenerator.genSuccessResult(flag);
    }

    @DeleteMapping("/unbind-article")
    @AuthorshipInterceptor(moduleName = Module.PORTFOLIO)
    public GlobalResult<Boolean> unbindArticle(Long idArticle, Long idPortfolio) throws ServiceException {
        if (idPortfolio == null || idPortfolio == 0) {
            throw new IllegalArgumentException("作品集主键参数异常");
        }
        if (idArticle == null || idArticle == 0) {
            throw new IllegalArgumentException("文章主键参数异常");
        }
        boolean flag = portfolioService.unbindArticle(idPortfolio, idArticle);
        return GlobalResultGenerator.genSuccessResult(flag);
    }

    @DeleteMapping("/delete")
    @AuthorshipInterceptor(moduleName = Module.PORTFOLIO)
    public GlobalResult<Boolean> delete(Long idPortfolio) throws BaseApiException {
        if (idPortfolio == null || idPortfolio == 0) {
            throw new IllegalArgumentException("参数异常!");
        }
        User user = UserUtils.getCurrentUserByToken();
        Long idUser = user.getIdUser();
        Integer roleWeights = userService.findRoleWeightsByUser(idUser);
        boolean flag = portfolioService.deletePortfolio(idPortfolio, idUser, roleWeights);
        return GlobalResultGenerator.genSuccessResult(flag);
    }

}
