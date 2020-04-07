package fulltext.print.demo.service;

import fulltext.print.demo.bean.Document;
import fulltext.print.demo.bean.SearchResult;
import fulltext.print.demo.component.OCR;
import fulltext.print.demo.dao.DocumentDao;
import fulltext.print.demo.dao.SolrDao;
import fulltext.print.demo.dao.SolrDaoUsingSolrTemplate;
import lombok.extern.log4j.Log4j2;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.*;
import java.util.*;
import java.util.List;

@Log4j2
@Service
public class DocumentService {

    @Autowired
    private SolrClient solrClient;
    @Autowired
    private SolrDao solrDao;
    @Autowired
    private SolrDaoUsingSolrTemplate solrDaoUsingSolrTemplate;

    @Autowired
    private DocumentDao documentDao;

    @Autowired
    private OCR ocr;

    @Value("${spring.data.solr.core}")
    private String collection;

    @Value("${spring.data.solr.keepDays}")
    private int keepDays;

    public SearchResult search(String queryString, int page, int row)  throws Exception {

        log.info("Start searching " + queryString + " page: " + page + " row: " + row);
        SearchResult searchResult = new SearchResult();
        SolrQuery solrQuery = new SolrQuery();

        // set query
        solrQuery.setQuery(queryString);
        // set page
        solrQuery.setStart(page * row);
        solrQuery.setRows(row);
        // set highlighting
        solrQuery.setHighlight(true);
        solrQuery.addHighlightField("title");
        solrQuery.addHighlightField("author");
        solrQuery.addHighlightField("content");
        solrQuery.setHighlightSimplePre("<b style='color:red'>");
        solrQuery.setHighlightSimplePost("</b>");
        // sort by print time
        solrQuery.setSort("printTime", SolrQuery.ORDER.desc);
        // select corresponding text
        solrQuery.setHighlightFragsize(100);

        QueryResponse response = solrClient.query(collection, solrQuery);
        SolrDocumentList results = response.getResults();
        List<Document> documents = new ArrayList<>();
        Map<String, Map<String, List<String>>> highlightResult = response.getHighlighting();

        for (SolrDocument document : results) {
            String id = (String) document.get("id");
            if (highlightResult.get(id) != null && highlightResult.get(id).get("title") != null) {
                document.setField("title", highlightResult.get(id).get("title").get(0));
            }
            if (highlightResult.get(id) != null && highlightResult.get(id).get("content") != null) {
                document.setField("content", highlightResult.get(id).get("content").get(0));
            }

            if (highlightResult.get(id) != null && highlightResult.get(id).get("author") != null) {
                document.setField("author", highlightResult.get(id).get("author").get(0));
            }


            Document doc = new Document();
            doc.setId(id);
            doc.setTitle((String) document.get("title"));
            doc.setAuthor((String) document.get("author"));
            if (((String) document.get("content")).length() > 500) { // Solr has some bug here
                document.setField("content", "Click view to see the detail");
            }
            doc.setContent((String) document.get("content"));
            doc.setPrintTime((Date) document.get("printTime"));
            documents.add(doc);
        }

        searchResult.setRecordCount(results.getNumFound());
        searchResult.setDocumentList(documents);
        searchResult.setCurPage(page);

        long recordCount = searchResult.getRecordCount();
        long pageCount = recordCount / row;

        if (recordCount % row > 0) {
            pageCount += 1;
        }

        searchResult.setPageCount(pageCount);

        log.info("Search finished");

        return searchResult;
    }

    public List<Document> selectAll() {
        return documentDao.findAll();
    }

    public Document selectDocumentById(String id) {
        return documentDao.findDocumentById(id);
    }

    @Transactional
    public void insertDocument(Document document) {
        documentDao.insertOneDocument(document);
        solrDaoUsingSolrTemplate.addDocumentWithOutCommit(document);
        System.out.println(Thread.currentThread().getName() + ": Insert succeed for document " + document.getUrl());
        //log.info(Thread.currentThread().getName() + ": Insert succeed for document " + document.getUrl());
        //
    }

    @Transactional
    public void deleteOldDocuments() {
        log.info("Start deleting old documents");
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -keepDays);
        Date deleteTime = calendar.getTime();

        deleteOldDocumentOfSolr(deleteTime);
        deleteOldDocumentsOfSQL(deleteTime);
        log.info("Delete succeed!");
    }

    @Transactional
    public void deleteAll() {
        log.info("Start deleting all documents");
        Date deleteTime = new Date();
        deleteOldDocumentsOfSQL(deleteTime);
        deleteOldDocumentOfSolr(deleteTime);
        log.info("Delete succeed!");
    }

    public boolean insertFile(File file) throws IOException {
        String content = "";
        if (isImage(file)) {
            content = ocr.doOCRForOneFile(file);
        } else {
            File filename = new File(String.valueOf(file));
            InputStreamReader reader = new InputStreamReader(new FileInputStream(filename));
            BufferedReader br = new BufferedReader(reader);

            String line = br.readLine();
            while (line != null) {
                content = content + line;
                line = br.readLine();
            }
            br.close();
        }

        Document document = new Document();
        document.setId(UUID.randomUUID().toString());
        document.setAuthor("作者");
        document.setTitle(file.getName());
        document.setContent(content);
        document.setPrintTime(new Date());
        document.setUrl(file.getPath());
        insertDocument(document);
        return true;
    }

    public boolean insertFile(String filePath) throws IOException {
        File file = new File(filePath);
        return insertFile(file);
    }

    private boolean isImage(File file) {
        try {
            Image image = ImageIO.read(file);
            return image != null;
        } catch (IOException e) {
            return false;
        }
    }

    public void deleteOldDocumentsOfSQL(Date date) {
        documentDao.deleteDocumentByPrintTimeBefore(date);
    }

    public void deleteOldDocumentOfSolr(Date date) {
        solrDao.deleteDocumentByPrintTimeBefore(date);
    }

    public void commitToSolr() {
        solrDaoUsingSolrTemplate.commit();
    }


}
