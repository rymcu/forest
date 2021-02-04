package com.rymcu.forest.lucene.service.impl;

import com.rymcu.forest.lucene.dic.Dictionary;
import com.rymcu.forest.lucene.mapper.UserDicMapper;
import com.rymcu.forest.lucene.model.UserDic;
import com.rymcu.forest.lucene.service.UserDicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * UserDicServiceImpl
 *
 * @author suwen
 * @date 2021/2/4 09:26
 */
@Service
public class UserDicServiceImpl implements UserDicService {

  @Resource private UserDicMapper userDicMapper;
  @Resource private Dictionary dictionary;

  @Override
  public List<String> getAllDic() {

    return userDicMapper.getAllDic();
  }

  @Override
  public List<UserDic> getAll() {
    return userDicMapper.getAll();
  }

  @Override
  public void addDic(String dic) {
    userDicMapper.addDic(dic);
  }

  @Override
  public void deleteDic(String id) {
    userDicMapper.deleteDic(id);
  }

  @Override
  public void updateDic(UserDic userDic) {
    userDicMapper.updateDic(userDic.getId(), userDic.getDic());
  }
}
