package com.rymcu.forest.lucene.service.impl;

import com.rymcu.forest.dto.ArticleDTO;
import com.rymcu.forest.lucene.lucene.ArticleBeanIndex;
import com.rymcu.forest.lucene.lucene.IKAnalyzer;
import com.rymcu.forest.lucene.mapper.ArticleLuceneMapper;
import com.rymcu.forest.lucene.model.ArticleLucene;
import com.rymcu.forest.lucene.service.LuceneService;
import com.rymcu.forest.lucene.util.ArticleIndexUtil;
import com.rymcu.forest.lucene.util.SearchUtil;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * LuceneServiceImpl
 *
 * @author suwen
 * @date 2021/2/3 10:29
 */
@Service
public class LuceneServiceImpl implements LuceneService {

  @Resource private ArticleLuceneMapper luceneMapper;

  /** Lucene索引文件路径 */
  private final String indexPath = "lucene/index";

  /**
   * 将文章的数据解析为一个个关键字词存储到索引文件中
   *
   * @param list
   */
  @Override
  public void writeArticle(List<ArticleLucene> list) {
    try {
      int totalCount = list.size();
      int perThreadCount = 3000;
      int threadCount = totalCount / perThreadCount + (totalCount % perThreadCount == 0 ? 0 : 1);
      ExecutorService pool = Executors.newFixedThreadPool(threadCount);
      CountDownLatch countDownLatch1 = new CountDownLatch(1);
      CountDownLatch countDownLatch2 = new CountDownLatch(threadCount);

      for (int i = 0; i < threadCount; i++) {
        int start = i * perThreadCount;
        int end = Math.min((i + 1) * perThreadCount, totalCount);
        List<ArticleLucene> subList = list.subList(start, end);
        Runnable runnable =
            new ArticleBeanIndex(indexPath, i, countDownLatch1, countDownLatch2, subList);
        // 子线程交给线程池管理
        pool.execute(runnable);
      }
      countDownLatch1.countDown();
      System.out.println("开始创建索引");
      // 等待所有线程都完成
      countDownLatch2.await();
      // 线程全部完成工作
      System.out.println("所有线程都创建索引完毕");
      // 释放线程池资源
      pool.shutdown();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void writeArticle(String id) {
    writeArticle(luceneMapper.getById(id));
  }

  @Override
  public void writeArticle(ArticleLucene articleLucene) {
    ArticleIndexUtil.addIndex(articleLucene);
  }

  @Override
  public void updateArticle(String id) {
    ArticleIndexUtil.updateIndex(luceneMapper.getById(id));
  }

  @Override
  public void deleteArticle(String id) {
    ArticleIndexUtil.deleteIndex(id);
  }

  /**
   * 关键词搜索
   *
   * @param value
   * @return
   * @throws Exception
   */
  @Override
  public List<ArticleLucene> searchArticle(String value) {
    List<ArticleLucene> resList = new ArrayList<>();
    ExecutorService service = Executors.newCachedThreadPool();
    // 定义分词器
    Analyzer analyzer = new IKAnalyzer();
    try {
      IndexSearcher searcher = SearchUtil.getIndexSearcherByParentPath(indexPath, service);
      String[] fields = {"title", "summary"};
      // 构造Query对象
      MultiFieldQueryParser parser = new MultiFieldQueryParser(fields, analyzer);

      BufferedReader in =
          new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
      String line = value != null ? value : in.readLine();
      Query query = parser.parse(line);
      // 最终被分词后添加的前缀和后缀处理器，默认是粗体<B></B>
      SimpleHTMLFormatter htmlFormatter =
          new SimpleHTMLFormatter("<font color=" + "\"" + "red" + "\"" + ">", "</font>");
      // 高亮搜索的词添加到高亮处理器中
      Highlighter highlighter = new Highlighter(htmlFormatter, new QueryScorer(query));

      // 获取搜索的结果，指定返回document返回的个数
      // TODO 默认搜索结果为显示第一页，1000 条，可以优化
      TopDocs results = SearchUtil.getScoreDocsByPerPage(1, 100, searcher, query);
      ScoreDoc[] hits = results.scoreDocs;

      // 遍历，输出
      for (ScoreDoc hit : hits) {
        int id = hit.doc;
        float score = hit.score;
        Document hitDoc = searcher.doc(hit.doc);
        Map<String, String> map = new HashMap<>();
        map.put("id", hitDoc.get("id"));

        // 获取到summary
        String name = hitDoc.get("summary");
        // 将查询的词和搜索词匹配，匹配到添加前缀和后缀
        TokenStream tokenStream =
            TokenSources.getAnyTokenStream(searcher.getIndexReader(), id, "summary", analyzer);
        // 传入的第二个参数是查询的值
        TextFragment[] frag = highlighter.getBestTextFragments(tokenStream, name, false, 10);
        StringBuilder baikeValue = new StringBuilder();
        for (TextFragment textFragment : frag) {
          if ((textFragment != null) && (textFragment.getScore() > 0)) {
            //  if ((frag[j] != null)) {
            // 获取 summary 的值
            baikeValue.append(textFragment.toString());
          }
        }

        // 获取到title
        String title = hitDoc.get("title");
        TokenStream titleTokenStream =
            TokenSources.getAnyTokenStream(searcher.getIndexReader(), id, "title", analyzer);
        TextFragment[] titleFrag =
            highlighter.getBestTextFragments(titleTokenStream, title, false, 10);
        StringBuilder titleValue = new StringBuilder();
        for (int j = 0; j < titleFrag.length; j++) {
          if ((frag[j] != null)) {
            titleValue.append(titleFrag[j].toString());
          }
        }
        resList.add(
            ArticleLucene.builder()
                .idArticle(hitDoc.get("id"))
                .articleTitle(titleValue.toString())
                .articleContent(baikeValue.toString())
                .score(String.valueOf(score))
                .build());
      }
    } catch (IOException | ParseException | InvalidTokenOffsetsException e) {
      System.out.println(e.getMessage());
      e.printStackTrace();
    } finally {
      service.shutdownNow();
    }
    return resList;
  }

  @Override
  public List<ArticleLucene> getAllArticleLucene() {
    return luceneMapper.getAllArticleLucene();
  }

  @Override
  public List<ArticleDTO> getArticlesByIds(String[] ids) {
    return luceneMapper.getArticlesByIds(ids);
  }
}
