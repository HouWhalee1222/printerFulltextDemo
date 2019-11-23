package fulltext.print.demo;

import fulltext.print.demo.bean.Document;
import fulltext.print.demo.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.io.*;
import java.util.Date;
import java.util.List;

@SpringBootTest
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
    public void insertDocument() throws IOException {
        String filesPath = "/data/SmallTestFiles";

        File file = new File(filesPath);

        File[] tempList = file.listFiles();

        for (int i = 0; i < 10000; i++) {
            File filename = new File(String.valueOf(tempList[i]));
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);

            String content = "";
            String line = br.readLine();
            while (line != null) {
                content = content + line;
                line = br.readLine();
            }

            Document document = new Document();
            document.setId(String.valueOf(i + 1));
            document.setAuthor("author "+i);
            document.setTitle("book " + i);
            document.setContent(content);
            document.setPrintTime(new Date());
            document.setUrl(String.valueOf(tempList[i]));
            documentService.insertDocument(document);

            System.out.println("Document " + i + " finished!");
            br.close();
        }
    }

    @Test
    public void deleteDocumentByPrintTimeBeforeTest() {
        documentService.deleteOldDocumentsOfSQL(new Date());
    }

}
