package com.rymcu.forest.lucene.api;

import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.lucene.model.UserDic;
import com.rymcu.forest.lucene.service.UserDicService;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.FileNotFoundException;

/**
 * UserDicController
 *
 * @author suwen
 * @date 2021/2/4 09:29
 */
@Log4j2
@RestController
@RequestMapping("/api/v1/lucene/dic")
public class UserDicController {

  @Resource private UserDicService dicService;

  @GetMapping("/getAll")
  public GlobalResult getAll() {

    return GlobalResultGenerator.genSuccessResult(dicService.getAll());
  }

  @GetMapping("/getAllDic")
  public GlobalResult getAllDic() {

    return GlobalResultGenerator.genSuccessResult(dicService.getAllDic());
  }

  @GetMapping("/loadUserDic")
  public GlobalResult loadUserDic() throws FileNotFoundException {
    dicService.writeUserDic();
    return GlobalResultGenerator.genSuccessResult("加载用户自定义字典成功");
  }

  @PostMapping("/addDic/{dic}")
  public GlobalResult addDic(@PathVariable String dic) {
    dicService.addDic(dic);
    return GlobalResultGenerator.genSuccessResult("新增字典成功");
  }

  @PutMapping("/editDic")
  public GlobalResult getAllDic(@RequestBody UserDic dic) {
    dicService.updateDic(dic);
    return GlobalResultGenerator.genSuccessResult("更新字典成功");
  }

  @DeleteMapping("/deleteDic/{id}")
  public GlobalResult deleteDic(@PathVariable String id) {
    dicService.deleteDic(id);
    return GlobalResultGenerator.genSuccessResult("删除字典成功");
  }
}
