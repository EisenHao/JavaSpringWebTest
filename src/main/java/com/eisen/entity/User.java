package com.eisen.entity;

import javax.persistence.*;
import javax.validation.constraints.Future;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
//获取当前系统时间
import java.text.SimpleDateFormat;
import java.util.Date;

//数据库中存放数据类
@Entity //实体
@Table(name = "table_user")
public class User {
//    预约用户的唯一标识
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //自增长策略
    @Column(name = "id")
    private int id;

//    预约用户的手机号
    @Column(name = "phone")
    private String phone = "";

//    预约用户的预约日期
    @Column(name = "date")
    private String date = ""; //日期

//    预约用户的预约就诊时间
    @Column(name = "time")
    private String time = ""; //时间

//    预约用户的预约科室
    @Column(name = "department")
    private String department = ""; //科室 1：内科、 2：外科


    public User(String phone, String date, String time, String department) {
        this.phone = phone;
        this.date = date;
        this.time = time;
        this.department = department;
    }

    public User() {
    }

    //当只有手机号码时候
    public User(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    @Override
    public String toString() {
        return String.format("User[id=%d, phone='%s', date='%s', time='%s', department='%s']",
                id, phone, date, time, department);
    }

//    判断是否为纯数字
    public boolean isPhoneCorrect() {
        if(phone.length() != 11) {
            return false;
        }
        for (int i = phone.length();--i>=0;){
            if (!Character.isDigit(phone.charAt(i))){
                return false;
            }
        }
        return true;
    }

    public int calcDays(){
        int timeReq = 0, timeNow=0;
        //请求预约时间
        String dataReq = date.replaceAll("-",""); //eg. 20181130
        //获取当前系统时间
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");//设置日期格式
        String dataNow = df.format(new Date()).replaceAll("-",""); //eg. 20181123
        //字符串转换为int
        try {
            timeReq = Integer.parseInt(dataReq);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        try {
            timeNow = Integer.parseInt(dataNow);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        int days = (timeReq%1000000/10000 - timeNow%1000000/10000)*365
                + (timeReq%10000/100 - timeNow%10000/100)*30
                + (timeReq%100 - timeNow%100);
        System.out.println(" 时间差为：" + days);
        return days;
    }
};
