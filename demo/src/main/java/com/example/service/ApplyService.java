package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.Apply;
import com.example.exception.CustomerException;
import com.example.mapper.AdminMapper;
import com.example.mapper.ApplyMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplyService {

    @Autowired
    private ApplyMapper applyMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    public List<Apply> getListApply(){
        return applyMapper.getListApply(null);
    }

    public int addApply(Apply apply){
        Account currentUser = TokenUtils.getCurrentUser();//获取当前用户信息
        apply.setTime(DateUtil.now());//获取时间

        apply.setUserId(currentUser.getId());//绑定用户
        apply.setStatus(2);//给状态赋值2：待审核
        System.out.println("当前状态");
        return applyMapper.addApply(apply);
    }

    public int update(Apply apply){
        return applyMapper.update(apply);
    }

    public int deleteApplyById(int id){
        return applyMapper.deleteApplyById(id);
    }

    public void deleteBatch(List<Apply> list) {
        for (Apply apply : list) {
            this.deleteApplyById(apply.getId());
        }
    }

    public Apply selectById(Integer id){
        return applyMapper.selectById(id);
    }

    public PageInfo<Apply> selectPage(Integer pageNum, Integer pageSize, Apply apply) {
        Account currentUser = TokenUtils.getCurrentUser();
        apply.setUserId(currentUser.getId());

        // 开启分页查询

        PageHelper.startPage(pageNum, pageSize);
        List<Apply> applyList = applyMapper.getListApply(apply);
        return PageInfo.of(applyList);
    }

}
