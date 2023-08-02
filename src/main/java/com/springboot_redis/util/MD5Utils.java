package com.springboot_redis.util;

import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.util.UUID;

/**
 * @author Aimin
 * @Description: MD5加密
 * @ClassName MD5Utils
 * @date 2023/4/18
 */
public class MD5Utils {

    public static String getPWD( String strs){
        /*
         * 加密需要使用JDK中提供的类
         */
        StringBuffer sb = new StringBuffer();
        try{
            MessageDigest digest = MessageDigest.getInstance("MD5");

            byte[] bs = digest.digest(strs.getBytes());

            /*
             *  加密后的数据是-128 到 127 之间的数字，这个数字也不安全。
             *   取出每个数组的某些二进制位进行某些运算，得到一个具体的加密结果
             *
             *   0000 0011 0000 0100 0010 0000 0110 0001
             *  &0000 0000 0000 0000 0000 0000 1111 1111
             *  ---------------------------------------------
             *   0000 0000 0000 0000 0000 0000 0110 0001
             *   把取出的数据转成十六进制数
             */

            for (byte b : bs) {
                int x = b & 255;
                String s = Integer.toHexString(x);
                if( x > 0 && x < 16 ){
                    sb.append("0");
                    sb.append(s);
                }else{
                    sb.append(s);
                }
            }

        }catch( Exception e){
            System.out.println("加密失败");
        }
        return sb.toString();
    }

    public static String encode(String rawPassword) {
        // 加密过程
        // 1. 使用MD5算法
        // 2. 使用随机的盐值
        // 3. 循环5次
        // 4. 盐的处理方式为：盐 + 原密码 + 盐 + 原密码 + 盐
        // 注意：因为使用了随机盐，盐值必须被记录下来，本次的返回结果使用$分隔盐与密文
        String salt = UUID.randomUUID().toString().replace("-", "");
        String encodedPassword = rawPassword;
        for (int i = 0; i < 5; i++) {
            encodedPassword = DigestUtils.md5DigestAsHex(
                    (salt + encodedPassword + salt + encodedPassword + salt).getBytes());
        }
        return salt + encodedPassword;
    }

    public boolean matches(String rawPassword, String encodedPassword) {
        String salt = encodedPassword.substring(0, 32);
        String newPassword = rawPassword;
        for (int i = 0; i < 5; i++) {
            newPassword = DigestUtils.md5DigestAsHex(
                    (salt + newPassword + salt + newPassword + salt).getBytes());
        }
        newPassword = salt + newPassword;
        return newPassword.equals(encodedPassword);
    }


}
