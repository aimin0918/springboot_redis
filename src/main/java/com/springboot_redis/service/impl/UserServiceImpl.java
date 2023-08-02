package com.springboot_redis.service.impl;

import com.springboot_redis.mapper.Mapper;
import com.springboot_redis.model.User;
import com.springboot_redis.responseEntity.ServerResponse;
import com.springboot_redis.service.UserService;
import com.springboot_redis.util.ExceptionUtil.MyException;
import com.springboot_redis.util.MD5Utils;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author Aimin
 * @Description: 处理复杂的业务逻辑
 * @ClassName UserServiceImpl
 * @date 2023/4/10
 */

@Slf4j
@Service("UserService")
public class UserServiceImpl implements UserService {

    @Qualifier("mapper")
    @Autowired
    private Mapper mapper;

    @Autowired
    private RedisTemplate redisTemplate;


    @Override
    public Map<String, Object> add(User user) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String key = "user" + user.getId();
        List<User> users = mapper.select(user.getId());
        if (users.size() > 1) {
            dataMap.put("error", ServerResponse.badRequest());
            return dataMap;
        }
        user.setPassword(MD5Utils.getPWD(user.getPassword()));
        int num = mapper.add(user);
        redisTemplate.opsForValue().set(key, user, 300, TimeUnit.SECONDS);
        if (num > 0) {
            dataMap.put("success", ServerResponse.ok(user));
        } else {
            dataMap.put("error", ServerResponse.notFound());
        }
        return dataMap;
    }

    @Override
    public Map<String, Object> getById(String id) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String key = "user" + id;
        Object redisObj = redisTemplate.opsForValue().get(key);
        if (redisObj == null) {
            List<User> user = mapper.select(id);
            if (user.size() < 1) {
                dataMap.put("error", ServerResponse.badRequest());
                return dataMap;
            }
            redisTemplate.opsForValue().set(key, user, 300, TimeUnit.SECONDS);
            dataMap.put("success", ServerResponse.ok(user));
        } else {
            dataMap.put("success", ServerResponse.ok(redisObj));
        }
        return dataMap;
    }

    @Override
    public Map<String, Object> update(User user) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String key = "user" + user.getId();
        redisTemplate.delete(key);
        List<User> users = mapper.select(user.getId());
        if (users.size() < 1) {
            dataMap.put("error", ServerResponse.badRequest());
            return dataMap;
        }
        user.setPassword(MD5Utils.getPWD(user.getPassword()));
        int num = mapper.update(user);
        if (num > 0) {
            dataMap.put("success", ServerResponse.ok(user));

        } else {
            dataMap.put("error", ServerResponse.badRequest());
        }
        redisTemplate.opsForValue().set(key, user, 300, TimeUnit.SECONDS);
        return dataMap;
    }

    @Override
    public Map<String, Object> del(String id) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String key = "user" + id;
        List<User> users = mapper.select(id);
        if (users.size() < 1) {
            dataMap.put("error", ServerResponse.badRequest());
            return dataMap;
        }
        int num = mapper.delete(id);
        if (num > 0) {
            dataMap.put("success", ServerResponse.ok(users));
        } else {
            dataMap.put("error", ServerResponse.badRequest());
        }
        Object redisObj = redisTemplate.opsForValue().get(key);
        if (redisObj != null) {
            redisTemplate.delete(key);
        }
        return dataMap;
    }


    @Override
    public Object addUser(User user) {
        if (user.getId() == null || user.getName() == null || user.getAge() == null || user.getPassword() == null) {
            return ServerResponse.badRequest();
        }
        List<User> num = mapper.select(user.getId());
        if (num.size() > 1) {
            return ServerResponse.badRequest();
        }
        user.setPassword(MD5Utils.getPWD(user.getPassword()));
        String key = "user" + user.getId();
        Object users = mapper.add(user);
        if (users != null) {
            redisTemplate.opsForValue().set(key, user, 300, TimeUnit.SECONDS);
            return ServerResponse.ok(user);
        } else {
            return ServerResponse.badRequest();
        }
    }

    @Override
    public Object getId(String id) {
        if (id == null) {
            return ServerResponse.badRequest();
        }
        String key = "user" + id;
        Object redisObj = redisTemplate.opsForValue().get(key);
        if (redisObj == null) {
            List user = mapper.select(id);
            if (user.size() < 1) {
                return ServerResponse.notFound();
            }
            redisTemplate.opsForValue().set(key, user, 300, TimeUnit.SECONDS);
            return ServerResponse.ok(user);
        }
        return ServerResponse.ok(redisObj);
    }

    @Override
    public Object getByAll(Integer page, Integer pageSize) {
        if (page == null) {
            page = 1;
        }
        if (pageSize == null) {
            pageSize = 20;
        }
        List user = mapper.selectByAll(page, pageSize);
        return ServerResponse.ok(user);
    }

    @Override
    public Object updateUser(User user) {
        if (user.getId() == null || user.getName() == null || user.getAge() == null || user.getPassword() == null) {
            return ServerResponse.badRequest();
        }
        String key = "user" + user.getId();
        redisTemplate.delete(key);
        user.setPassword(MD5Utils.getPWD(user.getPassword()));
        Object users = mapper.update(user);
        if (users != null) {
            redisTemplate.opsForValue().set(key, user, 300, TimeUnit.SECONDS);
            return ServerResponse.ok(user);
        } else {
            return ServerResponse.badRequest();
        }
    }

    @Override
    public Object deleteUser(String id) {
        if (id != null) {
            return ServerResponse.badRequest();
        }
        String key = "user" + id;
        int users = mapper.delete(id);
        if (users != 0) {
            redisTemplate.delete(key);
            return ServerResponse.ok(users);
        } else {
            return ServerResponse.notFound();
        }
    }


    /**
     * 数据库导出Excel
     *
     * @return
     */
    @Override
    public List<User> userExportExcel() {
        return mapper.userExportExcel();
    }

    /**
     * Excel导入数据库
     *
     * @param fileName
     * @param file
     * @return
     * @throws Exception
     */
    @Override
    public boolean batchImport(String fileName, MultipartFile file) throws Exception {
        boolean notNull = false;
        List<User> userList = new ArrayList<User>();
        if (!fileName.matches("^.+\\.(?i)(xls)$") && !fileName.matches("^.+\\.(?i)(xlsx)&")) {
            throw new MyException("上传文件格式不正确");
        }
        boolean isExcel2003 = true;
        if (fileName.matches("^.+\\.(?i)(xlsx)$")) {
            isExcel2003 = false;
        }
        InputStream is = file.getInputStream();
        Workbook wb = null;
        /**
         * Excel2003以前的版本，扩展名是.xls，使用HSSFWordbook()
         * Excel2007之后的版本，扩展名是.xlsx，使用XSSFWorkbook()
         */
        if (isExcel2003) {
            wb = new HSSFWorkbook(is);
        } else {
            wb = new XSSFWorkbook(is);
        }
        //获取excel的第一页sheet页
        Sheet sheet = wb.getSheetAt(0);
        if (sheet != null) {
            notNull = true;
        }
        User user;
        //循环行数
        for (int r = 1; r <= sheet.getLastRowNum(); r++) {
            //获取sheet的第r行的数据
            Row row = sheet.getRow(r);
            if (row == null) {
                continue;
            }
            user = new User();

            /**
             * _NONE(-1)   none类型
             * NUMERIC(0)  数值类型
             * STRING(1)   字符串类型
             * FORMULA(2)  公式类型
             * BLANK(3)    空格类型
             * BOOLEAN(4)  布尔类型
             * ERROR(5);   错误
             */
            if (row.getCell(0) != null) {
                row.getCell(0).setCellType(CellType.STRING);
                user.setId(row.getCell(0).getStringCellValue());
            } else {
                throw new MyException("导入失败(第" + (r + 1) + "行,用户id未填写)");
            }
            if (row.getCell(1) != null) {
                row.getCell(1).setCellType(CellType.STRING);
                user.setName((row.getCell(1).getStringCellValue()));
            } else {
                throw new MyException("导入失败(第" + (r + 1) + "行,用户姓名未填写)");
            }
            if (row.getCell(2) != null) {
                row.getCell(2).setCellType(CellType.STRING);
                user.setAge(row.getCell(2).getStringCellValue());
            } else {
                throw new MyException("导入失败(第" + (r + 1) + "行,用户年龄未填写)");
            }
            if (row.getCell(3) != null) {
                row.getCell(3).setCellType(CellType.STRING);
                user.setPassword(row.getCell(3).getStringCellValue());
            } else {
                throw new MyException("导入失败(第" + (r + 1) + "行，用户密码未填写)");
            }
            userList.add(user);

        }

        for (User userResord : userList) {
            String id = userResord.getId();
            String key = "user" + id;
            List<User> cnt = mapper.select(id);
            if (cnt.size() < 1) {
                userResord.setPassword(MD5Utils.getPWD(userResord.getPassword()));
                mapper.add(userResord);
                redisTemplate.opsForValue().set(key, userResord, 300, TimeUnit.SECONDS);
            } else {
                userResord.setPassword(MD5Utils.getPWD(userResord.getPassword()));
                mapper.update(userResord);
                redisTemplate.delete(key);
                redisTemplate.opsForValue().set(key, userResord, 300, TimeUnit.SECONDS);
            }

        }
        return notNull;
    }

}
