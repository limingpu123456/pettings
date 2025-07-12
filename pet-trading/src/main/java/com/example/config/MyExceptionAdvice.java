package com.example.config;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;

//Spring Boot の共通例外(れいがい)ハンドリングを実装する（统一异常拦截器）
@ControllerAdvice
@ResponseBody
public class MyExceptionAdvice {

    @ExceptionHandler(Exception.class)
    public Object ExceptionAdvice(Exception e){
        HashMap<String,Object> result  = new HashMap<>();
        result.put("state",-1);
        result.put("msg","プログラムで例外が発生しました" + e.getMessage());
        return result;
    }
}
