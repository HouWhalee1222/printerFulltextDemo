package fulltext.print.demo.component;

import fulltext.print.demo.service.DocumentService;
import fulltext.print.demo.utils.UploadAndIndexingFiles;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Log4j2
@Component
public class Scheduler {

    @Autowired
    private DocumentService documentService;

    @Scheduled(cron = "${spring.scheduler.cleanOldDocument.cron}")
    public void deleteOldDocument() {
        // delete old document when its too old
        log.info("Scheduled job deleteOldDocument starts");
        documentService.deleteOldDocuments();
        log.info("Job deleteOldDocument finished successfully");
    }

}
