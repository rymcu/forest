package com.rymcu.forest.web.api.auth;

import com.alibaba.fastjson2.JSONObject;
import com.rymcu.forest.auth.TokenManager;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.dto.TokenUser;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.service.UserService;
import com.rymcu.forest.util.BeanCopierUtil;
import com.rymcu.forest.util.UserUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Resource
    private UserService userService;
    @Resource
    TokenManager tokenManager;

    @PostMapping("/login")
    public GlobalResult<TokenUser> login(@RequestBody User user) {
        TokenUser tokenUser = userService.login(user.getAccount(), user.getPassword());
        return GlobalResultGenerator.genSuccessResult(tokenUser);
    }

    @PostMapping("/refresh-token")
    public GlobalResult<TokenUser> refreshToken(@RequestBody TokenUser tokenUser) {
        tokenUser = userService.refreshToken(tokenUser.getRefreshToken());
        return GlobalResultGenerator.genSuccessResult(tokenUser);
    }

    @PostMapping("/logout")
    public GlobalResult logout() {
        User user = UserUtils.getCurrentUserByToken();
        if (Objects.nonNull(user)) {
            tokenManager.deleteToken(user.getAccount());
        }
        return GlobalResultGenerator.genSuccessResult();
    }

    @GetMapping("/user")
    public GlobalResult<JSONObject> user() {
        User user = UserUtils.getCurrentUserByToken();
        TokenUser tokenUser = new TokenUser();
        BeanCopierUtil.copy(user, tokenUser);
        tokenUser.setScope(userService.findUserPermissions(user));
        JSONObject object = new JSONObject();
        object.put("user", tokenUser);
        return GlobalResultGenerator.genSuccessResult(object);
    }

}
