package fulltext.print.demo;

import fulltext.print.demo.bean.User;
import fulltext.print.demo.dao.UserDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
public class UserTest {

    @Autowired
    private UserDao userDao;

    @Test
    public void findAllTest() {
        List<User> users = userDao.findAll();
        for (User user : users) {
            System.out.println(user);
        }
    }

    @Test
    public void findPasswordByIdTest() {
        System.out.println(userDao.findPasswordById("1"));
    }

    @Test
    public void findPasswordByUsernameTest() {
        System.out.println(userDao.findPasswordByUsername("zhuhang2"));
    }
}
