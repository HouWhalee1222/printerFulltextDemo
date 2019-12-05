package fulltext.print.demo.utils;

import fulltext.print.demo.bean.Document;
import fulltext.print.demo.service.DocumentService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicInteger;

@Log4j2
@Component
public class UploadAndIndexingFiles {

    @Autowired
    private DocumentService documentService;

    @Value("${spring.data.solr.commitRate}")
    private int commitRate;

    private static int totalDocs = 0;
    private static AtomicInteger currentDocsIndexed = new AtomicInteger(0);
    private static long startTime = System.currentTimeMillis();

    private Collection<Future<String>> results = new ConcurrentLinkedDeque<>(); //Bug Here
    private ConcurrentHashMap<String, Double> performanceMap = new ConcurrentHashMap<>();

    public void insertDocument(String filePath) throws IOException {
        // Calculate the total number of docs need to index
        getTotalDocs(filePath);

        // Initialize the
        performanceMap.put("10000", -1.0);
        performanceMap.put("50000", -1.0);
        performanceMap.put("100000", -1.0);
        performanceMap.put("200000", -1.0);
        performanceMap.put("500000", -1.0);
        performanceMap.put("All", -1.0);

        startTime = System.currentTimeMillis();
        // Index all the files in parallel
        UploadAndIndexingFiles uploadAndIndexingFilesProxy = SpringUtil.getBean(UploadAndIndexingFiles.class);
        results.add(uploadAndIndexingFilesProxy.insertDocumentHelper(filePath));
    }

    @Async
    public Future<String> insertDocumentHelper(String path) throws IOException {
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
                increaseCurrentDocIndexed();
                br.close();

                if (i % commitRate == 0) { // Commit every 100 documents
                    documentService.commitToSolr();
                }

            } else if (tempList[i].isDirectory()) {
                UploadAndIndexingFiles uploadAndIndexingFilesProxy = SpringUtil.getBean(UploadAndIndexingFiles.class);
                results.add(uploadAndIndexingFilesProxy.insertDocumentHelper(tempList[i].toString()));
            }
        }
        return new AsyncResult<>("Complete");
    }

    private static void getTotalDocs(String filePath) {
        File file = new File(filePath);
        File[] tempList = file.listFiles();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                totalDocs += 1;
            } else if (tempList[i].isDirectory()) {
               getTotalDocs(tempList[i].toString());
            }
        }
    }

    private void increaseCurrentDocIndexed() {
        int currentValue = currentDocsIndexed.getAndIncrement();
        if (currentValue == totalDocs) {
            performanceMap.replace("All", (System.currentTimeMillis() - startTime) / 1000.0);
        }
        switch (currentValue) {
            case 10000:
                performanceMap.replace("10000", (System.currentTimeMillis() - startTime) / 1000.0);
                break;
            case 50000:
                performanceMap.replace("50000", (System.currentTimeMillis() - startTime) / 1000.0);
                break;
            case 100000:
                performanceMap.replace("100000", (System.currentTimeMillis() - startTime) / 1000.0);
                break;
            case 200000:
                performanceMap.replace("200000", (System.currentTimeMillis() - startTime) / 1000.0);
                break;
            case 500000:
                performanceMap.replace("500000", (System.currentTimeMillis() - startTime) / 1000.0);
                break;
            default:
                break;
        }
    }

    public Integer getTotalDoc() {
        return totalDocs;
    }

    public Integer getCurrentDocIndexed() {
        return currentDocsIndexed.get();
    }

    public ConcurrentHashMap<String, Double> getPerformanceMap() {
        return performanceMap;
    }
}
