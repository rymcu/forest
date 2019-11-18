package com.rymcu.vertical.web.api.article;

import com.rymcu.vertical.core.result.GlobalResult;
import com.rymcu.vertical.core.result.GlobalResultGenerator;
import com.rymcu.vertical.service.ArticleService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @PostMapping("/post")
    public GlobalResult postArticle(@RequestParam(name = "idArticle",defaultValue = "0") Integer idArticle,@RequestParam(name = "articleTitle",defaultValue = "") String articleTitle,
                                    @RequestParam(name = "articleContent",defaultValue = "") String articleContent,@RequestParam(name = "articleContentHtml",defaultValue = "") String articleContentHtml,
                                    @RequestParam(name = "articleTags",defaultValue = "") String articleTags, HttpServletRequest request){
        Map map = articleService.postArticle(idArticle,articleTitle,articleContent,articleContentHtml,articleTags,request);
        return GlobalResultGenerator.genSuccessResult(map);
    }

}
