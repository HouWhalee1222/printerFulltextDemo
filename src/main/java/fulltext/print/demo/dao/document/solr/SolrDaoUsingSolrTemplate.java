package fulltext.print.demo.dao.document.solr;

import fulltext.print.demo.bean.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Repository;

import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class SolrDaoUsingSolrTemplate {

    @Autowired
    private SolrTemplate solrTemplate;

    @Value("${spring.data.solr.commitRate}")
    private int commitRate;

    @Value("${spring.data.solr.core}")
    private String collection;

    private AtomicInteger unCommittedDocuments = new AtomicInteger(0);

    public void addDocumentWithOutCommit(Document document) {
        solrTemplate.saveBean(collection, document);
        unCommittedDocuments.incrementAndGet();
        if (unCommittedDocuments.incrementAndGet() % commitRate == 0) {
            commit();
        }
    }

    public void commit() {
        unCommittedDocuments.getAndSet(0);
        solrTemplate.commit(collection);
    }
}
