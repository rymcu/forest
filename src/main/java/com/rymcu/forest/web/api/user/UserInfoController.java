package com.rymcu.forest.web.api.user;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.exception.ServiceException;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.core.service.security.annotation.SecurityInterceptor;
import com.rymcu.forest.dto.ChangeEmailDTO;
import com.rymcu.forest.dto.UpdatePasswordDTO;
import com.rymcu.forest.dto.UserInfoDTO;
import com.rymcu.forest.entity.LoginRecord;
import com.rymcu.forest.entity.UserExtend;
import com.rymcu.forest.service.LoginRecordService;
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
@RequestMapping("/api/v1/user-info")
public class UserInfoController {

    @Resource
    private UserService userService;
    @Resource
    private LoginRecordService loginRecordService;

    @GetMapping("/detail/{idUser}")
    @SecurityInterceptor
    public GlobalResult<UserInfoDTO> detail(@PathVariable Long idUser) {
        UserInfoDTO userInfo = userService.findUserInfo(idUser);
        return GlobalResultGenerator.genSuccessResult(userInfo);
    }
    @GetMapping("/detail/{idUser}/extend-info")
    @SecurityInterceptor
    public GlobalResult<UserExtend> extendInfo(@PathVariable Long idUser) {
        UserExtend userExtend = userService.findUserExtendInfo(idUser);
        return GlobalResultGenerator.genSuccessResult(userExtend);
    }

    @GetMapping("/check-nickname")
    @SecurityInterceptor
    public GlobalResult checkNickname(@RequestParam Long idUser, @RequestParam String nickname) {
        boolean flag = userService.checkNicknameByIdUser(idUser, nickname);
        return GlobalResultGenerator.genSuccessResult(flag);
    }

    @PatchMapping("/update")
    @SecurityInterceptor
    public GlobalResult<UserInfoDTO> updateUserInfo(@RequestBody UserInfoDTO user) throws ServiceException {
        user = userService.updateUserInfo(user);
        return GlobalResultGenerator.genSuccessResult(user);
    }

    @PatchMapping("/update-extend")
    @SecurityInterceptor
    public GlobalResult<UserExtend> updateUserExtend(@RequestBody UserExtend userExtend) throws ServiceException {
        userExtend = userService.updateUserExtend(userExtend);
        return GlobalResultGenerator.genSuccessResult(userExtend);
    }

    @PatchMapping("/update-email")
    @SecurityInterceptor
    public GlobalResult<Boolean> updateEmail(@RequestBody ChangeEmailDTO changeEmailDTO) throws ServiceException {
        boolean flag = userService.updateEmail(changeEmailDTO);
        return GlobalResultGenerator.genSuccessResult(flag);
    }

    @PatchMapping("/update-password")
    @SecurityInterceptor
    public GlobalResult<Boolean> updatePassword(@RequestBody UpdatePasswordDTO updatePasswordDTO) {
        boolean flag = userService.updatePassword(updatePasswordDTO);
        return GlobalResultGenerator.genSuccessResult(flag);
    }

    @GetMapping("/login-records")
    @SecurityInterceptor
    public GlobalResult<PageInfo<LoginRecord>> loginRecords(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "10") Integer rows, @RequestParam Integer idUser) {
        PageHelper.startPage(page, rows);
        List<LoginRecord> list = loginRecordService.findLoginRecordByIdUser(idUser);
        PageInfo<LoginRecord> pageInfo = new PageInfo<>(list);
        return GlobalResultGenerator.genSuccessResult(pageInfo);
    }

}
