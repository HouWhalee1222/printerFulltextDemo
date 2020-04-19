package fulltext.print.demo.component;

import lombok.extern.log4j.Log4j2;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.awt.image.BufferedImage;
import java.io.File;
import java.util.concurrent.Future;

@Log4j2
@Component
public class OCR {

    @Value("${spring.ocr.datapath}")
    private String dataPath;

    @Async
    public Future<String> doOCRForSingleImage(BufferedImage image) {
        // conducting OCR for one single image
        log.info("Starting OCR for BufferedImage: " + image.hashCode());
        ITesseract iTesseract = new Tesseract();
        // dataPath contains all the pre-training data for OCR
        iTesseract.setDatapath(dataPath);

        String result = null;
        try {
            result = iTesseract.doOCR(image);
        } catch (TesseractException e) {
            e.printStackTrace();
        }

        return new AsyncResult<>(result);
    }
}
