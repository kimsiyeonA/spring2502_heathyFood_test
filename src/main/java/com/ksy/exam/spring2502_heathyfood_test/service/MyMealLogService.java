package com.ksy.exam.spring2502_heathyfood_test.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksy.exam.spring2502_heathyfood_test.repository.UserRepository;
import com.ksy.exam.spring2502_heathyfood_test.vo.FoodTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class MyMealLogService {

//    // 생성자
//    public MyMealLogService(UserRepository userRepository) {
//        this.userRepository = userRepository;
//        // 이유는 생기는 시점 때문에 뒤에있는게 만들어지기전에 쓸려고해서 오류가 생김
//    }

    //FoodTracker 객체를 Json 형태의 String으로 보냄
    public String makeFoodTrackerByJson(FoodTracker ft) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        // 객체를 JSON 문자열로 변환
        String jsonString = objectMapper.writeValueAsString(ft);

        System.out.println(jsonString);
        return jsonString;
    }
}
