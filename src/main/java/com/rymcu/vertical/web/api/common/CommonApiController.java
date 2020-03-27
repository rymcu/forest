package com.rymcu.vertical.web.api.common;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.vertical.core.result.GlobalResult;
import com.rymcu.vertical.core.result.GlobalResultGenerator;
import com.rymcu.vertical.core.result.GlobalResultMessage;
import com.rymcu.vertical.core.service.log.annotation.VisitLogger;
import com.rymcu.vertical.dto.ArticleDTO;
import com.rymcu.vertical.dto.ForgetPasswordDTO;
import com.rymcu.vertical.dto.TokenUser;
import com.rymcu.vertical.dto.UserRegisterInfoDTO;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.service.ArticleService;
import com.rymcu.vertical.service.JavaMailService;
import com.rymcu.vertical.service.UserService;
import com.rymcu.vertical.util.UserUtils;
import com.rymcu.vertical.util.Utils;
import io.swagger.annotations.ApiOperation;
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

    @ApiOperation(value = "获取邮件验证码")
    @GetMapping("/get-email-code")
    public GlobalResult<Map<String, String>> getEmailCode(@RequestParam("email") String email) throws MessagingException {
        Map<String, String> map = new HashMap<>(1);
        map.put("message",GlobalResultMessage.SEND_SUCCESS.getMessage());
        User user = userService.findByAccount(email);
        if (user != null) {
            map.put("message","该邮箱已被注册！");
        } else {
            Integer result = javaMailService.sendEmailCode(email);
            if(result == 0){
                map.put("message",GlobalResultMessage.SEND_FAIL.getMessage());
            }
        }
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @ApiOperation(value = "获取找回密码邮件")
    @GetMapping("/get-forget-password-email")
    public GlobalResult<Map<Object, Object>> getForgetPasswordEmail(@RequestParam("email") String email) throws MessagingException {
        Map<Object, Object> map = new HashMap<>(1);
        map.put("message",GlobalResultMessage.SEND_SUCCESS.getMessage());
        User user = userService.findByAccount(email);
        if (user != null) {
            Integer result = javaMailService.sendForgetPasswordEmail(email);
            if(result == 0){
                map.put("message",GlobalResultMessage.SEND_FAIL.getMessage());
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
    public GlobalResult<Map> articles(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows, @RequestParam(defaultValue = "") String searchText, @RequestParam(defaultValue = "") String tag){
        PageHelper.startPage(page, rows);
        List<ArticleDTO> list = articleService.findArticles(searchText,tag);
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
}
