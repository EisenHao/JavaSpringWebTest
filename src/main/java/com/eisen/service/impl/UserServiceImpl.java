package com.eisen.service.impl;

import com.eisen.entity.User;
import com.eisen.repository.UserRepository;
import com.eisen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {
    @Autowired(required = false)
    private UserRepository userRepository;
//    列出所有用户
    @Override
    public List<User> listAllUser() {
        return userRepository.findAll();
    }
//    通过电话号码查询已预约用户
    @Override
    public User getUserByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

//    通过日期查询是否有预约
    @Override
    public  List<User> getUserByDate(String date) {
        return userRepository.findByDate(date);
    }

//    通过电话号码删除已预约用户
    @Override
    public void deleteUserByPhone (String phone) {
        userRepository.removeByPhone(phone);
    }
//    创建并保存用户
    @Override
    public void createUser(User user) {
        userRepository.save(user);
    }
}
