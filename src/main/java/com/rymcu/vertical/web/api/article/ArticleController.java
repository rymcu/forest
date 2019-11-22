package com.rymcu.vertical.web.api.article;

import com.rymcu.vertical.core.result.GlobalResult;
import com.rymcu.vertical.core.result.GlobalResultGenerator;
import com.rymcu.vertical.service.ArticleService;
import com.rymcu.vertical.web.api.exception.MallApiException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @PostMapping("/post")
    public GlobalResult postArticle(@RequestParam(name = "idArticle",defaultValue = "0") Integer idArticle,@RequestParam(name = "articleTitle",defaultValue = "") String articleTitle,
                                    @RequestParam(name = "articleContent",defaultValue = "") String articleContent,@RequestParam(name = "articleContentHtml",defaultValue = "") String articleContentHtml,
                                    @RequestParam(name = "articleTags",defaultValue = "") String articleTags, HttpServletRequest request) throws MallApiException, UnsupportedEncodingException {
        Map map = articleService.postArticle(idArticle,articleTitle,articleContent,articleContentHtml,articleTags,request);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PutMapping("/post")
    public GlobalResult updateArticle(@RequestParam(name = "idArticle",defaultValue = "0") Integer idArticle,@RequestParam(name = "articleTitle",defaultValue = "") String articleTitle,
                                    @RequestParam(name = "articleContent",defaultValue = "") String articleContent,@RequestParam(name = "articleContentHtml",defaultValue = "") String articleContentHtml,
                                    @RequestParam(name = "articleTags",defaultValue = "") String articleTags, HttpServletRequest request) throws MallApiException, UnsupportedEncodingException {
        Map map = articleService.postArticle(idArticle,articleTitle,articleContent,articleContentHtml,articleTags,request);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @DeleteMapping("/delete/{id}")
    public GlobalResult delete(@PathVariable Integer id){
        articleService.deleteById(id.toString());
        return GlobalResultGenerator.genSuccessResult();
    }

}
