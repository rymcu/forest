package com.rymcu.vertical.web.api.topic;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.vertical.core.result.GlobalResult;
import com.rymcu.vertical.core.result.GlobalResultGenerator;
import com.rymcu.vertical.dto.ArticleDTO;
import com.rymcu.vertical.entity.Topic;
import com.rymcu.vertical.service.ArticleService;
import com.rymcu.vertical.service.TopicService;
import com.rymcu.vertical.util.Utils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/topic")
public class TopicController {
    @Resource
    private ArticleService articleService;
    @Resource
    private TopicService topicService;

    @GetMapping("/topic-nav")
    public GlobalResult topicNav(){
        List<Topic> topics = topicService.findTopicNav();
        return GlobalResultGenerator.genSuccessResult(topics);
    }

    @GetMapping("/{name}")
    public GlobalResult articles(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows, @PathVariable String name){
        PageHelper.startPage(page, rows);
        List<ArticleDTO> list = articleService.findArticlesByTopicUri(name);
        PageInfo<ArticleDTO> pageInfo = new PageInfo(list);
        Map map = Utils.getArticlesGlobalResult(pageInfo);
        return GlobalResultGenerator.genSuccessResult(map);
    }
}
