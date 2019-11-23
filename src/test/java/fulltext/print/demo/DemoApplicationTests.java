package fulltext.print.demo;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoApplicationTests {

    @Value("${spring.scheduler.cleanOldDocument.executeTime}")
    private String s1;

    @Test
    void contextLoads() {
        System.out.println(s1);
    }

}
