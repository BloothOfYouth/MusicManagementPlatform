package com.hjf.music.entity;

import java.io.Serializable;

/**
 * 歌单包含歌曲列表(ListSong)实体类
 *
 * @author hjf
 * @since 2020-11-18 14:12:33
 */
public class ListSong implements Serializable {

    /**
     * 歌曲的主键id
     */
    private Integer songId;
    /**
     * 歌单的主键id
     */
    private Integer songListId;
    /**
     * 本表的主键
     */
    private Integer id;


    public Integer getSongId() {
        return songId;
    }

    public void setSongId(Integer songId) {
        this.songId = songId;
    }

    public Integer getSongListId() {
        return songListId;
    }

    public void setSongListId(Integer songListId) {
        this.songListId = songListId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ListSong{" +
                "songId=" + songId +
                ", songListId=" + songListId +
                ", id=" + id +
                '}';
    }
}
