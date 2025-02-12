package com.ksy.exam.spring2502_heathyfood_test.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class User {
    private int uIdx;
    private String uEmail;
    private String uPwd;
    private String uName;
    private String uGender;
    private String uBirth;
    private String uPhone;
    private String uNickName;
    private String uThumbnail;
    private String uIp;
    private String uEnterDate;
    private String uUpdateDate;
    private String uDelyn;
    private String uDelDate;
    private String uTOSAgree;
    private String  uPIPAgree;
    private String uAdminPwd;
    private String  uRole;
    private int uLevel;
    private int uExp;
    private int uMileage;
    private String uCreateAt;
    private String uResetToken;  // 비밀번호 재설정을 위한 토큰
}
