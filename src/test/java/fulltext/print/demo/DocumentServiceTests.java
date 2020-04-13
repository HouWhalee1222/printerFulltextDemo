package fulltext.print.demo;

import fulltext.print.demo.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
public class DocumentServiceTests {

    @Autowired
    private DocumentService documentService;

    @Test
    public void insertFileTest() throws IOException {
        documentService.insertFile("D:\\fulltext\\Data\\test1.png");
        documentService.commitToSolr();
    }
}
