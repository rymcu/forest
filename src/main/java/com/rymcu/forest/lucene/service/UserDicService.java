package com.rymcu.forest.lucene.service;

import com.rymcu.forest.lucene.model.UserDic;

import java.io.FileNotFoundException;
import java.util.List;

/**
 * UserDicService
 *
 * @author suwen
 * @date 2021/2/4 09:25
 */
public interface UserDicService {

    /**
     * 加载所有字典
     *
     * @return
     */
    List<String> getAllDic();

    /**
     * 加载所有字典
     *
     * @return
     */
    List<UserDic> getAll();

    /**
     * 增加字典
     *
     * @return
     */
    void addDic(String dic);

    /**
     * 删除字典
     *
     * @param id
     */
    void deleteDic(String id);

    /**
     * 更新字典
     *
     * @param userDic
     */
    void updateDic(UserDic userDic);

    /**
     * 写入字典至内存
     */
    void writeUserDic() throws FileNotFoundException;
}
