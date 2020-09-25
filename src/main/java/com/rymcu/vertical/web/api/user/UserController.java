package com.rymcu.vertical.web.api.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.vertical.core.result.GlobalResult;
import com.rymcu.vertical.core.result.GlobalResultGenerator;
import com.rymcu.vertical.core.service.log.annotation.VisitLogger;
import com.rymcu.vertical.dto.ArticleDTO;
import com.rymcu.vertical.dto.PortfolioDTO;
import com.rymcu.vertical.dto.UserDTO;
import com.rymcu.vertical.service.ArticleService;
import com.rymcu.vertical.service.PortfolioService;
import com.rymcu.vertical.service.UserService;
import com.rymcu.vertical.util.Utils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    @Resource
    private UserService userService;
    @Resource
    private ArticleService articleService;
    @Resource
    private PortfolioService portfolioService;

    @GetMapping("/{nickname}")
    @VisitLogger
    public GlobalResult detail(@PathVariable String nickname){
        UserDTO userDTO = userService.findUserDTOByNickname(nickname);
        return GlobalResultGenerator.genSuccessResult(userDTO);
    }

    @GetMapping("/{nickname}/articles")
    public GlobalResult userArticles(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "12") Integer rows, @PathVariable String nickname){
        UserDTO userDTO = userService.findUserDTOByNickname(nickname);
        if (userDTO == null){
            return GlobalResultGenerator.genErrorResult("用户不存在！");
        }
        PageHelper.startPage(page, rows);
        List<ArticleDTO> list = articleService.findUserArticlesByIdUser(userDTO.getIdUser());
        PageInfo<ArticleDTO> pageInfo = new PageInfo(list);
        Map map = Utils.getArticlesGlobalResult(pageInfo);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/{nickname}/portfolios")
    public GlobalResult userPortfolios(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "12") Integer rows, @PathVariable String nickname){
        UserDTO userDTO = userService.findUserDTOByNickname(nickname);
        if (userDTO == null){
            return GlobalResultGenerator.genErrorResult("用户不存在！");
        }
        PageHelper.startPage(page, rows);
        List<PortfolioDTO> list = portfolioService.findUserPortfoliosByUser(userDTO);
        PageInfo<PortfolioDTO> pageInfo = new PageInfo(list);
        Map map = new HashMap(2);
        map.put("portfolios", list);
        Map pagination = Utils.getPagination(pageInfo);
        map.put("pagination", pagination);
        return GlobalResultGenerator.genSuccessResult(map);
    }

}
