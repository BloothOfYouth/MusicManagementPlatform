package com.hjf.music.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hjf.music.entity.Song;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @author hjf
 * @create 2020-11-05 19:30
 */
@Repository
public interface SongDao extends BaseMapper<Song> {

    /**
     * 根据ids查询List<Song>，Song里面封装Singer
     * @param map
     * @return
     */
    List<Song> selectBySongIds(Map map);

    /**
     * 查询全部List<Song>，Song里面封装Singer
     * @return
     */
    List<Song> selectAllSongs(String name);

}
