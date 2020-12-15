package com.hjf.music.controller;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.hjf.music.entity.Admin;
import com.hjf.music.model.ResultMap;
import com.hjf.music.service.IAdminService;
import com.hjf.music.utils.JWTUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;

/**
 * @author hjf
 * @create 2020-10-26 23:21
 */
@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private IAdminService adminService;

    @Autowired
    private ResultMap resultMap;

    /**
     * 管理员登录
     * @param admin
     * @return
     */
    @PostMapping("/login")
    public ResultMap login(@RequestBody Admin admin){
        Admin realAdmin = adminService.selectByUsernameAndPassword(admin.getUsername(), admin.getPassword());
        if (realAdmin == null) {
            return resultMap.fail().code(401).message("用户名或密码错误");
        }
        HashMap<String, Object> map = new HashMap<>();
        map.put("JWT_TOKEN",JWTUtils.getToken(null, realAdmin.getUsername()));
        map.put("username", realAdmin.getUsername());
        return resultMap.success().code(200).message(map);
    }

    /**
     * 非法请求跳转
     * @return
     */
    @GetMapping(path = "/unauthorized")
    public ResultMap unauthorized(){
        return resultMap.fail().code(401).message("没有权限");
    }

    /**
     * 令牌验证
     * @param token
     * @return
     */
    @PostMapping("/tokenVerity")
    public ResultMap tokenVerity(String token){
        DecodedJWT decodedJWT = JWTUtils.getDecodedJWT(token);
        if (decodedJWT == null) {
            return resultMap.fail().code(401).message("令牌验证错误");
        }
        return resultMap.success().code(200).message("令牌验证成功");
    }

}
