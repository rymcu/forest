package com.rymcu.vertical.web.api.article;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.vertical.core.mapper.Mapper;
import com.rymcu.vertical.core.result.GlobalResult;
import com.rymcu.vertical.core.result.GlobalResultGenerator;
import com.rymcu.vertical.dto.ArticleDTO;
import com.rymcu.vertical.entity.Article;
import com.rymcu.vertical.jwt.def.JwtConstants;
import com.rymcu.vertical.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/article")
public class ArticleController {

    @Resource
    private ArticleService articleService;

    @GetMapping("/articles")
    public GlobalResult articles(@RequestParam(defaultValue = "0") Integer page,@RequestParam(defaultValue = "10") Integer rows,@RequestParam(defaultValue = "") String searchText,@RequestParam(defaultValue = "") String tag){
        PageHelper.startPage(page, rows);
        List<ArticleDTO> list = articleService.articles(searchText,tag);
        PageInfo pageInfo = new PageInfo(list);
        Map map = new HashMap();
        map.put("articles", pageInfo.getList());
        Map pagination = new HashMap();
        pagination.put("paginationPageCount",pageInfo.getPages());
        pagination.put("paginationPageNums",pageInfo.getNavigatepageNums());
        pagination.put("currentPage",pageInfo.getPageNum());
        map.put("pagination", pagination);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PostMapping("/post")
    public GlobalResult postArticle(@RequestParam(name = "idArticle",defaultValue = "0") Integer idArticle,@RequestParam(name = "articleTitle",defaultValue = "") String articleTitle,
                                    @RequestParam(name = "articleContent",defaultValue = "") String articleContent,@RequestParam(name = "articleContentHtml",defaultValue = "") String articleContentHtml,
                                    @RequestParam(name = "articleTags",defaultValue = "") String articleTags, HttpServletRequest request){
        Map map = articleService.postArticle(idArticle,articleTitle,articleContent,articleContentHtml,articleTags,request);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/{id}")
    public GlobalResult detail(@PathVariable Integer id){
        ArticleDTO articleDTO = articleService.findArticleDTOById(id);
        Map map = new HashMap<>();
        map.put("article", articleDTO);
        return GlobalResultGenerator.genSuccessResult(map);
    }

}
