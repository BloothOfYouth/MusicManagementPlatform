package com.hjf.music.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.Date;

/**
 * 前端用户表(User)实体类
 *
 * @author hjf
 * @since 2020-11-20 12:50:25
 */
public class User implements Serializable {

    /**
     * 用户表主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 用户账号
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用户性别（1男，0女）
     */
    private Short sex;
    /**
     * 用户电话
     */
    private String phoneNum;
    /**
     * 用户邮箱
     */
    private String email;
    /**
     * 用户生日
     */
    private Date birth;
    /**
     * 用户签名
     */
    private String introduction;
    /**
     * 用户所在地
     */
    private String location;
    /**
     * 用户头像URL
     */
    private String avator;
    /**
     * 用户创建时间
     */
    private Date createTime;
    /**
     * 用户更新时间
     */
    private Date updateTime;
    /**
     * 用户名
     */
    private String name;


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

    public Short getSex() {
        return sex;
    }

    public void setSex(Short sex) {
        this.sex = sex;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAvator() {
        return avator;
    }

    public void setAvator(String avator) {
        this.avator = avator;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", sex=" + sex +
                ", phoneNum='" + phoneNum + '\'' +
                ", email='" + email + '\'' +
                ", birth=" + birth +
                ", introduction='" + introduction + '\'' +
                ", location='" + location + '\'' +
                ", avator='" + avator + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", name='" + name + '\'' +
                '}';
    }
}
