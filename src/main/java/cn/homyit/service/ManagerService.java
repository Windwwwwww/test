package cn.homyit.service;

import cn.homyit.dto.ManagerDto;
import cn.homyit.domain.Result;
import cn.homyit.dto.ManagerSelectPageDto;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

@Service
public interface ManagerService extends IService<ManagerDto> {

    public Result updateManager(ManagerDto managerDto);
    public Result delManager(int id);
    public Result seleteManager(ManagerSelectPageDto managerSelectPageDto);
}
