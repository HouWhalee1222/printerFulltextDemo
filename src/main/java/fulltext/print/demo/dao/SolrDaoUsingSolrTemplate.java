package fulltext.print.demo.dao;

import fulltext.print.demo.bean.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class SolrDaoUsingSolrTemplate {

    @Autowired
    private SolrTemplate solrTemplate;

    @Value("${spring.data.solr.core}")
    private String collection;

    public void addDocumentWithOutCommit(Document document) {
        solrTemplate.saveBean(collection, document);
    }

    public void commit() {
        solrTemplate.commit(collection);
    }
}
