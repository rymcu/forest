package com.rymcu.forest.service;

import com.rymcu.forest.core.service.Service;
import com.rymcu.forest.dto.UserDTO;
import com.rymcu.forest.entity.Follow;

import java.util.List;

/**
 * @author ronger
 */
public interface FollowService extends Service<Follow> {
    /**
     * 判断是否关注
     *
     * @param followingId
     * @param followingType
     * @param idUser
     * @return
     */
    Boolean isFollow(Integer followingId, String followingType, Long idUser);

    /**
     * 关注操作
     *
     * @param follow
     * @param nickname
     * @return
     */
    Boolean follow(Follow follow, String nickname);

    /**
     * 取消关注操作
     *
     * @param follow
     * @return
     */
    Boolean cancelFollow(Follow follow);

    /**
     * 获取关注用户者数据
     *
     * @param followType
     * @param followingId
     * @return
     */
    List<Follow> findByFollowingId(String followType, Long followingId);


    /**
     * 查询用户粉丝
     *
     * @param userDTO
     * @return
     */
    List<UserDTO> findUserFollowersByUser(UserDTO userDTO);

    /**
     * 查询用户关注用户
     *
     * @param userDTO
     * @return
     */
    List<UserDTO> findUserFollowingsByUser(UserDTO userDTO);
}
