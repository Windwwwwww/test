package cn.homyit.controller;

import cn.homyit.domain.Result;
import cn.homyit.dto.ApplicationDto;
import cn.homyit.dto.GetAppDto;
import cn.homyit.dto.JustifySiteDto;
import cn.homyit.service.ApplicationService;
import cn.homyit.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage/app")
@PreAuthorize("@jm.admin")
public class ApplicationController {
    @Autowired
    private ApplicationService applicationService;

    @Autowired
    private SiteService siteService;
    @PutMapping("/update")
    public Result audit(@RequestBody ApplicationDto applicationDto){return applicationService.audit(applicationDto);}
    @PostMapping("/getApp")//根据类型获取自己需要审批的申请，0场地1社团申请
    public Result getApp(@RequestBody GetAppDto getAppDto){
        return applicationService.getApp(getAppDto);
    }

    @GetMapping("/ifoccupied")
    public Result selectUnoccupiedSite(@RequestBody JustifySiteDto justifySiteDto){
        return siteService.selectUnoccupiedSite(justifySiteDto);
    }



}
