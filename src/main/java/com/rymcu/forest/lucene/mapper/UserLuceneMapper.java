package com.rymcu.forest.lucene.mapper;

import com.rymcu.forest.dto.UserDTO;
import com.rymcu.forest.lucene.model.UserLucene;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UserLuceneMapper
 *
 * @author suwen
 * @date 2021/3/6 10:00
 */
@Mapper
public interface UserLuceneMapper {

    /**
     * 加载所有用户信息
     *
     * @return
     */
    List<UserLucene> getAllUserLucene();

    /**
     * 加载所有用户信息
     *
     * @param ids 用户id(半角逗号分隔)
     * @return
     */
    List<UserDTO> getUsersByIds(@Param("ids") Long[] ids);

    /**
     * 加载 UserLucene
     *
     * @param id 用户id
     * @return
     */
    UserLucene getById(@Param("id") String id);
}
