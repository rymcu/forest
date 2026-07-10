/*
 * Copyright (c) 2020 RYMCU (https://rymcu.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */


package com.rymcu.forest.service.impl;

import com.rymcu.forest.core.service.AbstractService;
import com.rymcu.forest.dto.UserDTO;
import com.rymcu.forest.entity.Follow;
import com.rymcu.forest.handler.event.FollowEvent;
import com.rymcu.forest.mapper.FollowMapper;
import com.rymcu.forest.service.FollowService;
import com.rymcu.forest.config.RabbitMQConfig;
import com.rymcu.forest.event.EventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author ronger
 */
@Service
public class FollowServiceImpl extends AbstractService<Follow> implements FollowService {

    @Resource
    private FollowMapper followMapper;
    @Resource
    private EventPublisher eventPublisher;

    @Override
    public Boolean isFollow(Integer followingId, String followingType, Long idUser) {
        return followMapper.isFollow(followingId, idUser, followingType);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Boolean follow(Follow follow, String nickname) {
        int result = followMapper.insertSelective(follow);
        if (result > 0) {
            eventPublisher.publish(RabbitMQConfig.RK_FOLLOW,
                    new FollowEvent(follow.getFollowingId(), follow.getFollowerId(),
                            nickname + " 关注了你!"));
        }
        return result > 0;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
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
