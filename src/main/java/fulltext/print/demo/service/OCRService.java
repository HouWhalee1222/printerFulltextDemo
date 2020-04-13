package fulltext.print.demo.service;

import fulltext.print.demo.component.OCR;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;

@Service
public class OCRService {

    @Autowired
    private OCR ocr;

    public String doOCRforOneFile(File file) throws IOException {
        ImageInputStream imageInputStream = ImageIO.createImageInputStream(file);
        if (imageInputStream == null || imageInputStream.length() == 0) {
            System.out.println("error");
        }
        Iterator<ImageReader> iterator = ImageIO.getImageReaders(imageInputStream);
        if (iterator == null || !iterator.hasNext()) {
            throw new IOException("Image file format not supported by ImageIO: " + file.getPath());
        }
        ImageReader reader = iterator.next();
        iterator = null;
        reader.setInput(imageInputStream);
        int numPages = reader.getNumImages(true);

        return "";
    }

}
