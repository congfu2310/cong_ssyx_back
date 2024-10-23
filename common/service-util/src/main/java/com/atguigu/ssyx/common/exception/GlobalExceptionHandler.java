package com.atguigu.ssyx.common.exception;

import com.atguigu.ssyx.common.result.Result;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice //集中管理，不必在每个控制器中重复代码
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)  //异常处理器
    @ResponseBody //返回json数据
    public Result error(Exception e) {
        e.printStackTrace();
        return Result.fail(null);
    }


    /**
     * 自定义异常处理方法
     */
    @ExceptionHandler(SsyxException.class)
    @ResponseBody
    public Result error(SsyxException exception) {
        return Result.fail(null);
    }
}
