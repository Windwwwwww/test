package cn.homyit.service.impl;

import cn.homyit.dao.ArticleDao;
import cn.homyit.dao.UserDao;
import cn.homyit.domain.*;
import cn.homyit.domain.Enum.Code;
import cn.homyit.dto.ArticleDto;
import cn.homyit.dto.BlogDto;
import cn.homyit.dto.GetBlogsDto;
import cn.homyit.execption.SystemException;
import cn.homyit.service.ArticleService;
import cn.homyit.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @param:
 * @description:
 * @author: Answer
 * @create:2024/3/17 18:29
 **/
@Slf4j
@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleDao articleDao;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private UserDao userDao;

    @Override
    public Result publicationArticle(ArticleDto articleDto) {
        Authentication authentication = SecurityUtils.getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User user = loginUser.getUser();
        Article article = new Article();
        String images = FileUtil.fileUpload(articleDto.getFiles());
        article.setTitle(articleDto.getTitle());
        article.setTags(articleDto.getTags());
        article.setContent(articleDto.getContent());
        article.setImages(images);
        article.setAuthor(user.getUserName());
        article.setAuthorId(user.getId());
        article.setAuthorImage(userDao.selectImage(user.getId()));
        article.setPublicationTime(LocalDateTime.now());
        article.setUpdateTime(LocalDateTime.now());
        int insert = articleDao.insert(article);
        return insert > 0 ? Result.success("发表成功") : Result.fail(664,"当前网络不佳,请稍后重试");
    }

    @Override
    public Result selectByArticleName(String articleName,Page<Article> page) {
        Authentication authentication = SecurityUtils.getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        if (articleName == null){
            return Result.fail(664,"请确认是否输入正确");
        }
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        lqw.like(Article::getTitle,articleName);
//        List<Article> articles = articleDao.selectList(lqw);
        Page<Article> articles = articleDao.selectPage(page, lqw);
        articles.setRecords(articles.getRecords().stream()
                .peek(article -> article.setLikesParam(redisCache.getCacheObject(article.getTitle() + "用户id"  + loginUser.getUser().getId() + ":")))
                .sorted((a1, a2) -> a2.getLikes() - a1.getLikes()).collect(Collectors.toList()));
//        articles.setRecords(articles.getRecords().stream())
        if (articles.getRecords().isEmpty()){
            return Result.fail(664,"无相关文章,请重试");

        }
//        redisCache.setCacheList("关键词：" + articleName + "",articles.getRecords(),30,TimeUnit.MINUTES);
        return Result.success(articles);
    }

    @Override
    public Result updateLikes(Integer articleId) {
        Authentication authentication = SecurityUtils.getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
//        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        Article article = articleDao.selectById(articleId);
        Integer likes = article.getLikes();
        Integer id = loginUser.getUser().getId();
        Integer value = redisCache.getCacheObject(article.getTitle() + "用户id"  + id + ":");
        System.out.println(value);
        if (value == 0) {
            redisCache.setCacheObject(article.getTitle() + "用户id"  + id + ":", 1);
            article.setLikes(likes - 1);
            int i = articleDao.updateById(article);
            return i > 0 ? Result.success() : Result.fail(664, "当前网络不佳,请稍后重试");
        }
        article.setLikes(likes + 1);
        redisCache.setCacheObject(article.getTitle() + "用户id" + id + ":",0);
        int i = articleDao.updateById(article);
        return i > 0 ? Result.success() : Result.fail(664, "当前网络不佳,请稍后重试");
    }

    @Override
    public Result deleteLikes(Integer articleId) {
        int i = articleDao.deleteById(articleId);
        return i > 0 ? Result.success() : Result.fail(664,"当前网络不佳，请稍后重试");
    }
    //获取个人所有文章
    @Override
    public Result getAll(Page<Article> page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Article::getAuthorId,loginUser.getUser().getId());
        lqw.orderByDesc(Article::getPublicationTime);
        //分页需要总数据条数，当前页，一页数据显示条数，
        //总数据数
        Page<Article> articlePage = articleDao.selectPage(page, lqw);
        articlePage.setRecords(articlePage.getRecords().stream()
                .peek(article -> article.setLikesParam(redisCache.getCacheObject(article.getTitle() + "用户id"  + loginUser.getUser().getId() + ":")))
                .collect(Collectors.toList()));
        return Result.success(articlePage);
    }

    //获取全部文章按照标签分类
    @Override
    public Result all(Page<Article> page) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Page<Article> articlePage0 = getArticlePage(page, loginUser,"美食");
        Page<Article> articlePage1 = getArticlePage(page, loginUser,"选课");
        Page<Article> articlePage2 = getArticlePage(page, loginUser,"失物招领");
        Page<Article> articlePage3 = getArticlePage(page, loginUser,"社团");
        Page<Article> articlePage4 = getArticlePage(page, loginUser,"其他");
        Map<String, Page<Article>> map = new HashMap<>();
        map.put("美食",articlePage0);
        map.put("选课",articlePage1);
        map.put("失物招领",articlePage2);
        map.put("社团",articlePage3);
        map.put("其他",articlePage4);
        return Result.success(map);
    }

    //根据标签查
    public  Page<Article> getArticlePage(Page<Article> page, LoginUser loginUser,String tag) {
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Article::getTags,tag);
        Page<Article> articlePage = articleDao.selectPage(page, lqw);
        articlePage.setRecords(articlePage.getRecords().stream()
                .peek(article -> article.setLikesParam(redisCache.getCacheObject(article.getTitle() + "用户id"  + loginUser.getUser().getId() + ":")))
                .sorted((a1,a2) ->a2.getLikes() - a1.getLikes()).collect(Collectors.toList()));
        return articlePage;
    }

    @Override
    public Result updateArticle(ArticleDto articleDto) {
        LambdaQueryWrapper<Article> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Article::getId,articleDto.getId());
        Article article = articleDao.selectOne(lqw);
        if (articleDto.getTitle() != null){
            article.setTitle(articleDto.getTitle());
        }
        if (articleDto.getTags() != null){
            article.setTags(articleDto.getTags());
        }
        if (articleDto.getContent() != null){
            article.setContent(articleDto.getContent());
        }
        if (articleDto.getFiles() != null){
            String urlAll = articleDao.selectImagesById(articleDto.getId());
            String[] urls = ProcessDataUtil.stringToList(urlAll);
            Arrays.stream(urls)
                    .map(url -> new File("/home/answer/ui/images/", ProcessDataUtil.getFileName(url)))
                    .forEach(file -> {
                        if (file.delete()) {
                            System.out.println("删除成功;");
                        } else {
                            System.out.println("文件源损坏");
                        }
                    });
            String s = FileUtil.fileUpload(articleDto.getFiles());
            article.setImages(s);
        }
        article.setUpdateTime(LocalDateTime.now());
        int i = articleDao.updateById(article);
        return i > 0 ? Result.success("修改成功") : Result.fail(664,"修改失败");
    }

    @Override
    public Result selectById(Integer id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Article article = articleDao.selectById(id);
        Object cacheObject = redisCache.getCacheObject(article.getTitle() + "用户id" + loginUser.getUser().getId() + ":");
        if (cacheObject== null){
        article.setLikesParam(0);
        }
        else {
            article.setLikesParam((Integer) cacheObject);
        }
        return Result.success(article);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class})
    public Result deleteArticle(int id) {
        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        lambdaQueryWrapper.eq(Article::getId,id);
        if(articleDao.delete(lambdaQueryWrapper)==0){
            throw new SystemException(Code.DELETE_ERR.getStateNum(),Code.SYSTEM_ERR.getMsg());
        }
        return new Result(Code.DELETE_OK.getStateNum(),Code.DELETE_OK.getMsg());
    }

    @Override
    @Transactional(readOnly = true,rollbackFor = {Exception.class})
    public Result getBlogs(GetBlogsDto blogDto) {
        //LambdaQueryWrapper<Article> lambdaQueryWrapper= CreateWrapper.createWrapper(blogDto.getBlogDto(),Article.class);
        LambdaQueryWrapper<Article> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        BlogDto b=blogDto.getBlogDto();
        if(b.getBlogId()!=0){
            lambdaQueryWrapper.eq(Article::getId,b.getBlogId());
        }else if(b.getBlogTitle()!=null&&!b.getBlogTitle().isEmpty()){
            lambdaQueryWrapper.like(Article::getTitle,b.getBlogTitle());
        }else if(b.getAudience()!=null&&!b.getAudience().isEmpty()){
            lambdaQueryWrapper.like(Article::getAuthor,b.getAudience());
        }else if(b.getAudienceId()!=0){
            lambdaQueryWrapper.like(Article::getAuthorId,b.getAudienceId());
        }
        Page<Article> iPage=new Page<>(blogDto.getPageNum(),30);
        Page<Article> iPage1=articleDao.selectPage(iPage,lambdaQueryWrapper);
        return new Result(Code.SELETE_OK.getStateNum(), Code.SELETE_OK.getMsg());

    }


}
