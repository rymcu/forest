package com.rymcu.forest.web.api.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.core.service.log.annotation.VisitLogger;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.PortfolioDTO;
import com.rymcu.forest.dto.UserDTO;
import com.rymcu.forest.entity.UserExtend;
import com.rymcu.forest.service.ArticleService;
import com.rymcu.forest.service.FollowService;
import com.rymcu.forest.service.PortfolioService;
import com.rymcu.forest.service.UserService;
import com.rymcu.forest.util.Utils;
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
    @Resource
    private FollowService followService;

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

    @GetMapping("/{nickname}/followers")
    public GlobalResult userFollowers(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "12") Integer rows, @PathVariable String nickname){
        UserDTO userDTO = userService.findUserDTOByNickname(nickname);
        if (userDTO == null){
            return GlobalResultGenerator.genErrorResult("用户不存在！");
        }
        PageHelper.startPage(page, rows);
        List<UserDTO> list = followService.findUserFollowersByUser(userDTO);
        PageInfo<UserDTO> pageInfo = new PageInfo(list);
        Map map = new HashMap(2);
        map.put("users", list);
        Map pagination = Utils.getPagination(pageInfo);
        map.put("pagination", pagination);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/{nickname}/followings")
    public GlobalResult userFollowings(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "12") Integer rows, @PathVariable String nickname){
        UserDTO userDTO = userService.findUserDTOByNickname(nickname);
        if (userDTO == null){
            return GlobalResultGenerator.genErrorResult("用户不存在！");
        }
        PageHelper.startPage(page, rows);
        List<UserDTO> list = followService.findUserFollowingsByUser(userDTO);
        PageInfo<UserDTO> pageInfo = new PageInfo(list);
        Map map = new HashMap(2);
        map.put("users", list);
        Map pagination = Utils.getPagination(pageInfo);
        map.put("pagination", pagination);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/{nickname}/user-extend")
    public GlobalResult userExtend(@PathVariable String nickname) {
        UserExtend userExtend = userService.selectUserExtendByNickname(nickname);
        return GlobalResultGenerator.genSuccessResult(userExtend);
    }

}
