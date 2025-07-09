package com.rymcu.forest.service;

import com.rymcu.forest.base.BaseServiceTest;
import com.rymcu.forest.dto.UserDTO;
import com.rymcu.forest.entity.Follow;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FollowServiceTest extends BaseServiceTest {
    private final Long followingId = 1L;
    private final Long idUser = 2L;
    private final String followingType = "0";

    private final Follow follow;
    @Autowired
    private FollowService followService;

    {
        follow = new Follow();
        follow.setFollowerId(idUser);
        follow.setFollowingType(followingType);
        follow.setFollowingId(followingId);

    }

    @Test
    void isFollow() {
        Boolean b = followService.isFollow(followingId.intValue(), followingType, idUser);
        assertFalse(b);

    }

    @Test
    @DisplayName("关注操作")
    void follow() {

        Boolean b = followService.follow(follow, "nickname");
        assertTrue(b);

    }

    @Test
    @DisplayName("取消关注操作")
    void cancelFollow() {
        Boolean b = followService.cancelFollow(follow);
        assertTrue(b);
    }

    @Test
    @DisplayName("获取关注用户者数据")
    void findByFollowingId() {
        List<Follow> list = followService.findByFollowingId(followingType, followingId);
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("查询用户粉丝")
    void findUserFollowersByUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setIdUser(idUser);
        List<UserDTO> list = followService.findUserFollowersByUser(userDTO);
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("查询用户关注用户")
    void findUserFollowingsByUser() {
        UserDTO userDTO = new UserDTO();
        userDTO.setIdUser(idUser);
        List<UserDTO> list = followService.findUserFollowingsByUser(userDTO);
        assertTrue(list.isEmpty());
    }
}
