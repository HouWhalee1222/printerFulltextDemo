package fulltext.print.demo.component;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class OCR {

    @Value("${spring.ocr.datapath}")
    private String dataPath;

    @Async
    public String doOCRForOnePageFile(String filePath) {
        File file = new File(filePath);
        return doOCRForOnePageFile(file);
    }

    @Async
    public String doOCRForOnePageFile(File file) {
        ITesseract iTesseract = new Tesseract();
        iTesseract.setDatapath(dataPath);

        String result = null;
        try {
            result = iTesseract.doOCR(file);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        return result.substring(1);
    }
}
