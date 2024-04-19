package cn.homyit.controller;

import cn.homyit.domain.Page;
import cn.homyit.domain.Result;
import cn.homyit.dto.ArticleDto;
import cn.homyit.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * @param:
 * @description:
 * @author: Answer
 * @create:2024/3/17 18:24
 **/
@RestController
@RequestMapping("/article")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @PostMapping("/post")
    public Result publication(@RequestParam("files")MultipartFile[] files,
                              @RequestParam("title")String title,
                              @RequestParam("content")String content,
                              @RequestParam("tags")String tags){
        return articleService.publicationArticle(new ArticleDto(files,title,content,tags));
    }
    //根据文章名称模糊查询
    @PostMapping("/select")
    public Result selectArticle(@RequestParam(value = "articleName") String articleName,
                                @RequestParam(value = "current",defaultValue = "1") Integer current,
                                @RequestParam(value = "size", defaultValue = "5") Integer size){
        return articleService.selectByArticleName(articleName,new Page<>(current,size));
    }

    @PostMapping("/updateLikes")
    public Result updateLikes(@RequestParam Integer articleId){
        return articleService.updateLikes(articleId);
    }

    @PostMapping("/delete")
    public Result deleteLikes(@RequestParam Integer articleId){
        return articleService.deleteLikes(articleId);
    }

    //获取个人所有文章
    @PostMapping("/getAll")
    public Result getAll(@RequestParam(value = "current",defaultValue = "1") Integer current,
                         @RequestParam(value = "size", defaultValue = "5") Integer size){
        return articleService.getAll(new Page<>(current,size));
    }

    @PostMapping("/all")
    public Result all(@RequestParam(value = "current",defaultValue = "1") Integer current,
                      @RequestParam(value = "size", defaultValue = "5") Integer size){
        return articleService.all(new Page<>(current,size));
    }

    //修改文章
    @PostMapping("/updateArticle")
    public Result updateArticle(@RequestParam("files")MultipartFile[] files,
                                @RequestParam("title")String title,
                                @RequestParam("content")String content,
                                @RequestParam("tags")String tags,
                                @RequestParam("id")Integer id){
        return articleService.updateArticle(new ArticleDto(files,id,title,content,tags));
    }

    //根据id查询文章
    @PostMapping("/selectById")
    public Result selectById(@RequestParam("id")Integer id){
        return articleService.selectById(id);
    }

}

