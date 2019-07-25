package com.eisen.controller;

import com.eisen.entity.User;
import com.eisen.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;


@Controller
@RequestMapping("/")
public class MainController {

    @Autowired
    UserService userService;
    //首页： 填写预约日期、时间、科目
    @RequestMapping(value = "/home", method = RequestMethod.GET)
    public String home(ModelMap model, @RequestParam(value = "statusMsg",defaultValue = "")String msg) {
        model.addAttribute( "user", new User());
        if(msg!= null) {
            model.addAttribute("statusMsg", msg);
        }else{
            model.addAttribute("statusMsg","");
        }
        return "home";
    }

    //控制行为：检查输入信息正确性并尝试 保存预约信息
    @RequestMapping(value = "/checkAndTryToSave", method = RequestMethod.POST)
    public String checkInformation(@ModelAttribute User user, Model model, RedirectAttributes attributes){
        System.out.println("传递的user信息： "+user.toString());
        //S1. 日期检查
        int days = user.calcDays();
        if(days > 2){
            //重定向 回初始界面
            String statusMsg = "Error1: Date:" + user.getDate() + " is more than two days!!!(" + days + " days)";
            attributes.addAttribute("statusMsg",statusMsg);
            return "redirect:/home";//这是重定向
        }
        //S2. 号码有效性判断： 如果取消预约手机号非11位 或 非纯数字，提示无效号码
        if (!user.isPhoneCorrect()) {
            //重定向 回初始界面
            String statusMsg = " Error2: phone number (" + user.getPhone() + ") is invalid!!!";
            attributes.addAttribute("statusMsg", statusMsg);
            return "redirect:/home";//这是重定向
        }

//        S3. 查询数据库中关于 日期 + 科室 的用户
        boolean isExistFlag = false;
        List<User> lists = userService.getUserByDate(user.getDate());
        if (lists == null){
            //必定不存在
            isExistFlag = false;
        }else if(lists.size()==1) {
            //取出唯一一个元素
            User temp = lists.get(0);
            //判断这个元素与user的科室是否一致
            if(temp.getDepartment() == user.getDepartment()) {
                //a.若当天当科室已有预约 -> 抱歉已有预约 ,重定向 home
                isExistFlag = true;
                String statusMsg = " Error3: User[" + temp.getPhone() + "] has reserved in " + temp.getDate() + " department:" + temp.getDepartment() + ", please choose another day!!!";
                attributes.addAttribute("statusMsg", statusMsg);
                return "redirect:/home";//这是重定向
            }
        }else if(lists.size()>=2){
            //a.若当天当科室已有预约 -> 抱歉已有预约 ,重定向 home
            //必定存在
            isExistFlag = true;
            //取出唯一一个元素
            User temp1 = lists.get(0);
            User temp2 = lists.get(1);
            if(temp1.getDepartment() == user.getDepartment()){
                String statusMsg = " Error3: User[" + temp1.getPhone() + "] has reserved in " + temp1.getDate() + " department:" + temp1.getDepartment() + ", please choose another day!!!";
                attributes.addAttribute("statusMsg", statusMsg);
                return "redirect:/home";//这是重定向
            }else{
                String statusMsg = " Error3: User[" + temp2.getPhone() + "] has reserved in " + temp2.getDate() + " department:" + temp2.getDepartment() + ", please choose another day!!!";
                attributes.addAttribute("statusMsg", statusMsg);
                return "redirect:/home";//这是重定向
            }
        }

//        S4. 查询此手机号是否注册
        //a. 若已被注册 -> 重定向回首页
        if(userService.getUserByPhone(user.getPhone()) != null) {
            //重定向 回初始界面
            String statusMsg = "Error4: phone number (" + user.getPhone() + ") has been registered, please try again!!!";
            attributes.addAttribute("statusMsg",statusMsg);
            return "redirect:/home";//这是重定向
        }
        //b. 未被注册 -> 添加到数据库，并提示
        userService.createUser(user);
        System.out.println(" 尝试存入数据库 "+user.toString());
        //b.若当天当科室 无 预约 显示 已填写的信息  ：等待点击“预约按钮”跳转 -> 跳转 saveUser 控制行为（传递user数据）
        model.addAttribute("user", user);
        return "successfulSave";//显示成功预约页面
    }


//    取消预约 页面
    @RequestMapping(value = "/cancelUser", method = RequestMethod.GET)
    public String cancelUser(ModelMap model, @RequestParam(value = "statusMsg",defaultValue = "")String msg) {
        model.addAttribute( "user", new User());
        if(msg!= null) {
            model.addAttribute("statusMsg", msg);
        }else{
            model.addAttribute("statusMsg","");
        }
        return "cancelUser";
    }

//    控制行为：处理 取消预约
    @RequestMapping(value = "/dealWithCancel", method = RequestMethod.POST)
    public String dealWithCancel(@ModelAttribute User user, Model model, RedirectAttributes attributes){
        //S1. 号码有效性判断： 如果取消预约手机号非11位 或 非纯数字，提示无效号码
        if (!user.isPhoneCorrect()) {
            //重定向 回取消预约页面
            String statusMsg = " Error2: Cancel phone number (" + user.getPhone() + ") is invalid!!!";
            attributes.addAttribute("statusMsg", statusMsg);
            return "redirect:/cancelUser";//这是重定向
        }
        //S2.从数据库中搜索 user.getPhone() 用户
        User result = userService.getUserByPhone(user.getPhone());
        //a.若不存在，重定向 取消预约页面
        if(result == null){
            String statusMsg = " Error5: Cancel phone number (" + user.getPhone() + ") is not exist in database!!!";
            attributes.addAttribute("statusMsg", statusMsg);
            return "redirect:/cancelUser";//这是重定向
        }
        //b. 若存在 -> 执行删除 预约用户
        //执行数据库中删除命令
        userService.deleteUserByPhone(user.getPhone());
        String statusMsg = " Success: delete user [" + user.getPhone() + "] in datebase!!!";
        attributes.addAttribute("statusMsg", statusMsg);

        model.addAttribute("user", user);
        return "redirect:/cancelUser";
    }
}
