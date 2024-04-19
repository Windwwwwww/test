package cn.homyit.service;

import cn.homyit.domain.Article;
import cn.homyit.domain.Page;
import cn.homyit.domain.Result;
import cn.homyit.dto.ArticleDto;
import cn.homyit.dto.BlogDto;
import cn.homyit.dto.GetBlogsDto;

/**
 * @param:
 * @description:
 * @author: Answer
 * @create:2024/3/17 18:23
 **/
public interface ArticleService {
    Result publicationArticle(ArticleDto articleDto);

    Result selectByArticleName(String articleName,Page<Article> page);
    Result updateLikes(Integer id);

    Result deleteLikes(Integer articleId);

    Result getAll(Page<Article> page);

    Result all(Page<Article> page);

    Result updateArticle(ArticleDto articleDto);

    Result selectById(Integer id);

    Result deleteArticle(int id);

    Result getBlogs(GetBlogsDto blogDto);
}
