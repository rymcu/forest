package com.rymcu.forest.lucene.lucene;


import com.rymcu.forest.lucene.model.Baike;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * BaiKeBeanIndex
 *
 * @author suwen
 * @date 2021/2/2 14:10
 */
public class BaiKeBeanIndex extends BaseIndex<Baike>{

    public BaiKeBeanIndex(IndexWriter writer, CountDownLatch countDownLatch1,
                         CountDownLatch countDownLatch2, List<Baike> list) {
        super(writer, countDownLatch1, countDownLatch2, list);
    }
    public BaiKeBeanIndex(String parentIndexPath, int subIndex, CountDownLatch countDownLatch1,
                         CountDownLatch countDownLatch2, List<Baike> list) {
        super(parentIndexPath, subIndex, countDownLatch1, countDownLatch2, list);
    }
    @Override
    public void indexDoc(IndexWriter writer, Baike t) throws Exception {
        Document doc = new Document();
        Field id = new Field("id", t.getId()+"", TextField.TYPE_STORED);
        Field title = new Field("title", t.getTitle(), TextField.TYPE_STORED);
        Field summary = new Field("summary", t.getSummary(), TextField.TYPE_STORED);
        //添加到Document中
        doc.add(id);
        doc.add(title);
        doc.add(summary);
        if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE){
            writer.addDocument(doc);
        }else{
            writer.updateDocument(new Term("id", t.getId()+""), doc);
        }
    }


}
