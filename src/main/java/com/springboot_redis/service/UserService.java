package com.springboot_redis.service;

import com.springboot_redis.model.User;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

/**
 * @author Aimin
 * @Description: 处理复杂的业务逻辑的接口
 * @ClassName UserService
 * @date 2023/4/10
 */
public interface UserService {

    /**
     * 添加
     *
     * @param user
     * @return
     */
    Map<String, Object> add(User user);

    /**
     * 查询
     *
     * @param id
     * @return
     */
    Map<String, Object> getById(String id);

    /**
     * 修改
     *
     * @param user
     * @return
     */
    Map<String, Object> update(User user);

    /**
     * 删除
     *
     * @param id
     * @return
     */
    Map<String, Object> del(String id);


    Object updateUser(User user);

    Object getId(String id);

    Object getByAll(Integer page, Integer pageSize);

    Object addUser(User user);

    Object deleteUser(String id);


    /**
     * 数据库导出Excel
     *
     * @return
     */
    List<User> userExportExcel();

    /**
     * Excel导入数据库
     *
     * @param fileName
     * @param file
     * @return
     * @throws Exception
     */
    boolean batchImport(String fileName, MultipartFile file) throws Exception;

}
