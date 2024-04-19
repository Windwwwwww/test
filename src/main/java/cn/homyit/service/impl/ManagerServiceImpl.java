package cn.homyit.service.impl;

import cn.homyit.dao.ManagerDao;
import cn.homyit.dao.UserDao;
import cn.homyit.domain.Enum.Code;
import cn.homyit.dto.ManagerDto;
import cn.homyit.domain.Result;
import cn.homyit.domain.User;
import cn.homyit.dto.ManagerSelectDto;
import cn.homyit.dto.ManagerSelectPageDto;
import cn.homyit.execption.SystemException;
import cn.homyit.service.ManagerService;
import cn.homyit.utils.CreateWrapper;
import cn.homyit.utils.LambdaUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional(readOnly =false,rollbackFor = {Exception.class})
public class ManagerServiceImpl extends ServiceImpl<ManagerDao, ManagerDto> implements ManagerService{
    @Autowired
    private ManagerDao managerDao;

    @Autowired
    private UserDao userDao;
    @Override
    public Result updateManager(ManagerDto managerDto){
        User user=new User(managerDto.getUserid(),managerDto.getType().getNum()+"");
        if(userDao.updateById(user)==0){
            throw new SystemException(Code.UPDATE_ERR.getStateNum(), Code.UPDATE_ERR.getMsg());
        }
        return new Result(Code.UPDATE_OK.getStateNum(),Code.UPDATE_OK.getMsg());
    }

  //TODO 批量操作记得写，添加管理员时需要把user表的权限也改掉（联表删除）

    @Override
    public Result delManager(int id) {
        User user=userDao.selectOne(new LambdaQueryWrapper<User>().eq(User::getId,id));
        if(user==null){
            throw new SystemException(Code.DELETE_ERR.getStateNum(), "没有在数据库中查找到该用户，请刷新重试");
        }else{
            user.setType(String.valueOf(0));//将用户权限设置为普通用户
            if(userDao.updateById(user)==0){
                throw new SystemException(Code.DELETE_ERR.getStateNum(), Code.DELETE_OK.getMsg());
            }
        }
        return new Result(Code.DELETE_OK.getStateNum(), Code.DELETE_OK.getMsg());
    }


    @Override
    @Transactional(readOnly =true,rollbackFor = {Exception.class})
    public Result seleteManager(ManagerSelectPageDto managerSelectPageDto) {
        Page<User> page=new Page<>(managerSelectPageDto.getPageNum(),30);
        //LambdaQueryWrapper<User> lambdaQueryWrapper= CreateWrapper.createWrapper(managerSelectPageDto.getManagerDto(), User.class);
        LambdaQueryWrapper<User> lambdaQueryWrapper=new LambdaQueryWrapper<>();
        ManagerSelectDto managerDto=managerSelectPageDto.getManagerDto();
        if(managerDto.getId()!=0){
            lambdaQueryWrapper.like(User::getId,managerDto.getId());
        }else if (managerDto.getUserName()!=null&&!managerDto.getUserName().isEmpty()){
            lambdaQueryWrapper.like(User::getUserName,managerDto.getUserName());
        }else if(managerDto.getWorkplace()!=null&&!managerDto.getWorkplace().isEmpty()){
            lambdaQueryWrapper.eq(User::getWorkplace,managerDto.getWorkplace());
        }else if(managerDto.getType()!=null&&!managerDto.getType().isEmpty()){
            lambdaQueryWrapper.eq(User::getType,managerDto.getType());
        }

        Page<User> iPage=userDao.selectPage(page, lambdaQueryWrapper);
        return new Result(Code.SELETE_OK.getStateNum(), Code.SELETE_OK.getMsg(),iPage);
    }

}
