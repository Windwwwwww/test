package cn.homyit.service.impl;

import cn.homyit.dao.UserDao;
import cn.homyit.domain.LoginUser;
import cn.homyit.domain.Result;
import cn.homyit.domain.User;
import cn.homyit.dto.SelectDto;
import cn.homyit.dto.SelectSiteDto;
import cn.homyit.service.UserService;
import cn.homyit.utils.*;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @param:
 * @description:
 * @author: Answer
 * @create:2024/3/11 21:38
 **/
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private MailServiceImpl mailService;

    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

    @Override
    public Result login(User user, HttpServletRequest request) {
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getAccount(), user.getPassword());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        //如果认证未通过给出提示
        if (Objects.isNull(authenticate)){
            return Result.fail(664,"登录失败");
        }
        //认证通过。使用userId生成jwt密钥
        LoginUser loginUser = (LoginUser) authenticate.getPrincipal();
        User user1 = loginUser.getUser();
        String userId = user1.getId().toString();
        String jwt = JwtUtil.createJWT(userId);
        Integer timeout = 15;
        TimeUnit timeUnit = TimeUnit.DAYS;
        Map<String, String> map = new HashMap<>();
        map.put("token",jwt);
        map.put("type",user1.getType());
//        map.put("time",LocalDateTime.now().toString());
//        System.out.println(LocalDateTime.now());
//        redisCache.setCacheObject(userId,map);
        redisCache.setCacheObject("campus_login:" + userId,loginUser,timeout,timeUnit);

        return new Result(200,"登陆成功",map);
    }

    @Override
    public Result logout() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Integer userId = loginUser.getUser().getId();
        redisCache.deleteObject("campus_login:" + userId);
        return Result.success("成功退出");
    }

    @Override
    public Result register(User user) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        String email = user.getEmail();
        if (!EmailValidator.isValidEmail(email)){
            return Result.fail(664,"请检查邮箱是否正确");
        }
        Integer i = userDao.selectCount(lqw.eq(User::getEmail, user.getEmail()));
        if (i == 0 ){
            Thread thread = new Thread(()->{
                boolean b = mailService.sendSimpleMail(user.getEmail(), user.getRealName());
                System.out.println("b = " + b);
            });
            thread.start();
            lqw.eq(User::getUserName,user.getUserName());
            User selectOne = userDao.selectOne(lqw);
            if (Objects.isNull(selectOne)){
                user.setType("0");
                user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
                user.setAccount(user.getEmail());
                int insert = userDao.insert(user);
                if (insert > 0){
                    return Result.success("注册成功，请留意查看邮箱");
                }
                return Result.fail(664,"注册失败");
            }
            return Result.fail(664,"用户名已经存在");
        }
        return Result.fail(664,"该邮箱已经注册过");
    }
    /*
    查询个人信息出去密码
     */
    @Override
    public Result getPerson() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        List<Map<String, String>> maps = userDao.selectPerson(loginUser.getUser().getId());
        return Result.success(maps);
    }

    @Override
    public Result updatePerson(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User realUser = userDao.selectById(loginUser.getUser().getId());
        if (user.getUserName() != null){
            realUser.setUserName(user.getUserName());
        }
        if (user.getEmail() != null){
            realUser.setEmail(user.getEmail());
        }
        if (user.getRealName() != null){
            realUser.setRealName(user.getRealName());
        }
        if (user.getWorkplace() != null){
            realUser.setWorkplace(user.getWorkplace());
        }
        int i = userDao.updateById(realUser);

        return  i > 0 ? Result.success("更新成功") : Result.fail(664,"更新失败请稍后重试");
    }

    @Override
    public Result updatePassword(User user) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User user1 = userDao.selectById(loginUser.getUser().getId());
        user1.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        int i = userDao.updateById(user1);
        return i > 0 ? Result.success("修改成功") : Result.fail(664,"修改失败，请稍后重试");
    }

    @Override
    public Result updateProfileImage(MultipartFile file) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        User user = userDao.selectById(loginUser.getUser().getId());
        String imageName = FileUtil.fileUpload(file);//接收文件名
//        User user1 = userDao.selectById(user.getId());
        String url = user.getImage();
        if (url != null){
            File file1 = new File("/home/answer/ui/images/", ProcessDataUtil.getFileName(url));
            boolean delete = file1.delete();
            if (delete){
                System.out.println("删除原头像成功");
            }else {
                System.out.println("头像源文件已损毁");
            }
        }
        user.setImage(imageName);
        int i = userDao.updateById(user);
        if (i > 0 ){
            SecurityContextHolder.clearContext();
        }
        Map<String, String> map = new HashMap<>();
        map.put("图片url路径",imageName);
        return i > 0 ? Result.success(map) : Result.fail(664,"修改失败，请稍后重试");
    }

    @Override
    public Result getApplication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Integer id = loginUser.getUser().getId();
        User user = userDao.selectById(id);
        String realName = user.getRealName();
        Map<String, List<SelectDto>> mapSocial = new HashMap<>();
        //社团申请查询
        List<SelectDto> selectSocials = userDao.selectByPresident(realName);
        mapSocial.put("0", selectSocials.stream()
                .filter(social -> social.getStatus().equals("0"))
                .collect(Collectors.toList()));
        mapSocial.put("1",selectSocials.stream()
                .filter(social -> social.getStatus().equals("1"))
                .collect(Collectors.toList()));
        mapSocial.put("2",selectSocials.stream()
                .filter(social -> social.getStatus().equals("2"))
                .collect(Collectors.toList()));

        Map<String, List<SelectSiteDto>> mapSite = new HashMap<>();
        //场地申请查询
        List<SelectSiteDto> selectSites = userDao.selectByApplicant(realName);
        mapSite.put("0",selectSites.stream()
                .filter(site -> site.getStatus().equals("0"))
                .collect(Collectors.toList()));
        mapSite.put("1",selectSites.stream()
                .filter(site -> site.getStatus().equals("1"))
                .collect(Collectors.toList()));
        mapSite.put("2",selectSites.stream()
                .filter(site -> site.getStatus().equals("2"))
                .collect(Collectors.toList()));

        Map<String, Object> map = new HashMap<>();
        map.put("社团申请",mapSocial);
        map.put("场地申请",mapSite);
        return Result.success(map);
    }

    @Override
    public Result getProfileImage() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Integer id = loginUser.getUser().getId();
        String url = userDao.selectProfile(id);
        return url != null ? Result.success(url) : Result.fail(664,"该用户还未上传头像，目前为默认头像");
    }


    //生成随机n位不重复的数字
    public static int generateUniqueRandomNumber(int digits) {
        Random random = new Random();
        Set<Integer> generatedNumbers = new HashSet<>();
        int min = (int) Math.pow(10, digits - 1);
        int max = (int) Math.pow(10, digits) - 1;

        int randomNum = random.nextInt(max - min + 1) + min;
        while (generatedNumbers.contains(randomNum)) {
            randomNum = random.nextInt(max - min + 1) + min;
        }
        generatedNumbers.add(randomNum);
        return randomNum;
    }
}
