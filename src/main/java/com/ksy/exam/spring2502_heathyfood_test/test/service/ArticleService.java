package com.ksy.exam.spring2502_heathyfood_test.test.service;

import com.ksy.exam.spring2502_heathyfood_test.test.repository.ArticleRepository;
import com.ksy.exam.spring2502_heathyfood_test.vo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleService {


    @Autowired
    ArticleRepository articleRepository;
    // 생성자
    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
        // 이유는 생기는 시점 때문에 뒤에있는게 만들어지기전에 쓸려고해서 오류가 생김
    }

    // 테스트 데이터 및 추가 데이터 생성
    public int writeArticle( String title,  String body) {
        articleRepository.writeArticle(title, body);
        return articleRepository.getLastInsertId() ;
    }

    // id값이 들어오면 관련 아이디 값을 찾는메서드
    public Article getArticleById(int id) {
        return articleRepository.getArticleById(id);
    }

    // 데이터 지우기
    public void deleteArticle(int id){
         articleRepository.deleteArticle(id);
    }

    public void modifyArticle(int id, String title, String body){
        articleRepository.modifyArticle(id,title,body);
    }

    public List<Article> getArticles() {
        return articleRepository.getArticles();
    }
}
