package com.def.config;

import com.def.exception.BizException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(basePackages = "com.def.controller")
@Slf4j
public class MyExceptionHandler{

    @ExceptionHandler(BizException.class)
    @ResponseBody
    public JsonData parse(BizException e) {
        log.info("后端报错:{}", e.getMessage());
        return JsonData.buildError("错误编码" + e.getCode() + ":" + e.getMsg());
    }

}
