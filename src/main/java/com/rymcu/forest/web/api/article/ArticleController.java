package com.rymcu.forest.web.api.article;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.exception.BusinessException;
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
import org.apache.shiro.authz.annotation.Logical;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.authz.annotation.RequiresRoles;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;
import java.util.List;
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
    @RequiresPermissions(value = "user")
    public GlobalResult<Long> postArticle(@RequestBody ArticleDTO article) throws UnsupportedEncodingException {
        User user = UserUtils.getCurrentUserByToken();
        return GlobalResultGenerator.genSuccessResult(articleService.postArticle(article, user));
    }

    @PutMapping("/post")
    @AuthorshipInterceptor(moduleName = Module.ARTICLE)
    public GlobalResult<Long> updateArticle(@RequestBody ArticleDTO article) throws UnsupportedEncodingException {
        User user = UserUtils.getCurrentUserByToken();
        return GlobalResultGenerator.genSuccessResult(articleService.postArticle(article, user));
    }

    @DeleteMapping("/delete/{idArticle}")
    @AuthorshipInterceptor(moduleName = Module.ARTICLE)
    public GlobalResult<Integer> delete(@PathVariable Long idArticle) {
        return GlobalResultGenerator.genSuccessResult(articleService.delete(idArticle));
    }

    @GetMapping("/{idArticle}/comments")
    public GlobalResult<PageInfo<CommentDTO>> commons(@PathVariable Integer idArticle, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer rows) {
        PageHelper.startPage(page, rows);
        List<CommentDTO> list = commentService.getArticleComments(idArticle);
        PageInfo<CommentDTO> pageInfo = new PageInfo<>(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/drafts")
    @RequiresPermissions(value = "user")
    public GlobalResult<PageInfo<ArticleDTO>> drafts(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows) {
        PageHelper.startPage(page, rows);
        User user = UserUtils.getCurrentUserByToken();
        List<ArticleDTO> list = articleService.findDrafts(user.getIdUser());
        PageInfo<ArticleDTO> pageInfo = new PageInfo<>(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

    @GetMapping("/{idArticle}/share")
    @RequiresPermissions(value = "user")
    public GlobalResult<String> share(@PathVariable Integer idArticle) {
        User user = UserUtils.getCurrentUserByToken();
        return GlobalResultGenerator.genResult(true, articleService.share(idArticle, user.getAccount()), "");
    }

    @PostMapping("/update-tags")
    @RequiresPermissions(value = "user")
    @AuthorshipInterceptor(moduleName = Module.ARTICLE_TAG)
    public GlobalResult<Boolean> updateTags(@RequestBody Article article) throws UnsupportedEncodingException {
        Long idArticle = article.getIdArticle();
        String articleTags = article.getArticleTags();
        User user = UserUtils.getCurrentUserByToken();
        return GlobalResultGenerator.genSuccessResult(articleService.updateTags(idArticle, articleTags, user.getIdUser()));
    }

    @PostMapping("/thumbs-up")
    @RequiresPermissions(value = "user")
    public GlobalResult<Integer> thumbsUp(@RequestBody ArticleThumbsUp articleThumbsUp) {
        if (Objects.isNull(articleThumbsUp) || Objects.isNull(articleThumbsUp.getIdArticle())) {
            throw new BusinessException("数据异常,文章不存在!");
        }
        User user = UserUtils.getCurrentUserByToken();
        articleThumbsUp.setIdUser(user.getIdUser());
        return GlobalResultGenerator.genSuccessResult(articleThumbsUpService.thumbsUp(articleThumbsUp));
    }

    @PostMapping("/sponsor")
    @RequiresPermissions(value = "user")
    public GlobalResult<Boolean> sponsor(@RequestBody Sponsor sponsor) {
        if (Objects.isNull(sponsor) || Objects.isNull(sponsor.getDataId()) || Objects.isNull(sponsor.getDataType())) {
            throw new IllegalArgumentException("数据异常");
        }
        User user = UserUtils.getCurrentUserByToken();
        sponsor.setSponsor(user.getIdUser());
        Boolean flag = sponsorService.sponsorship(sponsor);
        return GlobalResultGenerator.genSuccessResult(flag);
    }

}
