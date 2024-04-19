package cn.homyit.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @param:
 * @description:
 * @author: Answer
 * @create:2024/3/17 12:20
 **/
@RestController
public class Hello {
    @RequestMapping("/hello")
    @PreAuthorize("hasAuthority('test1')")
    public String hello(){
        return "hello";
    }
}

