package com.hjf.music.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 歌手表(Singer)实体类
 *
 * @author hjf
 * @since 2020-10-29 22:07:44
 */
public class Singer implements Serializable {

    /**
     * 歌手表的主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 歌手姓名
     */
    private String name;
    /**
     * 歌手性别（0：女，1：男，2：组合，3：不明）
     */
    private Short sex;
    /**
     * 歌手头像URL
     */
    private String pic;
    /**
     * 歌手生日
     */
//    @JsonFormat(pattern="yyyy-MM-dd", timezone="GMT+8")
    private Date birth;
    /**
     * 歌手所属地区
     */
    private String location;
    /**
     * 歌手简介
     */
    private String introduction;
    /**
     * 有多少个歌曲
     */
    @TableField(exist = false)
    private Integer songCount;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Short getSex() {
        return sex;
    }

    public void setSex(Short sex) {
        this.sex = sex;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public Date getBirth() {
        return birth;
    }

    public void setBirth(Date birth) {
        this.birth = birth;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public Integer getSongCount() {
        return songCount;
    }

    public void setSongCount(Integer songCount) {
        this.songCount = songCount;
    }

    @Override
    public String toString() {
        return "Singer{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", sex=" + sex +
                ", pic='" + pic + '\'' +
                ", birth=" + birth +
                ", location='" + location + '\'' +
                ", introduction='" + introduction + '\'' +
                ", songCount=" + songCount +
                '}';
    }
}
