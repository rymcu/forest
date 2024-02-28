package com.rymcu.forest.web.api.admin;

import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.dto.ArticleUpdateStatusDTO;
import com.rymcu.forest.entity.Article;
import com.rymcu.forest.service.ArticleService;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * Created on 2022/1/3 10:11.
 *
 * @author ronger
 * @email ronger-x@outlook.com
 */
@RestController
@RequestMapping("/api/v1/admin/article")
public class AdminArticleController {

    @Resource
    private ArticleService articleService;

    @PatchMapping("/update-perfect")
    public GlobalResult<Boolean> updatePerfect(@RequestBody Article article) {
        Long idArticle = article.getIdArticle();
        String articlePerfect = article.getArticlePerfect();
        return GlobalResultGenerator.genSuccessResult(articleService.updatePerfect(idArticle, articlePerfect));
    }

    @PatchMapping("/update-status")
    public GlobalResult<Boolean> updateArticleStatus(@RequestBody ArticleUpdateStatusDTO article) {
        Long idArticle = article.getIdArticle();
        String articleStatus = article.getArticleStatus();
        String remarks = article.getRemarks();
        return GlobalResultGenerator.genSuccessResult(articleService.updateStatus(idArticle, articleStatus, remarks));
    }
}
