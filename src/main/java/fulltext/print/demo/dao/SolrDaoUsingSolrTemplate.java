package fulltext.print.demo.dao;

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

    private AtomicInteger unCommittedDocuments = new AtomicInteger(0);

    @Value("${spring.data.solr.core}")
    private String collection;

    public void addDocumentWithOutCommit(Document document) {
        solrTemplate.saveBean(collection, document);

        if (unCommittedDocuments.incrementAndGet() % commitRate == 0) {
            commit();
        }
    }

    public void commit() {
        solrTemplate.commit(collection);
    }
}
