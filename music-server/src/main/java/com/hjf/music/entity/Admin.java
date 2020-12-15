package com.hjf.music.entity;

import java.io.Serializable;

/**
 * 管理员表(Admin)实体类
 *
 * @author hjf
 * @since 2020-10-26 23:03:44
 */
public class Admin implements Serializable {

    /**
     * 管理员表主键
     */
    private Integer id;
    /**
     * 管理员账号
     */
    private String username;
    /**
     * 管理员密码
     */
    private String password;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "Admin{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
