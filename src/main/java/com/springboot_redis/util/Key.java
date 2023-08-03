package com.springboot_redis.util;

import org.springframework.stereotype.Repository;

/**
 * @author Aimin
 * @Description: key值
 * @ClassName key
 * @date 2023/8/3
 */
@Repository
public class Key {
    public Object user(String id) {
        String key = "user" + id;
        return key;
    }
}
