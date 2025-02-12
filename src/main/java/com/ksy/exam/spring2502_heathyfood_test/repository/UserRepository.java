package com.ksy.exam.spring2502_heathyfood_test.repository;

import com.ksy.exam.spring2502_heathyfood_test.vo.Article;
import com.ksy.exam.spring2502_heathyfood_test.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface UserRepository {
    //@Insert("INSERT INTO article SET regDate = NOW(), updateDate = NOW(), title = #{title}, `body` = #{body}")
    public void doJoin(String uEmail, String uPwd, String uName,
                       String uPhone, String uBirth, String uNickName,
                       String uGender, String uTOSAgree, String uPIPAgree, String uIP, String uThumbnail, String uRole);

    @Select("SELECT * FROM User WHERE uIdx = #{uIdx}")
    public User getUserById(int uIdx);

    @Select("SELECT LAST_INSERT_ID();")
    public int getLastInsertId();

    @Select("SELECT * FROM User WHERE uEmail=#{uEmail}")
    public User getUserByEmail(String uEmail) ;

    @Select("SELECT * FROM User WHERE uNickName=#{uNickName}")
    public User getUserByNickName(String uNickName);

    @Select("SELECT * FROM User WHERE uName=#{uName} and uPhone=#{uPhone} ")
    public User getUseFoundByEmail(String uName, String uPhone);

    @Select("SELECT * FROM User WHERE uName=#{uName} and uEmail=#{uEmail} ")
    public User getUseFoundByPassword(String uName, String uEmail);




    @Update("UPDATE User SET uUpdateDate = NOW(), uResetToken = #{uResetToken} WHERE uIdx = #{uIdx}")
    public void tokenUpdate(String uResetToken,int uIdx);

    @Select("SELECT * FROM User WHERE uIdx=#{uIdx}")
    public User getUseFoundByToken(int uIdx);

    @Select("SELECT * FROM User WHERE uResetToken=#{uResetToken}")
    public User getUserByPasswordResetToken(String uResetToken);

    @Update("UPDATE User SET uUpdateDate = NOW(), uPwd = #{uPwd} WHERE uIdx = #{uIdx}")
    public void changeUserPassword(int uIdx, String uPwd );

    @Update("UPDATE User SET uUpdateDate = NOW(), uResetToken = #{uResetToken} WHERE uIdx = #{uIdx}")
    public void changeUserResetToken(int uIdx,String uResetToken);
}


