package cn.homyit.service;

import cn.homyit.domain.Result;
import cn.homyit.domain.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @param:
 * @description:
 * @author: Answer
 * @create:2024/3/11 21:36
 **/
@Service
public interface UserService {

    Result login(User user, HttpServletRequest request);

    Result logout();

    Result register(User user);

    Result getPerson();

    Result updatePerson(User user);

    Result updatePassword(User user);

    Result updateProfileImage(MultipartFile file);

    Result getApplication();
    Result getProfileImage();
}
