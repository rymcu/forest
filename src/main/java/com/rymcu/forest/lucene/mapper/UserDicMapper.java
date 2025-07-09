package com.rymcu.forest.lucene.mapper;

import com.rymcu.forest.lucene.model.UserDic;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * UserDicMapper
 *
 * @author suwen
 * @date 2021/2/4 09:11
 */
@Mapper
public interface UserDicMapper {

    /**
     * 加载所有字典
     *
     * @return
     */
    List<String> getAllDic();

    /**
     * 加载所有字典信息
     *
     * @return
     */
    List<UserDic> getAll();

    /**
     * 增加字典
     *
     * @return
     */
    void addDic(@Param("dic") String userDic);

    /**
     * 删除字典
     *
     * @param id
     */
    void deleteDic(@Param("id") String id);

    /**
     * 更新字典
     *
     * @param id
     * @param userDic
     */
    void updateDic(@Param("id") Integer id, @Param("dic") String userDic);
}
