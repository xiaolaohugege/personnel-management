package com.example.controller;
import com.example.common.Result;
import com.example.entity.Account;
import com.example.entity.Apply;
import com.example.mapper.ApplyMapper;
import com.example.service.ApplyService;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/apply")
public class ApplyController {

    @Autowired
    private ApplyService applyService;

    @Autowired
    private ApplyMapper applyMapper;

    @RequestMapping("/getListApply")
    public List<Apply> getListApply() {
        return applyService.getListApply();
    }

    /**
     * 分页查询
     * pageNum: 当前的页码
     * pageSize：每页的个数
     */
    @RequestMapping("/selectPage")
    public Result selectPage(Integer pageNum, Integer pageSize, Apply apply) {
        Account currentUser = TokenUtils.getCurrentUser(); // 获取当前用户信息
        // 如果当前用户是管理员，查询所有用户的申请
        if ("ADMIN".equals(currentUser.getRole())) {
            // 管理员查询所有数据
            apply.setUserId(null); // 确保管理员查询所有用户的申请

            System.out.println("管理员查询，设置 user_id 为 null");
        } else {
            // 普通用户只能查询自己的申请记录
            apply.setUserId(currentUser.getId());
        }

        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<Apply> applyList = applyMapper.getListApply(apply);
        return Result.success(applyList);
    }

    @RequestMapping("/addApply")
    public Result addApply(@RequestBody Apply apply){
        applyService.addApply(apply);
        return Result.success();
    }

    @RequestMapping("/update")
    public Result update(@RequestBody Apply apply){
        applyService.update(apply);
        return Result.success();
    }

    @RequestMapping("/deleteApplyById/{id}")
    public Result deleteApplyById(@PathVariable int id){
        applyService.deleteApplyById(id);
        return Result.success();
    }

    @RequestMapping("/deleteBatch")
    public Result deleteBatch(@RequestBody List<Apply> list) {  //  @RequestBody 接收前端传来的 json数组
        applyService.deleteBatch(list);
        return Result.success();
    }



}
