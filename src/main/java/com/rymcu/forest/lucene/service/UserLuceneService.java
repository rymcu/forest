package com.rymcu.forest.lucene.service;

import com.rymcu.forest.dto.UserDTO;
import com.rymcu.forest.lucene.model.UserLucene;

import java.util.List;

/**
 * UserLuceneService
 *
 * @author suwen
 * @date 2021/3/5 10:10
 */
public interface UserLuceneService {

    /**
     * 批量写入用户信息到索引
     *
     * @param list
     */
    void writeUser(List<UserLucene> list);

    /**
     * 写入单个用户索引
     *
     * @param id
     */
    void writeUser(String id);

    /**
     * 写入单个用户索引
     *
     * @param UserLucene
     */
    void writeUser(UserLucene UserLucene);

    /**
     * 更新单个用户索引
     *
     * @param id
     */
    void updateUser(String id);

    /**
     * 删除单个用户索引
     *
     * @param id
     */
    void deleteUser(String id);

    /**
     * 关键词搜索
     *
     * @param value
     * @return
     * @throws Exception
     */
    List<UserLucene> searchUser(String value);

    /**
     * 加载所有用户内容
     *
     * @return
     */
    List<UserLucene> getAllUserLucene();

    /**
     * 加载所有用户内容
     *
     * @param ids 用户id(半角逗号分隔)
     * @return
     */
    List<UserDTO> getUsersByIds(Long[] ids);
}
