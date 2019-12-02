package com.rymcu.vertical.web.api.admin;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.vertical.core.result.GlobalResult;
import com.rymcu.vertical.core.result.GlobalResultGenerator;
import com.rymcu.vertical.entity.Topic;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.service.TopicService;
import com.rymcu.vertical.service.UserService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ronger
 * */
@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {

    @Resource
    private UserService userService;
    @Resource
    private TopicService topicService;

    @GetMapping("/users")
    public GlobalResult users(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows){
        PageHelper.startPage(page, rows);
        List<User> list = userService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        Map map = new HashMap(2);
        map.put("users", pageInfo.getList());
        Map pagination = new HashMap(3);
        pagination.put("pageSize",pageInfo.getPageSize());
        pagination.put("total",pageInfo.getTotal());
        pagination.put("currentPage",pageInfo.getPageNum());
        map.put("pagination", pagination);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/topics")
    public GlobalResult topics(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows){
        PageHelper.startPage(page, rows);
        List<Topic> list = topicService.findAll();
        PageInfo pageInfo = new PageInfo(list);
        Map map = new HashMap(2);
        map.put("topics", pageInfo.getList());
        Map pagination = new HashMap(3);
        pagination.put("pageSize",pageInfo.getPageSize());
        pagination.put("total",pageInfo.getTotal());
        pagination.put("currentPage",pageInfo.getPageNum());
        map.put("pagination", pagination);
        return GlobalResultGenerator.genSuccessResult(map);
    }

}
