package com.blog.exception;

import com.blog.utils.ResData;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.ShiroException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j // 日志
@RestControllerAdvice
public class MyExceptionResolver {

    // 没有访问权限异常 UnauthorizedException

    // 运行异常
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(RuntimeException.class)
    public ResData handler (RuntimeException e) {
        log.error("运行异常");
        return ResData.customRes(HttpStatus.INTERNAL_SERVER_ERROR.value(),e.getMessage(),null);
    }

    // shiro权限异常
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(ShiroException.class)
    public ResData handler (ShiroException e) {
        log.error("shiro权限异常");
        return ResData.customRes(HttpStatus.UNAUTHORIZED.value(),e.getMessage(),null);
    }

    // 前端传递过来的参数字段缺失异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResData handler (MethodArgumentNotValidException e) {
        log.error("实体类参数校验异常");

        // 如果有多个校验异常抛出第一个就行
        BindingResult bindingResult = e.getBindingResult();
        ObjectError objectError = bindingResult.getAllErrors().stream().findFirst().get();

        return ResData.customRes(HttpStatus.BAD_REQUEST.value(),objectError.getDefaultMessage(),null);
    }


    // 断言IllegalArgumentException异常
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(IllegalArgumentException.class)
    public ResData handler (IllegalArgumentException e) {
        log.error("前端参数传递异常");
        return ResData.customRes(HttpStatus.BAD_REQUEST.value(),e.getMessage(),null);
    }
}
