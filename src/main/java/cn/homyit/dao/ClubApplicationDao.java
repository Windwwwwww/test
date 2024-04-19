package cn.homyit.dao;

import cn.homyit.domain.ClubApplication;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ClubApplicationDao extends BaseMapper<ClubApplication> {
    @Insert("insert into club_application(club_name, introduction, applicant,applicant_id,reviewer,reviewer_id,picture) values (#{name},#{introduction},#{president},#{president_id},#{reviewer},#{reviewer_id},#{file})")
    int insert(String name,String introduction,String president,Integer president_id,String reviewer,Integer reviewer_id,String file);
}
