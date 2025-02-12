package com.ksy.exam.spring2502_heathyfood_test.service;

import com.ksy.exam.spring2502_heathyfood_test.repository.AdmRepository;
import com.ksy.exam.spring2502_heathyfood_test.vo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AdmService {


    @Autowired
    AdmRepository admRepository;

    // 생성자
    public AdmService(AdmRepository admRepository) {
        this.admRepository = admRepository;
        // 이유는 생기는 시점 때문에 뒤에있는게 만들어지기전에 쓸려고해서 오류가 생김
    }

    public User getUserByEmail(String uEmail) {
        return admRepository.getUserByEmail(uEmail);
    }

    public User getUserById(int id) {
        return admRepository.getUserById(id);
    }

}
