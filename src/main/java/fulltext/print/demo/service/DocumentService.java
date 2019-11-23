package fulltext.print.demo.service;

import fulltext.print.demo.bean.Document;
import fulltext.print.demo.bean.SearchResult;
import fulltext.print.demo.dao.DocumentDao;
import fulltext.print.demo.dao.SolrDao;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.SolrDocument;
import org.apache.solr.common.SolrDocumentList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
public class DocumentService {

    @Autowired
    private SolrClient solrClient;

    @Autowired
    private SolrDao solrDao;

    @Autowired
    private DocumentDao documentDao;

    @Value("${spring.data.solr.core}")
    private String collection;

    @Value("${spring.data.solr.keepDays}")
    private int keepDays;

    public SearchResult search(String queryString, int page, int row)  throws Exception {

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
        solrDao.save(document);
    }

    @Transactional
    public void deleteOldDocuments() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DATE, -keepDays);
        Date deleteTime = calendar.getTime();

        deleteOldDocumentOfSolr(deleteTime);
        deleteOldDocumentsOfSQL(deleteTime);
    }

    public void deleteOldDocumentsOfSQL(Date date) {
        documentDao.deleteDocumentByPrintTimeBefore(date);
    }

    public void deleteOldDocumentOfSolr(Date date) {
        solrDao.deleteDocumentByPrintTimeBefore(date);
    }
}
