package com.ksy.exam.spring2502_heathyfood_test.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodDBInfo {
    private int id=1;
    private int pageNo; // 페이지 수
    private int numOfRows; // 한 페이지에 나오는 갯수
    private String FOOD_NM_KR; // 식품명 키워드
    private String type;
    private int NUM ; // 한 객체의 번호

    public void setId(int id){
        this.id = ++id;
    }
}
