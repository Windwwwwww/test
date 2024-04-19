package cn.homyit.controller;

import cn.homyit.dto.ManagerDto;
import cn.homyit.domain.Result;
import cn.homyit.dto.ManagerSelectPageDto;
import cn.homyit.service.ManagerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/manage/manager")
@PreAuthorize("@jm.admin")
public class ManagerController {
    @Autowired
    private ManagerService managerService;
    @PutMapping("/update")
    @PreAuthorize("@jm.super")
    public Result updateManager(@RequestBody ManagerDto managerDto){
        return managerService.updateManager(managerDto);

    }

    @PostMapping("/add")
    @PreAuthorize("@jm.super")
    public Result addManager(@RequestBody ManagerDto managerDto){
        return managerService.updateManager(managerDto);
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("@jm.super")
    public Result delManager(@PathVariable int id){
        return managerService.delManager(id);
    }
    @PostMapping("/select")
    public Result seleteManagers(@RequestBody ManagerSelectPageDto managerSelectPageDto){
        Result re=managerService.seleteManager(managerSelectPageDto);
        return managerService.seleteManager(managerSelectPageDto);
    }


}
