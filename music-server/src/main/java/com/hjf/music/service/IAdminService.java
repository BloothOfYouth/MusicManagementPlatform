package com.hjf.music.service;

import com.hjf.music.entity.Admin;

/**
 * @author hjf
 * @create 2020-10-26 23:11
 */
public interface IAdminService {
    /**
     * 根据账号和密码查询管理员
     * @param username
     * @param password
     * @return
     */
    Admin selectByUsernameAndPassword(String username,String password);
}
