package com.hjf.music.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hjf.music.entity.SongList;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author hjf
 * @create 2020-11-16 12:52
 */
@Repository
public interface SongListDao extends BaseMapper<SongList> {
    /**
     * 模糊查询List<SongList>
     * 对SongList封装songCount(有多少个歌曲)
     * @param title
     * @return
     */
    List<SongList> selectSongLists(String title);
}
