package cn.homyit.controller;

import cn.homyit.domain.Result;
import cn.homyit.dto.BlogDto;
import cn.homyit.dto.GetBlogsDto;
import cn.homyit.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage/blogaudit")
@PreAuthorize("@jm.admin")
public class BlogController {
    @Autowired
    ArticleService articleService;
    @DeleteMapping("/delete/{id}")
    public Result deleteIllegalBlog(@PathVariable int id){
        return articleService.deleteArticle(id);
    }

    @PostMapping("/getblog")
    public Result getBlog(@RequestBody GetBlogsDto blogDto){

        return articleService.getBlogs(blogDto);
    }

}
