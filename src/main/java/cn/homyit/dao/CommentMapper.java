package cn.homyit.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import cn.homyit.domain.Comments;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

@Mapper
public interface CommentMapper extends BaseMapper<Comments> {

    @Select("SELECT a.author FROM t_article a JOIN comment c ON a.id = c.article_id where c.article_id = #{id}")
    String selectAuthor(Integer id);
}
