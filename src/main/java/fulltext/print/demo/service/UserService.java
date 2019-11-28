package fulltext.print.demo.service;

import fulltext.print.demo.bean.User;
import fulltext.print.demo.dao.UserDao;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.logging.LogManager;

@Log4j2
@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public boolean login(String username, String password) {
        User user = userDao.findUserByUsername(username);
        if (user == null) {
            log.info("Cannot find user " + username);
            return false;
        } else {
            if (user.getPassword().equals(password)) {
                log.info("User " + username + " logged in");
                return true;
            } else {
                log.info("Invalid password for user " + username);
                return false;
            }
        }
    }
}
