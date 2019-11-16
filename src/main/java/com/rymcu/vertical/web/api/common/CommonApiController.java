package com.rymcu.vertical.web.api.common;

import com.rymcu.vertical.core.exception.ServiceException;
import com.rymcu.vertical.core.result.GlobalResult;
import com.rymcu.vertical.core.result.GlobalResultGenerator;
import com.rymcu.vertical.core.result.GlobalResultMessage;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.service.JavaMailService;
import com.rymcu.vertical.service.UserService;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1")
public class CommonApiController {

    @Resource
    private JavaMailService javaMailService;
    @Resource
    private UserService userService;

    @ApiOperation(value = "获取邮件验证码")
    @PostMapping("/get-email-code")
    public GlobalResult getEmailCode(@RequestParam("email") String email) throws ServiceException {
        Map map = new HashMap();
        map.put("message",GlobalResultMessage.SEND_SUCCESS);
        User user = userService.findByAccount(email);
        if (user != null) {
            map.put("message","该邮箱已被注册！");
        } else {
            Integer result = javaMailService.sendEmailCode(email);
            if(result == 0){
                map.put("message",GlobalResultMessage.SEND_FAIL);
            }
        }
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PostMapping("/register")
    public GlobalResult register(@RequestParam("email") String email, @RequestParam("password") String password, @RequestParam("code") String code){
        Map map = userService.register(email,password,code);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PostMapping("/login")
    public GlobalResult login(@RequestParam("account") String account, @RequestParam("password") String password){
        Map map = userService.login(account,password);
        return GlobalResultGenerator.genSuccessResult(map);
    }
}
