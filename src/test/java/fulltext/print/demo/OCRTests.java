package fulltext.print.demo;

import fulltext.print.demo.component.OCR;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class OCRTests {

    @Autowired
    private OCR ocr;

    @Test
    public void doOCRForOneFileTest() {
        System.out.println(ocr.doOCRForOnePageFile("D:\\fulltext\\Data\\test1.png"));
    }
}
