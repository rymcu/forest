package com.rymcu.forest.lucene.service.impl;

import com.rymcu.forest.lucene.dic.Dictionary;
import com.rymcu.forest.lucene.mapper.UserDicMapper;
import com.rymcu.forest.lucene.model.UserDic;
import com.rymcu.forest.lucene.service.UserDicService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.*;
import java.nio.charset.StandardCharsets;
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

  /** Lucene索引文件路径 */
  private final String dicPath = System.getProperty("user.dir") + "/lucene/userDic/userDic.dic";

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
    writeUserDic();
  }

  @Override
  public void deleteDic(String id) {
    userDicMapper.deleteDic(id);
    writeUserDic();
  }

  @Override
  public void updateDic(UserDic userDic) {
    userDicMapper.updateDic(userDic.getId(), userDic.getDic());
    writeUserDic();
  }

  @Override
  public void writeUserDic() {
    try {
      File file = new File(dicPath);
      FileOutputStream stream = new FileOutputStream(file, false);
      OutputStreamWriter outfw = new OutputStreamWriter(stream, StandardCharsets.UTF_8);
      PrintWriter fw = new PrintWriter(new BufferedWriter(outfw));
      userDicMapper
          .getAllDic()
          .forEach(
              each -> {
                fw.write(each);
                fw.write("\r\n");
              });
      fw.flush();
      fw.close();
      Dictionary.getSingleton().updateUserDict();
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
  }
}
