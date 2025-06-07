package com.example.controller;

import cn.hutool.core.util.StrUtil;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    /**
     * 分页查询
     * pageNum: 当前的页码
     * pageSize：每页的个数
     */
    @RequestMapping("/selectPage")
    public Result selectPage(@RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize,
                             User User) {
        PageInfo<User> pageInfo = userService.selectPage(pageNum, pageSize, User);
        return Result.success(pageInfo);  // 返回的是分页的对象
    }
    @GetMapping("/export")
    public void exportData(User user, HttpServletResponse response) throws Exception {
        String ids = user.getIds();
        if (StrUtil.isNotBlank(ids)){
            String[] idArr = ids.split(",");
            user.setIdsArr(idArr);
        }
        // 1. 拿到所有数据
        List<User> list = userService.getListUser(user);
        // 2. 构建Writer对象
        ExcelWriter writer = ExcelUtil.getWriter(true);
        // 3. 设置中文表头
        writer.addHeaderAlias("username", "账号");
        // 默认的，未添加alias的属性也会写出，如果想只写出加了别名的字段，可以调用此方法排除之
        writer.setOnlyAlias(true);
        // 4. 写出数据到writer
        writer.write(list);
        // 5. 设置输出的文件的名称以及输出流的头信息
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet;charset=utf-8");
        String fileName = URLEncoder.encode("用户信息", String.valueOf(StandardCharsets.UTF_8));
        response.setHeader("Content-Disposition", "attachment;filename=" + fileName + ".xlsx");
        // 6. 写出到输出流 并关闭 writer
        ServletOutputStream os = response.getOutputStream();
        writer.flush(os);
        writer.close();
        os.close();
    }


    @RequestMapping("/addUser")
    public Result addUser(@RequestBody User User) {
        userService.addUser(User);
        return Result.success();
    }

    @RequestMapping("/update")
    public Result update(@RequestBody User User) {
        userService.update(User);
        return Result.success();
    }

    @RequestMapping("/deleteUserById/{id}")
    public Result deleteUserById(@PathVariable int id) {
        userService.deleteUserById(id);
        return Result.success();
    }

    @RequestMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<User> list) {  //  @RequestBody 接收前端传来的 json数组
        userService.deleteBatch(list);
        return Result.success();
    }

    @RequestMapping("/register")
    public Result register(@RequestBody User user) {
        userService.register(user);
        return Result.success();
    }

    @RequestMapping("/login")
    public Result login(@RequestBody Account account) {
        Account dbAccount = null;
        if ("ADMIN".equals(account.getRole())) {
            dbAccount = adminService.login(account);
        } else if ("USER".equals(account.getRole())) {
            dbAccount = userService.login(account);
        } else {
            throw new CustomerException("非法请求");
        }
        return Result.success(dbAccount);

    }

    @RequestMapping("/updataSalary")
    public Result updataSalary(){
        userService.updataSalary();
        return Result.success();
    }



}

