package com.rymcu.forest.web.api.common;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.core.result.GlobalResultMessage;
import com.rymcu.forest.core.service.log.annotation.VisitLogger;
import com.rymcu.forest.dto.*;
import com.rymcu.forest.entity.Portfolio;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.service.*;
import com.rymcu.forest.util.UserUtils;
import com.rymcu.forest.util.Utils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/console")
public class CommonApiController {

    @Resource
    private JavaMailService javaMailService;
    @Resource
    private UserService userService;
    @Resource
    private ArticleService articleService;
    @Resource
    private PortfolioService portfolioService;
    @Resource
    private SearchService SearchService;

    @GetMapping("/get-email-code")
    public GlobalResult<Map<String, String>> getEmailCode(@RequestParam("email") String email) throws MessagingException {
        Map<String, String> map = new HashMap<>(1);
        map.put("message", GlobalResultMessage.SEND_SUCCESS.getMessage());
        User user = userService.findByAccount(email);
        if (user != null) {
            map.put("message","该邮箱已被注册！");
        } else {
            Integer result = javaMailService.sendEmailCode(email);
            if(result == 0){
                map.put("message", GlobalResultMessage.SEND_FAIL.getMessage());
            }
        }
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/get-forget-password-email")
    public GlobalResult<Map<Object, Object>> getForgetPasswordEmail(@RequestParam("email") String email) throws MessagingException {
        Map<Object, Object> map = new HashMap<>(1);
        map.put("message", GlobalResultMessage.SEND_SUCCESS.getMessage());
        User user = userService.findByAccount(email);
        if (user != null) {
            Integer result = javaMailService.sendForgetPasswordEmail(email);
            if(result == 0){
                map.put("message", GlobalResultMessage.SEND_FAIL.getMessage());
            }
        } else {
            map.put("message","该邮箱未注册！");
        }
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PostMapping("/register")
    public GlobalResult<Map> register(@RequestBody UserRegisterInfoDTO registerInfo){
        Map map = userService.register(registerInfo.getEmail(), registerInfo.getPassword(), registerInfo.getCode());
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PostMapping("/login")
    public GlobalResult<Map> login(@RequestBody User user){
        Map map = userService.login(user.getAccount(), user.getPassword());
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/heartbeat")
    public GlobalResult heartbeat(){
        return GlobalResultGenerator.genSuccessResult("heartbeat");
    }

    @GetMapping("/articles")
    @VisitLogger
    public GlobalResult<Map> articles(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows, ArticleSearchDTO searchDTO){
        PageHelper.startPage(page, rows);
        List<ArticleDTO> list = articleService.findArticles(searchDTO);
        PageInfo<ArticleDTO> pageInfo = new PageInfo(list);
        Map map = Utils.getArticlesGlobalResult(pageInfo);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/article/{id}")
    @VisitLogger
    public GlobalResult<Map<String, Object>> article(@PathVariable Integer id){
        ArticleDTO articleDTO = articleService.findArticleDTOById(id,1);
        Map<String, Object> map = new HashMap<>(1);
        map.put("article", articleDTO);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/token/{token}")
    public GlobalResult<TokenUser> token(@PathVariable String token){
        TokenUser tokenUser = UserUtils.getTokenUser(token);
        return GlobalResultGenerator.genSuccessResult(tokenUser);
    }

    @PatchMapping("/forget-password")
    public GlobalResult<Map> forgetPassword(@RequestBody ForgetPasswordDTO forgetPassword){
        Map map = userService.forgetPassword(forgetPassword.getCode(), forgetPassword.getPassword());
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/portfolio/{id}")
    @VisitLogger
    public GlobalResult<Map<String, Object>> portfolio(@PathVariable Integer id){
        PortfolioDTO portfolioDTO = portfolioService.findPortfolioDTOById(id,1);
        Map<String, Object> map = new HashMap<>(1);
        map.put("portfolio", portfolioDTO);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/portfolio/{id}/articles")
    public GlobalResult articles(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows, @PathVariable Integer id) {
        PageHelper.startPage(page, rows);
        List<ArticleDTO> list = articleService.findArticlesByIdPortfolio(id);
        PageInfo<ArticleDTO> pageInfo = new PageInfo(list);
        Map map = Utils.getArticlesGlobalResult(pageInfo);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/initial-search")
    public GlobalResult initialSearch() {
        List<SearchModel> list = SearchService.initialSearch();
        return GlobalResultGenerator.genSuccessResult(list);
    }

    @GetMapping("/portfolios")
    public GlobalResult portfolios(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows) {
        PageHelper.startPage(page, rows);
        List<Portfolio> list = portfolioService.findAll();
        PageInfo<Portfolio> pageInfo = new PageInfo(list);
        Map map = new HashMap(2);
        map.put("portfolios", pageInfo.getList());
        Map pagination = Utils.getPagination(pageInfo);
        map.put("pagination", pagination);
        return GlobalResultGenerator.genSuccessResult(map);
    }
}
