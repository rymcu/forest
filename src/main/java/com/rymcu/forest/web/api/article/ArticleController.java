package com.rymcu.forest.web.api.article;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.CommentDTO;
import com.rymcu.forest.entity.Article;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.CommentService;
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

    @GetMapping("/detail/{id}")
    public GlobalResult<Map<String, Object>> detail(@PathVariable Integer id, @RequestParam(defaultValue = "2") Integer type){
        ArticleDTO articleDTO = articleService.findArticleDTOById(id,type);
        Map map = new HashMap<>(1);
        map.put("article", articleDTO);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PostMapping("/post")
    public GlobalResult postArticle(@RequestBody ArticleDTO article, HttpServletRequest request) throws BaseApiException, UnsupportedEncodingException {
        Map map = articleService.postArticle(article,request);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PutMapping("/post")
    public GlobalResult updateArticle(@RequestBody ArticleDTO article, HttpServletRequest request) throws BaseApiException, UnsupportedEncodingException {
        Map map = articleService.postArticle(article,request);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @DeleteMapping("/delete/{id}")
    public GlobalResult delete(@PathVariable Integer id){
        Map map = articleService.delete(id);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/{id}/comments")
    public GlobalResult<Map<String, Object>> commons(@PathVariable Integer id){
        List<CommentDTO> commentDTOList = commentService.getArticleComments(id);
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

    @GetMapping("/{id}/share")
    public GlobalResult share(@PathVariable Integer id) throws BaseApiException {
        Map map = articleService.share(id);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PostMapping("/{id}/update-tags")
    public GlobalResult updateTags(@PathVariable Integer id, @RequestBody Article article) throws BaseApiException, UnsupportedEncodingException {
        Map map = articleService.updateTags(id, article.getArticleTags());
        return GlobalResultGenerator.genSuccessResult(map);
    }

}
