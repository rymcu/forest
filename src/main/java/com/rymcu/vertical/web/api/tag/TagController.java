package com.rymcu.vertical.web.api.tag;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.vertical.core.result.GlobalResult;
import com.rymcu.vertical.core.result.GlobalResultGenerator;
import com.rymcu.vertical.dto.ArticleDTO;
import com.rymcu.vertical.dto.LabelModel;
import com.rymcu.vertical.service.ArticleService;
import com.rymcu.vertical.service.TagService;
import com.rymcu.vertical.util.Utils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/tag")
public class TagController {

    @Resource
    private ArticleService articleService;
    @Resource
    private TagService tagService;

    @GetMapping("/{name}")
    public GlobalResult articles(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows, @PathVariable String name){
        PageHelper.startPage(page, rows);
        List<ArticleDTO> list = articleService.findArticlesByTagName(name);
        PageInfo<ArticleDTO> pageInfo = new PageInfo(list);
        Map map = Utils.getArticlesGlobalResult(pageInfo);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/tags")
    public GlobalResult tags() {
        List<LabelModel> list = tagService.findTagLabels();
        return GlobalResultGenerator.genSuccessResult(list);
    }
}
