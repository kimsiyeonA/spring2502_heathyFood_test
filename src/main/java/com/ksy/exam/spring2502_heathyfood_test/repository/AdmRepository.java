package com.ksy.exam.spring2502_heathyfood_test.repository;

import com.ksy.exam.spring2502_heathyfood_test.vo.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AdmRepository {
    //@Insert("INSERT INTO article SET regDate = NOW(), updateDate = NOW(), title = #{title}, `body` = #{body}")
    public void doJoin(String uEmail, String uPwd, String uName,
                       String uPhone, String uBirth, String uNickName,
                       String uGender, String uTOSAgree, String uPIPAgree, String uIP, String uThumbnail, String uRole);

    @Select("SELECT * FROM User WHERE uIdx = #{uIdx}")
    public User getUserById(int uIdx);

    @Select("SELECT * FROM User WHERE uEmail=#{uEmail} ")
    public User getUserByEmail(String uEmail) ;


}


