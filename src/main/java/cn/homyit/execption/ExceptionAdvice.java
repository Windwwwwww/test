package cn.homyit.execption;

import cn.homyit.domain.Result;
import cn.homyit.utils.WebUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @param:
 * @description:
 * @author: Answer
 * @create:2024/3/27 11:00
 **/
@ControllerAdvice(annotations = Controller.class)
public class ExceptionAdvice {
    private static final Logger logger = LoggerFactory.getLogger(ExceptionAdvice.class);


    @ExceptionHandler(Exception.class)
    public void handleException(Exception exception, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (exception instanceof AccessDeniedException || exception instanceof  AuthenticationException ) {
            // 不处理AccessDeniedException和AuthenticationException
            throw exception;
        }


//        if (exception instanceof  RuntimeException){
//            WebUtils.resultToJsonResponse(response, Result.fail(401,exception.getMessage()));
//        }else {
        else  WebUtils.resultToJsonResponse(response, Result.fail(664, "未知错误"));
//        }
        // 打印异常日志
        logger.error(exception.getMessage());
        StackTraceElement[] stackTrace = exception.getStackTrace();
        for (StackTraceElement element : stackTrace) {
            logger.error(element.toString());
        }
    }


}

