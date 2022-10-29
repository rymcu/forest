package com.rymcu.forest.web.api.follow;

import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.entity.Follow;
import com.rymcu.forest.entity.User;
import com.rymcu.forest.service.FollowService;
import com.rymcu.forest.util.UserUtils;
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
    public GlobalResult isFollow(@RequestParam Integer followingId, @RequestParam String followingType) {
        User tokenUser = UserUtils.getCurrentUserByToken();
        Boolean b = followService.isFollow(followingId, followingType, tokenUser.getIdUser());
        return GlobalResultGenerator.genSuccessResult(b);
    }

    @PostMapping
    public GlobalResult<Boolean> follow(@RequestBody Follow follow) {
        User tokenUser = UserUtils.getCurrentUserByToken();
        follow.setFollowerId(tokenUser.getIdUser());
        Boolean b = followService.follow(follow, tokenUser.getNickname());
        return GlobalResultGenerator.genSuccessResult(b);
    }

    @PostMapping("cancel-follow")
    public GlobalResult cancelFollow(@RequestBody Follow follow) {
        User tokenUser = UserUtils.getCurrentUserByToken();
        follow.setFollowerId(tokenUser.getIdUser());
        Boolean b = followService.cancelFollow(follow);
        return GlobalResultGenerator.genSuccessResult(b);
    }


}
