package com.rymcu.forest.lucene.api;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageInfo;
import com.rymcu.forest.core.result.GlobalResult;
import com.rymcu.forest.core.result.GlobalResultGenerator;
import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.lucene.model.ArticleLucene;
import com.rymcu.forest.lucene.service.LuceneService;
import com.rymcu.forest.util.Utils;
import lombok.extern.log4j.Log4j2;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * LuceneSearchController
 *
 * @author suwen
 * @date 2021/2/3 10:41
 */
@Log4j2
@RestController
@RequestMapping("/api/v1/lucene")
public class LuceneSearchController {

  @Resource private LuceneService luceneService;

  @GetMapping("/getArticles")
  public GlobalResult createIndex() {
    return GlobalResultGenerator.genSuccessResult(luceneService.getAllArticleLucene());
  }

  @GetMapping("/getArticlesByIds")
  public GlobalResult getArticlesByIds() {
    return GlobalResultGenerator.genSuccessResult(
        luceneService.getArticlesByIds(new String[] {"1", "2", "3"}));
  }

  @GetMapping("/createIndex")
  public GlobalResult createIndex(
      @RequestParam(required = false, defaultValue = "0") Integer limit,
      @RequestParam(required = false, defaultValue = "1000") Integer offset) {
    // 拉取数据
    luceneService.writeArticle(luceneService.getAllArticleLucene());
    return GlobalResultGenerator.genSuccessResult("创建索引成功");
  }

  /**
   * 搜索，实现高亮
   *
   * @param q
   * @return
   */
  @GetMapping("/searchArticle/{q}")
  public GlobalResult searchArticle(
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
      temp.setArticlePreviewContent(subList.get(i).getArticleContent());
      articleDTOList.set(i, temp);
    }
    page.addAll(articleDTOList);
    PageInfo<ArticleDTO> pageInfo = new PageInfo<>(page);
    return GlobalResultGenerator.genSuccessResult(Utils.getArticlesGlobalResult(pageInfo));
  }
}
