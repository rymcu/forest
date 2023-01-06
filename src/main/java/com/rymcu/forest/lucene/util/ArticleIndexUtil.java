package com.rymcu.forest.lucene.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.rymcu.forest.lucene.model.ArticleLucene;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 文章索引更新工具类
 *
 * @author suwen
 */
public class ArticleIndexUtil {

    /**
     * lucene索引保存目录
     */
    private static final String PATH = System.getProperty("user.dir") + StrUtil.SLASH + LucenePath.ARTICLE_INDEX_PATH;

    /**
     * 删除所有运行中保存的索引
     */
    public static void deleteAllIndex() {
        if (FileUtil.exist(LucenePath.ARTICLE_INCREMENT_INDEX_PATH)) {
            FileUtil.del(LucenePath.ARTICLE_INCREMENT_INDEX_PATH);
        }
    }

    public static void addIndex(ArticleLucene t) {
        creatIndex(t);
    }

    public static void updateIndex(ArticleLucene t) {
        deleteIndex(t.getIdArticle());
        creatIndex(t);
    }

    /**
     * 增加或创建单个索引
     *
     * @param t
     * @throws Exception
     */
    private static void creatIndex(ArticleLucene t) {
        System.out.printf("创建单个索引");
        IndexWriter writer;
        ReentrantLock reentrantLock = new ReentrantLock();
        reentrantLock.lock();
        try {
            boolean create = true;
            if (FileUtil.exist(LucenePath.ARTICLE_INCREMENT_INDEX_PATH)) {
                create = false;
            }
            writer = IndexUtil.getIndexWriter(LucenePath.ARTICLE_INCREMENT_INDEX_PATH, create);
            Document doc = new Document();
            doc.add(new StringField("id", t.getIdArticle() + "", Field.Store.YES));
            doc.add(new TextField("title", t.getArticleTitle(), Field.Store.YES));
            doc.add(new TextField("summary", t.getArticleContent(), Field.Store.YES));
            writer.addDocument(doc);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            reentrantLock.unlock();
        }
    }

    /**
     * 删除单个索引
     */
    public static void deleteIndex(Long id) {
        Arrays.stream(FileUtil.ls(PATH))
                .forEach(
                        each -> {
                            if (each.isDirectory()) {
                                IndexWriter writer;
                                ReentrantLock reentrantLock = new ReentrantLock();
                                reentrantLock.lock();
                                try {
                                    writer = IndexUtil.getIndexWriter(each.getAbsolutePath(), false);
                                    writer.deleteDocuments(new Term("id", String.valueOf(id)));
                                    writer.forceMerge(1);
                                    // 强制删除
                                    writer.forceMergeDeletes();
                                    writer.commit();
                                    writer.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                } finally {
                                    reentrantLock.unlock();
                                }
                            }
                        });
    }
}
