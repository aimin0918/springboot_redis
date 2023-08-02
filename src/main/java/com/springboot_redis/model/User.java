package com.springboot_redis.model;

import lombok.Data;

/**
 * @author Aimin
 * @Description:
 * @ClassName User
 * @date 2023/4/10
 */
@Data
public class User {
    private static final long serialVersionUID = 1L;
    private String id;
    private String name;
    private String age;
    private String password;

}
