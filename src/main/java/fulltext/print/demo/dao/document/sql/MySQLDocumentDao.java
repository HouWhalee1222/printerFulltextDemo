package fulltext.print.demo.dao.document.sql;

import fulltext.print.demo.bean.Document;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface MySQLDocumentDao {
    // sample persistent layer Dao, we use MySQL here

    @Select("SELECT * FROM printerData WHERE id = #{id}")
    Document findDocumentById(@Param("id") String id);

    @Select("SELECT * FROM printerData")
    List<Document> findAll();

    @Insert("INSERT INTO printerData(id, title, author, printTime, url, content) values(#{id}, #{title}, #{author}, #{printTime}, #{url}, #{content})")
    void insertOneDocument(Document document);

    @Delete("DELETE FROM printerData WHERE printTime <= #{date}")
    void deleteDocumentByPrintTimeBefore(Date date);

}
