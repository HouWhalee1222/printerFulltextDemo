package fulltext.print.demo.controller;

import fulltext.print.demo.bean.SearchResult;
import fulltext.print.demo.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;


@RestController
public class DocumentSearchController {

    @Autowired
    private DocumentService documentService;

    @RequestMapping("/search")
    public ModelAndView search(@RequestParam(name = "q", defaultValue = "") String queryString,
                               @RequestParam(defaultValue = "0") Integer page,
                               @RequestParam(defaultValue = "15") Integer rows) throws Exception {
        page = page >= 0 ? page : 0;
        rows = rows >= 15 ? rows : 15;

        SearchResult search  = documentService.search(queryString, page, rows);

        ModelAndView mv = new ModelAndView("search");
        mv.addObject("query", queryString);
        mv.addObject("totalPages", search.getPageCount());
        mv.addObject("documentList", search.getDocumentList());
        mv.addObject("page", search.getCurPage());
        mv.addObject("totalRecord", search.getRecordCount());

        return mv;
    }
}