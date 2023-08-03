package com.springboot_redis;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SpringbootRedisApplicationTests {

    //同步锁(synchronized)
    public static synchronized String Toilet(String name){
        //上厕所5分钟
        for (int i=0;i<5;i++){
            //这里是为了方便观察才进行睡眠
            if (i==3){
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            System.out.println(name+"正在厕所");
        }
        return null;
    }

    public static void main(String[] args) {
        //在这里开了四个线程,去厕所
        new Thread(new Runnable() {
            @Override
            public void run() {
                Toilet("我");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Toilet("彭于晏");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Toilet("吴彦祖");
            }
        }).start();

        new Thread(new Runnable() {
            @Override
            public void run() {
                Toilet("胡歌");
            }
        }).start();
    }

    @Test
    void contextLoads() {
    }

}
