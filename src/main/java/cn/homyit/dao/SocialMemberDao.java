package cn.homyit.dao;

import cn.homyit.vo.SocialMem;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface SocialMemberDao extends BaseMapper<SocialMem> {
    @Select("select name ,picture from t_social_member where social_name=#{socialName}")
    ArrayList<Map<String,Object>> selectMembers(String socialName);
}
