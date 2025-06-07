package com.example;

import com.alibaba.fastjson2.JSON;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class DemoApplicationTests {

    @Test
    void contextLoads() {
    }

    @Autowired
    private RedisTemplate redisTemplate;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    /**
     *  首先我们知道Redis是一款key，value存储结构的数据库
     *   因此我们假定这个set()测试方法以key，value的形式往Redis数据库中存储数据
     *   key，value形式的存储命令：set
     *   key，value形式的取数据命令：get
     */
    @Test
    public void set() {
        ValueOperations ops = redisTemplate.opsForValue();    // 首先redisTemplate.opsForValue的目的就是表明是以key，value形式储存到Redis数据库中数据的

        ops.set("address1","zhengzhou");// 到这里就表明Redis数据库中存储了key为address1，value为zhengzhou的数据了（取的时候通过key取数据）
    }

    @Test
    public void get() {
        ValueOperations ops = redisTemplate.opsForValue();  // 表明取的是key，value型的数据
        Object o = ops.get("address1");  // 获取Redis数据库中key为address1对应的value数据
        System.out.println(o);
    }

    @Test
    public void hset() {
        // 表明数据是以哈希类型的格式进行储存到Redis数据库的
        HashOperations ops = redisTemplate.opsForHash();
        // 通过put命令，向Redis数据库中储存一个哈希类型的数据（一个为info的key里面放着一个key为a，value为aaaaaaa的数据）
        ops.put("info","a","aaaaaaa");

    }

//    @Test
//    public void testStringRedisTemplate(){
//        stringRedisTemplate.opsForValue().set("age","测试中文22");
//        Object age = stringRedisTemplate.opsForValue().get("age");
//        System.out.println("从redis中获取的age数据为："+age);
//
//        Map<String, Object> map = new HashMap<>();
//        map.put("name","张三");
//        map.put("age",22);
//        map.put("sex","男");
//        //通过fastjson 将map数据转换为json格式字符串在使用StringRedisTemplate进行存储
//        stringRedisTemplate.opsForValue().set("map", JSON.toJSONString(map));
//    }
//
//
//    @Test
//    public void getStringRedisTemplate(){
//        String map = stringRedisTemplate.opsForValue().get("map");
//        System.out.println(map);
//    }






}
