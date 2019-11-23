package fulltext.print.demo.dao;

import fulltext.print.demo.bean.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface UserDao {

    @Select("SELECT * FROM user")
    List<User> findAll();

    @Select("SELECT * FROM user WHERE username=#{username}")
    User findUserByUsername(String username);

    @Select("SELECT password FROM user WHERE id=#{id}")
    String findPasswordById(String id);

    @Select("SELECT password FROM user WHERE username=#{username}")
    String findPasswordByUsername(String username);
}
