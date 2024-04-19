package cn.homyit.service;

import cn.homyit.domain.Result;
import cn.homyit.dto.ApplicationDto;
import cn.homyit.dto.GetAppDto;
import cn.homyit.utils.RedisCache;
import org.springframework.stereotype.Service;

@Service
public interface ApplicationService  {

    public Result audit(ApplicationDto applicationDto);
    public Result getApp(GetAppDto getAppDto);
}
