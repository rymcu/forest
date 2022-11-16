package com.rymcu.forest.lucene.util;

import com.rymcu.forest.lucene.lucene.IKAnalyzer;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.LogByteSizeMergePolicy;
import org.apache.lucene.index.LogMergePolicy;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;


/**
 * IndexUtil
 *
 * @author suwen
 * @date 2021/2/2 14:03
 */
public class IndexUtil {
    /**
     * 创建索引写入器
     *
     * @param indexPath
     * @param create
     * @return
     * @throws IOException
     */
    public static IndexWriter getIndexWriter(String indexPath, boolean create) throws IOException {
        Directory dir = FSDirectory.open(Paths.get(indexPath));
        Analyzer analyzer = new IKAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        LogMergePolicy mergePolicy = new LogByteSizeMergePolicy();
        //设置segment添加文档(Document)时的合并频率
        // 值较小,建立索引的速度就较慢
        // 值较大,建立索引的速度就较快,>10适合批量建立索引
        mergePolicy.setMergeFactor(50);
        //设置segment最大合并文档(Document)数
        //值较小有利于追加索引的速度
        //值较大,适合批量建立索引和更快的搜索
        mergePolicy.setMaxMergeDocs(5000);
        if (create) {
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        } else {
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE_OR_APPEND);
        }
        return new IndexWriter(dir, iwc);
    }
}
