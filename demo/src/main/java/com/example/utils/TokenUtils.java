package com.example.utils;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.entity.Account;
import com.example.mapper.AdminMapper;
import com.example.service.AdminService;
import com.example.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Component
public class TokenUtils {

    @Autowired
    AdminService adminService;
    @Autowired
    UserService userService;

    static AdminService staticAdminService;;
    static UserService staticUserService;

    // springboot工程启动后会加载这段代码
    @PostConstruct
    public void init() {
        staticAdminService = adminService;
        staticUserService = userService;
    }


    /**
     * 生成token
     */
    public static String createToken(String data, String sign) {
        return JWT.create().withAudience(data) // 将 userId-role 保存到 token 里面,作为载荷
                .withExpiresAt(DateUtil.offsetDay(new Date(), 1)) // 1天后token过期
                .sign(Algorithm.HMAC256(sign)); // 以 password 作为 token 的密钥, HMAC256算法加密
    }

    /**
     * 获取当前登录的用户信息
     */
    public static Account getCurrentUser() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String token = request.getHeader("token");
        if (StrUtil.isBlank(token)) {
            token = request.getParameter("token");
        }
        // 拿到token 的载荷数据
        String audience = JWT.decode(token).getAudience().get(0);
        String[] split = audience.split("-");
        String userId = split[0];
        String role = split[1];
        // 根据token解析出来的userId去对应的表查询用户信息
        if ("ADMIN".equals(role)) {
            return staticAdminService.selectAdminById(Integer.valueOf(userId));
        } else if ("USER".equals(role)) {
            return staticUserService.selectUserById(Integer.valueOf(userId));
        }
        return null;
    }

}
