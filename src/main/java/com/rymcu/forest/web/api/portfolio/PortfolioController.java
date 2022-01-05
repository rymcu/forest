package com.rymcu.forest.web.api.portfolio;

import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.core.service.security.annotation.AuthorshipInterceptor;
import com.rymcu.forest.dto.PortfolioArticleDTO;
import com.rymcu.forest.dto.PortfolioDTO;
import com.rymcu.forest.entity.Portfolio;
import com.rymcu.forest.enumerate.Module;
import com.rymcu.forest.service.PortfolioService;
import com.rymcu.forest.web.api.exception.BaseApiException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/portfolio")
public class PortfolioController {

    @Resource
    private PortfolioService portfolioService;

    @GetMapping("/detail/{idPortfolio}")
    public GlobalResult detail(@PathVariable Integer idPortfolio,@RequestParam(defaultValue = "0") Integer type) {
        PortfolioDTO portfolio = portfolioService.findPortfolioDTOById(idPortfolio, type);
        Map map = new HashMap<>(1);
        map.put("portfolio", portfolio);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PostMapping("/post")
    public GlobalResult add(@RequestBody Portfolio portfolio) throws BaseApiException {
        portfolio = portfolioService.postPortfolio(portfolio);
        return GlobalResultGenerator.genSuccessResult(portfolio);
    }

    @PutMapping("/post")
    @AuthorshipInterceptor(moduleName = Module.PORTFOLIO)
    public GlobalResult update(@RequestBody Portfolio portfolio) throws BaseApiException {
        portfolio = portfolioService.postPortfolio(portfolio);
        return GlobalResultGenerator.genSuccessResult(portfolio);
    }

    @GetMapping("/{idPortfolio}/unbind-articles")
    @AuthorshipInterceptor(moduleName = Module.PORTFOLIO)
    public GlobalResult unbindArticles(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows, @RequestParam(defaultValue = "") String searchText,@PathVariable Integer idPortfolio) throws BaseApiException {
        Map map = portfolioService.findUnbindArticles(page, rows, searchText, idPortfolio);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PostMapping("/bind-article")
    @AuthorshipInterceptor(moduleName = Module.PORTFOLIO)
    public GlobalResult bindArticle(@RequestBody PortfolioArticleDTO portfolioArticle) {
        Map map = portfolioService.bindArticle(portfolioArticle);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PutMapping("/update-article-sort-no")
    @AuthorshipInterceptor(moduleName = Module.PORTFOLIO)
    public GlobalResult updateArticleSortNo(@RequestBody PortfolioArticleDTO portfolioArticle) {
        Map map = portfolioService.updateArticleSortNo(portfolioArticle);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @DeleteMapping("/unbind-article")
    @AuthorshipInterceptor(moduleName = Module.PORTFOLIO)
    public GlobalResult unbindArticle(Integer idArticle,Integer idPortfolio) {
        Map map = portfolioService.unbindArticle(idPortfolio,idArticle);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @DeleteMapping("/delete")
    @AuthorshipInterceptor(moduleName = Module.PORTFOLIO)
    public GlobalResult delete(Integer idPortfolio) throws BaseApiException {
        Map map = portfolioService.deletePortfolio(idPortfolio);
        return GlobalResultGenerator.genSuccessResult(map);
    }

}
