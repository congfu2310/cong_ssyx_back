package com.atguigu.ssyx.common.result;

import lombok.Data;

@Data
public class Result<T> {

//Result<T> 是一个支持泛型的类，意味着这个类可以用于封装任意类型的数据返回结果。T 代表具体的数据类型。

    //状态码
    private Integer code;
    //信息
    private String message;
    //数据
    private T data;

    //构造私有化
    //构造方法 private Result() 被私有化，防止外部直接创建 Result 对象
    // 这个设计模式称为 工厂模式，通过静态方法来创建 Result 对象，而不是直接使用 new 关键字
    private Result() {
    }

    //设置数据,返回对象的方法
    public static <T> Result<T> build(T data, ResultCodeEnum resultCodeEnum) {
        //创建Resullt对象，设置值，返回对象
        Result<T> result = new Result<>();
        //判断返回结果中是否需要数据
        if (data != null) {
            //设置数据到result对象
            result.setData(data);
        }
        //设置其他值
        result.setCode(resultCodeEnum.getCode());
        result.setMessage(resultCodeEnum.getMessage());
        //返回设置值之后的对象
        return result;
    }

    //成功的方法
    public static <T> Result<T> ok(T data) {
        Result<T> result = build(data, ResultCodeEnum.SUCCESS);
        return result;
    }

    //失败的方法
    public static <T> Result<T> fail(T data) {
        return build(data, ResultCodeEnum.FAIL);
    }
}
