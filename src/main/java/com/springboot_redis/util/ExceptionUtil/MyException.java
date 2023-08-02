package com.springboot_redis.util.ExceptionUtil;

/**
 * @author Aimin
 * @Description: 异常处理类
 * @ClassName MyException
 * @date 2023/4/13
 */
public class MyException extends Exception {
    public MyException(String message) {
        super(message);
    }
}
