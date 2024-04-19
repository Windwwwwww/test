package cn.homyit.controller;

import cn.homyit.dao.ReviewerDao;
import cn.homyit.dao.SiteDao;
import cn.homyit.domain.Enum.SiteSort;
import cn.homyit.domain.Result;
import cn.homyit.domain.Reviewer;
import cn.homyit.domain.Site;
import cn.homyit.domain.SiteApplication;
import cn.homyit.service.SiteService;
import cn.homyit.vo.JoinSite;
import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 场地申请
 */
@RestController
@RequestMapping("/site")
public class SiteController {

    @Autowired
    private SiteDao siteDao;

    @Autowired
    private SiteService siteService;
    @Autowired
    private ReviewerDao reviewerDao;


    @PostMapping("/applySite")
    public Result JoinSite(@RequestBody SiteApplication siteApplication) {

        return siteService.JoinSite(siteApplication);


    }

    @PostMapping("/addSite")
    public Result addSite(@RequestBody Site site) {
        return siteService.addSite(site);
    }


    @PostMapping("/selectPeriod")
    public ArrayList<Map<String, Object>> selectPeriod(@RequestParam String site) {
        return siteService.selectPeriod(site);
    }

    @PostMapping("/selectSites")
    public ArrayList<Map<String,Object>> selectSite(@RequestParam SiteSort siteSort) {

        return siteService.getSite(siteSort);
    }


    /**
     * 查找审核员
     * @param site
     * @return
     */
    @PostMapping("/selectReviewer")
    public Result ReviewerBySite(@RequestParam("site") String site) {
        return siteService.reviewers(site);

    }





    @PutMapping("/updateReviewer/{id}")
    public Result updateReviewer(@PathVariable Long id,@RequestBody  Reviewer reviewer)
    {
        Wrapper<Reviewer> updateWrapper = new QueryWrapper<Reviewer>().eq("id", id);

        int i = reviewerDao.update(reviewer, updateWrapper);

        return i>0? Result.ok("更新成功", null):new Result(0, "更新失败，请检查并重试", null);
    }

    @PutMapping("/deleteReviewer/{id}")
    public Result updateReviewer(@PathVariable Long id){

        int i = reviewerDao.deleteById(id);
        return i>0?  Result.ok("删除成功", null):new Result(0, "删除失败，请重试", null);


    }
}