package fulltext.print.demo.dao;

import fulltext.print.demo.bean.Document;
import org.springframework.data.solr.repository.SolrCrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Date;

@Repository
public interface SolrDao extends SolrCrudRepository<Document, String> {

    void deleteDocumentByPrintTimeBefore(Date date);

}
