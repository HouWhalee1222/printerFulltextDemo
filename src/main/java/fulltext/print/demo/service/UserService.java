package fulltext.print.demo.service;

import fulltext.print.demo.bean.User;
import fulltext.print.demo.dao.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserDao userDao;

    public boolean login(String username, String password) {
        User user = userDao.findUserByUsername(username);
        if (user == null) {
            return false;
        } else {
            if (user.getPassword().equals(password)) {
                return true;
            } else {
                return false;
            }
        }
    }
}
