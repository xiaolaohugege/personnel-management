package com.example.controller;

import cn.hutool.core.util.StrUtil;
import cn.hutool.poi.excel.ExcelReader;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.User;
import com.example.exception.CustomerException;
import com.example.service.AdminService;
import com.example.service.UserService;
import com.github.pagehelper.PageInfo;
import org.apache.ibatis.jdbc.Null;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Stream;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;
    private Object ServletOutput;

    @RequestMapping("/getListAdmin")
    public Result getListAdmin() {
        List<Admin> adminList = adminService.getListAdmin();
        return Result.success(adminList);
    }

    /**
     * 数据导出
     */

    @GetMapping("/export")
    public void exportData(Admin admin, HttpServletResponse response) throws Exception {
        String ids = admin.getIds();
        if (StrUtil.isNotBlank(ids)){
            String[] idArr = ids.split(",");
            admin.setIdsArr(idArr);
        }
        // 1. 拿到所有数据
        List<Admin> list = adminService.getListAdmin(admin);
        // 2. 构建Writer对象
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 3. 设置中文表头
        writer.addHeaderAlias("username", "账号");
        writer.addHeaderAlias("name", "名称");
        writer.addHeaderAlias("phone", "电话");
        writer.addHeaderAlias("email", "邮箱");
        // 默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);
        // 4. 写出数据到writer
        writer.write(list);
        // 5. 设置输出的文件的名称以及输出流的头信息
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("管理员信息", String.valueOf(StandardCharsets.UTF_8));
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        // 6. 写出到输出流 并关闭 writer
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os);
        writer.close();
        os.close();
    }

    /**
     * 批量导入
     */
    @PostMapping("/import")
    public Result importData(MultipartFile file) throws Exception {
        //  1. 拿到输入流 构建 reader
        InputStream inputStream = file.getInputStream();
        ExcelReader reader = ExcelUtil.getReader(inputStream);
        //  2. 通过Reader 读取 excel 里面的数据
        reader.addHeaderAlias("账号", "username");
        reader.addHeaderAlias("名称", "name");
        reader.addHeaderAlias("电话", "phone");
        reader.addHeaderAlias("邮箱", "email");
        List<Admin> list = reader.readAll(Admin.class);
        // 3. 将数据写到数据库
        for (Admin admin : list) {
            adminService.addAdmin(admin);
        }
        return Result.success();
    }

    @RequestMapping("/selectAdminById/{id}")
    public Result selectAdminById(@PathVariable Integer id) {
        Admin admin = adminService.selectAdminById(id);
        return Result.success(admin);
    }

    @RequestMapping("/addAdmin")
    public Result addAdmin(@RequestBody Admin admin){
        adminService.addAdmin(admin);
        return Result.success();
    }
    @PutMapping("/update")
    public Result update(@RequestBody Admin admin) {
        adminService.update(admin);
        return Result.success();
    }


    @RequestMapping("/deleteAdminById/{id}")
    public Result deleteAdminById(@PathVariable Integer id){
        int admin = adminService.deleteAdminById(id);
        return Result.success(admin);
    }

    @RequestMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Admin> list){
        adminService.deleteBatch(list);
        return Result.success();
    }
    /**
     * 分页查询
     * pageNum: 当前的页码
     * pageSize：每页的个数
     */
    @RequestMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             Admin admin) {
        PageInfo<Admin> pageInfo = adminService.selectPage(pageNum, pageSize, admin);
        return Result.success(pageInfo);  // 返回的是分页的对象
    }



    }
