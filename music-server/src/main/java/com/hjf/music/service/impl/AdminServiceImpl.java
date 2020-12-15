package com.hjf.music.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hjf.music.dao.AdminDao;
import com.hjf.music.entity.Admin;
import com.hjf.music.service.IAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author hjf
 * @create 2020-10-26 23:13
 */
@Service
public class AdminServiceImpl implements IAdminService {

    @Autowired
    private AdminDao adminDao;

    /**
     * 根据账号和密码查询管理员
     * @param username
     * @param password
     * @return
     */
    @Override
    public Admin selectByUsernameAndPassword(String username, String password) {
        QueryWrapper<Admin> adminQueryWrapper = new QueryWrapper<>();
        adminQueryWrapper.eq("username",username).eq("password",password);
        Admin admin = adminDao.selectOne(adminQueryWrapper);
        return admin;
    }
}
