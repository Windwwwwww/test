package cn.homyit.service;

import cn.homyit.dao.MatchDao;
import cn.homyit.domain.MatchPageReq;
import cn.homyit.domain.PageResult;
import cn.homyit.domain.Result;
import cn.homyit.vo.Match;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MatchService {
    @Autowired
    private MatchDao matchDao;
    public Result selectMatch(MatchPageReq pr){
        // 创建分页对象
        IPage page = new Page(pr.getPageNo(), pr.getPageSize());

        // 创建查询条件
        LambdaQueryWrapper<Match> qw = new LambdaQueryWrapper<>();
        qw.like(null != pr.getLabel(),Match::getLabel,pr.getLabel())
                .like(null != pr.getName(),Match::getName,pr.getName());

        // 执行分页查询
        IPage matchPage = matchDao.selectPage(page, qw);


        // 构建返回结果
        PageResult<Match> pageResult = new PageResult<>();
        pageResult.setTotal(matchPage.getTotal());
        pageResult.setRecords(matchPage.getRecords());

        return Result.ok("查询成功",pageResult);
    }

    public Result addMatch(Match match){
        try {
            int success = matchDao.insert(match);
            if(success >0){
                return Result.ok("添加成功");
            }
            else {
                return Result.error("添加失败，请重试");
            }
        }catch (Exception e){
            return Result.error(e.getMessage());
        }

    }

}
