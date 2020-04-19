package fulltext.print.demo.dao.document.solr;

import fulltext.print.demo.bean.Document;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public class SolrDao {
    // Unified Dao for Solr

    @Autowired
    private SolrDaoUsingSolrTemplate solrDaoUsingSolrTemplate;

    @Autowired
    private SolrDaoUsingCrudRepository solrDaoUsingCrudRepository;

    public void deleteOldDocumentBeforeDate(Date date) {
        solrDaoUsingCrudRepository.deleteDocumentByPrintTimeBefore(date);
    }

    public void addDocument(Document document) {
        solrDaoUsingSolrTemplate.addDocumentWithOutCommit(document);
    }

    public void commit() {
        solrDaoUsingSolrTemplate.commit();
    }
}
