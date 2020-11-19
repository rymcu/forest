package com.rymcu.forest.mapper;

import com.rymcu.forest.core.mapper.Mapper;
import com.rymcu.forest.dto.UserDTO;
import com.rymcu.forest.entity.Follow;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 查询用户粉丝
     * @param idUser
     * @return
     */
    List<UserDTO> selectUserFollowersByUser(@Param("idUser") Integer idUser);

    /**
     * 查询用户关注用户
     * @param idUser
     * @return
     */
    List<UserDTO> selectUserFollowingsByUser(@Param("idUser") Integer idUser);
}
