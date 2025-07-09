package com.rymcu.forest.lucene.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.rymcu.forest.lucene.model.PortfolioLucene;
import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import java.io.IOException;
import java.util.Arrays;

/**
 * 作品集索引更新工具类
 *
 * @author suwen
 */
@Slf4j
public class PortfolioIndexUtil {

    /**
     * lucene索引保存目录
     */
    private static final String PATH = System.getProperty("user.dir") + StrUtil.SLASH + LucenePath.PORTFOLIO_PATH;

    /**
     * 删除所有运行中保存的索引
     */
    public static void deleteAllIndex() {
        if (FileUtil.exist(LucenePath.PORTFOLIO_INCREMENT_INDEX_PATH)) {
            FileUtil.del(LucenePath.PORTFOLIO_INCREMENT_INDEX_PATH);
        }
    }

    public static void addIndex(PortfolioLucene t) {
        creatIndex(t);
    }

    public static void updateIndex(PortfolioLucene t) {
        deleteIndex(t.getIdPortfolio());
        creatIndex(t);
    }

    /**
     * 增加或创建单个索引
     *
     * @param t
     * @throws Exception
     */
    private static synchronized void creatIndex(PortfolioLucene t) {
        log.info("创建单个索引");
        IndexWriter writer;
        try {
            boolean create = true;
            if (FileUtil.exist(LucenePath.PORTFOLIO_INCREMENT_INDEX_PATH)) {
                create = false;
            }
            writer = IndexUtil.getIndexWriter(LucenePath.PORTFOLIO_INCREMENT_INDEX_PATH, create);
            Document doc = new Document();
            doc.add(new StringField("id", t.getIdPortfolio() + "", Field.Store.YES));
            doc.add(new TextField("title", t.getPortfolioTitle(), Field.Store.YES));
            doc.add(new TextField("summary", t.getPortfolioDescription(), Field.Store.YES));
            writer.addDocument(doc);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除单个索引
     */
    public static synchronized void deleteIndex(Long id) {
        Arrays.stream(FileUtil.ls(PATH))
                .forEach(
                        each -> {
                            if (each.isDirectory()) {
                                IndexWriter writer;
                                try {
                                    writer = IndexUtil.getIndexWriter(each.getAbsolutePath(), false);
                                    writer.deleteDocuments(new Term("id", String.valueOf(id)));
                                    writer.forceMerge(1);
                                    writer.forceMergeDeletes(); // 强制删除
                                    writer.commit();
                                    writer.close();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
    }
}
