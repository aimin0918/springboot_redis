package com.springboot_redis.controller;

import com.springboot_redis.model.User;
import com.springboot_redis.service.UserService;
import org.apache.poi.hssf.usermodel.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;


/**
 * @author Aimin
 * @Description: 业务模块流程的控制
 * @ClassName UserController
 * @date 2023/4/10
 */
@RestController
public class UserController {

    @Autowired
    private UserService userService;


    @PostMapping("/insert")
    public @ResponseBody Map<String, Object> add(User user) {
        Map<String, Object> addMap = userService.add(user);
        return addMap;
    }

    @GetMapping("/select")
    public @ResponseBody Map<String, Object> getById(String id) {
        Map<String, Object> getMap = userService.getById(id);
        return getMap;
    }

    @PostMapping("/update")
    public @ResponseBody Map<String, Object> update(User user) {
        Map<String, Object> updateMap = userService.update(user);
        return updateMap;
    }

    @PostMapping("/delete")
    public @ResponseBody Map<String, Object> delete(String id) {
        Map<String, Object> deleteMap = userService.del(id);
        return deleteMap;
    }


    @PostMapping("/zeng")
    public @ResponseBody Object addUser(User user) {
        return userService.addUser(user);
    }

    @GetMapping("/cha")
    public @ResponseBody Object selectUser(String id) {
        return userService.getId(id);
    }
    @GetMapping("/chaAll")
    public @ResponseBody Object selectUserByAll(Integer page, Integer pageSize) {
        return userService.getByAll(page, pageSize);
    }

    @PostMapping("/gai")
    public @ResponseBody Object updateUser(User user) {
        return userService.updateUser(user);
    }

    @PostMapping("/shan")
    public @ResponseBody Object deleteUser(String id) {
        return userService.deleteUser(id);
    }


    @GetMapping("/userExcel")
    public void downloadAllUser(HttpServletResponse response) throws IOException {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet("用户表");

        List<User> userList = userService.userExportExcel();

        //设置要导出的文件的名字
        String fileName = "user" + ".xls";
        //新增数据行，并且设置单元格数据

        int rowNum = 1;

        String[] headers = {"编号", "姓名", "年龄", "密码"};
        //headers表示excel表中第一行的表头

        HSSFRow row = sheet.createRow(0);
        //在excel表中添加表头

        for (int i = 0; i < headers.length; i++) {
            HSSFCell cell = row.createCell(i);
            HSSFRichTextString text = new HSSFRichTextString(headers[i]);
            cell.setCellValue(text);
        }

        //在表中存放查询到的数据放入对应的列
        for (User user : userList) {
            HSSFRow row1 = sheet.createRow(rowNum);
            row1.createCell(0).setCellValue(user.getId());
            row1.createCell(1).setCellValue(user.getName());
            row1.createCell(2).setCellValue(user.getAge());
            row1.createCell(3).setCellValue(user.getPassword());
            rowNum++;
        }

        response.setContentType("application/octet-stream");
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);
        response.flushBuffer();
        workbook.write(response.getOutputStream());
    }

    @PostMapping("/import")
    public @ResponseBody boolean addUser(MultipartFile file) {
        boolean a = false;
        String fileName = file.getOriginalFilename();
        try {
            a = userService.batchImport(fileName, file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return a;

    }

}
