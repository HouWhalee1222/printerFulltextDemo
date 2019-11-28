package fulltext.print.demo.utils;

import fulltext.print.demo.bean.Document;
import fulltext.print.demo.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.Date;
import java.util.UUID;

@Component
public class UploadAndIndexingFiles {

    @Autowired
    private DocumentService documentService;

    public void insertDocument(String filesPath) throws IOException {
        UploadAndIndexingFiles uploadAndIndexingFilesProxy = SpringUtil.getBean(UploadAndIndexingFiles.class);
        uploadAndIndexingFilesProxy.insertDocumentHelper(filesPath);
    }

    @Async
    public void insertDocumentHelper(String path) throws IOException {
        File file = new File(path);
        File[] tempList = file.listFiles();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
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
                document.setId(UUID.randomUUID().toString());
                document.setAuthor("author "+i);
                document.setTitle(tempList[i].getParent() + "-" + tempList[i].getName());
                document.setContent(content);
                document.setPrintTime(new Date());
                document.setUrl(String.valueOf(tempList[i]));
                documentService.insertDocument(document);

                br.close();
            } else if (tempList[i].isDirectory()) {
                UploadAndIndexingFiles uploadAndIndexingFilesProxy = SpringUtil.getBean(UploadAndIndexingFiles.class);
                uploadAndIndexingFilesProxy.insertDocumentHelper(tempList[i].toString());
            }
        }
    }
}
