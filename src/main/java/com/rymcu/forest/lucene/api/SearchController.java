package com.rymcu.forest.lucene.api;

import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.dto.ArticleSearchDTO;
import com.rymcu.forest.lucene.mapper.BaikeMapper;
import com.rymcu.forest.lucene.model.Baike;
import com.rymcu.forest.lucene.service.SearchService;
import com.rymcu.forest.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/lucene")
public class SearchController {
  @Autowired private BaikeMapper baikeMapper;
  @Autowired private SearchService searchService;
  @Autowired private ArticleService articleService;

  @GetMapping("/index")
  public String createIndex(int limit, int offset) {
    // 拉取数据
    List<Baike> baikes = baikeMapper.getAllBaike(limit, offset);
    searchService.write(baikes);
    return "成功";
  }

  @GetMapping("/indexArticle")
  public String createArticleIndex() {
    // 拉取数据
    List<ArticleDTO> list = articleService.findArticles(new ArticleSearchDTO());
    searchService.writeArticle(list);
    return "成功";
  }

  /**
   * 搜索，实现高亮
   *
   * @param q
   * @return
   * @throws Exception
   */
  @GetMapping("/search/{q}")
  public List<Map<String, String>> getSearchText(@PathVariable String q) throws Exception {
    return searchService.search(q);
  }

  /**
   * 搜索，实现高亮
   *
   * @param q
   * @return
   * @throws Exception
   */
  @GetMapping("/searchArticle/{q}")
  public List<?> searchArticle(@PathVariable String q) throws Exception {

    return searchService.searchArticle(q);
  }

  @GetMapping(value = "/search")
  public ModelAndView test(ModelAndView mv) {
    mv.setViewName("/search");
    return mv;
  }
}
