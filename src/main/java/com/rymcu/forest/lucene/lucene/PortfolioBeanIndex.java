package com.rymcu.forest.lucene.lucene;

import com.rymcu.forest.lucene.model.PortfolioLucene;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;

import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * PortfolioBeanIndex
 *
 * @author suwen
 * @date 2021/4/17 14:10
 */
public class PortfolioBeanIndex extends BaseIndex<PortfolioLucene> {

    public PortfolioBeanIndex(
            String parentIndexPath,
            int subIndex,
            CountDownLatch countDownLatch1,
            CountDownLatch countDownLatch2,
            List<PortfolioLucene> list) {
        super(parentIndexPath, subIndex, countDownLatch1, countDownLatch2, list);
    }

    @Override
    public void indexDoc(IndexWriter writer, PortfolioLucene user) throws Exception {
        Document doc = new Document();
        Field id = new Field("id", user.getIdPortfolio() + "", TextField.TYPE_STORED);
        Field title = new Field("title", user.getPortfolioTitle(), TextField.TYPE_STORED);
        Field summary = new Field("summary", user.getPortfolioDescription(), TextField.TYPE_STORED);
        // 添加到Document中
        doc.add(id);
        doc.add(title);
        doc.add(summary);
        if (writer.getConfig().getOpenMode() == IndexWriterConfig.OpenMode.CREATE) {
            writer.addDocument(doc);
        } else {
            writer.updateDocument(new Term("id", user.getIdPortfolio() + ""), doc);
        }
    }
}
