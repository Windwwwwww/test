package cn.homyit.controller;

import cn.homyit.domain.Result;
import cn.homyit.domain.User;
import cn.homyit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @param:
 * @description:
 * @author: Answer
 * @create:2024/3/11 21:33
 **/
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;


    @PostMapping("/login")
    public Result login(@RequestBody User user, HttpServletRequest request){
        return userService.login(user,request);
    }

    @GetMapping("/logout")
    public Result logout(){
        return userService.logout();
    }

    @PostMapping("/register")
    public Result register(@RequestBody User user){
        return userService.register(user);
    }

    @GetMapping("/getPerson")
    public Result getPerson(){
        return userService.getPerson();
    }

    @PostMapping("/updatePerson")
    public Result updatePerson(@RequestBody User user){
        return userService.updatePerson(user);
    }

    @PostMapping("/updatePassword")
    public Result updatePassword(@RequestBody User user){
        return userService.updatePassword(user);
    }

    @PostMapping("/updateProfileImage")
    public Result updateProfileImage(@RequestParam(value = "file") MultipartFile file){
        return userService.updateProfileImage(file);
    }

    @GetMapping("/getApplication")
    public Result getApplication(){
        return userService.getApplication();
    }

    @GetMapping("/getProfile")
    public Result getProfile(){
        return userService.getProfileImage();
    }
//    @PostMapping

//    public Result sendEmail(@RequestParam String email){
//        return null;
//    }

//    public Result hello

//    @PostMapping("/linkEmail")
//    public Result linkEmail
}
