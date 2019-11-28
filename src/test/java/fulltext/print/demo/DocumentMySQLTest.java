package fulltext.print.demo;

import fulltext.print.demo.bean.Document;
import fulltext.print.demo.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Date;
import java.util.List;

@SpringBootTest
@ActiveProfiles(profiles = "non-async")
public class DocumentMySQLTest {

    @Autowired
    private DocumentService documentService;

    @Test
    public void selectByIdTest() {
        Document document = documentService.selectDocumentById("1");
        System.out.println(document);
    }

    @Test
    public void selectAllTest() {
        List<Document> documents = documentService.selectAll();
        for (Document doc : documents) {
            System.out.println(doc);
        }
    }

    @Test
    public void deleteDocumentByPrintTimeBeforeTest() {
        documentService.deleteOldDocumentsOfSQL(new Date());
    }

}
