package fulltext.print.demo;

import fulltext.print.demo.bean.SearchResult;
import fulltext.print.demo.service.DocumentService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class DocumentSearchTest {

    @Autowired
    private DocumentService documentService;

    @Test
    public void searchTest() throws Exception {
        SearchResult searchResult = documentService.search("hello", 0, 10);
        System.out.println(searchResult);
    }
}
