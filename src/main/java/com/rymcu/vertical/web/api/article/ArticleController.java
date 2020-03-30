package com.rymcu.vertical.web.api.article;

import com.rymcu.vertical.core.result.GlobalResult;
import com.rymcu.vertical.core.result.GlobalResultGenerator;
import com.rymcu.vertical.dto.ArticleDTO;
import com.rymcu.vertical.service.ArticleService;
import com.rymcu.vertical.web.api.exception.BaseApiException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;



    @GetMapping("/detail/{id}")
    public GlobalResult<Map<String, Object>> detail(@PathVariable Integer id){
        ArticleDTO articleDTO = articleService.findArticleDTOById(id,2);
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

}
