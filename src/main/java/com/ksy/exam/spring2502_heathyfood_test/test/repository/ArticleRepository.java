package com.ksy.exam.spring2502_heathyfood_test.test.repository;

import com.ksy.exam.spring2502_heathyfood_test.vo.Article;
import com.ksy.exam.spring2502_heathyfood_test.vo.Article;
import org.apache.ibatis.annotations.*;

import java.util.List;

//@Component
@Mapper
public interface ArticleRepository {
    // db역할 값을 받는 아이들 옮기기
    // 이렇게 변수와 리스트로 저장하는 것은 연속성이 없다 그래서 디비에 연결해서 값을 저장하고 가지고 와야한다.
    // interface > 전체 추상화 | abstract class > 추상메서드 하나라도 있으면 추상화
    // ? 대신 #{변수}
    // 마이바티스에서는 Article타입을 받지 않음

     //@Insert("INSERT INTO article SET regDate = NOW(), updateDate = NOW(), title = #{title}, `body` = #{body}")
    public void writeArticle(String title, String body);

    //@Delete("DELETE FROM article WHERE id = #{id}")
    public void deleteArticle(int id);

    //@Update("UPDATE article SET updateDate = NOW(), title = #{title}, `body` = #{body} WHERE id = #{id}")
    public void modifyArticle(int id, String title, String body);

    //@Select("SELECT * FROM article WHERE id = #{id}")
    public Article getArticleById(int id);

    //@Select("SELECT * FROM article ORDER BY id DESC")
    public List<Article> getArticles();

     @Select("SELECT LAST_INSERT_ID();")
    public int getLastInsertId();
}
