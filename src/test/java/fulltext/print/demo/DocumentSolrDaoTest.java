package fulltext.print.demo;

import fulltext.print.demo.bean.Document;
import fulltext.print.demo.dao.document.solr.SolrDaoUsingCrudRepository;
import fulltext.print.demo.dao.document.solr.SolrDaoUsingSolrTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;

@SpringBootTest
public class DocumentSolrDaoTest {

    @Autowired
    private SolrDaoUsingCrudRepository solrDaoUsingCrudRepository;

    @Autowired
    private SolrDaoUsingSolrTemplate solrDaoUsingSolrTemplate;

    @Test
    public void insertSolrTest() {
        Document document = new Document();
        document.setId(String.valueOf(1));
        document.setAuthor("happy coding");
        document.setTitle("beautiful code");
        document.setContent("hello world!!");
        document.setPrintTime(new Date());
        document.setUrl("/data");
        solrDaoUsingCrudRepository.save(document);
    }

    @Test
    public void findAllTest() {
        Pageable pageable = PageRequest.of(0, 100);
        Page<Document> page = solrDaoUsingCrudRepository.findAll(pageable);

        for (Document document : page.getContent()) {
            System.out.println(document.getId());
        }
        System.out.println(page.getTotalElements());
    }

    @Test
    public void deleteDocumentByPrintTimeBeforeTest() {
        solrDaoUsingCrudRepository.deleteDocumentByPrintTimeBefore(new Date());
    }

    @Test
    public void addDocumentWithOutCommitTest() {
        Document document = new Document();
        document.setId(String.valueOf(1));
        document.setAuthor("happy coding");
        document.setTitle("beautiful code");
        document.setContent("hello world!!");
        document.setPrintTime(new Date());
        document.setUrl("/data");
        solrDaoUsingSolrTemplate.addDocumentWithOutCommit(document);
    }

    @Test
    public void commitTest() {
        solrDaoUsingSolrTemplate.commit();
    }

}
