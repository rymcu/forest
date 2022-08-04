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
import com.rymcu.forest.entity.User;
import com.rymcu.forest.enumerate.Module;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.ArticleThumbsUpService;
import com.rymcu.forest.service.CommentService;
import com.rymcu.forest.service.SponsorService;
import com.rymcu.forest.util.UserUtils;
import com.rymcu.forest.web.api.exception.BaseApiException;
import com.rymcu.forest.web.api.exception.ErrorCode;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

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
    public GlobalResult<ArticleDTO> detail(@PathVariable Long idArticle, @RequestParam(defaultValue = "2") Integer type) {
        ArticleDTO dto = articleService.findArticleDTOById(idArticle, type);
        return GlobalResultGenerator.genSuccessResult(dto);
    }

    @PostMapping("/post")
    public GlobalResult<Long> postArticle(@RequestBody ArticleDTO article) throws BaseApiException, UnsupportedEncodingException {
        User user = UserUtils.getCurrentUserByToken();
        if (Objects.isNull(user)) {
            throw new BaseApiException(ErrorCode.INVALID_TOKEN);
        }
        return GlobalResultGenerator.genSuccessResult(articleService.postArticle(article, user));
    }

    @PutMapping("/post")
    @AuthorshipInterceptor(moduleName = Module.ARTICLE)
    public GlobalResult<Long> updateArticle(@RequestBody ArticleDTO article) throws BaseApiException, UnsupportedEncodingException {
        User user = UserUtils.getCurrentUserByToken();
        if (Objects.isNull(user)) {
            throw new BaseApiException(ErrorCode.INVALID_TOKEN);
        }
        return GlobalResultGenerator.genSuccessResult(articleService.postArticle(article, user));
    }

    @DeleteMapping("/delete/{idArticle}")
    @AuthorshipInterceptor(moduleName = Module.ARTICLE)
    public GlobalResult<Integer> delete(@PathVariable Long idArticle) throws BaseApiException {
        return GlobalResultGenerator.genSuccessResult(articleService.delete(idArticle));
    }

    @GetMapping("/{idArticle}/comments")
    public GlobalResult<List<CommentDTO>> commons(@PathVariable Integer idArticle) {
        return GlobalResultGenerator.genSuccessResult(commentService.getArticleComments(idArticle));
    }

    @GetMapping("/drafts")
    public GlobalResult<PageInfo<ArticleDTO>> drafts(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows) throws BaseApiException {
        PageHelper.startPage(page, rows);
        User user = UserUtils.getCurrentUserByToken();
        if (Objects.isNull(user)) {
            throw new BaseApiException(ErrorCode.INVALID_TOKEN);
        }
        List<ArticleDTO> list = articleService.findDrafts(user.getIdUser());
        PageInfo<ArticleDTO> pageInfo = new PageInfo<>(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/{idArticle}/share")
    public GlobalResult<String> share(@PathVariable Integer idArticle) throws BaseApiException {
        return GlobalResultGenerator.genResult(true, articleService.share(idArticle), "");
    }

    @PostMapping("/update-tags")
    @AuthorshipInterceptor(moduleName = Module.ARTICLE_TAG)
    public GlobalResult<Boolean> updateTags(@RequestBody Article article) throws BaseApiException, UnsupportedEncodingException {
        Long idArticle = article.getIdArticle();
        String articleTags = article.getArticleTags();
        User user = UserUtils.getCurrentUserByToken();
        return GlobalResultGenerator.genSuccessResult(articleService.updateTags(idArticle, articleTags, user.getIdUser()));
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
