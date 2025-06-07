package com.example.service;

import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.User;
import com.example.exception.CustomerException;

import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public List<User> getListUser(User user){
        return userMapper.getListUser(user);
    }



    public PageInfo<User> selectPage(Integer pageNum, Integer pageSize, User user) {
        // 开启分页查询
        PageHelper.startPage(pageNum, pageSize);
        List<User> userList = userMapper.getListUser(user);
        return PageInfo.of(userList);
    }


    public int addUser(User user){

        List<User> userList = userMapper.getListUser(user);
        if (userList.size() > 0){
            throw new CustomerException("该账号已被占用，请更换新的账号");
        }
        if (Strings.isBlank(user.getPassword())) {
            user.setPassword("123456");
        }
        if (Strings.isBlank(user.getName())) {
            user.setName(user.getUsername());
        }
        user.setRole("USER");
        return userMapper.addUser(user);
    }

    public int update(User user){
        return userMapper.update(user);
    }

    public User selectUserById(Integer id){
        return userMapper.selectUserById(id);
    }

    public int deleteUserById(int id){
        return userMapper.deleteUserById(id);
    }

    public void deleteBatch(List<User> list) {
        for (User user : list) {
            this.deleteUserById(user.getId());
        }
    }

    public void register(User user) {
        this.addUser(user);
    }

    public User login(Account account) {
        // 验证账号是否存在
        User dbUser = userMapper.selectUserByName(account.getUsername());
        if (dbUser == null) {
            throw new CustomerException("账号不存在");
        }
        // 验证密码是否正确
        if (!dbUser.getPassword().equals(account.getPassword())) {
            throw new CustomerException("账号或密码错误");
        }
        String token = TokenUtils.createToken(dbUser.getId() + "-" + "USER", dbUser.getPassword());
        dbUser.setToken(token);

        return dbUser;
    }

    @Transactional
    public int updataSalary(){
        User user = new User();
        user.setId(3);
        user.setSalary(new BigDecimal(200));
        userMapper.update(user);
        user.setId(4);
        user.setSalary(new BigDecimal(0));
        userMapper.update(user);
        return 1;
    }





}

