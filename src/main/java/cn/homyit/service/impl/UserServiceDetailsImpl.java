package cn.homyit.service.impl;

import cn.homyit.dao.UserDao;
import cn.homyit.domain.LoginUser;
import cn.homyit.domain.User;
import cn.homyit.utils.StringToListUtils;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @param:
 * @description:
 * @author: Answer
 * @create:2024/3/13 21:53
 **/
@Service
public class UserServiceDetailsImpl implements UserDetailsService {

    @Autowired
    private UserDao userDao;

    @Override
    public UserDetails loadUserByUsername(String username) {
        LambdaQueryWrapper<User> userLambdaQueryWrapper = new LambdaQueryWrapper<>();
        userLambdaQueryWrapper.eq(User::getAccount,username);
        User user = userDao.selectOne(userLambdaQueryWrapper);
        //如果没查到用户，就抛出异常
        if (Objects.isNull(user)){
            throw new RuntimeException("用户名或密码错误");
        }
        String s = userDao.selectType(user.getId());
        List<String> list = StringToListUtils.stringToList(s);
        return new LoginUser(user,list);
    }
}

