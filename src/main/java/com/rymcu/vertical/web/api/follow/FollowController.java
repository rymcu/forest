package com.rymcu.vertical.web.api.follow;

import com.rymcu.vertical.core.result.GlobalResult;
import com.rymcu.vertical.core.result.GlobalResultGenerator;
import com.rymcu.vertical.entity.Follow;
import com.rymcu.vertical.jwt.def.JwtConstants;
import com.rymcu.vertical.service.FollowService;
import com.rymcu.vertical.web.api.exception.BaseApiException;
import com.rymcu.vertical.web.api.exception.ErrorCode;
import org.apache.commons.lang.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

/**
 * @author ronger
 */
@RestController
@RequestMapping("/api/v1/follow")
public class FollowController {

    @Resource
    private FollowService followService;

    @GetMapping("/is-follow")
    public GlobalResult isFollow(@RequestParam Integer followingId, @RequestParam String followingType) throws BaseApiException {
        Boolean b = followService.isFollow(followingId, followingType);
        return GlobalResultGenerator.genSuccessResult(b);
    }

    @PostMapping
    public GlobalResult follow(@RequestBody Follow follow) throws BaseApiException {
        Boolean b = followService.follow(follow);
        return GlobalResultGenerator.genSuccessResult(b);
    }

    @PostMapping("cancel-follow")
    public GlobalResult cancelFollow(@RequestBody Follow follow) throws BaseApiException {
        Boolean b = followService.cancelFollow(follow);
        return GlobalResultGenerator.genSuccessResult(b);
    }


}
