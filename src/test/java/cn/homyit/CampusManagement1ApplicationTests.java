package cn.homyit;

import cn.homyit.dao.ArticleDao;
import cn.homyit.dao.SocialDao;
import cn.homyit.dao.SocialMemberDao;
import cn.homyit.dao.UserDao;
import cn.homyit.domain.Article;
import cn.homyit.dto.SelectDto;
import cn.homyit.dto.SelectSiteDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootTest
class CampusManagement1ApplicationTests {
    @Autowired
    private SocialDao socialDao;

    @Autowired
    private ArticleDao articleDao;
    @Test
    void contextLoads() {
        Article article = articleDao.selectById(27);
        System.out.println(article);

    }

}
