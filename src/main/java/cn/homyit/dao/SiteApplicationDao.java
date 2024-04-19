package cn.homyit.dao;

import cn.homyit.domain.SiteApplication;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

import java.time.LocalDate;

@Mapper
public interface SiteApplicationDao extends BaseMapper<SiteApplication> {

    @Insert("insert into t_join_site(applicant_id,applicant,name,academy,period,date,reviewer,reviewer_id,purpose) values (#{appId},#{app},#{name},#{academy},#{period},#{localDate},#{reviewer},#{reviewerId},#{purpose})")
    int insertApplication(Integer appId,String app,String name, String academy, String period, LocalDate localDate, String reviewer, Integer reviewerId, String purpose);
}