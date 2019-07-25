package com.eisen.service;

import com.eisen.entity.User;

import java.util.List;

public interface UserService {

//    列出所有用户
    public List<User> listAllUser();

//    通过电话号码查询已预约用户
    public User getUserByPhone(String phone);

//    通过日期科室查询是否有预约
    public List<User> getUserByDate(String date);

//    通过电话号码删除已预约用户
    public void deleteUserByPhone (String phone);

//    创建并保存用户
    public void createUser(User user);
}
