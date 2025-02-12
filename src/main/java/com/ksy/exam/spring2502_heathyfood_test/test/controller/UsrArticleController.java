package com.ksy.exam.spring2502_heathyfood_test.test.controller;

import com.ksy.exam.spring2502_heathyfood_test.test.service.ArticleService;
import com.ksy.exam.spring2502_heathyfood_test.vo.Article;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/test")
public class UsrArticleController {

    @Autowired
    public ArticleService articleService;

    // 액션 메서드
    // 데이터에 추가 입력 > 작성 부분을 write에 옮기고 들어오면 전달함
    @RequestMapping("/doAdd")
    @ResponseBody
    public Article doAdd( String title,  String body) {
        int id = articleService.writeArticle(title, body);
        Article article = articleService.getArticleById(id);
//        Article article = articleService.writeArticle(title, body);
        return article;
    }

    // 데이터 삭제
    @RequestMapping("/doDelete")
    @ResponseBody
    public String doDelete(int id) {
        Article article = articleService.getArticleById(id);
        if(article == null){
            return id+"번 글은 아이디 없음";
        }

        articleService.deleteArticle(id);

        return id+"번 글 삭제됨";
    }

    // 데이터 업데이트
    @RequestMapping("/doModify")
    @ResponseBody
    public Object doModify(int id, String title, String body){
        Article article = articleService.getArticleById(id);

        if(article == null){
            return id + "번 글은 없음";
        }

        articleService.modifyArticle(id, title, body);

        return article;
    }
    // 작성된 데이터들 보기
    @RequestMapping("/getArticles")
    @ResponseBody
    public List<Article> getArticles() {
        return articleService.getArticles();
    }

}
