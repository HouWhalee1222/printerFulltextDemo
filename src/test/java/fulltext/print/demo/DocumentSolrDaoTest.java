package fulltext.print.demo;

import fulltext.print.demo.bean.Document;
import fulltext.print.demo.dao.SolrDao;
import fulltext.print.demo.dao.SolrDaoUsingSolrTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Date;
import java.util.List;

@SpringBootTest
public class DocumentSolrDaoTest {

    @Autowired
    private SolrDao solrDao;

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
        solrDao.save(document);
    }

    @Test
    public void findAllTest() {
        Pageable pageable = PageRequest.of(0, 100);
        Page<Document> page = solrDao.findAll(pageable);

        for (Document document : page.getContent()) {
            System.out.println(document.getId());
        }
        System.out.println(page.getTotalElements());
    }

    @Test
    public void deleteDocumentByPrintTimeBeforeTest() {
        solrDao.deleteDocumentByPrintTimeBefore(new Date());
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
