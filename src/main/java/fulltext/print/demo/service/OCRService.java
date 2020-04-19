package fulltext.print.demo.service;

import fulltext.print.demo.component.OCR;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Log4j2
@Service
public class OCRService {

    @Autowired
    private OCR ocr;

    public String doOCRForOneFile(File file) throws IOException, ExecutionException, InterruptedException {
        // Requires the file to be image file
        ImageInputStream imageInputStream = ImageIO.createImageInputStream(file);
        if (imageInputStream == null || imageInputStream.length() == 0) {
            System.out.println("error");
        }
        Iterator<ImageReader> iterator = ImageIO.getImageReaders(imageInputStream);
        if (iterator == null || !iterator.hasNext()) {
            throw new IOException("Image file format not supported by ImageIO: " + file.getPath());
        }
        ImageReader reader = iterator.next();
        reader.setInput(imageInputStream);
        int numPages = reader.getNumImages(true);
        log.info("Image file " + file.getName() + " contains " + numPages + " pages");

        List<Future> futures = new ArrayList<>();

        for (int i = 0; i < numPages; i++) {
            BufferedImage image = reader.read(i);
            Future<String> currentResult = ocr.doOCRForSingleImage(image);
            futures.add(currentResult);
        }

        String result = "";
        for (Future<?> future : futures) {
            while (true) {
                if (future.isDone() && !future.isCancelled()) {
                    String myResult = (String) future.get();
                    result += myResult;
                    break;
                } else {
                    Thread.sleep(1);
                }
            }
        }
        return result;
    }

}
