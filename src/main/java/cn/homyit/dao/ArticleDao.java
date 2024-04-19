package cn.homyit.dao;

import cn.homyit.domain.Article;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @param:
 * @description:
 * @author: Answer
 * @create:2024/3/17 18:05
 **/
@Mapper
public interface ArticleDao extends BaseMapper<Article> {

    @Select("select * from campus_management.t_article where id = #{id}")
    String selectImagesById(Integer id);
}
