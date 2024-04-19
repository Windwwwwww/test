package cn.homyit.controller;


import cn.homyit.dao.SocialDao;
import cn.homyit.dao.SocialMemberDao;
import cn.homyit.domain.PageReq;
import cn.homyit.domain.PageResult;
import cn.homyit.domain.Result;
import cn.homyit.service.SocialService;
import cn.homyit.vo.Social;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

/**
 * 社团申请
 */
@RestController
@RequestMapping("/social")
public class SocialController {




    @Autowired
    private SocialDao socialDao;

    @Autowired
    private SocialMemberDao socialMemDao;

    @Autowired
    private SocialService socialService;

    @PostMapping("/apply")
    public Result ApplySocial(
            @RequestParam("name")String name,
            @RequestParam("introduction")String introduction,
            @RequestParam("reviewer")String reviewer, //审核人
            @RequestParam("reviewerId")Integer reviewerId,
            @RequestParam("file") MultipartFile file) {

        return socialService.ApplySocial(name,introduction,reviewer,reviewerId,file);

    }

    /**
     * 查找社团分类
     *
     * @return
     */
    @PostMapping("/selectSum")
    public PageResult<Map<String, Object>> SelectSum(@RequestBody PageReq pageReq){


        return socialService.getSocialSum(pageReq);
    }


    /**
     * 查找社团图标和社团简介
     * @param name
     * @return
     */
    @PostMapping("/selectInfo")
    public ArrayList SelectSocial(@RequestParam("name") String name){

        return socialDao.selectInfo(name);
    }


    /**
     * 社团成员的名字和头像信息和社员加入时间
     * @param socialName
     * @return
     */
    @PostMapping("/selectMember")
    public ArrayList<Map<String,Object>>  SelectMember(@RequestParam("name") String socialName){

        return socialMemDao.selectMembers(socialName);
    }

    /**
     * 社员加入
     */
    //获取当前登录人的姓名和头像
    @PostMapping("/join")
    public Result JoinSocial(@RequestParam String name){

        return socialService.JoinSocial(name);

    }

    @GetMapping("/reviewers")
    ArrayList<Map<String, Object>> reviewers(){
        return socialService.reviewers();
    }


    @GetMapping("/myClub")
    ArrayList<Map<String, Object>> myClub(){
        return socialService.myClub();
    }
}
