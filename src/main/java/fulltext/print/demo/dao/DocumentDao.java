package fulltext.print.demo.dao;

import fulltext.print.demo.bean.Document;
import org.apache.ibatis.annotations.*;

import java.util.Date;
import java.util.List;

@Mapper
public interface DocumentDao {

    @Select("SELECT * FROM printerData WHERE id = #{id}")
    Document findDocumentById(@Param("id") String id);

    @Select("SELECT * FROM printerData")
    List<Document> findAll();

    @Insert("INSERT INTO printerData(id, title, author, printTime, url) values(#{id}, #{title}, #{author}, #{printTime}, #{url})")
    void insertOneDocument(Document document);

    @Delete("DELETE FROM printerData WHERE printTime <= #{date}")
    void deleteDocumentByPrintTimeBefore(Date date);
}
