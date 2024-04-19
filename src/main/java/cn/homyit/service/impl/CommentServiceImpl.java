package cn.homyit.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import cn.homyit.domain.Comments;
import cn.homyit.domain.Result;
import cn.homyit.dao.CommentMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl {

    @Autowired
    private CommentMapper commentsMapper;


    private final RabbitTemplate rabbitTemplate;
    /**
     *  查找文章评论
     */

    public List<Comments> listCommentByArticleId(Integer ArticleId) {
        QueryWrapper<Comments> wrapper = new QueryWrapper<Comments>().eq("article_id", ArticleId).orderByDesc("create_time");
        wrapper.select("id", "nickname", "content", "create_time",  "parent_id", "article_id");
        List<Comments> comments = commentsMapper.selectList(wrapper);
        return firstComment(comments);
    }




    public List<Comments> firstComment(List<Comments> comments) {
        //存储父评论为根评论-1的评论
        ArrayList<Comments> list = new ArrayList<>();
        for (Comments comment : comments) {
            //其父id等于-1则为第一级别的评论
            if (comment.getParentId() == -1) {
                //我们将该评论下的所有评论都查出来
                comment.setReplyComments(findReply(comments, comment.getId()));
                //这就是我们最终数组中的Comment
                list.add(comment);
            }
        }
        return list;
    }

    /**
     * @param comments 我们所有的该博客下的评论
     * @param targetId 我们要查到的目标父id
     * @return 返回该评论下的所有评论
     */
    public List<Comments> findReply(List<Comments> comments, int targetId) {
        //第一级别评论的子评论集合
        ArrayList<Comments> reply = new ArrayList<>();
        for (Comments comment : comments) {
            //发现该评论的父id为targetId就将这个评论加入子评论集合
            if (find(comment.getParentId(), targetId)) {
                reply.add(comment);
            }
        }
        return reply;
    }

    /**
     *  递归查找子评论的父评论
     *
     *
     * @param id
     * @param target  目标父id
     * @return
     */
    public boolean find(int id, int target) {
        //不将第一节评论本身加入自身的子评论集合
        if (id == -1) {
            return false;
        }
        //如果父id等于target，那么该评论就是id为target评论的子评论
        if (id == target) {
            return true;
        } else {
            //否则就再向上找
            return find(commentsMapper.selectById(id).getParentId(), target);
        }
    }

    @Transactional
    public Result save(Comments comments) {
        //用户评论存储操作
        commentsMapper.insert(comments);

        //把评论发送到MQ队列
        rabbitTemplate.convertAndSend("emailQueue",comments);

        return new Result(comments);
    }
}
