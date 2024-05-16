package com.rymcu.forest.lucene.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import com.rymcu.forest.lucene.model.UserLucene;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.Term;

import java.io.IOException;
import java.util.Arrays;


/**
 * 用户索引更新工具类
 *
 * @author suwen
 */
@Slf4j
public class UserIndexUtil {

    /**
     * lucene索引保存目录
     */
    private static final String PATH = System.getProperty("user.dir") + StrUtil.SLASH + LucenePath.USER_PATH;

    /**
     * 系统运行时索引保存目录
     */
    private static final String INDEX_PATH = LucenePath.USER_INCREMENT_INDEX_PATH;

    /**
     * 删除所有运行中保存的索引
     */
    public static void deleteAllIndex() {
        if (FileUtil.exist(INDEX_PATH)) {
            FileUtil.del(INDEX_PATH);
        }
    }

    public static void addIndex(UserLucene t) {
        creatIndex(t);
    }

    public static void updateIndex(UserLucene t) {
        deleteIndex(t.getIdUser().toString());
        creatIndex(t);
    }

    /**
     * 增加或创建单个索引
     *
     * @param t
     * @throws Exception
     */
    private static synchronized void creatIndex(UserLucene t) {
        log.info("创建单个索引");
        IndexWriter writer;
        try {
            boolean create = true;
            if (FileUtil.exist(LucenePath.USER_INCREMENT_INDEX_PATH)) {
                create = false;
            }
            writer = IndexUtil.getIndexWriter(INDEX_PATH, create);
            Document doc = new Document();
            doc.add(new StringField("id", t.getIdUser() + "", Field.Store.YES));
            doc.add(new TextField("nickname", t.getNickname(), Field.Store.YES));
            // 新注册用户无签名
            doc.add(new TextField("signature", StringUtils.isNotBlank(t.getSignature()) ? t.getSignature() : "", Field.Store.YES));
            writer.addDocument(doc);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除单个索引
     */
    public static synchronized void deleteIndex(String id) {
        Arrays.stream(FileUtil.ls(PATH))
                .forEach(
                        each -> {
                            if (each.isDirectory()) {
                                IndexWriter writer;
                                try {
                                    writer = IndexUtil.getIndexWriter(each.getAbsolutePath(), false);
                                    writer.deleteDocuments(new Term("id", id));
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
