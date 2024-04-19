package cn.homyit.service;

import cn.homyit.dao.JoinSiteDao;
import cn.homyit.dao.SiteApplicationDao;
import cn.homyit.dao.SiteDao;
import cn.homyit.domain.*;
import cn.homyit.domain.Enum.Code;
import cn.homyit.domain.Enum.SiteSort;
import cn.homyit.domain.Page;
import cn.homyit.domain.Result;
import cn.homyit.domain.Site;
import cn.homyit.domain.User;
import cn.homyit.dto.*;
import cn.homyit.dto.GetSitesDto;
import cn.homyit.dto.JustifySiteDto;
import cn.homyit.dto.UpdateSiteDto;
import cn.homyit.execption.SystemException;
import cn.homyit.utils.*;
import cn.homyit.vo.JoinSite;
import cn.homyit.vo.OccupiedSiteListVO;
import cn.homyit.vo.OccupiedSiteVO;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Stream;

@Service
@Slf4j
public class SiteService {

    @Autowired
    private SiteDao siteDao;

    @Autowired
    private SiteApplicationDao siteApplicationDao;

    @Autowired
    RedisCache redisCache;


    @Autowired
    private JoinSiteDao joinSiteDao;

    public Result reviewers(String name){
        int type=siteDao.typeByName(name);

        //该地为一级审核
        if(type == 1){
            List<ArrayList<Map<String,Object>>> list = new ArrayList<>();
            list.add(siteDao.reviewerByType1(name));
            return Result.ok("查询成功",list);
        }
        if(type == 2)  //为二级审核
        {

            List<ArrayList<Map<String,Object>>> list = new ArrayList<>();

            ArrayList<Map<String,Object>> reviewers1 = siteDao.reviewerByType1(name);
            ArrayList<Map<String,Object>> reviewers2 = siteDao.reviewerByType2(name);

            list.add(reviewers1);
            list.add(reviewers2);

            return Result.ok("查询成功",list);

        }
        else{
            return Result.error("查询失败，请重试");
        }

    }

    public Result JoinSite(SiteApplication siteApplication){


        if (Stream.of(
                siteApplication.getAcademy(),
                siteApplication.getPurpose(),
                siteApplication.getReviewer(),
                siteApplication.getReviewerId(),
                siteApplication.getName(),
                siteApplication.getDate(),
                siteApplication.getPeriod()
        ).anyMatch(Objects::isNull)) {
            return Result.error("请填写完整信息");
        }


        try {
            if(!ifSiteIsOccupied(siteApplication.getDate(),siteApplication.getName(),siteApplication.getPeriod())){
                return Result.ok("抱歉，该场地该时期已占用,请更换时间");

            }
            else{

                Authentication authentication = SecurityUtils.getAuthentication();
                LoginUser loginUser = (LoginUser) authentication.getPrincipal();
                User user = loginUser.getUser();

                int success = siteApplicationDao.insertApplication(user.getId(), user.getRealName(),siteApplication.getName(),siteApplication.getAcademy(),
                        siteApplication.getPeriod(),siteApplication.getDate(),siteApplication.getReviewer(),siteApplication.getReviewerId(),
                        siteApplication.getPurpose());
                if (success > 0) {
                    return Result.ok("申请成功");
                } else {
                    return Result.error( "申请失败，请检查并重试");
                }
            }
        }catch (Exception e) {
            return Result.error( e.getMessage());
        }

    }


    public Result addSite(Site site){
        if(site.getRoomNum()==null){
            //没有房间号

            if(siteDao.getSites(site.getSite()) >0){
                return Result.error("改场地已存在，添加失败");
            }
        }
        if(site.getRoomNum()!=null)
        {
            //有房间号
            if(siteDao.ifHaveSite(site.getSite(), site.getRoomNum()) >0){
                return Result.error("改场地已存在，添加失败");
            }

        }

        if(siteDao.insert(site)>0){
            return Result.ok("添加场地成功");

        }

        else return Result.error("出错了，请重试");
    }



    public ArrayList<Map<String,Object>> getSite(SiteSort siteSort) {
        if (siteSort == SiteSort.INDOOR) {
            return siteDao.getSiteIn();
        } else if (siteSort == SiteSort.OUTDOOR) {
            return siteDao.getSiteOut();
        }


        return null;
    }


    public ArrayList<Map<String, Object>> selectPeriod(String site){
        return siteDao.selectPeriod(site);
    }

    //TODO 每天从数据库更新三天的预约信息

    @Transactional(readOnly = true,rollbackFor = {Exception.class})
    public Result selectUnoccupiedSite(JustifySiteDto justifySiteDto){

        LambdaQueryWrapper<Site> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.select(Site::getRoomNum).eq(Site::getSite,justifySiteDto.getSiteName());
        List<Map<String,Object>> mapList=siteDao.selectMaps(lambdaQueryWrapper);
        List<OccupiedSiteVO> occupiedSiteListVO=new OccupiedSiteListVO().getOccupiedSiteVOS();
        for (Map<String, Object> stringObjectMap : mapList) {
            OccupiedSiteVO occupiedSiteVO=new OccupiedSiteVO((String)stringObjectMap.get("room_num"),ifSiteIsOccupied(justifySiteDto.getDate(), justifySiteDto.getSiteName()+"_"+stringObjectMap.get("room_num"), justifySiteDto.getPeriod() ));
            occupiedSiteListVO.add(occupiedSiteVO);
        }
        return new Result(Code.SELETE_OK.getStateNum(), occupiedSiteListVO);

    }
    public boolean ifSiteIsOccupied(LocalDate otherdate,String siteName,String period){


        long begintime=GetRedisTimeNum.getRedisNum(otherdate,period).get("begintime");
        long endtime=GetRedisTimeNum.getRedisNum(otherdate,period).get("endtime");
        for(long i=begintime;i<=endtime;i++){
            Set<String> set=redisCache.getCacheSet(i+"");
            if(set.contains(siteName)){
                return false;
            }

        }

        return  true;



    }

    public boolean ifSiteIsInsideRoom(String name){
        LambdaQueryWrapper<Site> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Site::getSite,name);
        List<Site> list=siteDao.selectList(lambdaQueryWrapper);
        if(list.size()==0){
            throw new SystemException(Code.SYSTEM_ERR.getStateNum(), "数据库中没有该场地，请刷新重试");
        }
        if (list.size()==1){
            return false;
        }
        return true;

    }
    @Transactional(rollbackFor = {Exception.class})
    public Result updateSite(UpdateSiteDto updateSiteDto){
        Site site =new Site();
        site.setId(updateSiteDto.getId());
        site.setType(updateSiteDto.getType().getNum());
        if(siteDao.updateById(site)==0){

            throw new SystemException(Code.UPDATE_ERR.getStateNum(), Code.UPDATE_ERR.getMsg());

        }

        return new Result(Code.UPDATE_OK.getStateNum(),Code.UPDATE_OK.getMsg());

    }
    @Transactional(rollbackFor = {Exception.class})
    public Result deleteSite(int id){
        if(siteDao.deleteById(id)==0){
            throw  new SystemException(Code.DELETE_ERR.getStateNum(), Code.DELETE_ERR.getMsg());
        }

        return new Result(Code.DELETE_OK.getStateNum(),Code.DELETE_OK.getMsg());
    }
    @Transactional(readOnly = true,rollbackFor = {Exception.class})
    public Result getAllSite(int pageNum){
        Page<Site> iPage=new Page<>(pageNum,30);
        Page<Site> iPage1=siteDao.selectPage(iPage,null);
        return new Result(Code.SELETE_OK.getStateNum(), Code.SELETE_OK.getMsg(),iPage1);


    }
    @Transactional(readOnly = true,rollbackFor = {Exception.class})
    public Result selectSites(GetSitesDto getSitesDto){
        //LambdaQueryWrapper<Site>  lambdaQueryWrapper= CreateWrapper.createWrapper(getSitesDto.getGetSiteDto(), Site.class);
        LambdaQueryWrapper<Site> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        GetSiteDto siteDto=getSitesDto.getGetSiteDto();
        if(siteDto.getId()!=0){
            lambdaQueryWrapper.like(Site::getId,siteDto.getId());
        }else if (siteDto.getSite()!=null&&!siteDto.getSite().isEmpty()){
            lambdaQueryWrapper.like(Site::getSite,siteDto.getSite());
        }else if(siteDto.getType()!=0){
            lambdaQueryWrapper.eq(Site::getType,siteDto.getType());
        }

        Page<Site> iPage=new Page<>(getSitesDto.getPageNum(),30);
        Page<Site> iPage1=siteDao.selectPage(iPage,lambdaQueryWrapper);

        return  new Result(Code.SELETE_OK.getStateNum(), Code.SELETE_OK.getMsg(),iPage1);
    }

}
