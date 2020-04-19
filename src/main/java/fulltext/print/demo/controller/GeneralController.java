package fulltext.print.demo.controller;

import fulltext.print.demo.service.DocumentService;
import fulltext.print.demo.utils.UploadAndIndexingFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ExecutionException;

@RestController
@RequestMapping("/util")
public class GeneralController {
    // controller for some utilities like upload new files

    @Autowired
    private UploadAndIndexingFiles uploadAndIndexingFiles;

    @Autowired
    private DocumentService documentService;

    @RequestMapping("/uploadAndIndexingFiles")
    public ModelAndView uploadAndIndexingFilesExecute() throws IOException, ExecutionException, InterruptedException {
        String filesPath = "E:\\printerData";
        uploadAndIndexingFiles.insertDocument(filesPath);
        return new ModelAndView("redirect:/util/uploadAndIndexingFiles/status");
    }

    @RequestMapping("/uploadAndIndexingFiles/status")
    public ModelAndView getIndexingStatus() {
        ModelAndView mv = new ModelAndView("UploadAndIndexingFilesStatus");
        Map<String, Double> performanceMap = uploadAndIndexingFiles.getPerformanceMap();
        mv.addObject("currentIndexed", uploadAndIndexingFiles.getCurrentDocIndexed());
        mv.addObject("totalDoc", uploadAndIndexingFiles.getTotalDoc());
        mv.addObject("time10000", performanceMap.get("10000"));
        mv.addObject("time50000", performanceMap.get("50000"));
        mv.addObject("time100000", performanceMap.get("100000"));
        mv.addObject("time200000", performanceMap.get("200000"));
        mv.addObject("time500000", performanceMap.get("500000"));
        mv.addObject("timeTotal", performanceMap.get("All"));
        return mv;
    }

    @RequestMapping("/deleteAll")
    public ModelAndView deleteAll() {
        documentService.deleteAll();
        return new ModelAndView("redirect:/search");
    }
}
