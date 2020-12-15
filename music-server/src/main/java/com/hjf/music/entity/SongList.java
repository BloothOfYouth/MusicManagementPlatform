package com.hjf.music.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/**
 * 歌单表(SongList)实体类
 *
 * @author hjf
 * @since 2020-11-16 11:52:59
 */
public class SongList implements Serializable {

    /**
     * 歌单表的主键
     */
    @TableId(type = IdType.AUTO)
    private Integer id;
    /**
     * 歌单的标题
     */
    private String title;
    /**
     * 歌单封面URL
     */
    private String pic;
    /**
     * 歌单的简介
     */
    private String introduction;
    /**
     * 歌曲的风格
     */
    private String style;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getIntroduction() {
        return introduction;
    }

    public void setIntroduction(String introduction) {
        this.introduction = introduction;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public Integer getSongCount() {
        return songCount;
    }

    public void setSongCount(Integer songCount) {
        this.songCount = songCount;
    }

    @Override
    public String toString() {
        return "SongList{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", pic='" + pic + '\'' +
                ", introduction='" + introduction + '\'' +
                ", style='" + style + '\'' +
                ", songCount=" + songCount +
                '}';
    }
}
