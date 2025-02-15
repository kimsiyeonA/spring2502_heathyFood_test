package com.ksy.exam.spring2502_heathyfood_test.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ksy.exam.spring2502_heathyfood_test.service.FoodDBInfoService;
import com.ksy.exam.spring2502_heathyfood_test.service.MyMealLogService;
import com.ksy.exam.spring2502_heathyfood_test.vo.FoodDBInfo;
import com.ksy.exam.spring2502_heathyfood_test.vo.FoodTracker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@Controller
@RequestMapping("/myMealLog")
public class UsrMyMealLogController {

    @Value("${public.api.key}")  // application.yml에서 값 가져오기
    private String apiKey;

    @Autowired
    FoodDBInfoService foodDBInfoService;
    @Autowired
    MyMealLogService myMealLogService;

    FoodDBInfo foodDBInfo;

    @RequestMapping("/main")
    public String  main(){
        return "/usr/myMealLog/myMealLogMain";
    }

    @RequestMapping("/goRecord")
    public String goRecord (String ftMealTime, String ftWriteDate, Model model)throws IOException {
        FoodTracker ft = new FoodTracker();
        ft.setFtMealTime(ftMealTime);
        ft.setFtWriteDate(ftWriteDate);

        String foodTrackerJson = myMealLogService.makeFoodTrackerByJson(ft);
        model.addAttribute("foodTrackerJson", foodTrackerJson);
        return "usr/myMealLog/myMealLogRecord";
    }

    @RequestMapping("/record")
    public String record(){
        return "usr/myMealLog/myMealLogRecord";
    }

    @RequestMapping("/goFoodDictionary")
    public String goFoodDictionary(@RequestParam String searchFoodName,
                                   String foodTrackerJson,
                                   Model model,
                                   @RequestParam(defaultValue = "1") int pageNo) throws IOException {

         int numOfRows = 10;
         String type = "json";

        FoodDBInfo foodDBInfo = new FoodDBInfo();
        foodDBInfo.setPageNo(pageNo);
        foodDBInfo.setNumOfRows(numOfRows);
        foodDBInfo.setType(type);
        foodDBInfo.setFOOD_NM_KR(searchFoodName);

        String foodDBResultJson = foodDBInfoService.makeFoodDBResultByJson(foodDBInfo);
        String foodDBInfoJson = foodDBInfoService.makeFoodDBInfoByJson(foodDBInfo);

        System.out.println("foodTrackerJson" +foodTrackerJson);
        System.out.println("foodDBInfoJson" +foodDBInfoJson);
        //System.out.println("foodDBResultJson" +foodDBResultJson);

        model.addAttribute("foodTrackerJson", foodTrackerJson);
        model.addAttribute("foodDBResultJson", foodDBResultJson);
        model.addAttribute("foodDBInfoJson", foodDBInfoJson);
        return "usr/myMealLog/myMealLogFoodDictionary";
    }

    @RequestMapping("/loadMoreFoodDictionary")
    @ResponseBody
    public String loadMoreFoodDictionary(@RequestBody FoodDBInfo foodDBInfo) throws IOException {

//        System.out.println("Received food_NM_KR: " + foodDBInfo.getFOOD_NM_KR());
//        System.out.println("Received pageNo: " + foodDBInfo.getPageNo());
//        System.out.println("Received numOfRows: " + foodDBInfo.getNumOfRows());

        // 현재 행 개수를 가져와서 10개씩 증가시킴
        int pageNo = foodDBInfo.getPageNo();
        pageNo += 1; // 더보기 버튼 클릭 시 10개씩 추가
        String type = "json";

        System.out.println("Received pageNo: " + pageNo);

        FoodDBInfo foodDBInfoMore = new FoodDBInfo();
        foodDBInfoMore.setPageNo(pageNo);
        foodDBInfoMore.setNumOfRows(foodDBInfo.getNumOfRows());
        foodDBInfoMore.setType(type);
        foodDBInfoMore.setFOOD_NM_KR(foodDBInfo.getFOOD_NM_KR());


        // 서비스 호출하여 새로운 데이터 가져오기
        String sb = foodDBInfoService.makeFoodDBResultByJson(foodDBInfoMore);
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

    @RequestMapping("/goFoodDetail")
    public String goFoodDetail (String foodDBResultJsonOneItem, String foodTrackerJson,  Model model){

       //System.out.println("foodTrackerJson" +foodTrackerJson);
        //System.out.println("foodDBResultJsonOneItem" +foodDBResultJsonOneItem);
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> foodDBResultJsonOneItem2;
        Map<String, Object> foodTrackerJson2;
        try {
            foodDBResultJsonOneItem2 = objectMapper.readValue(foodDBResultJsonOneItem, Map.class);
            foodTrackerJson2 = objectMapper.readValue(foodTrackerJson, Map.class);
        } catch (Exception e) {
            e.printStackTrace(); // 에러 로그 출력
            foodDBResultJsonOneItem2 = Map.of("FOOD_NM_KR", "알 수 없음", "CALORIES", 0); // 기본값 설정
            foodTrackerJson2 = Map.of("FOOD_NM_KR", "알 수 없음", "CALORIES", 0); // 기본값 설정

        }

        model.addAttribute("foodTrackerJson", foodTrackerJson2);
        model.addAttribute("foodDBResultJsonOneItem", foodDBResultJsonOneItem2);
        return "usr/myMealLog/myMealLogFoodDetail";
    }

    @RequestMapping("/foodDetail")
    public String foodDetail(){
        return "usr/myMealLog/myMealLogFoodDetail";
    }

    //String SearchFoodName, FoodTracker main_record_ft
    @RequestMapping("/test")
    @ResponseBody
    public String test() throws IOException {
        FoodTracker ft = new FoodTracker();
        ft.setFtMealTime("아침식사");
        ft.setFtWriteDate("2024.02.03");

        String foodTrackerJson = myMealLogService.makeFoodTrackerByJson(ft);
        return foodTrackerJson;
    }

}
