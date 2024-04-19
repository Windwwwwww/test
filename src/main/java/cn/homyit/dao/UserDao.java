package cn.homyit.dao;

import cn.homyit.domain.User;
import cn.homyit.dto.SelectDto;
import cn.homyit.dto.SelectSiteDto;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * @param:
 * @description:
 * @author: Answer
 * @create:2024/3/11 21:40
 **/
@Mapper
public interface UserDao extends BaseMapper<User> {
    @Select("select type from campus_management.t_userCampus where id = #{id}")
    String selectType(Integer id);

    @Select("select image from campus_management.t_userCampus where id = #{id}")
    String selectImage(Integer id);

    @Select("select id, user_name, account, param, real_name, workplace,t_userCampus.email,image from campus_management.t_userCampus where id =#{id}")
    List<Map<String,String>> selectPerson(Integer id);


    //查社团的申请
    @Select("select club_name,applicant,picture,time,status from campus_management.club_application where applicant = #{applicant}")
    List<SelectDto> selectByPresident(String applicant);
    //查场地的申请
    @Select("select name,applicant,time,status from campus_management.t_join_site where applicant = #{applicant}")
    List<SelectSiteDto> selectByApplicant(String applicant);

    @Select("select image from campus_management.t_userCampus where id = #{id}")
    String selectProfile(Integer id);
}
