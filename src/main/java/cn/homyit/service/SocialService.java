package cn.homyit.service;

import cn.homyit.dao.ClubApplicationDao;
import cn.homyit.dao.SocialDao;
import cn.homyit.dao.SocialMemberDao;
import cn.homyit.domain.*;
import cn.homyit.utils.FileUtil;
import cn.homyit.utils.SecurityUtils;
import cn.homyit.utils.TimeUtil;
import cn.homyit.vo.Social;
import cn.homyit.vo.SocialMem;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Stream;

@Service
public class SocialService {

    @Autowired
    private SocialDao socialDao;

    @Autowired
    private ClubApplicationDao clubApplicationDao;


    @Autowired
    private SocialMemberDao socialMemDao;

    public PageResult<Map<String, Object>> getSocialSum(PageReq pageReq) {

        int offset = (pageReq.getPageNo() - 1) * pageReq.getPageSize();
        List<Map<String, Object>> records = socialDao.selectSum(pageReq.getPageSize(), offset);
        Long total = socialDao.count();

        PageResult<Map<String, Object>> pageResult = new PageResult<>();
        pageResult.setRecords(records);
        pageResult.setTotal(total);

        return pageResult;

    }

    public Result ApplySocial(String name,String introduction,String reviewer,Integer revId,MultipartFile file){

        //上传行程码保存到文件夹并返回路径接口

        String fileName2 = FileUtil.fileUpload(file);

        if (Stream.of(name, introduction, reviewer, revId,file).anyMatch(Objects::isNull)) {
            return Result.error("请填写完整信息");
        }
        Authentication authentication = SecurityUtils.getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User user = loginUser.getUser();



        try {
            int success = clubApplicationDao.insert(name, introduction, user.getRealName(),user.getId(),reviewer, revId, fileName2);
            if(success >0){
                return Result.ok("申请成功");
            }
            else {
                return  Result.error("申请失败，请重试");
            }
        }catch (MissingFormatArgumentException e){
            return Result.error(e.getMessage());
        }

    }

    public Result JoinSocial(String name){
        if(socialDao.ifHave(name)>0){

            Authentication authentication = SecurityUtils.getAuthentication();
            LoginUser loginUser = (LoginUser) authentication.getPrincipal();
            User user = loginUser.getUser();


            SocialMem socialMem = new SocialMem();
            socialMem.setMemberId(user.getId());
            socialMem.setName(user.getRealName());
            socialMem.setPicture(user.getImage());
            socialMem.setSocialName(name);
            try {
                // 创建 QueryWrapper 对象
                QueryWrapper<SocialMem> queryWrapper = new QueryWrapper<>();
// 添加查询条件
                queryWrapper.eq("member_id", user.getId()).eq("social_name", name);
                if(socialMemDao.selectCount(queryWrapper)>0){
                    return Result.error("已经是社团成员，请勿重复加入");
                }
                int success = socialMemDao.insert(socialMem);
                if(success >0){

                    return Result.ok("加入成功");
                }

                else {
                    return Result.error("加入失败请重试");
                }
            }catch (Exception e)
            {
                return Result.error(e.getMessage());
            }
        }


        else {
            return Result.error("不存在该社团，请检查重试");
        }
    }

    public ArrayList<Map<String, Object>> reviewers(){
        return socialDao.reviewerByType2();
    }

    public ArrayList<Map<String, Object>> myClub(){

        Authentication authentication = SecurityUtils.getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User user = loginUser.getUser();


        SocialMem socialMem = new SocialMem();
        socialMem.setMemberId(user.getId());

        socialMem.setName(user.getRealName());

        return socialDao.myClub(socialMem.getName(),socialMem.getMemberId());
    }
}