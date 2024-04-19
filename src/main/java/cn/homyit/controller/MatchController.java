package cn.homyit.controller;

import cn.homyit.domain.MatchPageReq;
import cn.homyit.domain.Result;
import cn.homyit.service.MatchService;
import cn.homyit.vo.Match;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/match")
public class MatchController {


    @Autowired
    private MatchService matchService;

    @PostMapping("/addMatch")
    public Result addMatch(@RequestBody Match match){
        return matchService.addMatch(match);
    }



    @PostMapping("/selectMatch")
    public Result selectMatch(@RequestBody MatchPageReq pr){
        return matchService.selectMatch(pr);



    }
}
