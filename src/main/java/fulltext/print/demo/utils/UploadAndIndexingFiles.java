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
import java.util.*;
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

    private static int totalDocs = 0;
    private static long startTime = System.currentTimeMillis();
    private static long endTime = System.currentTimeMillis();

    private ConcurrentHashMap<String, Double> performanceMap = new ConcurrentHashMap<>();
    private Collection<Future<String>> results = new ConcurrentLinkedDeque<>();

    public void insertDocument(String filePath) throws IOException, ExecutionException, InterruptedException {
        // Calculate the total number of docs need to index
        totalDocs = 0;
        results.clear();
        List<File> allFiles = new LinkedList<>();
        collectTotalDocs(filePath, allFiles);

        // Initialize the
        performanceMap.put("10000", -1.0);
        performanceMap.put("50000", -1.0);
        performanceMap.put("100000", -1.0);
        performanceMap.put("200000", -1.0);
        performanceMap.put("500000", -1.0);
        performanceMap.put("All", -1.0);

        startTime = System.currentTimeMillis();
        endTime = System.currentTimeMillis();
        insertDocumentHelper(allFiles);
    }

    public void insertDocumentHelper(List<File> files) throws IOException, ExecutionException, InterruptedException {
        for (File file : files) {
            Future<String> currentResult = documentService.insertFileAsync(file);
            results.add(currentResult);
        }
    }


    private static void collectTotalDocs(String filePath, List<File> result) {
        File file = new File(filePath);
        File[] tempList = file.listFiles();
        for (int i = 0; i < tempList.length; i++) {
            if (tempList[i].isFile()) {
                totalDocs += 1;
                result.add(tempList[i]);
            } else if (tempList[i].isDirectory()) {
                collectTotalDocs(tempList[i].toString(), result);
            }
        }
    }

    public ConcurrentHashMap<String, Double> getPerformanceMap() {
        int currentValue = getCurrentDocIndexed();
        log.info("UploadAndIndexingFiles: " + currentValue + "files are successfully indexed.");
        if (currentValue != totalDocs) {
            endTime = System.currentTimeMillis();
        }
        performanceMap.replace("All", (endTime - startTime) / 1000.0);
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
        return performanceMap;
    }

    public Integer getCurrentDocIndexed() {
        int result = 0;
        for (Future<String> r : results) {
            if (r.isDone()) {
                result += 1;
            }
        }
        return result;
    }

    public Integer getTotalDoc() {
        return totalDocs;
    }
}
