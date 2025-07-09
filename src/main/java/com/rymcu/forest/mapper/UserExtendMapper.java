package com.rymcu.forest.mapper;

import com.rymcu.forest.core.mapper.Mapper;
import com.rymcu.forest.entity.UserExtend;
import org.apache.ibatis.annotations.Param;

/**
 * @author ronger
 */
public interface UserExtendMapper extends Mapper<UserExtend> {
    /**
     * 获取用户扩展信息
     *
     * @param account
     * @return
     */
    UserExtend selectUserExtendByAccount(@Param("account") String account);
}
