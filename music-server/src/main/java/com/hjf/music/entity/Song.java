package com.hjf.music.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;
import java.util.Date;

/**
 * 歌曲表(Song)实体类
 *
 * @author hjf
 * @since 2020-11-05 19:25:32
 */
public class Song implements Serializable {

    /**
     * 歌曲表主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 歌手的id
     */
    private Integer singerId;
    /**
     * 歌名
     */
    private String name;
    /**
     * 歌曲简介
     */
    private String introduction;
    /**
     * 歌曲创建时间
     */
    private Date createTime;
    /**
     * 歌曲更新时间
     */
    private Date updateTime;
    /**
     * 歌曲封面URL
     */
    private String pic;
    /**
     * 歌曲歌词
     */
    private String lyric;
    /**
     * 歌曲的URL
     */
    private String url;
    /**
     * 歌曲对应的歌手
     */
    @TableField(exist = false)
    private Singer singer;
    /**
     * list_song表的主键
     */
    @TableField(exist = false)
    private String listSongId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getSingerId() {
        return singerId;
    }

    public void setSingerId(Integer singerId) {
        this.singerId = singerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
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

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getLyric() {
        return lyric;
    }

    public void setLyric(String lyric) {
        this.lyric = lyric;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Singer getSinger() {
        return singer;
    }

    public void setSinger(Singer singer) {
        this.singer = singer;
    }

    public String getListSongId() {
        return listSongId;
    }

    public void setListSongId(String listSongId) {
        this.listSongId = listSongId;
    }

    @Override
    public String toString() {
        return "Song{" +
                "id=" + id +
                ", singerId=" + singerId +
                ", name='" + name + '\'' +
                ", introduction='" + introduction + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", pic='" + pic + '\'' +
                ", lyric='" + lyric + '\'' +
                ", url='" + url + '\'' +
                ", singer=" + singer +
                '}';
    }
}
