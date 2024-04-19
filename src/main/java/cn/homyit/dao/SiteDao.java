package cn.homyit.dao;

import cn.homyit.domain.Site;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface SiteDao extends BaseMapper<Site> {



    @Select("select type from t_site where site LIKE CONCAT('%', #{name}, '%')")
    Integer typeByName(String name);

    @Select("SELECT  COUNT(*) FROM t_site WHERE site = #{name}")
    Integer getSites(String name);

    @Select("SELECT  COUNT(*) FROM t_site WHERE site = #{name} and room_num = #{roomNum}")
    Integer ifHaveSite(String name,String roomNum);

    @Select("SELECT real_name,id FROM t_userCampus WHERE site LIKE CONCAT('%', #{site}, '%') AND type = 1;")
    ArrayList<Map<String,Object>> reviewerByType1(String site);

    @Select("SELECT real_name,id FROM t_userCampus WHERE site LIKE CONCAT('%', #{site}, '%') AND type = 2;")
    ArrayList<Map<String,Object>> reviewerByType2(String site);


    @Select("SELECT site,room_num FROM t_site WHERE room_num IS NOT NULL")
    ArrayList<Map<String,Object>> getSiteIn();

    @Select("SELECT site FROM t_site WHERE room_num IS  NULL")
    ArrayList<Map<String,Object>> getSiteOut();

    @Select("select state from t_site")
    Integer getState(String site);

    @Select("select site,state,room_num,period from t_site where site = #{site}")
    ArrayList<Map<String, Object>> selectPeriod(String site);
}
