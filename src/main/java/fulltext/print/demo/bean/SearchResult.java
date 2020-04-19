package fulltext.print.demo.bean;

import java.io.Serializable;
import java.util.List;

public class SearchResult implements Serializable {
    // search result
    private List<Document> documentList;

    // total number of result
    private long recordCount;

    // total page
    private long pageCount;

    // current page
    private long curPage;

    public List<Document> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List<Document> documentList) {
        this.documentList = documentList;
    }

    public long getRecordCount() {
        return recordCount;
    }

    public void setRecordCount(long recordCount) {
        this.recordCount = recordCount;
    }

    public long getPageCount() {
        return pageCount;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }

    public long getCurPage() {
        return curPage;
    }

    public void setCurPage(long curPage) {
        this.curPage = curPage;
    }

    @Override
    public String toString() {
        return "SearchResult{" +
                "documentList=" + documentList +
                ", recordCount=" + recordCount +
                ", pageCount=" + pageCount +
                ", curPage=" + curPage +
                '}';
    }
}
