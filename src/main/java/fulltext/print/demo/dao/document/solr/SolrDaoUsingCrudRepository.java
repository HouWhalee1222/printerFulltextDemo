package fulltext.print.demo.dao.document.solr;

import fulltext.print.demo.bean.Document;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface SolrDaoUsingCrudRepository extends SolrCrudRepository<Document, String> {

    void deleteDocumentByPrintTimeBefore(Date date);

}
