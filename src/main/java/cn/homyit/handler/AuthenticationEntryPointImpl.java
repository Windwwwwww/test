package cn.homyit.handler;

import cn.homyit.domain.Result;
import cn.homyit.utils.WebUtils;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class AuthenticationEntryPointImpl implements AuthenticationEntryPoint {
    //认证失败
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                         AuthenticationException e) throws IOException, ServletException {
        String message = e.getMessage();
        System.out.println("message = " + message);
        Result result = null;
        if (message.equals("Bad credentials")||message.equals("用户名或密码错误")){
            result = Result.fail(409,"用户名或密码错误");
        }else {
            result = Result.fail(HttpStatus.UNAUTHORIZED.value(),"认证失败");
        }

        WebUtils.resultToJsonResponse(httpServletResponse,result);
    }
}
