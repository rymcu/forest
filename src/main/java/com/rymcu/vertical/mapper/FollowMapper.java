package com.rymcu.vertical.mapper;

import com.rymcu.vertical.core.mapper.Mapper;
import com.rymcu.vertical.entity.Follow;
import org.apache.ibatis.annotations.Param;

/**
 * @author ronger
 */
public interface FollowMapper extends Mapper<Follow> {
    /**
     * 判断是否关注
     * @param followingId
     * @param followerId
     * @param followingType
     * @return
     */
    Boolean isFollow(@Param("followingId") Integer followingId, @Param("followerId") Integer followerId, @Param("followingType") String followingType);
}
