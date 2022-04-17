package com.rymcu.forest.web.api.article;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.core.service.security.annotation.AuthorshipInterceptor;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.CommentDTO;
import com.rymcu.forest.entity.Article;
import com.rymcu.forest.entity.ArticleThumbsUp;
import com.rymcu.forest.entity.Sponsor;
import com.rymcu.forest.enumerate.Module;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.ArticleThumbsUpService;
import com.rymcu.forest.service.CommentService;
import com.rymcu.forest.service.SponsorService;
import com.rymcu.forest.util.Utils;
import com.rymcu.forest.web.api.exception.BaseApiException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;
    @Resource
    private CommentService commentService;
    @Resource
    private ArticleThumbsUpService articleThumbsUpService;
    @Resource
    private SponsorService sponsorService;

    @GetMapping("/detail/{idArticle}")
    public GlobalResult<Map<String, Object>> detail(@PathVariable Integer idArticle, @RequestParam(defaultValue = "2") Integer type) {
        ArticleDTO articleDTO = articleService.findArticleDTOById(idArticle, type);
        Map map = new HashMap<>(1);
        map.put("article", articleDTO);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PostMapping("/post")
    public GlobalResult postArticle(@RequestBody ArticleDTO article, HttpServletRequest request) throws BaseApiException, UnsupportedEncodingException {
        Map map = articleService.postArticle(article, request);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PutMapping("/post")
    @AuthorshipInterceptor(moduleName = Module.ARTICLE)
    public GlobalResult updateArticle(@RequestBody ArticleDTO article, HttpServletRequest request) throws BaseApiException, UnsupportedEncodingException {
        Map map = articleService.postArticle(article, request);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @DeleteMapping("/delete/{idArticle}")
    @AuthorshipInterceptor(moduleName = Module.ARTICLE)
    public GlobalResult delete(@PathVariable Integer idArticle) throws BaseApiException {
        Map map = articleService.delete(idArticle);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/{idArticle}/comments")
    public GlobalResult<Map<String, Object>> commons(@PathVariable Integer idArticle) {
        List<CommentDTO> commentDTOList = commentService.getArticleComments(idArticle);
        Map map = new HashMap<>(1);
        map.put("comments", commentDTOList);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/drafts")
    public GlobalResult drafts(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows) throws BaseApiException {
        PageHelper.startPage(page, rows);
        List<ArticleDTO> list = articleService.findDrafts();
        PageInfo<ArticleDTO> pageInfo = new PageInfo(list);
        Map map = Utils.getArticlesGlobalResult(pageInfo);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/{idArticle}/share")
    public GlobalResult share(@PathVariable Integer idArticle) throws BaseApiException {
        Map map = articleService.share(idArticle);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PostMapping("/update-tags")
    @AuthorshipInterceptor(moduleName = Module.ARTICLE_TAG)
    public GlobalResult updateTags(@RequestBody Article article) throws BaseApiException, UnsupportedEncodingException {
        Map map = articleService.updateTags(article.getIdArticle(), article.getArticleTags());
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PostMapping("/thumbs-up")
    public GlobalResult thumbsUp(@RequestBody ArticleThumbsUp articleThumbsUp) throws BaseApiException {
        Map map = articleThumbsUpService.thumbsUp(articleThumbsUp);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PostMapping("/sponsor")
    public GlobalResult sponsor(@RequestBody Sponsor sponsor) throws Exception {
        Map map = sponsorService.sponsorship(sponsor);
        return GlobalResultGenerator.genSuccessResult(map);
    }

}
