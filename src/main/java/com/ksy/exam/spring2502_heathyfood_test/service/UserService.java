package com.ksy.exam.spring2502_heathyfood_test.service;

import com.ksy.exam.spring2502_heathyfood_test.repository.UserRepository;
import com.ksy.exam.spring2502_heathyfood_test.test.repository.ArticleRepository;
import com.ksy.exam.spring2502_heathyfood_test.util.Ut;
import com.ksy.exam.spring2502_heathyfood_test.vo.Article;
import com.ksy.exam.spring2502_heathyfood_test.vo.ResultData;
import com.ksy.exam.spring2502_heathyfood_test.vo.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class UserService {

    @Autowired  // JavaMailSender를 주입받음
    private JavaMailSender mailSender;

    @Autowired
    UserRepository userRepository;
    // 생성자
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
        // 이유는 생기는 시점 때문에 뒤에있는게 만들어지기전에 쓸려고해서 오류가 생김
    }

    public ResultData<Integer> doJoin(String uEmail, String uPwd, String uName,
                             String uPhone, String uBirth, String uNickName,
                             String uGender, String uTOSAgree, String uPIPAgree, String uIP, String uThumbnail, String uRole){

        User existUser = getUserByEmail(uEmail);
        if(existUser != null){
            return ResultData.from("F-9", Ut.f("이미 사용중인 아이디(%s) 입니다.", uEmail));
        }
        existUser= getUserByNickName(uNickName);
        if(existUser != null){
            return ResultData.from("F-10", Ut.f("이미 사용중인 닉네임(%s) 입니다.", uNickName));
        }
        userRepository.doJoin(uEmail, uPwd, uName, uPhone, uBirth, uNickName, uGender, uTOSAgree, uPIPAgree, uIP, uThumbnail, uRole);
        int id = userRepository.getLastInsertId();
        return  ResultData.from("S-1","회원가입성공", id);
    }
    public User getUserByNickName(String uNickName) {

        return userRepository.getUserByNickName(uNickName);
    }

    public User getUserByEmail(String uEmail) {

        return userRepository.getUserByEmail(uEmail);
    }

    public User getUserById(int id) {
        return userRepository.getUserById(id);
    }

    public User getUseFoundByEmail(String uName, String uPhone) {
        return userRepository.getUseFoundByEmail(uName,uPhone);
    }

    public User getUseFoundByPassword(String uName, String uEmail) {
        return userRepository.getUseFoundByPassword(uName,uEmail);
    }

    public String createPasswordResetTokenForUser(User user) {
        String token = UUID.randomUUID().toString();
        user.setUResetToken(token);
        userRepository.tokenUpdate(user.getUResetToken(),user.getUIdx());
        User userInToken = userRepository.getUseFoundByToken(user.getUIdx());
        return userInToken.getUResetToken();
    }

    public void sendPasswordResetEmail(User user, String token) throws MessagingException {

        // 현재 시간 설정
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String formattedNow = now.format(formatter);

        // 비밀번호 변경 링크
        //String resetLink = "http://localhost:8080/changePw?token=" + token;
        String resetLink = "http://localhost:8082/usr/user/changePassword?token=" + token;

        // 이메일 메시지 구성
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(user.getUEmail());
        helper.setSubject(user.getUNickName()+"님 비밀번호 재설정 메일");

        // HTML 메시지 설정
        String htmlMsg = "<!doctype html>" +
                "<html lang='ko'>" +
                "<head>" +
                "  <meta charset='UTF-8'>" +
                "  <meta name='viewport' content='width=device-width, user-scalable=no, initial-scale=1.0, maximum-scale=1.0, minimum-scale=1.0'>" +
                "  <meta http-equiv='X-UA-Compatible' content='ie=edge'>" +
                "  <title>Document</title>" +
                "</head>" +
                "<body>" +
                "<div style='max-width: 600px;'>" +
                "  <div style='background-color: rgb(250,144,88); border-bottom: 2px solid rgb(255,220,194); color: rgb(234, 236, 238); font-size: 28px; font-weight:bold; padding: 20px 30px;'>" +
                "    헬스 푸드 랩(HealthyFoodLab)" +
                "  </div>" +
                "  <div style='line-height: 133%; padding: 30px;'>" +
                "    <span>안녕하세요!&nbsp;<b>헬스 푸드 랩(HealthyFoodLab)</b>입니다.</span>" +
                "    <br><br>" +
                "    <p>회원님은 " + formattedNow + "에 비밀번호 찾기 요청을 하셨습니다.</p>" +
                "    <br><br>" +
                "    <span>해당 이메일 주소로 비밀번호 재설정을 요청하여 인증할 수 있는 링크를 발송하였습니다. 본인이 요청한 것이 맞다면 아래 링크를 클릭하여 이메일 인증을 완료한 뒤 비밀번호를 재설정해 주시기 바랍니다.</span>" +
                "    <p>회원 아이디: " + user.getUName() + "</p>" +
                "    <br><br>" +
                "    <a href='" + resetLink + "' style='color: white; text-decoration: none; padding: 10px 20px; background-color: rgb(250,144,88); border-radius: 5px; display: inline-block;'>비밀번호 변경</a>" +
                "    <br><br>" +
                "    <span>해당 링크를 타인에게 알려줄 경우 개인정보 도용의 위험이 있습니다. 절대 타인에게 알려주시 마시오.</span>" +
                "    <br><br>" +
                "    <span>혹시 본인이 요청한 적 없다면 이 이메일을 폐기해 주시기 바랍니다.</span>" +
                "    <br><br>" +
                "    <span>감사합니다.</span>" +
                "  </div>" +
                "</div>" +
                "</body>" +
                "</html>";
//        String htmlMsg = "<p>안녕하세요.</p>" +
//                "<p>회원님은 " + formattedNow + "에 비밀번호 찾기 요청을 하셨습니다.</p>" +
//                "<p>코와사는 특정 회원의 비밀번호를 확인할 수 없기 때문에 아래 버튼을 통해 비밀번호를 초기화해주세요.</p>" +
//                "<p>회원 아이디: " + user.getUName() + "</p>" +
//                "<a href='" + resetLink + "' style='color: white; text-decoration: none; padding: 10px 20px; background-color: #1a73e8; border-radius: 5px; display: inline-block;'>비밀번호 변경</a>";

        // HTML 형식으로 메시지를 설정합니다.
        helper.setText(htmlMsg, true);

        // 메일 발송
        mailSender.send(message);
    }
    public void changeUserPassword(User user, String newPassword) {
        user.setUPwd(newPassword); // 비밀번호 암호화
        userRepository.changeUserPassword(user.getUIdx(),user.getUPwd());
    }

    public User getUserByPasswordResetToken(String token) {
        User user= userRepository.getUserByPasswordResetToken(token);
        return user;
    }
    public void changeUserResetToken(User user,String token) {
        userRepository.changeUserResetToken(user.getUIdx(),token);
    }
}
