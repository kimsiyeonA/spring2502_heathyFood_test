package com.ksy.exam.spring2502_heathyfood_test.controller;

import com.ksy.exam.spring2502_heathyfood_test.service.UserService;
import com.ksy.exam.spring2502_heathyfood_test.test.service.ArticleService;
import com.ksy.exam.spring2502_heathyfood_test.util.Ut;
import com.ksy.exam.spring2502_heathyfood_test.vo.Article;
import com.ksy.exam.spring2502_heathyfood_test.vo.ResultData;
import com.ksy.exam.spring2502_heathyfood_test.vo.User;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/usr")
public class UsrUserController {

    @Autowired
    public UserService userService;


    @RequestMapping("/user/join")
    public String join(){
        return "usr/user/userJoin";
    }

//    @RequestMapping ("/user/doJoin")
//    @ResponseBody
//    public String doJoin(User uv, HttpServletRequest request, RedirectAttributes rttr)throws IOException, Exception {
//		logger.info("joinAction 들어옴");
//		logger.info(uv.getUPwd());
//		logger.info(uv.getUName());
//		logger.info(uv.getUGender());
//		logger.info(uv.getUBirth());
//		logger.info(uv.getUPhone());
//		logger.info(uv.getUNickName());
//		logger.info(uv.getUEmail());
//		logger.info(uv.getUThumbnail());
//		logger.info(uv.getUIp());
//
//
//
//		uv.setUThumbnail("themnailBasic.png");
//		uv.setURole("USER");
//
//		int value = userService.userInsert(uv);
//
//		String path = "";
//		if (value == 1) {
//			path = "redirect:/user/userLogin.do";
//		} else {
//			rttr.addFlashAttribute("msg", "입력이 잘못 되었습니다.");
//			path = "redirect:/user/userJoin.do";
//		}
//
//		return path;
//	}

    @RequestMapping ("/user/doJoin")
    @ResponseBody
    public String doJoin(HttpSession session, String uEmail, String uPwd, String uName,
                             String uPhone, String uBirth, String uNickName,
                             String uGender, String uTOSAgree, String uPIPAgree){
        boolean isLogined =false;
        if(session.getAttribute("LoginMemberId")!=null){
            isLogined = true;
        }
        if(isLogined){
            //return ResultData.from("F-A","이미 로그인 함");
            return Ut.jsReplace("F-A","이미 로그인 함","/common/introduce");
        }

        String uIP = "172.0.0.1";
        String uThumbnail = "thumbnailBasic.png";
        String uRole = "USER";
        if(Ut.isEmptyOrNull(uEmail)){
            return Ut.historyBack("F-1","이메일을 입력해주세요.");
        }
        if(Ut.isEmptyOrNull(uPwd)){
            return Ut.historyBack("F-2","비밀번호를 입력해주세요.");
        }
        if(Ut.isEmptyOrNull(uName)){
            return Ut.historyBack("F-3","이름을 입력해주세요.");
        }
        if(Ut.isEmptyOrNull(uPhone)){
            return Ut.historyBack("F-4","연락처를 입력해주세요.");
        }
        if(Ut.isEmptyOrNull(uBirth)){
            return Ut.historyBack("F-5","생일을 입력해주세요.");
        }
        if(Ut.isEmptyOrNull(uNickName)){
            return Ut.historyBack("F-6","닉네임을 입력해주세요.");
        }
        if(Ut.isEmptyOrNull(uGender)){
            return Ut.historyBack("F-7","성별을 입력해주세요.");
        }
        if(Ut.isEmptyOrNull(uTOSAgree)){
            return Ut.historyBack("F-8","이용약관 동의를 선택해주세요.");
        }
        if(Ut.isEmptyOrNull(uPIPAgree)){
            return Ut.historyBack("F-9","개인정보 동의를 선택해주세요.");
        }

        ResultData doJoinRd = userService.doJoin(uEmail, uPwd, uName, uPhone, uBirth, uNickName, uGender, uTOSAgree, uPIPAgree, uIP, uThumbnail,uRole);

        if(doJoinRd.isFail()){
            return Ut.historyBack(doJoinRd.getResultCode(),doJoinRd.getMsg());
        }
//        if(doJoinRd.isFail()){
//            return Ut.f("이미 사용중인 닉네임(%s) 입니다.", uNickName);
//        }
//
//        if(doJoinRd.isFail()){
//            return Ut.f("이미 사용중인 닉네임(%s) 입니다.", uNickName);
//        }

        User user = userService.getUserById((int)doJoinRd.getData());
       // return ResultData.newData(doJoinRd,user);
        return Ut.jsReplace(doJoinRd.getResultCode(),doJoinRd.getMsg(),"/common/introduce");
	}


    @RequestMapping("/user/login")
    public String login(){
        return "usr/user/userLogin";
    }

    @RequestMapping ("/user/doLogin")
    @ResponseBody
    public String doLogin(HttpSession session, String uEmail, String uPwd){
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

        User user = userService.getUserByEmail(uEmail);

        if(user == null){
            return Ut.historyBack("F-3",Ut.f("%s는 존재하지 않는 아이디입니다.",uEmail));
        }
        if(user.getUPwd().equals(uPwd) == false){
            return Ut.historyBack("F-4",Ut.f("%s는 일치하지 않는 비밀번호입니다.",uPwd));
        }

        session.setAttribute("user",user);
        //return ResultData.from("S-1",Ut.f("%s님 환영합니다", user.getUNickName()));
        return Ut.jsReplace("S-A",Ut.f("%s님 환영합니다", user.getUNickName()),"/myMealLog/main");
    }

    @RequestMapping ("/user/doLogout")
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

    @RequestMapping("/user/foundEmail")
    public String foundEmail(){
        return "usr/user/userFoundEmail";
    }

    @RequestMapping("/user/doFoundEmail")
    public String doFoundEmail(HttpSession session, String uName, String uPhone, Model model){

        if (session.getAttribute("user") != null) {
            // 로그인 상태일 경우, 로그인 상태에서 할 필요 없다는 메시지 출력
            return Ut.jsReplace("F-A", "이미 로그인 상태입니다.", "/common/introduce");
        }

        if(Ut.isEmptyOrNull(uName)){
            return  Ut.historyBack("F-1","이름을 입력해주세요.");
        }
        if(Ut.isEmptyOrNull(uPhone)){
            return Ut.historyBack("F-2","연락처를 입력해주세요.");
        }

        User user = userService.getUseFoundByEmail(uName,uPhone);
        if(user==null){
            return Ut.historyBack("F-3","존재하지 않는 유저 정보입니다.");
        }

        model.addAttribute("user",user);
        return "usr/user/userSearchEmail";
    }
    @RequestMapping("/user/searchEmail")
    public String searchEmail (){
        return "usr/user/userSearchEmail";
    }



    @RequestMapping("/user/foundPassword")
    public String foundPassword(){
        return "usr/user/userFoundPassword";
    }
    @RequestMapping("/user/doFoundPassword")
    public String doFoundPassword(HttpSession session, String uName, String uEmail, Model model){

        if (session.getAttribute("user") != null) {
            // 로그인 상태일 경우, 로그인 상태에서 할 필요 없다는 메시지 출력
            return Ut.jsReplace("F-A", "이미 로그인 상태입니다.", "/common/introduce");
        }

        if(Ut.isEmptyOrNull(uName)){
            return  Ut.historyBack("F-1","이름을 입력해주세요.");
        }
        if(Ut.isEmptyOrNull(uEmail)){
            return Ut.historyBack("F-2","이메일를 입력해주세요.");
        }

        User user = userService.getUseFoundByPassword(uName,uEmail);
        if(user==null){
            return Ut.historyBack("F-3","존재하지 않는 유저 정보입니다.");
        }

        model.addAttribute("user",user);

        User userInToken = userService.getUserByEmail(uEmail);
        if (user != null) {
            try {
                // 토큰 생성 및 사용자에 저장
                String token = userService.createPasswordResetTokenForUser(userInToken);
                // 비밀번호 재설정 이메일 발송
                userService.sendPasswordResetEmail(user, token);
                // 비밀번호 재설정 요청이 성공적으로 처리되었다는 메시지 표시
                model.addAttribute("message", "아이디찾기 및 비밀번호 재설정 이메일이 발송되었습니다.");
                return "usr/user/userSearchPassword"; // 비밀번호 재설정 이메일이 발송되었다는 메시지를 표시하는 뷰로 리다이렉트
            } catch (MessagingException e) {
                // 이메일 발송 중 오류가 발생한 경우 에러 메시지 표시
                model.addAttribute("error", "비밀번호 재설정 이메일 발송 중 오류가 발생했습니다.");
                return "usr/user/userFoundPassword";
            }
        } else {
            // 사용자를 찾을 수 없는 경우 에러 메시지 표시
            model.addAttribute("error", "해당 이메일 주소를 가진 사용자를 찾을 수 없습니다.");
            return "usr/user/userFoundPassword";
        }

    }

    @RequestMapping("/user/searchPassword")
    public String searchPassword(){
        return "usr/user/userSearchPassword";
    }

    @RequestMapping("/user/changePassword")
    public String changePassword(@RequestParam("token") String token, Model model) {
        User user = userService.getUserByPasswordResetToken(token);
        if (user != null) {
            model.addAttribute("username", user.getUName());
            model.addAttribute("token", token);
            return "usr/user/userChangePassword";
        } else {
            // 토큰이 유효하지 않거나 만료된 경우
            model.addAttribute("error", "비밀번호 재설정 요청이 유효하지 않습니다.");
            return "usr/user/userLogin";
        }
    }

    @RequestMapping("/user/doChangePassword")
    public String doChangePassword(@RequestParam("token") String token,
                                   @RequestParam("uPwd") String newPassword,
                                   Model model) {
        User user = userService.getUserByPasswordResetToken(token);
        if (user != null && newPassword != null) {
            userService.changeUserPassword(user, newPassword);
            userService.changeUserResetToken(user,null);
            model.addAttribute("message","비밀번호가 변경되었습니다.");
            return "usr/user/userLogin"; // 비밀번호 변경 성공 페이지로 이동
        } else {
            // 토큰이 유효하지 않거나 새 비밀번호가 제공되지 않은 경우
            model.addAttribute("error", "비밀번호 변경에 실패했습니다.");
            return "usr/user/userChangePassword"; // 비밀번호 재설정 페이지로 다시 이동
        }
    }

//    //비밀번호 변경 페이지
//    @GetMapping("/changePw")
//    public String showChangePasswordPage(@RequestParam("token") String token, Model model) {
//        User user = userService.getUserByPasswordResetToken(token);
//        if (user != null) {
//            model.addAttribute("username", user.getUName());
//            model.addAttribute("token", token);
//            return "login/changePw";
//        } else {
//            // 토큰이 유효하지 않거나 만료된 경우
//            model.addAttribute("error", "비밀번호 재설정 요청이 유효하지 않습니다.");
//            return "error";
//        }
//    }

//    // 비밀번호 변경 요청을 처리하는 메서드
//    @PostMapping("/changePw")
//    public String changeUserPassword(@RequestParam("token") String token,
//                                     @RequestParam("password") String newPassword,
//                                     Model model) {
//        User user = userService.getUserByPasswordResetToken(token);
//        if (user != null && newPassword != null) {
//            userService.changeUserPassword(user, newPassword);
//            model.addAttribute("message","비밀번호가 변경되었습니다.");
//            return "login/message"; // 비밀번호 변경 성공 페이지로 이동
//        } else {
//            // 토큰이 유효하지 않거나 새 비밀번호가 제공되지 않은 경우
//            model.addAttribute("error", "비밀번호 변경에 실패했습니다.");
//            return "login/changePw"; // 비밀번호 재설정 페이지로 다시 이동
//        }
//    }
}
