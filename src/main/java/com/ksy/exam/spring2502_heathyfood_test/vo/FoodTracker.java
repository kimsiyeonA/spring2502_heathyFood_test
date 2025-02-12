package com.ksy.exam.spring2502_heathyfood_test.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FoodTracker {
    private int ftIdx;
    private String ftWriteDate;
    private String ftMealTime;
    private String ftFoodName;
    private String ftFoodQuantity;
    private String ftFoodPortion;
    private String ftCalorie;
    private String ftCarb;
    private String ftProtein;
    private String ftFat;
    private String ftSugar;
    private String ftSodium;
    private String ftCreateAt ;
}
