package fulltext.print.demo.component;

import fulltext.print.demo.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class Scheduler {

    @Autowired
    private DocumentService documentService;

    @Scheduled(cron = "${spring.scheduler.cleanOldDocument.cron}")
    public void deleteOldDocument() {
        System.out.println("Document cleared!!");
        documentService.deleteOldDocuments();
    }

}
