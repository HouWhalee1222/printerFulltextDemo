package fulltext.print.demo.controller;

import fulltext.print.demo.bean.Document;
import fulltext.print.demo.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
public class ViewController {

    @Autowired
    private DocumentService documentService;

    @GetMapping("/view")
    public ModelAndView view(@RequestParam(name = "id") String id) {
        ModelAndView mv = new ModelAndView("view");

        Document document = documentService.selectDocumentById(id);
        mv.addObject("title", document.getTitle());
        mv.addObject("author", document.getAuthor());
        mv.addObject("printTime", document.getPrintTime());
        mv.addObject("url", document.getUrl());

        return mv;
    }

}
