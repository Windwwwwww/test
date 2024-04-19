package cn.homyit.controller;

import cn.homyit.domain.Comments;
import cn.homyit.domain.Result;
import cn.homyit.service.impl.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentsController {

    @Autowired
    private CommentServiceImpl commentServiceImpl;
    /**
     * 发表评论
     */
    @ResponseBody
    @PostMapping("/post")
    public Result comments(@RequestBody Comments comments) throws Exception {

        if (comments.getParentId() == -1) {
            comments.setSort(1);
        } else {
            comments.setSort(Integer.parseInt(String.valueOf(System.currentTimeMillis() / 990)));
        }
//        暂时可见
//        comments.setIsVisible("1");
//        设置父节点id，-1为首节点
//        return commentServiceImpl.save(comments);
//        数值越大则优先展示
        return commentServiceImpl.save(comments);

    }

    @ResponseBody
    @GetMapping("/list")
    public Result listComments(@RequestParam Integer id){
        List<Comments> comments = commentServiceImpl.listCommentByArticleId(id);
        return new Result(comments);
    }

}
