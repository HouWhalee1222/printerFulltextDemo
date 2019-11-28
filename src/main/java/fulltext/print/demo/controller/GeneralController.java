package fulltext.print.demo.controller;

import fulltext.print.demo.utils.UploadAndIndexingFiles;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/util")
public class GeneralController {

    @Autowired
    private UploadAndIndexingFiles uploadAndIndexingFiles;

    @RequestMapping("/uploadAndIndexingFiles")
    public String uploadAndIndexingFilesExecute() throws IOException {
        String filesPath = "/data/THUCNewsData/THUCNews";
        uploadAndIndexingFiles.insertDocument(filesPath);
        return "Success";
    }
}
