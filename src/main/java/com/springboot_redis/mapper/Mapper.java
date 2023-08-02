package com.springboot_redis.mapper;

import com.springboot_redis.model.User;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Aimin
 * @Description: 对数据库进行数据持久化操作的接口
 * @ClassName mapper
 * @date 2023/4/10
 */
@Repository("mapper")
public interface Mapper {
    List<User> selectByAll(Integer page, Integer pageSize);

    /**
     * 查询
     *
     * @param id
     * @return
     */
    List<User> select(String id);


    /**
     * 添加
     *
     * @param user
     * @return
     */
    Integer add(User user);

    /**
     * 修改
     *
     * @param user
     * @return
     */
    Integer update(User user);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    Integer delete(String id);

    /**
     * 数据导出Excel
     *
     * @return
     */
    List<User> userExportExcel();

}
