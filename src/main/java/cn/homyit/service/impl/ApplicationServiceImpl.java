package cn.homyit.service.impl;

import cn.homyit.dao.*;
import cn.homyit.domain.*;
import cn.homyit.domain.Enum.ApplicationState;
import cn.homyit.domain.Enum.Code;
import cn.homyit.domain.Enum.SiteState;
import cn.homyit.dto.ApplicationDto;
import cn.homyit.dto.Enum.Type;
import cn.homyit.dto.GetAppDto;
import cn.homyit.execption.SystemException;
import cn.homyit.service.ApplicationService;
import cn.homyit.utils.GetRedisTimeNum;
import cn.homyit.utils.RedisCache;
import cn.hutool.core.date.DateTime;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;

import kotlin.jvm.internal.Lambda;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

@Service
@Slf4j
@Transactional(rollbackFor = {Exception.class})
public class ApplicationServiceImpl implements ApplicationService {
//TODO 权限认证，批量操作，帖子审核删除，可能会有excel导入形式，获取管理员列表和帖子列表都需要调整成模糊查询
    @Autowired
    SiteApplicationDao siteApplicationDao;
    @Autowired
    ClubApplicationDao clubApplicationDao;

    @Autowired
    SiteDao siteDao;

    @Autowired
    ClubDao clubDao;

    @Autowired
    ClubMembersDao clubMembersDao;

    @Autowired
    UserDao userDao;

    @Autowired
    RedisCache redisCache;
    @Override
    public Result audit(ApplicationDto applicationDto) {

        if(applicationDto.getAppId()==0){
            throw new SystemException(Code.UPDATE_ERR.getStateNum(), Code.UPDATE_ERR.getMsg());
        }
        Type type=applicationDto.getType();
        SiteApplication siteApplication;
        ClubApplication clubApplication;
        int flag1=0;

        //TODO 场地这里如果是室内场需要判断哪些教师有空然后自动分配可用的教室给申请者
        if(type==Type.SITE){
            siteApplication=new SiteApplication(applicationDto.getAppId(),applicationDto.getApplicationState(),applicationDto.getAppAdvice(), LocalDateTime.now());
            if(applicationDto.getApplicationState()==ApplicationState.ACCEPTED) {
               LambdaQueryWrapper<SiteApplication> lambdaQueryWrapper=new LambdaQueryWrapper<>();
               lambdaQueryWrapper.eq(SiteApplication::getId,applicationDto.getAppId());
               SiteApplication t=siteApplicationDao.selectOne(lambdaQueryWrapper);
               long beginnum= GetRedisTimeNum.getRedisNum(t.getDate(),t.getPeriod()).get("begintime");
               long endnum=GetRedisTimeNum.getRedisNum(t.getDate(),t.getPeriod()).get("endtime");
               String siteName=t.getName();
               for(long i=beginnum;i<=endnum;i++){
                   Set<String> m=new HashSet<>();
                   m.add(siteName+"_"+applicationDto.getAppAdvice());
                   redisCache.setCacheSet(i+"",m);
               }
            }
            flag1=siteApplicationDao.updateById(siteApplication);
        }else if(type==Type.CLUB){
            clubApplication=new ClubApplication(applicationDto.getAppId(),applicationDto.getApplicationState(),applicationDto.getAppAdvice(),LocalDateTime.now());
            if(applicationDto.getApplicationState()==ApplicationState.ACCEPTED){
                LambdaQueryWrapper<ClubApplication> lambdaQueryWrapper=new LambdaQueryWrapper<>();
                lambdaQueryWrapper.select(ClubApplication::getClubName,ClubApplication::getIntroduction,ClubApplication::getApplicant,ClubApplication::getPicture).eq(ClubApplication::getId,applicationDto.getAppId());
                List<Map<String,Object>> maps = clubApplicationDao.selectMaps(lambdaQueryWrapper);

                if(maps!=null&&!maps.isEmpty()){
                    Map<String,Object> map=maps.get(0);
                    LambdaQueryWrapper<Club> lambdaQueryWrapper1=new LambdaQueryWrapper<>();
                    lambdaQueryWrapper1.eq(Club::getName,(String)map.get(FieldName.club_name.name()));
                    Club c=clubDao.selectOne(lambdaQueryWrapper1);
                    if(c!=null){
                        throw new SystemException(Code.UPDATE_ERR.getStateNum(), "已存在同名社团");
                    }
                    Club club=new Club((String)map.get(FieldName.club_name.name()),(String)map.get(FieldName.introduction.name()),DateTime.now(),(String)map.get(FieldName.applicant.name()),(String)map.get(FieldName.picture.name()));
                    if(clubDao.insert(club)==0){
                        throw new SystemException(Code.ADD_ERR.getStateNum(), Code.ADD_ERR.getMsg());
                    }
                    ClubMembers clubMembers=new ClubMembers((String)map.get(FieldName.applicant.name()),DateTime.now(),(String)map.get(FieldName.club_name.name()));
                    int flag3=clubMembersDao.insert(clubMembers);
                    if(flag3==0){
                        throw new SystemException(Code.UPDATE_OK.getStateNum(),Code.UPDATE_OK.getMsg());
                    }
                }
            }
            flag1=clubApplicationDao.updateById(clubApplication);
        }
        if(flag1==0){
            throw new SystemException(Code.UPDATE_ERR.getStateNum(), Code.UPDATE_ERR.getMsg());
        }

        return new Result(Code.UPDATE_OK.getStateNum(),Code.UPDATE_OK);

    }

    enum FieldName{
        club_name,introduction,applicant,picture
    }





    @Override
    @Transactional(readOnly = true,rollbackFor = {Exception.class})
    public Result getApp(GetAppDto getAppDto) {
        int type=getAppDto.getType().getNum();
        if(type==0){
            Page<SiteApplication> siteApplicationIPage=new Page<>(getAppDto.getPageNum(),30);
            LambdaQueryWrapper<SiteApplication> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(SiteApplication::getStatus, ApplicationState.AUDITING).ge(SiteApplication::getDate, LocalDate.now().plusDays(2));
            Page<SiteApplication> iPage=siteApplicationDao.selectPage(siteApplicationIPage,lambdaQueryWrapper);
            return new Result(Code.SELETE_OK.getStateNum(), iPage);
        }else if(type==1){
            Page<ClubApplication> clubApplicationIPage=new Page<>(getAppDto.getPageNum(),30);
            LambdaQueryWrapper<ClubApplication> lambdaQueryWrapper=new LambdaQueryWrapper<>();
            lambdaQueryWrapper.eq(ClubApplication::getStatus,ApplicationState.AUDITING);
            Page<ClubApplication> iPage=clubApplicationDao.selectPage(clubApplicationIPage,lambdaQueryWrapper);
            return new Result(Code.SELETE_OK.getStateNum(), iPage);
        }

        return null;
    }
}
