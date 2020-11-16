package com.rymcu.vertical.web.api.user;

import com.rymcu.vertical.core.result.GlobalResult;
import com.rymcu.vertical.core.result.GlobalResultGenerator;
import com.rymcu.vertical.dto.*;
import com.rymcu.vertical.entity.User;
import com.rymcu.vertical.entity.UserExtend;
import com.rymcu.vertical.service.UserService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.Map;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/user-info")
public class UserInfoController {

    @Resource
    private UserService userService;

    @GetMapping("/detail/{idUser}")
    public GlobalResult detail(@PathVariable Integer idUser) {
        Map map = userService.findUserInfo(idUser);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @GetMapping("/check-nickname")
    public GlobalResult checkNickname(@RequestParam Integer idUser, @RequestParam String nickname) {
        Map map = userService.checkNickname(idUser,nickname);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PatchMapping("/update")
    public GlobalResult updateUserInfo(@RequestBody UserInfoDTO user) {
        Map map = userService.updateUserInfo(user);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PatchMapping("/update-extend")
    public GlobalResult updateUserExtend(@RequestBody UserExtend userExtend) {
        Map map = userService.updateUserExtend(userExtend);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PatchMapping("/update-email")
    public GlobalResult updateEmail(@RequestBody ChangeEmailDTO changeEmailDTO) {
        Map map = userService.updateEmail(changeEmailDTO);
        return GlobalResultGenerator.genSuccessResult(map);
    }

    @PatchMapping("/update-password")
    public GlobalResult updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
        Map map = userService.updatePassword(updatePasswordDTO);
        return GlobalResultGenerator.genSuccessResult(map);
    }

}
