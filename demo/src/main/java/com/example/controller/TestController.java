package com.example.controller;

import com.example.common.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @RequestMapping("/hello")
    public String hello() {
        return "hello";
    }
//    @RequestMapping("/testResult")
//    public Result testResult(){
//        return Result.success("testResult");
//    }
//    @RequestMapping("/throwException")
//    public String throwException(){
//        throw new RuntimeException("人为触发500错误");
//    }

//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    @Autowired
//    private StringRedisTemplate stringRedisTemplate;
//    /**
//     *  首先我们知道Redis是一款key，value存储结构的数据库
//     *   因此我们假定这个set()测试方法以key，value的形式往Redis数据库中存储数据
//     *   key，value形式的存储命令：set
//     *   key，value形式的取数据命令：get
//     */
//    @RequestMapping("/redisSet")
//    public void set() {
//        ValueOperations ops = redisTemplate.opsForValue();    // 首先redisTemplate.opsForValue的目的就是表明是以key，value形式储存到Redis数据库中数据的
//        ops.set("address1","zhengzhou");// 到这里就表明Redis数据库中存储了key为address1，value为zhengzhou的数据了（取的时候通过key取数据）
//    }
//
//    @RequestMapping("/redisGet")
//    public void get() {
//        ValueOperations ops = redisTemplate.opsForValue();  // 表明取的是key，value型的数据
//        Object o = ops.get("address1");  // 获取Redis数据库中key为address1对应的value数据
//        System.out.println(o);
//    }


}
