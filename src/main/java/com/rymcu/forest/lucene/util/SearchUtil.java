package com.rymcu.forest.lucene.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.MultiReader;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;
import java.util.concurrent.ExecutorService;

/**
 * SearchUtil lucene索引查询工具类
 *
 * @author suwen
 * @date 2021/2/2 14:04
 */
@Slf4j
public class SearchUtil {
    /**
     * 获取IndexSearcher对象
     *
     * @param service
     * @return
     * @throws IOException
     */
    public static IndexSearcher getIndexSearcherByParentPath(
            String parentPath, ExecutorService service) {
        MultiReader reader = null;
        // 设置
        try {
            File[] files = new File(parentPath).listFiles();
            IndexReader[] readers = new IndexReader[files.length];
            for (int i = 0; i < files.length; i++) {
                readers[i] =
                        DirectoryReader.open(FSDirectory.open(Paths.get(files[i].getPath())));
            }
            reader = new MultiReader(readers);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new IndexSearcher(reader, service);
    }

    /**
     * 根据索引路径获取IndexReader
     *
     * @param indexPath
     * @return
     * @throws IOException
     */
    public static DirectoryReader getIndexReader(String indexPath) throws IOException {
        return DirectoryReader.open(FSDirectory.open(Paths.get(indexPath)));
    }

    /**
     * 根据索引路径获取IndexSearcher
     *
     * @param indexPath
     * @param service
     * @return
     * @throws IOException
     */
    public static IndexSearcher getIndexSearcherByIndexPath(String indexPath, ExecutorService service)
            throws IOException {
        IndexReader reader = getIndexReader(indexPath);
        return new IndexSearcher(reader, service);
    }

    /**
     * 如果索引目录会有变更用此方法获取新的IndexSearcher这种方式会占用较少的资源
     *
     * @param oldSearcher
     * @param service
     * @return
     * @throws IOException
     */
    public static IndexSearcher getIndexSearcherOpenIfChanged(
            IndexSearcher oldSearcher, ExecutorService service) throws IOException {
        DirectoryReader reader = (DirectoryReader) oldSearcher.getIndexReader();
        DirectoryReader newReader = DirectoryReader.openIfChanged(reader);
        return new IndexSearcher(newReader, service);
    }

    /**
     * 根据IndexSearcher和docID获取默认的document
     *
     * @param searcher
     * @param docID
     * @return
     * @throws IOException
     */
    public static Document getDefaultFullDocument(IndexSearcher searcher, int docID)
            throws IOException {
        return searcher.doc(docID);
    }

    /**
     * 根据IndexSearcher和docID
     *
     * @param searcher
     * @param docID
     * @param listField
     * @return
     * @throws IOException
     */
    public static Document getDocumentByListField(
            IndexSearcher searcher, int docID, Set<String> listField) throws IOException {
        return searcher.doc(docID, listField);
    }

    /**
     * 分页查询
     *
     * @param page     当前页数
     * @param perPage  每页显示条数
     * @param searcher searcher查询器
     * @param query    查询条件
     * @return
     * @throws IOException
     */
    public static TopDocs getScoreDocsByPerPage(
            int page, int perPage, IndexSearcher searcher, Query query) throws IOException {
        TopDocs result = null;
        if (query == null) {
            log.info(" Query is null return null ");
            return null;
        }
        ScoreDoc before = null;
        if (page != 1) {
            TopDocs docsBefore = searcher.search(query, (page - 1) * perPage);
            ScoreDoc[] scoreDocs = docsBefore.scoreDocs;
            if (scoreDocs.length > 0) {
                before = scoreDocs[scoreDocs.length - 1];
            }
        }
        result = searcher.searchAfter(before, query, perPage);
        return result;
    }

    public static TopDocs getScoreDocs(IndexSearcher searcher, Query query) throws IOException {
        TopDocs docs = searcher.search(query, getMaxDocId(searcher));
        return docs;
    }

    /**
     * 统计document的数量,此方法等同于matchAllDocsQuery查询
     *
     * @param searcher
     * @return
     */
    public static int getMaxDocId(IndexSearcher searcher) {
        return searcher.getIndexReader().maxDoc();
    }
}
