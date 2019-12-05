package fulltext.print.demo.controller;

import fulltext.print.demo.utils.UploadAndIndexingFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.jws.WebParam;
import java.io.IOException;

@RestController
@RequestMapping("/util")
public class GeneralController {

    @Autowired
    private UploadAndIndexingFiles uploadAndIndexingFiles;

    @RequestMapping("/uploadAndIndexingFiles")
    public ModelAndView uploadAndIndexingFilesExecute() throws IOException {
        String filesPath = "/data/THUCNewsData/THUCNews";
        uploadAndIndexingFiles.insertDocument(filesPath);
        return new ModelAndView("redirect:/util/uploadAndIndexingFiles/status");
    }

    @RequestMapping("/uploadAndIndexingFiles/status")
    public ModelAndView getIndexingStatus() {
        ModelAndView mv = new ModelAndView("UploadAndIndexingFilesStatus");
        mv.addObject("currentIndexed", uploadAndIndexingFiles.getCurrentDocIndexed());
        mv.addObject("totalDoc", uploadAndIndexingFiles.getTotalDoc());
        mv.addObject("time10000", uploadAndIndexingFiles.getPerformanceMap().get("10000"));
        mv.addObject("time50000", uploadAndIndexingFiles.getPerformanceMap().get("50000"));
        mv.addObject("time100000", uploadAndIndexingFiles.getPerformanceMap().get("100000"));
        mv.addObject("time200000", uploadAndIndexingFiles.getPerformanceMap().get("200000"));
        mv.addObject("time500000", uploadAndIndexingFiles.getPerformanceMap().get("500000"));
        mv.addObject("timeTotal", uploadAndIndexingFiles.getPerformanceMap().get("All"));
        return mv;
    }
}
