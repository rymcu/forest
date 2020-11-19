package com.rymcu.forest.web.api.follow;

import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.entity.Follow;
import com.rymcu.forest.service.FollowService;
import com.rymcu.forest.web.api.exception.BaseApiException;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

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
