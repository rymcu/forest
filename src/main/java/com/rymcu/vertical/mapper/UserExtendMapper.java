package com.rymcu.vertical.mapper;

import com.rymcu.vertical.core.mapper.Mapper;
import com.rymcu.vertical.entity.UserExtend;
import org.apache.ibatis.annotations.Param;

/**
 * @author ronger
 */
public interface UserExtendMapper extends Mapper<UserExtend> {
    /**
     * 获取用户扩展信息
     * @param nickname
     * @return
     */
    UserExtend selectUserExtendByNickname(@Param("nickname") String nickname);
}
