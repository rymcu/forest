package com.rymcu.forest.service.impl;

import com.rymcu.forest.core.constant.NotificationConstant;
import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.dto.UserDTO;
import com.rymcu.forest.entity.Follow;
import com.rymcu.forest.mapper.FollowMapper;
import com.rymcu.forest.service.FollowService;
import com.rymcu.forest.util.NotificationUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ronger
 */
@Service
public class FollowServiceImpl extends AbstractService<Follow> implements FollowService {

    @Resource
    private FollowMapper followMapper;

    @Override
    public Boolean isFollow(Integer followingId, String followingType, Long idUser) {
        return followMapper.isFollow(followingId, idUser, followingType);
    }

    @Override
    public Boolean follow(Follow follow, String nickname) {
        int result = followMapper.insertSelective(follow);
        if (result > 0) {
            NotificationUtils.saveNotification(follow.getFollowingId(), follow.getIdFollow(), NotificationConstant.Follow, nickname + " 关注了你!");
        }
        return result > 0;
    }

    @Override
    public Boolean cancelFollow(Follow follow) {
        int result = followMapper.delete(follow);
        return result == 0;
    }

    @Override
    public List<Follow> findByFollowingId(String followType, Long followingId) {
        Follow follow = new Follow();
        follow.setFollowingType(followType);
        follow.setFollowingId(followingId);
        return followMapper.select(follow);
    }

    @Override
    public List<UserDTO> findUserFollowersByUser(UserDTO userDTO) {
        return followMapper.selectUserFollowersByUser(userDTO.getIdUser());
    }

    @Override
    public List<UserDTO> findUserFollowingsByUser(UserDTO userDTO) {
        return followMapper.selectUserFollowingsByUser(userDTO.getIdUser());
    }
}
