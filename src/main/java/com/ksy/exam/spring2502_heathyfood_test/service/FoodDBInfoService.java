package com.ksy.exam.spring2502_heathyfood_test.service;

import com.ksy.exam.spring2502_heathyfood_test.repository.UserRepository;
import com.ksy.exam.spring2502_heathyfood_test.util.Ut;
import com.ksy.exam.spring2502_heathyfood_test.vo.FoodDBInfo;
import com.ksy.exam.spring2502_heathyfood_test.vo.FoodTracker;
import com.ksy.exam.spring2502_heathyfood_test.vo.ResultData;
import com.ksy.exam.spring2502_heathyfood_test.vo.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FoodDBInfoService {

    @Value("${public.api.key}")  // application.yml에서 값 가져오기
    private String apiKey;

    public String goFoodDictionary(FoodDBInfo foodDBInfo) throws IOException {
        StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/1471000/FoodNtrCpntDbInfo01/getFoodNtrCpntDbInq01");
        urlBuilder.append("?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + apiKey);
        urlBuilder.append("&" + URLEncoder.encode("pageNo", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(foodDBInfo.getPageNo()), "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(foodDBInfo.getNumOfRows()), "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("type", "UTF-8") + "=" + URLEncoder.encode(foodDBInfo.getType(), "UTF-8"));
        urlBuilder.append("&" + URLEncoder.encode("FOOD_NM_KR", "UTF-8") + "=" + URLEncoder.encode(foodDBInfo.getFOOD_NM_KR(), "UTF-8"));
        //urlBuilder.append("&" + URLEncoder.encode("FOOD_NM_KR","UTF-8") + "=" + URLEncoder.encode("%" + searchFoodName + "%", "UTF-8").replace("%25", "%"));


        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");

        BufferedReader rd;
        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        } else {
            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
        }

        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            sb.append(line);
        }
        rd.close();
        conn.disconnect();

        return sb.toString();
    }
}
