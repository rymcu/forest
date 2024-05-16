package com.rymcu.forest.lucene.api;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.PortfolioDTO;
import com.rymcu.forest.dto.UserDTO;
import com.rymcu.forest.lucene.model.ArticleLucene;
import com.rymcu.forest.lucene.model.PortfolioLucene;
import com.rymcu.forest.lucene.model.UserLucene;
import com.rymcu.forest.lucene.service.LuceneService;
import com.rymcu.forest.lucene.service.PortfolioLuceneService;
import com.rymcu.forest.lucene.service.UserDicService;
import com.rymcu.forest.lucene.service.UserLuceneService;
import com.rymcu.forest.lucene.util.ArticleIndexUtil;
import com.rymcu.forest.lucene.util.PortfolioIndexUtil;
import com.rymcu.forest.lucene.util.UserIndexUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * LuceneSearchController
 *
 * @author suwen
 * @date 2021/2/3 10:41
 */
@RestController
@RequestMapping("/api/v1/lucene")
@Slf4j
public class LuceneSearchController {

    @Resource
    private LuceneService luceneService;
    @Resource
    private UserLuceneService userLuceneService;
    @Resource
    private PortfolioLuceneService portfolioLuceneService;
    @Resource
    private UserDicService dicService;

    @PostConstruct
    public void createIndex() {
        // 删除系统运行时保存的索引，重新创建索引
        ArticleIndexUtil.deleteAllIndex();
        UserIndexUtil.deleteAllIndex();
        PortfolioIndexUtil.deleteAllIndex();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        CompletableFuture<String> future =
                CompletableFuture.supplyAsync(
                        () -> {
                            log.info(">>>>>>>>> 开始创建索引 <<<<<<<<<<<");
                            luceneService.writeArticle(luceneService.getAllArticleLucene());
                            userLuceneService.writeUser(userLuceneService.getAllUserLucene());
                            portfolioLuceneService.writePortfolio(portfolioLuceneService.getAllPortfolioLucene());
                            log.info(">>>>>>>>> 索引创建完毕 <<<<<<<<<<<");
                            log.info("加载用户配置的自定义扩展词典到主词库表");
                            try {
                                log.info(">>>>>>>>> 开始加载用户词典 <<<<<<<<<<<");
                                dicService.writeUserDic();
                            } catch (FileNotFoundException e) {
                                log.info("加载用户词典失败，未成功创建用户词典");
                            }
                            return ">>>>>>>>> 加载用户词典完毕 <<<<<<<<<<<";
                        },
                        executor);
        future.thenAccept(log::info);
    }

    /**
     * 文章搜索，实现高亮
     *
     * @param q
     * @return
     */
    @GetMapping("/search-article")
    public GlobalResult<?> searchArticle(
            @RequestParam String q,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer rows) {
        // 找出相关文章，相关度倒序
        List<ArticleLucene> resList = luceneService.searchArticle(q);
        // 分页组装文章详情
        int total = resList.size();
        if (total == 0) {
            return GlobalResultGenerator.genSuccessResult("未找到相关文章");
        }
        Page<ArticleDTO> articles = new Page<>(page, rows);
        articles.setTotal(total);
        int startIndex = (page - 1) * rows;
        int endIndex = Math.min(startIndex + rows, total);
        // 分割子列表
        List<ArticleLucene> subList = resList.subList(startIndex, endIndex);
        Long[] ids = subList.stream().map(ArticleLucene::getIdArticle).toArray(Long[]::new);
        List<ArticleDTO> articleDTOList = luceneService.getArticlesByIds(ids);
        ArticleDTO temp;
        // 写入文章关键词信息
        for (int i = 0; i < articleDTOList.size(); i++) {
            temp = articleDTOList.get(i);
            temp.setArticleTitle(subList.get(i).getArticleTitle());
            if (subList.get(i).getArticleContent().length() > 10) {
                // 内容中命中太少则不替换
                temp.setArticlePreviewContent(subList.get(i).getArticleContent());
            }
            articleDTOList.set(i, temp);
        }
        articles.addAll(articleDTOList);
        PageInfo<ArticleDTO> pageInfo = new PageInfo<>(articles);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 用户搜索，实现高亮
     *
     * @param q
     * @return
     */
    @GetMapping("/search-user")
    public GlobalResult<?> searchUser(
            @RequestParam String q,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer rows) {
        // 找出相关文章，相关度倒序
        List<UserLucene> resList = userLuceneService.searchUser(q);
        // 分页组装文章详情
        int total = resList.size();
        if (total == 0) {
            return GlobalResultGenerator.genSuccessResult("未找到相关用户");
        }
        Page<UserDTO> users = new Page<>(page, rows);
        users.setTotal(total);
        int startIndex = (page - 1) * rows;
        int endIndex = Math.min(startIndex + rows, total);
        // 分割子列表
        List<UserLucene> subList = resList.subList(startIndex, endIndex);
        Long[] ids = subList.stream().map(UserLucene::getIdUser).toArray(Long[]::new);
        List<UserDTO> userDTOList = userLuceneService.getUsersByIds(ids);
        UserDTO temp;
        // 写入文章关键词信息
        for (int i = 0; i < userDTOList.size(); i++) {
            temp = userDTOList.get(i);
            temp.setNickname(subList.get(i).getNickname());
            if (subList.get(i).getSignature().length() > 10) {
                // 内容中命中太少则不替换
                temp.setSignature(subList.get(i).getSignature());
            }
            userDTOList.set(i, temp);
        }
        users.addAll(userDTOList);
        PageInfo<UserDTO> pageInfo = new PageInfo<>(users);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    /**
     * 作品集搜索，实现高亮
     *
     * @param q
     * @return
     */
    @GetMapping("/search-portfolio")
    public GlobalResult<?> searchPortfolio(
            @RequestParam String q,
            @RequestParam(defaultValue = "1") Integer page,
            @RequestParam(defaultValue = "10") Integer rows) {
        // 找出相关文章，相关度倒序
        List<PortfolioLucene> resList = portfolioLuceneService.searchPortfolio(q);
        // 分页组装文章详情
        int total = resList.size();
        if (total == 0) {
            return GlobalResultGenerator.genSuccessResult("未找到相关作品集");
        }
        Page<PortfolioDTO> portfolios = new Page<>(page, rows);
        portfolios.setTotal(total);
        int startIndex = (page - 1) * rows;
        int endIndex = Math.min(startIndex + rows, total);
        // 分割子列表
        List<PortfolioLucene> subList = resList.subList(startIndex, endIndex);
        Long[] ids = subList.stream().map(PortfolioLucene::getIdPortfolio).toArray(Long[]::new);
        List<PortfolioDTO> portfolioDTOList = portfolioLuceneService.getPortfoliosByIds(ids);
        PortfolioDTO temp;
        // 写入文章关键词信息
        for (int i = 0; i < portfolioDTOList.size(); i++) {
            temp = portfolioDTOList.get(i);
            temp.setPortfolioTitle(subList.get(i).getPortfolioTitle());
            if (subList.get(i).getPortfolioDescription().length() > 10) {
                // 内容中命中太少则不替换
                temp.setPortfolioDescription(subList.get(i).getPortfolioDescription());
            }
            portfolioDTOList.set(i, temp);
        }
        portfolios.addAll(portfolioDTOList);
        PageInfo<PortfolioDTO> pageInfo = new PageInfo<>(portfolios);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }
}
