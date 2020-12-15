package com.hjf.music.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hjf.music.entity.ListSong;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author hjf
 * @create 2020-11-18 11:29
 */
@Repository
public interface ListSongDao extends BaseMapper<ListSong> {

    /**
     * 批量添加ListSong
     * @param map
     * @return
     */
    int insertSome(Map map);

    /**
     * 通过songListId查询歌曲的SongId集合
     * @param songListId
     * @return
     */
    List<Integer> selectSongIdsBySongListId(Integer songListId);

}
