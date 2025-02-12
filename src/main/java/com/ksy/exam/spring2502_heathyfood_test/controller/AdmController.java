package com.ksy.exam.spring2502_heathyfood_test.controller;

import com.ksy.exam.spring2502_heathyfood_test.service.AdmService;
import com.ksy.exam.spring2502_heathyfood_test.util.Ut;
import com.ksy.exam.spring2502_heathyfood_test.vo.ResultData;
import com.ksy.exam.spring2502_heathyfood_test.vo.User;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/adm")
public class AdmController {

    @Autowired
    public AdmService admService;


    @RequestMapping("/admin/login")
    public String login(){
        return "/adm/adminLogin";
    }

    @RequestMapping ("/admin/doLogin")
    @ResponseBody
    public String doLogin(HttpSession session, String uEmail, String uPwd, String uAdminPwd){
        // 로그인 정보 세션이 저장
        boolean isLogined =false;
        if(session.getAttribute("LoginMemberId")!=null){
            isLogined = true;
        }
        if(isLogined){
            //return ResultData.from("F-A","이미 로그인 함");
            return Ut.jsReplace("F-A","이미 로그인 함","/common/introduce");
        }
        if(Ut.isEmptyOrNull(uEmail)){
            return  Ut.historyBack("F-1","이메일을 입력해주세요.");
        }
        if(Ut.isEmptyOrNull(uPwd)){
            return Ut.historyBack("F-2","비밀번호를 입력해주세요.");
        }
        if(Ut.isEmptyOrNull(uAdminPwd)){
            return Ut.historyBack("F-4","비밀번호를 입력해주세요.");
        }

        User user = admService.getUserByEmail(uEmail);

        if(user == null){
            return Ut.historyBack("F-5",Ut.f("%s는 존재하지 않는 아이디입니다.",uEmail));
        }
        if(user.getUPwd().equals(uPwd) == false){
            return Ut.historyBack("F-6",Ut.f("%s는 일치하지 않는 비밀번호입니다.",uPwd));
        }
        if(user.getUAdminPwd().equals(uAdminPwd) == false){
            return Ut.historyBack("F-7",Ut.f("%s는 일치하지 않는 비밀번호입니다.",uPwd));
        }

        session.setAttribute("user",user);
        //return ResultData.from("S-1",Ut.f("%s님 환영합니다", user.getUNickName()));
        return Ut.jsReplace("S-A",Ut.f("%s님 환영합니다", user.getUNickName()),"/common/introduce");
    }

    @RequestMapping ("/admin/doLogout")
    @ResponseBody
    public ResultData<User> doLogout(HttpSession session){
        // 로그인 정보 세션이 저장
        boolean isLogined =false;
        if(session.getAttribute("user")!=null){
            isLogined = true;
        }
        if(!isLogined){
            return ResultData.from("F-A","이미 로그아웃 함");
        }
        session.removeAttribute("loginUserEmail");
        return ResultData.from("S-1",Ut.f("로그아웃 성공"));
    }

}
