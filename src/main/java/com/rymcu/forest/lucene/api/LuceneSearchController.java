package com.rymcu.forest.lucene.api;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.UserDTO;
import com.rymcu.forest.lucene.model.ArticleLucene;
import com.rymcu.forest.lucene.model.UserLucene;
import com.rymcu.forest.lucene.service.LuceneService;
import com.rymcu.forest.lucene.service.UserDicService;
import com.rymcu.forest.lucene.service.UserLuceneService;
import com.rymcu.forest.lucene.util.ArticleIndexUtil;
import com.rymcu.forest.lucene.util.UserIndexUtil;
import com.rymcu.forest.util.Utils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * LuceneSearchController
 *
 * @author suwen
 * @date 2021/2/3 10:41
 */
@RestController
@RequestMapping("/api/v1/lucene")
public class LuceneSearchController {

  @Resource private LuceneService luceneService;
  @Resource private UserLuceneService userLuceneService;
  @Resource private UserDicService dicService;

//  @PostConstruct
  public void createIndex() {
    // 删除系统运行时保存的索引，重新创建索引
    ArticleIndexUtil.deleteAllIndex();
    UserIndexUtil.deleteAllIndex();
    ExecutorService executor = Executors.newSingleThreadExecutor();
    CompletableFuture<String> future =
        CompletableFuture.supplyAsync(
            () -> {
              System.out.println(">>>>>>>>> 开始创建索引 <<<<<<<<<<<");
              luceneService.writeArticle(luceneService.getAllArticleLucene());
              userLuceneService.writeUser(userLuceneService.getAllUserLucene());
              System.out.println(">>>>>>>>> 索引创建完毕 <<<<<<<<<<<");
              System.out.println("加载用户配置的自定义扩展词典到主词库表");
              try {
                dicService.writeUserDic();
              } catch (FileNotFoundException e) {
                System.out.println("加载用户词典失败，未成功创建用户词典");
              }
              return "索引成功创建";
            },
            executor);
    future.thenAccept(System.out::println);
  }

  /**
   * 搜索，实现高亮
   *
   * @param q
   * @return
   */
  @GetMapping("/searchArticle/{q}")
  public GlobalResult<?> searchArticle(
      @PathVariable String q,
      @RequestParam(defaultValue = "1") Integer pageNum,
      @RequestParam(defaultValue = "10") Integer pageSize) {
    // 找出相关文章，相关度倒序
    List<ArticleLucene> resList = luceneService.searchArticle(q);
    // 分页组装文章详情
    int total = resList.size();
    if (total == 0) {
      return GlobalResultGenerator.genSuccessResult("未找到相关文章");
    }
    Page<ArticleDTO> page = new Page<>(pageNum, pageSize);
    page.setTotal(total);
    int startIndex = (pageNum - 1) * pageSize;
    int endIndex = Math.min(startIndex + pageSize, total);
    // 分割子列表
    List<ArticleLucene> subList = resList.subList(startIndex, endIndex);
    String[] ids = subList.stream().map(ArticleLucene::getIdArticle).toArray(String[]::new);
    List<ArticleDTO> articleDTOList = luceneService.getArticlesByIds(ids);
    ArticleDTO temp;
    // 写入文章关键词信息
    for (int i = 0; i < articleDTOList.size(); i++) {
      temp = articleDTOList.get(i);
      temp.setArticleTitle(subList.get(i).getArticleTitle());
      if (subList.get(i).getArticleContent().length() > 10) {
        // 内容中命中太少则不替换
        temp.setArticlePreviewContent(subList.get(i).getArticleContent());
      }
      articleDTOList.set(i, temp);
    }
    page.addAll(articleDTOList);
    PageInfo<ArticleDTO> pageInfo = new PageInfo<>(page);
    return GlobalResultGenerator.genSuccessResult(Utils.getArticlesGlobalResult(pageInfo));
  }

  /**
   * 搜索，实现高亮
   *
   * @param q
   * @return
   */
  @GetMapping("/searchUser/{q}")
  public GlobalResult<?> searchUser(
      @PathVariable String q,
      @RequestParam(defaultValue = "1") Integer pageNum,
      @RequestParam(defaultValue = "10") Integer pageSize) {
    // 找出相关文章，相关度倒序
    List<UserLucene> resList = userLuceneService.searchUser(q);
    // 分页组装文章详情
    int total = resList.size();
    if (total == 0) {
      return GlobalResultGenerator.genSuccessResult("未找到相关用户");
    }
    Page<UserDTO> page = new Page<>(pageNum, pageSize);
    page.setTotal(total);
    int startIndex = (pageNum - 1) * pageSize;
    int endIndex = Math.min(startIndex + pageSize, total);
    // 分割子列表
    List<UserLucene> subList = resList.subList(startIndex, endIndex);
    Integer[] ids = subList.stream().map(UserLucene::getIdUser).toArray(Integer[]::new);
    List<UserDTO> userDTOList = userLuceneService.getUsersByIds(ids);
    UserDTO temp;
    // 写入文章关键词信息
    for (int i = 0; i < userDTOList.size(); i++) {
      temp = userDTOList.get(i);
      temp.setNickname(subList.get(i).getNickname());
      if (subList.get(i).getSignature().length() > 10) {
        // 内容中命中太少则不替换
        temp.setSignature(subList.get(i).getSignature());
      }
      userDTOList.set(i, temp);
    }
    page.addAll(userDTOList);
    PageInfo<UserDTO> pageInfo = new PageInfo<>(page);
    return GlobalResultGenerator.genSuccessResult(Utils.getUserGlobalResult(pageInfo));
  }
}
