package cn.homyit.dao;

import cn.homyit.vo.Social;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.ArrayList;
import java.util.Map;

@Mapper
public interface SocialDao extends BaseMapper<Social> {




    @Select("SELECT id, name, picture, introduction, president, time, " +
            "(SELECT COALESCE(COUNT(tsm.social_name), 0) FROM t_social_member tsm WHERE tjs.name = tsm.social_name) AS count " +
            "FROM t_club tjs " +
            "WHERE tjs.name = #{name} ")
    ArrayList<Map<String, Object>> selectInfo(String name);


    /**
     * 查找名字和头像和成员数量
     *
     * @return
     */
    @Select("SELECT tc.name, tc.picture, COALESCE(COUNT(tsm.social_name), 0) AS count \n" +
            "FROM t_club tc  \n" +
            "LEFT JOIN t_social_member tsm ON tc.name = tsm.social_name \n" +
            "GROUP BY tc.name, tc.picture  \n" +
            "LIMIT #{pageSize} OFFSET #{offset};")
    ArrayList<Map<String, Object>> selectSum(int pageSize, int offset);


    @Select("SELECT  COUNT(*) FROM t_club")
    Long count();

    @Select("SELECT id,real_name FROM t_userCampus  where type = 2;")
    ArrayList<Map<String, Object>> reviewerByType2();

    @Select("SELECT COUNT(*) FROM t_club WHERE name = #{name};")
    Integer ifHave(String name);

    @Select("select tsm.social_name,tc.picture from t_social_member tsm,t_club tc where tsm.name = #{name} and tsm.member_id = #{memberId} ")
    ArrayList<Map<String, Object>> myClub(String name, Integer memberId); //通过用户名查找社团
}