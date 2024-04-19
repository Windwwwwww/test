package cn.homyit.controller;

import cn.homyit.domain.Result;
import cn.homyit.domain.Site;
import cn.homyit.dto.GetSitesDto;
import cn.homyit.dto.UpdateSiteDto;
import cn.homyit.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage/site")
@PreAuthorize("@jm.admin")
public class ManageSiteController {

    @Autowired
    SiteService siteService;
    @PutMapping("/updatesite")
    @PreAuthorize("@jm.super")
    public Result updateSite(@RequestBody UpdateSiteDto updateSiteDto){

        return siteService.updateSite(updateSiteDto);

    }
    @DeleteMapping("/deletesite/{id}")
    @PreAuthorize("@jm.super")
    public Result deleteSite(@PathVariable int id){
        return siteService.deleteSite(id);
    }

    @PostMapping("/addsite")
    @PreAuthorize("@jm.super")
    public Result addSite(@RequestBody Site site){
        return siteService.addSite(site);
    }

    @PostMapping("/getallsite/{pageNum}")
    public Result getAllSites(@PathVariable int pageNum){
        return  siteService.getAllSite(pageNum);
    }
    @PostMapping("/selectsites")
    public Result selectSites(@RequestBody GetSitesDto getSitesDto){
        return siteService.selectSites(getSitesDto);
    }


}
