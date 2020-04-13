package fulltext.print.demo.dao.document;

import fulltext.print.demo.bean.Document;
import fulltext.print.demo.dao.document.solr.SolrDao;
import fulltext.print.demo.dao.document.sql.MySQLDocumentDao;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Log4j2
@Repository
public class DocumentDao {

    @Autowired
    private SolrDao solrDao;

    @Autowired
    private MySQLDocumentDao mySQLDocumentDao;

    @Transactional
    public void insertDocument(Document document) {
        mySQLDocumentDao.insertOneDocument(document);
        solrDao.addDocument(document);
        System.out.println(Thread.currentThread().getName() + ": Insert succeed for document " + document.getUrl());
    }

    @Transactional
    public void deleteDocumentBeforeTime(Date date) {
        log.info("DocumentDao: Start deleting document before: " + date.toString());
        mySQLDocumentDao.deleteDocumentByPrintTimeBefore(date);
        solrDao.deleteOldDocumentBeforeDate(date);
        log.info("DocumentDao: Delete succeed!");
    }

    public List<Document> findAll() {
        log.info("DocumentDao: Starting finding all document");
        List<Document> result = mySQLDocumentDao.findAll();
        log.info("DocumentDao: findAll() succeed!");
        return result;
    }

    public Document findDocumentById(String id) {
        return mySQLDocumentDao.findDocumentById(id);
    }

    public void commitSolr() {
        solrDao.commit();
    }


}
