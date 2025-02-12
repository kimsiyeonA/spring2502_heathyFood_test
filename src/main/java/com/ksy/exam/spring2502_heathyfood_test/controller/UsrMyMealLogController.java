package com.ksy.exam.spring2502_heathyfood_test.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksy.exam.spring2502_heathyfood_test.repository.UserRepository;
import com.ksy.exam.spring2502_heathyfood_test.service.FoodDBInfoService;
import com.ksy.exam.spring2502_heathyfood_test.vo.FoodDBInfo;
import com.ksy.exam.spring2502_heathyfood_test.vo.FoodTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLEncoder;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/myMealLog")
public class UsrMyMealLogController {

    @Value("${public.api.key}")  // application.yml에서 값 가져오기
    private String apiKey;

    @Autowired
    FoodDBInfoService foodDBInfoService;

    FoodDBInfo foodDBInfo;

    @RequestMapping("/main")
    public String  main(){
        return "/usr/myMealLog/myMealLogMain";
    }

    @RequestMapping("/goRecord")
    public String goRecord(String ftMealTime, String ftWriteDate, Model model){
        FoodTracker ft = new FoodTracker();
        ft.setFtMealTime(ftMealTime);
        ft.setFtWriteDate(ftWriteDate);
        model.addAttribute("main_record_ft", ft);
        return "usr/myMealLog/myMealLogRecord";
    }




    @RequestMapping("/record")
    public String record(){
        return "usr/myMealLog/myMealLogRecord";
    }

    @RequestMapping("/goFoodDictionary")
    public String goFoodDictionary(@RequestParam String searchFoodName,
                                   FoodTracker main_record_ft,
                                   Model model,
                                   @RequestParam(defaultValue = "1") int pageNo) throws IOException {

         int numOfRows = 10;
         String type = "json";

        FoodDBInfo foodDBInfo = new FoodDBInfo();
        foodDBInfo.setPageNo(pageNo);
        foodDBInfo.setNumOfRows(numOfRows);
        foodDBInfo.setType(type);
        foodDBInfo.setFOOD_NM_KR(searchFoodName);

        String sb = foodDBInfoService.goFoodDictionary(foodDBInfo);

        model.addAttribute("foodDBInfo", foodDBInfo);
        model.addAttribute("main_record_ft", main_record_ft);
        model.addAttribute("jsonData", sb);
        return "usr/myMealLog/myMealLogFoodDictionary";
    }

    @RequestMapping("/loadMoreFoodDictionary")
    @ResponseBody
    public String loadMoreFoodDictionary(@RequestBody Map<String, Object> requestData) throws IOException {

        // 클라이언트에서 전달된 foodDBInfo 객체 변환
        ObjectMapper objectMapper = new ObjectMapper();
        FoodDBInfo foodDBInfo = objectMapper.convertValue(requestData.get("foodDBInfo"), FoodDBInfo.class);

        // 현재 행 개수를 가져와서 10개씩 증가시킴
        int numOfRows = (int) requestData.get("numOfRows");
        numOfRows += 10; // 더보기 버튼 클릭 시 10개씩 추가

        String type = "json";

        // foodDBInfo 업데이트
        foodDBInfo.setNumOfRows(numOfRows);
        foodDBInfo.setType(type);

        // 서비스 호출하여 새로운 데이터 가져오기
        String sb = foodDBInfoService.goFoodDictionary(foodDBInfo);

        //System.out.println(sb);


        return sb; // JSON 형식으로 반환
    }

//    @RequestMapping("/goFoodDictionary")
//    @ResponseBody
//    public Object goFoodDictionary(@RequestParam String searchFoodName,
//                                   FoodTracker main_record_ft,
//                                   Model model,
//                                   @RequestParam(defaultValue = "1") int pageNo,
//                                   @RequestParam(defaultValue = "true") boolean redirect) throws IOException {
//
//        StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/1471000/FoodNtrCpntDbInfo01/getFoodNtrCpntDbInq01");
//        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "=" + apiKey);
//        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode(String.valueOf(pageNo), "UTF-8"));
//        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8"));
//        urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8"));
//        urlBuilder.append("&" + URLEncoder.encode("FOOD_NM_KR","UTF-8") + "=" + URLEncoder.encode(searchFoodName, "UTF-8"));
//        //urlBuilder.append("&" + URLEncoder.encode("FOOD_NM_KR","UTF-8") + "=" + URLEncoder.encode("%" + searchFoodName + "%", "UTF-8").replace("%25", "%"));
//
//
//        URL url = new URL(urlBuilder.toString());
//        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//        conn.setRequestMethod("GET");
//        conn.setRequestProperty("Content-type", "application/json");
//
//        BufferedReader rd;
//        if (conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
//            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
//        } else {
//            rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
//        }
//
//        StringBuilder sb = new StringBuilder();
//        String line;
//        while ((line = rd.readLine()) != null) {
//            sb.append(line);
//        }
//        rd.close();
//        conn.disconnect();
//        //System.out.println(urlBuilder.toString());
//        //System.out.println(sb.toString());
//        if (sb.length() == 0) {
//            return "{\"body\": { \"items\": [] }}";
//        }
//        model.addAttribute("main_record_ft", main_record_ft);
//        model.addAttribute("jsonData", sb.toString());
//        HttpHeaders headers = new HttpHeaders();
//        headers.setLocation(URI.create("/myMealLog/foodDictionary"));
//        return new ResponseEntity<>(headers, HttpStatus.MOVED_PERMANENTLY);
//    }

    @RequestMapping("/foodDictionary")
    public String foodDictionary (){
        return "usr/myMealLog/myMealLogFoodDictionary";
    }
    @RequestMapping("/foodDetail")
    public String foodDetail(){
        return "usr/myMealLog/myMealLogFoodDetail";
    }

    //String SearchFoodName, FoodTracker main_record_ft
    @GetMapping("/test")
    public JsonNode test() throws IOException {
        StringBuilder urlBuilder = new StringBuilder("https://apis.data.go.kr/1471000/FoodNtrCpntDbInfo01/getFoodNtrCpntDbInq01"); /*URL*/
        urlBuilder.append("?" + URLEncoder.encode("serviceKey","UTF-8") + "="+apiKey); /*Service Key*/
        urlBuilder.append("&" + URLEncoder.encode("pageNo","UTF-8") + "=" + URLEncoder.encode("1", "UTF-8")); /*페이지 번호*/
        urlBuilder.append("&" + URLEncoder.encode("numOfRows","UTF-8") + "=" + URLEncoder.encode("10", "UTF-8")); /*한 페이지 결과 수*/
        urlBuilder.append("&" + URLEncoder.encode("type","UTF-8") + "=" + URLEncoder.encode("json", "UTF-8")); /*XML/JSON 여부*/
        urlBuilder.append("&" + URLEncoder.encode("FOOD_NM_KR","UTF-8") + "=" + URLEncoder.encode("국밥", "UTF-8")); /*식품명*/

        URL url = new URL(urlBuilder.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setRequestProperty("Content-type", "application/json");
        System.out.println("Response code: " + conn.getResponseCode());
        BufferedReader rd;
        if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
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
        System.out.println(sb.toString());

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode rootNode = objectMapper.readTree(sb.toString());

        // "body" -> "items" 배열 접근
        JsonNode itemsArray = rootNode.get("body").get("items");

        return itemsArray;
    }

}
